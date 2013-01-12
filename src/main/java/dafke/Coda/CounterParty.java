package be.dafke.Coda;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import be.dafke.Accounting.Objects.Account;

public class CounterParty implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String name, alias;
	private final HashMap<String, BankAccount> accounts;
	private final Collection<String> addressLines;

	private Account account;

	// private final ArrayList<Account> debetAccounts, creditAccounts;

	public CounterParty(String name) {
		accounts = new HashMap<String, BankAccount>();
		addressLines = new ArrayList<String>();
		this.name = name;
		alias = name;
		// debetAccounts = new ArrayList<Account>();
		// creditAccounts = new ArrayList<Account>();
	}

	public void addAccount(BankAccount newAccount) {
		accounts.put(newAccount.getAccountNumber(), newAccount);
	}

	@Override
	public String toString() {
		return alias;
	}

	public String getName() {
		return name;
	}

	public HashMap<String, BankAccount> getBankAccounts() {
		return accounts;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Account getAccount() {
		return account;
	}
}
