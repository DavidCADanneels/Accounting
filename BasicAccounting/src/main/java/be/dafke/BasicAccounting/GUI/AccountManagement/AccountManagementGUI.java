package be.dafke.BasicAccounting.GUI.AccountManagement;

import be.dafke.BasicAccounting.Actions.AccountingActionListener;
import be.dafke.BasicAccounting.GUI.AccountingComponentMap;
import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.AccountType;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.ComponentModel.RefreshableFrame;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.ObjectModel.Exceptions.NotEmptyException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

public class AccountManagementGUI extends RefreshableFrame implements ActionListener, ListSelectionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JButton newAccount, delete, modifyName, modifyType, modifyDefaultAmount;
	private final AccountManagementTableModel model;
	private final JTable tabel;
	private final DefaultListSelectionModel selection;
	private final Accounting accounting;

	public AccountManagementGUI(Accounting accounting, ActionListener actionListener) {
		super(getBundle("Accounting").getString("ACCOUNT_MANAGEMENT_TITLE")+" " + accounting.toString());
		this.accounting = accounting;
		this.model = new AccountManagementTableModel(accounting);

        // COMPONENTS
        //
        // Table
		tabel = new JTable(model);
		tabel.setPreferredScrollableViewportSize(new Dimension(500, 200));
		selection = new DefaultListSelectionModel();
		selection.addListSelectionListener(this);
		tabel.setSelectionModel(selection);
		JScrollPane scrollPane = new JScrollPane(tabel);
        //
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(scrollPane, BorderLayout.CENTER);

		JPanel south = new JPanel();
		modifyName = new JButton(getBundle("Accounting").getString("MODIFY_NAME"));
		modifyType = new JButton(getBundle("Accounting").getString("MODIFY_TYPE"));
		delete = new JButton(getBundle("Accounting").getString("DELETE_ACCOUNT"));
        newAccount = new JButton(getBundle("Accounting").getString("ADD_ACCOUNT"));
        modifyDefaultAmount = new JButton(getBundle("Accounting").getString("MODIFY_DEFAULT_AMOUNT"));
		modifyName.addActionListener(this);
		modifyType.addActionListener(this);
		delete.addActionListener(this);
        modifyDefaultAmount.addActionListener(this);
        newAccount.addActionListener(actionListener);
        newAccount.setActionCommand(AccountingActionListener.NEW_ACCOUNT);
		modifyName.setEnabled(false);
		modifyType.setEnabled(false);
		delete.setEnabled(false);
        modifyDefaultAmount.setEnabled(false);
		south.add(modifyName);
		south.add(modifyType);
        south.add(modifyDefaultAmount);
        south.add(delete);
        south.add(newAccount);
		panel.add(south, BorderLayout.SOUTH);
		setContentPane(panel);
		pack();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
        ArrayList<Account> accountList = getSelectedAccounts();
        if(!accountList.isEmpty()){
            Object source = event.getSource();
            if (source == modifyName) {
                modifyNames(accountList);
            } else if (source == modifyType) {
                modifyTypes(accountList);
            } else if (source == delete) {
                deleteAccounts(accountList);
            } else if (source == modifyDefaultAmount) {
                modifyAmount(accountList);
            }
        }
        delete.setEnabled(false);
        modifyName.setEnabled(false);
        modifyType.setEnabled(false);
        modifyDefaultAmount.setEnabled(false);
        AccountingComponentMap.refreshAllFrames();
	}

    private void deleteAccounts(ArrayList<Account> accountList) {
		ArrayList<String> failed = new ArrayList<String>();
        for(Account account : accountList) {
            try{
                accounting.getAccounts().removeBusinessObject(account);
            }catch (NotEmptyException e){
                failed.add(account.getName());
            }
        }
        if (failed.size() > 0) {
            if (failed.size() == 1) {
                JOptionPane.showMessageDialog(this, failed.get(0) + " "+getBundle("Accounting").getString("ACCOUNT_NOT_EMPTY"));
            } else {
                StringBuilder builder = new StringBuilder(getBundle("Accounting").getString("MULTIPLE_ACCOUNTS_NOT_EMPTY")+"\r\n");
                for(String s : failed){
                    builder.append("- ").append(s).append("\r\n");
                }
                JOptionPane.showMessageDialog(this, builder.toString());
            }
        }
	}

    private void modifyAmount(ArrayList<Account> accountList) {
        for(Account account : accountList){
            BigDecimal defaultAmount = account.getDefaultAmount();
            boolean retry = true;
            while(retry){
                String amount = JOptionPane.showInputDialog(account.getName() + ": " + getBundle("Accounting").getString("DEFAULT_AMOUNT"), defaultAmount);
                try{
                    defaultAmount = new BigDecimal(amount);
                    defaultAmount = defaultAmount.setScale(2);
                    account.setDefaultAmount(defaultAmount);
                    retry = false;
                } catch (NumberFormatException nfe) {
                }
            }
        }
    }

    private void modifyNames(ArrayList<Account> accountList) {
        for(Account account : accountList){
            String oldName = account.getName();
            boolean retry = true;
            while(retry){
                String newName = JOptionPane.showInputDialog(getBundle("Accounting").getString("NEW_NAME"), oldName.trim());
                try{
                    if(newName!=null && !oldName.trim().equals(newName.trim())){
                        accounting.getAccounts().modifyAccountName(oldName, newName);
                        AccountingComponentMap.refreshAllFrames();
                    }
                    retry = false;
                } catch (DuplicateNameException e) {
                    JOptionPane.showMessageDialog(this, getBundle("Accounting").getString("ACCOUNT_DUPLICATE_NAME")+
                            " \""+newName.trim()+"\".\r\n"+
                            getBundle("Accounting").getString("PROVIDE_NEW_NAME"));
                } catch (EmptyNameException e) {
                    JOptionPane.showMessageDialog(this, "Account name cannot be empty"+"\r\n"+
                            getBundle("Accounting").getString("PROVIDE_NEW_NAME"));
                }
            }
        }
    }

	private void modifyTypes(ArrayList<Account> accountList) {
        boolean singleMove;
        if (accountList.size() == 1) {
            singleMove = true;
        } else {
            int option = JOptionPane.showConfirmDialog(this, getBundle("Accounting").getString("APPLY_SAME_TYPE_FOR_ALL_ACCOUNTS"),
                    getBundle("Accounting").getString("ALL"),
                    JOptionPane.YES_NO_OPTION);
            singleMove = (option == JOptionPane.YES_OPTION);
        }
        if (singleMove) {
            Object[] types = accounting.getAccountTypes().getBusinessObjects().toArray();
            int nr = JOptionPane.showOptionDialog(this, getBundle("Accounting").getString("CHOOSE_NEW_TYPE"),
                    getBundle("Accounting").getString("CHANGE_TYPE"),
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, types, null);
            if(nr != JOptionPane.CANCEL_OPTION && nr != JOptionPane.CLOSED_OPTION){
                for(Account account : accountList) {
                    account.setType((AccountType) types[nr]);
                }
            }
        } else {
            for(Account account : accountList) {
                Object[] types = accounting.getAccountTypes().getBusinessObjects().toArray();
                int nr = JOptionPane.showOptionDialog(this, getBundle("Accounting").getString("CHOOSE_NEW_TYPE_FOR")
                        +" " + account.getName(),
                        getBundle("Accounting").getString("CHANGE_TYPE"), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, types,
                        account.getType());
                if(nr != JOptionPane.CANCEL_OPTION && nr != JOptionPane.CLOSED_OPTION){
                    account.setType((AccountType) types[nr]);
                }
            }
        }
	}

	/**
	 * Herlaadt de data van de tabel
	 * @see javax.swing.table.AbstractTableModel#fireTableDataChanged()
	 */

	@Override
	public void refresh() {
		model.fireTableDataChanged();
	}

	@Override
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

    private ArrayList<Account> getSelectedAccounts() {
        int[] rows = tabel.getSelectedRows();
        if (rows.length == 0) {
            JOptionPane.showMessageDialog(this, getBundle("Accounting").getString("SELECT_ACCOUNT_FIRST"));
        }
        ArrayList<Account> accountList = new ArrayList<Account>();
        for(int row : rows) {
            Account account = (Account) model.getValueAt(row, 0);
            accountList.add(account);
        }
        return accountList;
    }
}
