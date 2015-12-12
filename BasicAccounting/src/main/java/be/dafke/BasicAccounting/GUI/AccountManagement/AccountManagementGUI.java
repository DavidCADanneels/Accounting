package be.dafke.BasicAccounting.GUI.AccountManagement;

import be.dafke.BasicAccounting.Actions.AccountActions;
import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.AccountTypes;
import be.dafke.BasicAccounting.Objects.Accounts;
import be.dafke.ComponentModel.RefreshableFrame;
import be.dafke.ComponentModel.RefreshableTable;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

public class AccountManagementGUI extends RefreshableFrame implements ListSelectionListener, ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String MODIFY_NAME = "MODIFY_NAME";
	public static final String MODIFY_TYPE = "MODIFY_TYPE";
	public static final String NEW_ACCOUNT = "NEW_ACCOUNT";
	public static final String DELETE = "DELETE";
	public static final String MODIFY_DEFAULT_AMOUNT = "MODIFY_DEFAULT_AMOUNT";
	private final JButton newAccount, delete, modifyName, modifyType, modifyDefaultAmount;
	private final AccountManagementTableModel model;
	private final RefreshableTable<Account> tabel;
	private final DefaultListSelectionModel selection;
	private Accounts accounts;
	private AccountTypes accountTypes;

	public AccountManagementGUI(final Accounts accounts, final AccountTypes accountTypes) {
		super(getBundle("Accounting").getString("ACCOUNT_MANAGEMENT_TITLE"));
		this.accounts = accounts;
		this.accountTypes = accountTypes;
		this.model = new AccountManagementTableModel(accounts, accountTypes);

        // COMPONENTS
        //
        // Table
		tabel = new RefreshableTable<Account>(model);
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
		modifyName.setActionCommand(MODIFY_NAME);
		modifyType.setActionCommand(MODIFY_TYPE);
		modifyDefaultAmount.setActionCommand(MODIFY_DEFAULT_AMOUNT);
		delete.setActionCommand(DELETE);
		newAccount.setActionCommand(NEW_ACCOUNT);
		modifyName.addActionListener(this);
		modifyType.addActionListener(this);
		delete.addActionListener(this);
        modifyDefaultAmount.addActionListener(this);
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

	public void refresh() {
		model.fireTableDataChanged();
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

	public void actionPerformed(ActionEvent ae) {
		String actionCommand = ae.getActionCommand();
		if(MODIFY_NAME.equals(actionCommand)) {
			AccountActions.modifyAccountNames(getSelectedAccounts(), accounts);
		} else if(MODIFY_TYPE.equals(actionCommand)){
			AccountActions.modifyAccountTypes(getSelectedAccounts(), accountTypes);
		} else if(MODIFY_DEFAULT_AMOUNT.equals(actionCommand)){
			AccountActions.modifyDefaultAmounts(getSelectedAccounts(), accounts);
		} else if(DELETE.equals(actionCommand)){
			AccountActions.deleteAccounts(getSelectedAccounts(), accounts);
		} else if(NEW_ACCOUNT.equals(actionCommand)){
			new NewAccountGUI(accounts, accountTypes).setVisible(true);
		}
	}
}
