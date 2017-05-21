package be.dafke.BasicAccounting.Accounts;

import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.AccountType;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Accounts;
import be.dafke.ComponentModel.SelectableTable;
import be.dafke.ObjectModel.Exceptions.NotEmptyException;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.awt.Dimension;
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
	private JButton newAccount, delete, modifyType, modifyDefaultAmount;
	private final AccountManagementTableModel accountManagementTableModel;
	private final SelectableTable<Account> tabel;
	private Accounts accounts;
	private AccountTypes accountTypes;
	private static final HashMap<Accounts, AccountManagementGUI> accountManagementGuis = new HashMap<>();

	private AccountManagementGUI(final Accounts accounts, final AccountTypes accountTypes) {
		super(accounts.getAccounting().getName() + " / " + getBundle("Accounting").getString("ACCOUNT_MANAGEMENT_TITLE"));
		this.accounts = accounts;
		this.accountTypes = accountTypes;
		this.accountManagementTableModel = new AccountManagementTableModel(accounts);

		// COMPONENTS
		//
		// Table
		tabel = new SelectableTable<>(accountManagementTableModel);
		tabel.setPreferredScrollableViewportSize(new Dimension(500, 200));
		DefaultListSelectionModel selection = new DefaultListSelectionModel();
		selection.addListSelectionListener(this);
		tabel.setSelectionModel(selection);
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
			Main.addFrame(gui);
		}
		return gui;
	}

	private JPanel createContentPanel(){
		JPanel south = new JPanel();
		modifyType = new JButton(getBundle("Accounting").getString("MODIFY_TYPE"));
		delete = new JButton(getBundle("Accounting").getString("DELETE_ACCOUNT"));
        newAccount = new JButton(getBundle("Accounting").getString("ADD_ACCOUNT"));
		modifyDefaultAmount = new JButton(getBundle("Accounting").getString("MODIFY_DEFAULT_AMOUNT"));
		modifyType.addActionListener(e -> modifyAccountTypes(tabel.getSelectedObjects(), accountTypes));
		delete.addActionListener(e -> deleteAccounts(tabel.getSelectedObjects(), accounts));
        modifyDefaultAmount.addActionListener(e -> modifyDefaultAmounts(tabel.getSelectedObjects()));
		newAccount.addActionListener(e -> new NewAccountGUI(accounts, accountTypes).setVisible(true));
		modifyType.setEnabled(false);
		delete.setEnabled(false);
        modifyDefaultAmount.setEnabled(false);
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
				modifyType.setEnabled(true);
                modifyDefaultAmount.setEnabled(true);
			} else {
                delete.setEnabled(false);
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
		fireAccountDataChanged();
	}

	public void modifyDefaultAmounts(ArrayList<Account> accountList){
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
		fireAccountDataChanged();
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
		fireAccountDataChanged();
	}
}
