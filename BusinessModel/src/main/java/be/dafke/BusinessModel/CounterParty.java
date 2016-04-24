package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;
import be.dafke.Utils.Utils;

import java.util.*;

public class CounterParty extends BusinessObject {
	/**
	 *
	 */
	private final HashMap<String, BankAccount> accounts;
    private final ArrayList<BankAccount> accountsList;
	private ArrayList<String> addressLines;
    private ArrayList<String> aliases;

    private Account account;
    private boolean mergeable = true;

    // private final ArrayList<Account> debetAccounts, creditAccounts;

	public CounterParty() {
		accounts = new HashMap<String, BankAccount>();
        accountsList = new ArrayList<BankAccount>();
		addressLines = new ArrayList<String>();
		aliases = new ArrayList<String>();
		// debetAccounts = new ArrayList<Account>();
		// creditAccounts = new ArrayList<Account>();
	}

    public void setAddressLines(ArrayList<String> addressLines) {
        this.addressLines = addressLines;
    }

    @Override
    public boolean isMergeable(){
        return mergeable;
    }

	public void addAccount(BankAccount newAccount) {
		accounts.put(newAccount.getAccountNumber(), newAccount);
        accountsList.add(newAccount);
	}

    public ArrayList<BankAccount> getAccountsList() {
        return accountsList;
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

    public String getBankAccountsString() {
        Iterator<BankAccount> it = accountsList.iterator();
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
        Iterator<BankAccount> it = accountsList.iterator();
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
        Iterator<BankAccount> it = accountsList.iterator();
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

    // KeySets and Properties

    @Override
    public Properties getOutputProperties() {
        Properties properties = new Properties();
        properties.put(NAME,getName());
        properties.put(CounterParties.ACCOUNTNUMBER, getBankAccountsString());
        properties.put(CounterParties.ALIAS, Utils.toString(aliases));
        properties.put(CounterParties.BIC,getBICString());
        properties.put(CounterParties.CURRENCY,getCurrencyString());
        properties.put(CounterParties.ADDRESS, Utils.toString(addressLines));
        return properties;
    }

    @Override
    public TreeMap<String,String> getUniqueProperties(){
        TreeMap<String,String> keyMap = super.getUniqueProperties();
        for(BankAccount bankAccount:accountsList){
            keyMap.put(CounterParties.ACCOUNTNUMBER, bankAccount.getAccountNumber());
        }
        return keyMap;
    }

    public void setAliases(ArrayList<String> aliases) {
        this.aliases = aliases;
    }
}
