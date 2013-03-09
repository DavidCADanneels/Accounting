package be.dafke.Accounting.Objects.Accounting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CounterParty extends WriteableBusinessObject {
	/**
	 * 
	 */
    private final ArrayList<String> aliases;
	private final HashMap<String, BankAccount> accounts;
	private final Collection<String> addressLines;
    protected static final String ACCOUNTNUMBER = "accountNumber";

	private Account account;
    private boolean mergeable = true;

    // private final ArrayList<Account> debetAccounts, creditAccounts;

	public CounterParty() {
		accounts = new HashMap<String, BankAccount>();
		addressLines = new ArrayList<String>();
		aliases = new ArrayList<String>();
		// debetAccounts = new ArrayList<Account>();
		// creditAccounts = new ArrayList<Account>();
	}

    @Override
    public Map<String,String> getKeyMap(){
        Map<String,String> keyMap = new HashMap<String, String>();
        for(BankAccount bankAccount:accounts.values()){
            keyMap.put(ACCOUNTNUMBER, bankAccount.getAccountNumber());
        }
//        for(String alias:aliases){
//            keyMap.put(NAME, alias);
//        }
//        keyMap.put(NAME, getName());
        return keyMap;
    }

    @Override
    public boolean isMergeable(){
        return mergeable;
    }

	public void addAccount(BankAccount newAccount) {
		accounts.put(newAccount.getAccountNumber(), newAccount);
	}

    public ArrayList<String> getAliases(){
        return aliases;
    }

    public void addAlias(String alias){
        if(!aliases.contains(alias)){
            aliases.add(alias);
        }
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
            builder.append(" | ").append(aliases.get(i));
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
            builder.append(" | ").append(accountNumber!=null?accountNumber:"");
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
            builder.append(" | ").append(bic!=null?bic : "");
        }
        return builder.toString();
    }

    public String getCurrencyString() {
        Iterator<BankAccount> it = accounts.values().iterator();
        StringBuilder builder = new StringBuilder();
        if(it.hasNext()){
            String currency = it.next().getCurrency();
            builder.append(currency != null ? currency : "");
        }
        while(it.hasNext()){
            String currency = it.next().getCurrency();
            builder.append(" | ").append(currency!=null?currency:"");
        }
        return builder.toString();
    }

    public void removeAlias(String alias) {
        aliases.remove(alias);
    }

    public void setMergeable(boolean mergeable) {
        this.mergeable = mergeable;
    }
}
