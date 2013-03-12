package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Objects.WriteableBusinessObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

public class CounterParty extends WriteableBusinessObject {
	/**
	 * 
	 */
    private final ArrayList<String> aliases;
	private final HashMap<String, BankAccount> accounts;
    private final ArrayList<BankAccount> accountsList;
	private Collection<String> addressLines;
    protected static final String ACCOUNTNUMBER = "AccountNumber";
    protected static final String BIC = "Bic";
    protected static final String CURRENCY = "Currency";
    protected static final String ALIAS = "Alias";
    protected static final String ADDRESS = "Address";

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

    public void setAddressLines(Collection<String> addressLines) {
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

    private void parseAliasesString(String aliasesString){
        if(aliasesString!=null){
            String[] aliasesStrings = aliasesString.split("\\Q | \\E");
            for(String s : aliasesStrings){
                addAlias(s);
            }
        }
    }

    private void parseAddressLinesString(String addressLinesString){
        if(addressLinesString!=null){
            String[] addressLinesStrings = addressLinesString.split("\\Q | \\E");
            for(String s : addressLinesStrings){
                addressLines.add(s);
            }
        }

    }

    private void parseAccountNumberString(String accountNumberString){
        if(accountNumberString!=null){
            String[] accountNumberStrings = accountNumberString.split("\\Q | \\E");
            for(String s : accountNumberStrings){
                addAccount(new BankAccount(s));
            }
        }
    }

    private void parseBicsString(String bicsString){
        if(bicsString!=null){
            String[] bicsStrings = bicsString.split("\\Q | \\E");
            for(int i=0;i<bicsStrings.length;i++){
                accountsList.get(i).setBic(bicsStrings[i]);
            }
        }
    }

    private void parseCurrenciesString(String currenciesString){
        if(currenciesString!=null){
            String[] currenciesStrings = currenciesString.split("\\Q | \\E");
            for(int i=0;i<currenciesStrings.length;i++){
                accountsList.get(i).setCurrency(currenciesStrings[i]);
            }
        }
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

    public String getAddressLinesString() {
        if(addressLines.size()==0){
            return "";
        }
        StringBuilder builder = new StringBuilder(aliases.get(0));
        for(int i=1;i<addressLines.size();i++){
            builder.append(" | ").append(aliases.get(i));
        }
        return builder.toString();
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
    public Set<String> getInitKeySet() {
        Set<String> keySet = super.getInitKeySet();
        keySet.add(ACCOUNTNUMBER);
        keySet.add(ADDRESS);
        keySet.add(ALIAS);
        keySet.add(BIC);
        keySet.add(CURRENCY);
        return keySet;
    }

    @Override
    public TreeMap<String,String> getInitProperties() {
        TreeMap<String, String> properties = super.getUniqueProperties();
        properties.put(ACCOUNTNUMBER, getBankAccountsString());
        properties.put(ALIAS, getAliasesString());
        properties.put(BIC,getBICString());
        properties.put(CURRENCY,getCurrencyString());
        properties.put(ADDRESS, getAddressLinesString());
        return properties;
    }

    @Override
    public void setInitProperties(TreeMap<String, String> properties) {
        super.setInitProperties(properties);
        String aliasesString = properties.get(ALIAS);
        if(aliasesString!=null){
            parseAliasesString(aliasesString);
        }
        String accountNumberString = properties.get(ACCOUNTNUMBER);
        if(accountNumberString!=null){
            parseAccountNumberString(accountNumberString);
        }
        String addressLines = properties.get(ADDRESS);
        if(addressLines!=null){
            parseAddressLinesString(addressLines);
        }
        String bicString = properties.get(BIC);
        if(bicString!=null){
            parseBicsString(bicString);
        }
        String currenciesString = properties.get(CURRENCY);
        if(currenciesString!=null){
            parseCurrenciesString(currenciesString);
        }
    }

    @Override
    public TreeMap<String,String> getUniqueProperties(){
        TreeMap<String,String> keyMap = super.getUniqueProperties();
        for(BankAccount bankAccount:accountsList){
            keyMap.put(ACCOUNTNUMBER, bankAccount.getAccountNumber());
        }
        return keyMap;
    }
}
