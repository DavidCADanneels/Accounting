package be.dafke.Accounting.Objects.Coda;

import be.dafke.Accounting.Objects.Accounting.Account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class CounterParty implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String name;
    private final ArrayList<String> aliases;
	private final HashMap<String, BankAccount> accounts;
	private final Collection<String> addressLines;

	private Account account;

    // private final ArrayList<Account> debetAccounts, creditAccounts;

	public CounterParty(String name) {
		accounts = new HashMap<String, BankAccount>();
		addressLines = new ArrayList<String>();
		this.name = name;
		aliases = new ArrayList<String>();
		// debetAccounts = new ArrayList<Account>();
		// creditAccounts = new ArrayList<Account>();
	}

	public void addAccount(BankAccount newAccount) {
		accounts.put(newAccount.getAccountNumber(), newAccount);
	}

	@Override
	public String toString() {
		return name;
	}

    public ArrayList<String> getAliases(){
        return aliases;
    }

    public void addAlias(String alias){
        aliases.add(alias);
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

    public String getAliasesString() {
        if(aliases.size()==0){
            return "";
        }
        StringBuilder builder = new StringBuilder(aliases.get(0));
        for(int i=1;i<aliases.size();i++){
            builder.append(" | " + aliases.get(i));
        }
        return builder.toString();
    }

    public String getBankAccountsString() {
        if(accounts.size() == 0){
            return "";
        }
        else{
            Iterator<BankAccount> it = accounts.values().iterator();
            StringBuilder builder = new StringBuilder(it.next().getAccountNumber());
            while(it.hasNext()){
                builder.append(" | " + it.next().getAccountNumber());
            }
            return builder.toString();
        }
    }

    public String getBICString() {
        if(accounts.size() == 0){
            return "";
        }
        else{
            Iterator<BankAccount> it = accounts.values().iterator();
            StringBuilder builder = new StringBuilder(it.next().getBic());
            while(it.hasNext()){
                builder.append(" | " + it.next().getBic());
            }
            return builder.toString();
        }
    }

    public String getCurrencyString() {
        if(accounts.size() == 0){
            return "";
        }
        else{
            Iterator<BankAccount> it = accounts.values().iterator();
            StringBuilder builder = new StringBuilder(it.next().getCurrency());
            while(it.hasNext()){
                builder.append(" | " + it.next().getCurrency());
            }
            return builder.toString();
        }
    }
}
