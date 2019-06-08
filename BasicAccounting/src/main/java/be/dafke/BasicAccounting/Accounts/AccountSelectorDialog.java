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
	private AccountSelectorPanel selectorPanel;
	private static AccountSelectorDialog selectorDialog = null;

	private AccountSelectorDialog(Accounts accounts, ArrayList<AccountType> accountTypes) {
		this(accounts, accountTypes, "Select Account");
	}
	public AccountSelectorDialog(Accounts accounts, ArrayList<AccountType> accountTypes, String title) {
		super(title);
		selectorPanel = new AccountSelectorPanel(accounts, accountTypes);
		JPanel innerPanel = new JPanel(new BorderLayout());
		innerPanel.add(selectorPanel, BorderLayout.CENTER);

		ok = new JButton("Ok (Close popup)");
		ok.addActionListener(e -> dispose());
		innerPanel.add(ok, BorderLayout.SOUTH);

		setContentPane(innerPanel);
		setAccounts(accounts);
		pack();
	}

	public static AccountSelectorDialog getAccountSelector(Accounts accounts, ArrayList<AccountType> accountTypes, String title){
		if(selectorDialog ==null){
			selectorDialog = new AccountSelectorDialog(accounts, accountTypes, title);
		} else selectorDialog.setTitle(title);
		return selectorDialog;
	}

	public static AccountSelectorDialog getAccountSelector(Accounts accounts, ArrayList<AccountType> accountTypes){
		if(selectorDialog ==null){
			selectorDialog = new AccountSelectorDialog(accounts, accountTypes);
		}
		return selectorDialog;
	}

	public Account getSelection() {
		return selectorPanel.getSelection();
	}

    public void setAccounts(Accounts accounts) {
		selectorPanel.setAccounts(accounts);
    }

	public static void fireAccountDataChangedForAll() {
		if(selectorDialog !=null){
			selectorDialog.fireAccountDataChanged();
		}
	}

	public void fireAccountDataChanged() {
		selectorPanel.fireAccountDataChanged();
	}
}
