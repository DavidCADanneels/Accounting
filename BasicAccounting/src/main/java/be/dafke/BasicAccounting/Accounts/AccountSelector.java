package be.dafke.BasicAccounting.Accounts;

import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Accounts;
import be.dafke.ComponentModel.RefreshableDialog;

import javax.swing.*;
import java.awt.*;

public class AccountSelector extends RefreshableDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JButton create, ok;
	private Account account;
	private final JComboBox<Account> combo;
    private final DefaultComboBoxModel<Account> model;
	private Accounts accounts;
	private static AccountSelector accountSelector = null;

	private AccountSelector(Accounts accounts, AccountTypes accountTypes) {
		super("Select Account");
		model = new DefaultComboBoxModel<>();
		combo = new JComboBox<>(model);
		combo.addActionListener(e -> account = (Account) combo.getSelectedItem());
		create = new JButton("Add account(s) ...");
		create.addActionListener(e -> new NewAccountGUI(accounts, accountTypes).setVisible(true));
		ok = new JButton("Ok (Close popup)");
		ok.addActionListener(e -> dispose());
		JPanel innerPanel = new JPanel(new BorderLayout());
		JPanel panel = new JPanel();
		panel.add(combo);
		panel.add(create);
		innerPanel.add(panel, BorderLayout.CENTER);
		innerPanel.add(ok, BorderLayout.SOUTH);
		setContentPane(innerPanel);
		setAccounts(accounts);
		pack();
	}

	public static AccountSelector getAccountSelector(Accounts accounts, AccountTypes accountTypes){
		if(accountSelector==null){
			accountSelector = new AccountSelector(accounts, accountTypes);
		}
		return accountSelector;
	}

	public Account getSelection() {
		return account;
	}

    public void setAccounts(Accounts accounts) {
		this.accounts = accounts;
		fireAccountDataChanged();
    }

	public static void fireAccountDataChangedForAll() {
		if(accountSelector!=null){
			accountSelector.fireAccountDataChanged();
		}
	}

	public void fireAccountDataChanged() {
		model.removeAllElements();
		for (Account account:accounts.getBusinessObjects()) {
			model.addElement(account);
		}
		invalidate();
		combo.invalidate();
	}
}
