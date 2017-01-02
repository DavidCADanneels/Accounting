package be.dafke.BasicAccounting.Accounts;

import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BasicAccounting.MainApplication.SaveAllActionListener;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.AccountType;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Accounts;
import be.dafke.ComponentModel.RefreshableTable;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.ObjectModel.Exceptions.NotEmptyException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import static be.dafke.BasicAccounting.MainApplication.ActionUtils.CHOOSE_NEW_TYPE_FOR;
import static java.util.ResourceBundle.getBundle;

public class AccountManagementGUI extends JFrame implements ListSelectionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton newAccount, delete, modifyName, modifyType, modifyDefaultAmount;
	private final AccountManagementTableModel accountManagementTableModel;
	private final RefreshableTable<Account> tabel;
	private final DefaultListSelectionModel selection;
	private Accounts accounts;
	private AccountTypes accountTypes;
	private static final HashMap<Accounts, AccountManagementGUI> accountManagementGuis = new HashMap<>();

	private AccountManagementGUI(final Accounts accounts, final AccountTypes accountTypes) {
		super(getBundle("Accounting").getString("ACCOUNT_MANAGEMENT_TITLE"));
		this.accounts = accounts;
		this.accountTypes = accountTypes;
		this.accountManagementTableModel = new AccountManagementTableModel(accounts);

		// COMPONENTS
		//
		// Table
		tabel = new RefreshableTable<>(accountManagementTableModel);
		tabel.setPreferredScrollableViewportSize(new Dimension(500, 200));
		selection = new DefaultListSelectionModel();
		selection.addListSelectionListener(this);
		tabel.setSelectionModel(selection);
		tabel.setSelectedRow(-1);
		JScrollPane scrollPane = new JScrollPane(tabel);
		//
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(scrollPane, BorderLayout.CENTER);

		JPanel south = createContentPanel();

		panel.add(south, BorderLayout.SOUTH);
		setContentPane(panel);
		pack();
	}

	public static AccountManagementGUI showAccountManager(Accounts accounts, AccountTypes accountTypes) {
		AccountManagementGUI gui = accountManagementGuis.get(accounts);
		if(gui == null){
			gui = new AccountManagementGUI(accounts, accountTypes);
			accountManagementGuis.put(accounts, gui);
			SaveAllActionListener.addFrame(gui);
		}
		return gui;
	}

	private JPanel createContentPanel(){
		JPanel south = new JPanel();
		modifyName = new JButton(getBundle("Accounting").getString("MODIFY_NAME"));
		modifyType = new JButton(getBundle("Accounting").getString("MODIFY_TYPE"));
		delete = new JButton(getBundle("Accounting").getString("DELETE_ACCOUNT"));
        newAccount = new JButton(getBundle("Accounting").getString("ADD_ACCOUNT"));
		modifyDefaultAmount = new JButton(getBundle("Accounting").getString("MODIFY_DEFAULT_AMOUNT"));
		modifyName.addActionListener(e -> modifyAccountNames(tabel.getSelectedObjects(), accounts));
		modifyType.addActionListener(e -> modifyAccountTypes(tabel.getSelectedObjects(), accountTypes));
		delete.addActionListener(e -> deleteAccounts(tabel.getSelectedObjects(), accounts));
        modifyDefaultAmount.addActionListener(e -> modifyDefaultAmounts(tabel.getSelectedObjects(), accounts));
		newAccount.addActionListener(e -> new NewAccountGUI(accounts, accountTypes).setVisible(true));
		modifyName.setEnabled(false);
		modifyType.setEnabled(false);
		delete.setEnabled(false);
        modifyDefaultAmount.setEnabled(false);
		south.add(modifyName);
		south.add(modifyType);
        south.add(modifyDefaultAmount);
        south.add(delete);
        south.add(newAccount);
		return south;
	}

 	/**
	 * Herlaadt de data van de tabel
	 * @see javax.swing.table.AbstractTableModel#fireTableDataChanged()
	 */

	public static void fireAccountDataChangedForAll() {
		for(AccountManagementGUI accountManagementGUI:accountManagementGuis.values()){
			accountManagementGUI.fireAccountDataChanged();
		}
	}

	public void fireAccountDataChanged() {
		accountManagementTableModel.fireTableDataChanged();
	}

	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			int[] rows = tabel.getSelectedRows();
			if (rows.length != 0) {
				delete.setEnabled(true);
				modifyName.setEnabled(true);
				modifyType.setEnabled(true);
                modifyDefaultAmount.setEnabled(true);
			} else {
                delete.setEnabled(false);
                modifyName.setEnabled(false);
                modifyType.setEnabled(false);
                modifyDefaultAmount.setEnabled(false);
            }
		}
	}

	public void deleteAccounts(ArrayList<Account> accountList, Accounts accounts){
		if(!accountList.isEmpty()) {
			ArrayList<String> failed = new ArrayList<String>();
			for(Account account : accountList) {
				try{
					accounts.removeBusinessObject(account);
				}catch (NotEmptyException e){
					failed.add(account.getName());
				}
			}
			if (failed.size() > 0) {
				if (failed.size() == 1) {
					ActionUtils.showErrorMessage(ActionUtils.ACCOUNT_NOT_EMPTY, failed.get(0));
				} else {
					StringBuilder builder = new StringBuilder(getBundle("BusinessActions").getString("MULTIPLE_ACCOUNTS_NOT_EMPTY")+"\n");
					for(String s : failed){
						builder.append("- ").append(s).append("\n");
					}
					JOptionPane.showMessageDialog(null, builder.toString());
				}
			}
		}
	}

	public void modifyDefaultAmounts(ArrayList<Account> accountList, Accounts accounts){
		if(!accountList.isEmpty()) {
			for(Account account : accountList){
				BigDecimal defaultAmount = account.getDefaultAmount();
				String amount = JOptionPane.showInputDialog(account.getName() + ": " + getBundle("BusinessActions").getString("DEFAULT_AMOUNT"), defaultAmount);
				try{
					if (amount==null){
						account.setDefaultAmount(null);
					} else {
						defaultAmount = new BigDecimal(amount);
						defaultAmount = defaultAmount.setScale(2);
						account.setDefaultAmount(defaultAmount);
					}
				} catch (NumberFormatException nfe) {
					account.setDefaultAmount(null);
				}
			}
		}
	}

	public void modifyAccountNames(ArrayList<Account> accountList, Accounts accounts) {
		if (!accountList.isEmpty()) {
			for (Account account : accountList) {
				String oldName = account.getName();
				boolean retry = true;
				while (retry) {
					String newName = JOptionPane.showInputDialog(getBundle("BusinessActions").getString("NEW_NAME"), oldName.trim());
					try {
						if (newName != null && !oldName.trim().equals(newName.trim())) {
							accounts.modifyAccountName(oldName, newName);
							//ComponentMap.refreshAllFrames();
						}
						retry = false;
					} catch (DuplicateNameException e) {
						ActionUtils.showErrorMessage(ActionUtils.ACCOUNT_DUPLICATE_NAME,newName.trim());
					} catch (EmptyNameException e) {
						ActionUtils.showErrorMessage(ActionUtils.ACCOUNT_NAME_EMPTY);
					}
				}
			}
		}
	}

	public void modifyAccountTypes(ArrayList<Account> accountList, AccountTypes accountTypes){
		if(!accountList.isEmpty()) {
			boolean singleMove;
			if (accountList.size() == 1) {
				singleMove = true;
			} else {
				int option = JOptionPane.showConfirmDialog(null, getBundle("BusinessActions").getString("APPLY_SAME_TYPE_FOR_ALL_ACCOUNTS"),
						getBundle("BusinessActions").getString("ALL"),
						JOptionPane.YES_NO_OPTION);
				singleMove = (option == JOptionPane.YES_OPTION);
			}
			if (singleMove) {
				Object[] types = accountTypes.getBusinessObjects().toArray();
				int nr = JOptionPane.showOptionDialog(null, ActionUtils.getFormattedString(ActionUtils.CHOOSE_NEW_TYPE),
						ActionUtils.getFormattedString(ActionUtils.CHANGE_TYPE),
						JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, types, null);
				if (nr != JOptionPane.CLOSED_OPTION) {
					for (Account account : accountList) {
						account.setType((AccountType) types[nr]);
					}
				}
			} else {
				for (Account account : accountList) {
					Object[] types = accountTypes.getBusinessObjects().toArray();
					int nr = JOptionPane.showOptionDialog(null, ActionUtils.getFormattedString(CHOOSE_NEW_TYPE_FOR,account.getName()),
							ActionUtils.getFormattedString(ActionUtils.CHANGE_TYPE), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, types,
							account.getType());
					if (nr != JOptionPane.CLOSED_OPTION) {
						account.setType((AccountType) types[nr]);
					}
				}
			}
		}
	}
}
