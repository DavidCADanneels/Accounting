package be.dafke.BasicAccounting.GUI.AccountManagement;

import be.dafke.BasicAccounting.GUI.ComponentMap;
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
import java.util.ArrayList;

public class AccountManagementGUI extends RefreshableFrame implements ActionListener, ListSelectionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JButton newAccount, delete, modifyName, modifyType;
	private final AccountManagementTableModel model;
	private final JTable tabel;
	private final DefaultListSelectionModel selection;
	private final Accounting accounting;

	public AccountManagementGUI(Accounting accounting, ActionListener actionListener) {
		super("Manage accounts for " + accounting.toString());
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
		modifyName = new JButton("Modify name");
		modifyType = new JButton("Modify type");
		delete = new JButton("Delete account");
        newAccount = new JButton(("Add account ..."));
		modifyName.addActionListener(this);
		modifyType.addActionListener(this);
		delete.addActionListener(this);
        newAccount.addActionListener(actionListener);
        newAccount.setActionCommand(ComponentMap.NEW_ACCOUNT);
		modifyName.setEnabled(false);
		modifyType.setEnabled(false);
		delete.setEnabled(false);
		south.add(modifyName);
		south.add(modifyType);
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
            if (event.getSource() == modifyName) {
                modifyNames(accountList);
            } else if (event.getSource() == modifyType) {
                modifyTypes(accountList);
            } else if (event.getSource() == delete) {
                deleteAccounts(accountList);
            }
        }
        delete.setEnabled(false);
        modifyName.setEnabled(false);
        modifyType.setEnabled(false);
        ComponentMap.refreshAllFrames();
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
                JOptionPane.showMessageDialog(this, failed.get(0) + " already has bookings, so it can not be deleted.");
            } else {
                StringBuilder builder = new StringBuilder("The following accounts already have bookings, so they can not be deleted:\r\n");
                for(String s : failed){
                    builder.append("- ").append(s).append("\r\n");
                }
                JOptionPane.showMessageDialog(this, builder.toString());
            }
        }
	}

	private void modifyNames(ArrayList<Account> accountList) {
        for(Account account : accountList){
            String oldName = account.getName();
            boolean retry = true;
            while(retry){
                String newName = JOptionPane.showInputDialog("New name", oldName.trim());
                try{
                    if(newName!=null && !oldName.trim().equals(newName.trim())){
                        accounting.getAccounts().modifyAccountName(oldName, newName);
                        ComponentMap.refreshAllFrames();
                    }
                    retry = false;
                } catch (DuplicateNameException e) {
                    JOptionPane.showMessageDialog(this, "There is already an account with the name \""+newName.trim()+"\".\r\n"+
                            "Please provide a new name.");
                } catch (EmptyNameException e) {
                    JOptionPane.showMessageDialog(this, "Account name cannot be empty\r\nPlease provide a new name.");
                }
            }
        }
    }

	private void modifyTypes(ArrayList<Account> accountList) {
        boolean singleMove;
        if (accountList.size() == 1) {
            singleMove = true;
        } else {
            int option = JOptionPane.showConfirmDialog(this, "Apply same type for all selected accounts?", "All",
                    JOptionPane.YES_NO_OPTION);
            singleMove = (option == JOptionPane.YES_OPTION);
        }
        if (singleMove) {
            Object[] types = accounting.getAccountTypes().getBusinessObjects().toArray();
            int nr = JOptionPane.showOptionDialog(this, "Choose new type", "Change type",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, types, null);
            if(nr != JOptionPane.CANCEL_OPTION && nr != JOptionPane.CLOSED_OPTION){
                for(Account account : accountList) {
                    account.setType((AccountType) types[nr]);
                }
            }
        } else {
            for(Account account : accountList) {
                Object[] types = accounting.getAccountTypes().getBusinessObjects().toArray();
                int nr = JOptionPane.showOptionDialog(this, "Choose new type for " + account.getName(),
                        "Change type", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, types,
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
			} else {
                delete.setEnabled(false);
                modifyName.setEnabled(false);
                modifyType.setEnabled(false);
            }
		}
	}

    private ArrayList<Account> getSelectedAccounts() {
        int[] rows = tabel.getSelectedRows();
        if (rows.length == 0) {
            JOptionPane.showMessageDialog(this, "Select an account first");
        }
        ArrayList<Account> accountList = new ArrayList<Account>();
        for(int row : rows) {
            Account account = (Account) model.getValueAt(row, 0);
            accountList.add(account);
        }
        return accountList;
    }
}
