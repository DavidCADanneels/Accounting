package be.dafke.BasicAccounting.Accounts;

import be.dafke.BusinessActions.AccountDataChangeListener;
import be.dafke.BusinessActions.AccountsListener;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Accounts;
import be.dafke.ComponentModel.RefreshableDialog;

import javax.swing.*;
import java.awt.*;

public class AccountSelector extends RefreshableDialog implements AccountsListener, AccountDataChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JButton create, ok;
	private Account account;
	private final JComboBox<Account> combo;
    private final DefaultComboBoxModel<Account> model;
	private Accounts accounts;
	private AccountTypes accountTypes;

	public AccountSelector(final Accounts accounts, AccountTypes accountTypes) {
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
		setAccountTypes(accountTypes);
		pack();
	}

	public Account getSelection() {
		return account;
	}

	@Override
    public void setAccounts(Accounts accounts) {
		this.accounts = accounts;
		fireAccountDataChanged();
    }

	public void setAccountTypes(AccountTypes accountTypes) {
		this.accountTypes = accountTypes;
	}

	@Override
	public void fireAccountDataChanged() {
		model.removeAllElements();
		for (Account account:accounts.getBusinessObjects()) {
			model.addElement(account);
		}
		invalidate();
		combo.invalidate();
	}
}
