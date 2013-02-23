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
        if(!aliases.contains(alias)){
            aliases.add(alias);
        }
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
        Iterator<BankAccount> it = accounts.values().iterator();
        StringBuilder builder = new StringBuilder();
        if(it.hasNext()){
            String accountNumber = it.next().getAccountNumber();
            builder.append(accountNumber!=null?accountNumber:"");
        }
        while(it.hasNext()){
            String accountNumber = it.next().getAccountNumber();
            builder.append(" | " + (accountNumber!=null?accountNumber:""));
        }
        return builder.toString();
    }

    public String getBICString() {
        Iterator<BankAccount> it = accounts.values().iterator();
        StringBuilder builder = new StringBuilder();
        if(it.hasNext()){
            String bic = it.next().getBic();
            builder.append(bic!=null?bic : "");
        }
        while(it.hasNext()){
            String bic = it.next().getBic();
            builder.append(" | " + (bic!=null?bic : ""));
        }
        return builder.toString();
    }

    public String getCurrencyString() {
        Iterator<BankAccount> it = accounts.values().iterator();
        StringBuilder builder = new StringBuilder();
        if(it.hasNext()){
            String currency = it.next().getCurrency();
            builder.append(currency!=null?currency:"");
        }
        while(it.hasNext()){
            String currency = it.next().getCurrency();
            builder.append(" | " + (currency!=null?currency:""));
        }
        return builder.toString();
    }
}
