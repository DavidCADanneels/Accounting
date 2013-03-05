package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;
import be.dafke.Accounting.Exceptions.NotEmptyException;
import be.dafke.Accounting.Objects.Accounting.Account.AccountType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Serialiseerbare map die alle rekeningen bevat
 * @author David Danneels
 * @since 01/10/2010
 */
public class Accounts extends BusinessCollection<Account> {

    private HashMap<String, Account> accounts;

    public Accounts() {
        super("Accounts");
        accounts = new HashMap<String, Account>();
    }

	public Account addAccount(String accountName, AccountType accountType) throws DuplicateNameException, EmptyNameException {
        if(accountName==null || "".equals(accountName.trim())){
            throw new EmptyNameException();
        }
        if(accounts.containsKey(accountName.trim())){
            throw new DuplicateNameException();
        }
        Account account = new Account(accountName.trim(), accountType);
        accounts.put(account.getName(), account);
        return account;
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
		for(Account account : accounts.values()) {
			if (account.getAccountType() == type) col.add(account);
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

	public ArrayList<Account> getAccountsNotEmpty(ArrayList<AccountType> types) {
		ArrayList<Account> col = new ArrayList<Account>();
		for(AccountType type : types) {
			col.addAll(getAccountsNotEmpty(type));
		}
		return col;
	}

    private ArrayList<Account> getAccountsNotEmpty(AccountType type) {
        ArrayList<Account> col = new ArrayList<Account>();
        for(Account account : accounts.values()) {
            if (account.getAccountType() == type && account.saldo().compareTo(BigDecimal.ZERO) != 0) col.add(account);
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
		for(Account account : accounts.values()) {
			if (account.getProject() != project) result.add(account);
		}
		return result;
	}

	public Account modifyAccountName(String oldName, String newName) throws EmptyNameException, DuplicateNameException {
        if(newName==null || "".equals(newName.trim())){
            throw new EmptyNameException();
        }
        Account account = accounts.get(oldName);
        accounts.remove(oldName);
        if(accounts.containsKey(newName.trim())){
            accounts.put(oldName, account);
            throw new DuplicateNameException();
        }
        accounts.put(newName, account);
        account.setName(newName.trim());
        return account;
	}

    @Override
    public Account getBusinessObject(String name) {
        return accounts.get(name);
    }

    @Override
    public ArrayList<Account> getBusinessObjects() {
		ArrayList<Account> col = new ArrayList<Account>();
		for(Account account : accounts.values()) {
			col.add(account);
		}
		return col;
	}

    public void removeAccount(Account account) throws NotEmptyException {
        if(account.getBookings().isEmpty()){
            accounts.remove(account.getName());
        } else {
            throw new NotEmptyException();
        }
    }
}