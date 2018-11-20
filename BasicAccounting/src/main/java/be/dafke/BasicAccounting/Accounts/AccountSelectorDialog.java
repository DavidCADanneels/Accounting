package be.dafke.BasicAccounting.Accounts;

import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.AccountType;
import be.dafke.BusinessModel.Accounts;
import be.dafke.ComponentModel.RefreshableDialog;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.ArrayList;

public class AccountSelectorDialog extends RefreshableDialog {
	private JButton ok;
	private AccountSelectorPanel accountSelectorPanel;
	private static AccountSelectorDialog accountSelectorDialog = null;

	private AccountSelectorDialog(Accounts accounts, ArrayList<AccountType> accountTypes) {
		this(accounts, accountTypes, "Select Account");
	}
	public AccountSelectorDialog(Accounts accounts, ArrayList<AccountType> accountTypes, String title) {
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

	public static AccountSelectorDialog getAccountSelector(Accounts accounts, ArrayList<AccountType> accountTypes, String title){
		if(accountSelectorDialog ==null){
			accountSelectorDialog = new AccountSelectorDialog(accounts, accountTypes, title);
		} else accountSelectorDialog.setTitle(title);
		return accountSelectorDialog;
	}

	public static AccountSelectorDialog getAccountSelector(Accounts accounts, ArrayList<AccountType> accountTypes){
		if(accountSelectorDialog ==null){
			accountSelectorDialog = new AccountSelectorDialog(accounts, accountTypes);
		}
		return accountSelectorDialog;
	}

	public Account getSelection() {
		return accountSelectorPanel.getSelection();
	}

    public void setAccounts(Accounts accounts) {
		accountSelectorPanel.setAccounts(accounts);
    }

	public static void fireAccountDataChangedForAll() {
		if(accountSelectorDialog !=null){
			accountSelectorDialog.fireAccountDataChanged();
		}
	}

	public void fireAccountDataChanged() {
		accountSelectorPanel.fireAccountDataChanged();
	}
}
