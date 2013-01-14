package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Objects.Accounting.Account.AccountType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Serialiseerbare map die alle rekeningen bevat
 * @author David Danneels
 * @since 01/10/2010
 */
public class Accounts extends HashMap<String, Account> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * @see HashMap<String, Account>
	 */
	public Accounts() {
		super();
	}

	public Account add(Account value) {
		return put(value.getName(), value);
	}

	@Override
	public Account put(String key, Account value) {
		if (key == null) return null;
		if (!containsKey(key)) {
			return super.put(key, value);
		}
		Account result = get(key);
		if (result.equals(value)) {
			return result;
		}
		System.err.println("account already exists with different data");
		System.err.println(value);
		System.err.println(result);
		return super.put(key, value);
		// return value;
	}

	/**
	 * Geeft alle rekeningen terug van het gegeven type
	 * @param type het type van de gevraagde rekeningen
	 * <ul>
	 * <li>0 : Actief</li>
	 * <li>1 : Passief</li>
	 * <li>2 : Kost</li>
	 * <li>3 : Opbrengst</li>
	 * <li>4 : Tegoed van Klant</li>
	 * <li>5 : Schuld aan Leverancier</li>
	 * </ul>
	 * @return alle rekeningen van het gevraagde type
	 */
	public ArrayList<Account> getAccounts(AccountType type) {
		ArrayList<Account> col = new ArrayList<Account>();
		for(Account account : values()) {
			if (account.getType() == type) col.add(account);
		}
		return col;
	}

	public ArrayList<Account> getAccounts(ArrayList<AccountType> types) {
		ArrayList<Account> list = new ArrayList<Account>();
		for(AccountType type : types) {
			list.addAll(getAccounts(type));
		}
		return list;
	}

	public ArrayList<Account> getAccountsNotEmpty(AccountType type) {
		ArrayList<Account> col = new ArrayList<Account>();
		for(Account account : values()) {
			if (account.getType() == type && account.saldo().compareTo(BigDecimal.ZERO) != 0) col.add(account);
		}
		return col;
	}

	public ArrayList<Account> getAccountsEmpty(AccountType type) {
		ArrayList<Account> col = new ArrayList<Account>();
		for(Account account : values()) {
			if (account.getType() == type && account.saldo().compareTo(BigDecimal.ZERO) == 0) col.add(account);
		}
		return col;
	}

	/**
	 * Geeft alle rekeningen terug die niet tot het gegeven project behoren
	 * @param project het project waarvan we de rekeningen willen uitsluiten
	 * @return alle rekeningen die niet tot het gegeven project behoren
	 */
	public ArrayList<Account> getAccountNoMatchProject(Project project) {
		ArrayList<Account> result = new ArrayList<Account>();
		for(Account account : values()) {
			if (account.getProject() != project) result.add(account);
		}
		return result;
	}

	/**
	 * Schrijft alle gewijzigde rekeningen uit naar XML
	 * @see be.dafke.Accounting.GUI.MainWindow.AccountingGUIFrame#windowClosing(java.awt.event.WindowEvent) oorzaak
	 * windowClosing(WindowEvent)
	 */
	public void saveAllXML() {
		for(Account account : values()) {
//			if (!account.isSaved())
			account.toXML();
		}
	}

	public void saveAllHTML() {
		for(Account account : values()) {
//			if (!account.isSaved())
			account.toHTML();
		}
	}

	public void rename(String oldName, String newName) {
		Account account = get(oldName);
		account.setName(newName);
		remove(oldName);
		put(newName, account);
	}

	public void setType(String accountName, AccountType newType) {
		Account account = get(accountName);
		account.setType(newType);
	}

	public ArrayList<Account> getAccounts() {
		ArrayList<Account> col = new ArrayList<Account>();
		for(Account account : values()) {
			col.add(account);
		}
		return col;
	}
}