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
	private JButton ok;
	private AccountSelectorPanel accountSelectorPanel;
	private static AccountSelector accountSelector = null;

	private AccountSelector(Accounts accounts, AccountTypes accountTypes) {
		this(accounts, accountTypes, "Select Account");
	}
	private AccountSelector(Accounts accounts, AccountTypes accountTypes, String title) {
		super(title);
		accountSelectorPanel = new AccountSelectorPanel(accounts, accountTypes);
		JPanel innerPanel = new JPanel(new BorderLayout());
		innerPanel.add(accountSelectorPanel, BorderLayout.CENTER);

		ok = new JButton("Ok (Close popup)");
		ok.addActionListener(e -> dispose());
		innerPanel.add(ok, BorderLayout.SOUTH);

		setContentPane(innerPanel);
		setAccounts(accounts);
		pack();
	}

	public static AccountSelector getAccountSelector(Accounts accounts, AccountTypes accountTypes, String title){
		if(accountSelector==null){
			accountSelector = new AccountSelector(accounts, accountTypes, title);
		}
		return accountSelector;
	}

	public static AccountSelector getAccountSelector(Accounts accounts, AccountTypes accountTypes){
		if(accountSelector==null){
			accountSelector = new AccountSelector(accounts, accountTypes);
		}
		return accountSelector;
	}

	public Account getSelection() {
		return accountSelectorPanel.getSelection();
	}

    public void setAccounts(Accounts accounts) {
		accountSelectorPanel.setAccounts(accounts);
    }

	public static void fireAccountDataChangedForAll() {
		if(accountSelector!=null){
			accountSelector.fireAccountDataChanged();
		}
	}

	public void fireAccountDataChanged() {
		accountSelectorPanel.fireAccountDataChanged();
	}
}
