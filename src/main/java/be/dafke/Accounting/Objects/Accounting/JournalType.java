package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Objects.Accounting.Account.AccountType;

import java.io.Serializable;
import java.util.ArrayList;

public class JournalType implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final ArrayList<AccountType> debetTypes, creditTypes;
	private final String name;

	public JournalType(String name, ArrayList<AccountType> debetTypes, ArrayList<AccountType> creditTypes) {
		this.debetTypes = debetTypes;
		this.creditTypes = creditTypes;
		this.name = name;
	}

	/**
	 * Default implementation: accepts all AccountTypes for both debit and credit.
	 */
	public JournalType() {
		this("<default>", AccountType.getList(), AccountType.getList());
	}

	@Override
	public String toString() {
		return name;
	}
}
