package be.dafke.BasicAccounting.Accounts;

import be.dafke.BusinessActions.AccountActions;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Accounts;
import be.dafke.ComponentModel.RefreshableFrame;
import be.dafke.ComponentModel.RefreshableTable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	private JButton newAccount, delete, modifyName, modifyType, modifyDefaultAmount;
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
		tabel = new RefreshableTable<>(model);
		tabel.setPreferredScrollableViewportSize(new Dimension(500, 200));
		selection = new DefaultListSelectionModel();
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

	private JPanel createContentPanel(){
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
		return south;
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

	// TODO: check if we can add Actions to Buttons iso calling static actionMethods: --> How to pass selected Objects to the Action?
	public void actionPerformed(ActionEvent ae) {
		String actionCommand = ae.getActionCommand();
		if(MODIFY_NAME.equals(actionCommand)) {
			AccountActions.modifyAccountNames(tabel.getSelectedObjects(), accounts);
		} else if(MODIFY_TYPE.equals(actionCommand)){
			AccountActions.modifyAccountTypes(tabel.getSelectedObjects(), accountTypes);
		} else if(MODIFY_DEFAULT_AMOUNT.equals(actionCommand)){
			AccountActions.modifyDefaultAmounts(tabel.getSelectedObjects(), accounts);
		} else if(DELETE.equals(actionCommand)){
			AccountActions.deleteAccounts(tabel.getSelectedObjects(), accounts);
		} else if(NEW_ACCOUNT.equals(actionCommand)){
			new NewAccountGUI(accounts, accountTypes).setVisible(true);
		}
	}
}
