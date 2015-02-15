package be.dafke.BasicAccounting.GUI.AccountManagement;

import be.dafke.BasicAccounting.Actions.DeleteAccountsAction;
import be.dafke.BasicAccounting.Actions.ModifyAccountDefaultAmountsAction;
import be.dafke.BasicAccounting.Actions.ModifyAccountNamesAction;
import be.dafke.BasicAccounting.Actions.ModifyAccountTypesAction;
import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.ComponentModel.RefreshableFrame;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

public class AccountManagementGUI extends RefreshableFrame implements ListSelectionListener, ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JButton newAccount, delete, modifyName, modifyType, modifyDefaultAmount;
	private final AccountManagementTableModel model;
	private final JTable tabel;
	private final DefaultListSelectionModel selection;
    private ModifyAccountDefaultAmountsAction modifyAccountDefaultAmountsAction;
	private ModifyAccountNamesAction modifyAccountNamesAction;
	private ModifyAccountTypesAction modifyAccountTypesAction;
	private DeleteAccountsAction deleteAccountsAction;
	private final Accounting accounting;

	public AccountManagementGUI(Accounting accounting) {
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
		modifyAccountNamesAction = new ModifyAccountNamesAction(accounting);
		modifyAccountTypesAction = new ModifyAccountTypesAction(accounting);
		deleteAccountsAction = new DeleteAccountsAction(accounting);
		modifyAccountDefaultAmountsAction = new ModifyAccountDefaultAmountsAction();
		modifyName.addActionListener(modifyAccountNamesAction);
		modifyType.addActionListener(modifyAccountTypesAction);
		delete.addActionListener(deleteAccountsAction);
        modifyDefaultAmount.addActionListener(modifyAccountDefaultAmountsAction);
        newAccount.addActionListener(this);
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
                ArrayList<Account> selectedAccounts = getSelectedAccounts();
                modifyAccountDefaultAmountsAction.putValue("", selectedAccounts);
                modifyAccountNamesAction.putValue("", selectedAccounts);
                modifyAccountTypesAction.putValue("", selectedAccounts);
                deleteAccountsAction.putValue("", selectedAccounts);
			} else {
                delete.setEnabled(false);
                modifyName.setEnabled(false);
                modifyType.setEnabled(false);
                modifyDefaultAmount.setEnabled(false);
				modifyAccountDefaultAmountsAction.putValue("", null);
				modifyAccountNamesAction.putValue("", null);
				modifyAccountTypesAction.putValue("", null);
				deleteAccountsAction.putValue("", null);
            }
		}
	}

    public ArrayList<Account> getSelectedAccounts() {
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

	@Override
	public void actionPerformed(ActionEvent e) {
		new NewAccountGUI(accounting).setVisible(true);
		// or
        /*
        NewAccountGUI gui = new NewAccountGUI(accounting);
        ComponentMap.addRefreshableComponent(gui);
        gui.setVisible(true);
        */
	}
}
