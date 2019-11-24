package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessObject

class CounterParty extends BusinessObject {
    /**
     *
     */
    private final HashMap<String, BankAccount> accounts
    private final ArrayList<BankAccount> accountsList
    private ArrayList<String> addressLines
    private ArrayList<String> aliases

    private Account account
    private boolean mergeable = true

    // private final ArrayList<Account> debetAccounts, creditAccounts

    CounterParty() {
        accounts = new HashMap<String, BankAccount>()
        accountsList = new ArrayList<BankAccount>()
        addressLines = new ArrayList<String>()
        aliases = new ArrayList<String>()
        // debetAccounts = new ArrayList<Account>()
        // creditAccounts = new ArrayList<Account>()
    }

    void setAddressLines(ArrayList<String> addressLines) {
        this.addressLines = addressLines
    }

    @Override
    boolean isMergeable(){
        mergeable
    }

    void addAccount(BankAccount newAccount) {
        accounts.put(newAccount.getAccountNumber(), newAccount)
        accountsList.add(newAccount)
    }

    ArrayList<BankAccount> getAccountsList() {
        accountsList
    }

    ArrayList<String> getAliases(){
        aliases
    }

    void addAlias(String alias){
        if(!aliases.contains(alias)){
            aliases.add(alias)
        }
    }

    HashMap<String, BankAccount> getBankAccounts() {
        accounts
    }

    void setAccount(Account account) {
        this.account = account
    }

    Account getAccount() {
        account
    }

    String getBankAccountsString() {
        Iterator<BankAccount> it = accountsList.iterator()
        StringBuilder builder = new StringBuilder()
        if(it.hasNext()){
            String accountNumber = it.next().getAccountNumber()
            builder.append(accountNumber!=null?accountNumber:"")
        }
        while(it.hasNext()){
            String accountNumber = it.next().getAccountNumber()
            builder.append(" | ").append(accountNumber!=null?accountNumber:"")
        }
        builder.toString()
    }

    String getBICString() {
        Iterator<BankAccount> it = accountsList.iterator()
        StringBuilder builder = new StringBuilder()
        if(it.hasNext()){
            String bic = it.next().getBic()
            builder.append(bic!=null?bic : "")
        }
        while(it.hasNext()){
            String bic = it.next().getBic()
            builder.append(" | ").append(bic!=null?bic : "")
        }
        builder.toString()
    }

    String getCurrencyString() {
        Iterator<BankAccount> it = accountsList.iterator()
        StringBuilder builder = new StringBuilder()
        if(it.hasNext()){
            String currency = it.next().getCurrency()
            builder.append(currency != null ? currency : "")
        }
        while(it.hasNext()){
            String currency = it.next().getCurrency()
            builder.append(" | ").append(currency!=null?currency:"")
        }
        builder.toString()
    }

    void removeAlias(String alias) {
        aliases.remove(alias)
    }

    void setMergeable(boolean mergeable) {
        this.mergeable = mergeable
    }

    // KeySets and Properties

    Properties getOutputProperties() {
        Properties properties = new Properties()
        properties.put(NAME,getName())
        properties.put(CounterParties.ACCOUNTNUMBER, getBankAccountsString())
        properties.put(CounterParties.ALIAS, Utils.toString(aliases))
        properties.put(CounterParties.BIC,getBICString())
        properties.put(CounterParties.CURRENCY,getCurrencyString())
        properties.put(CounterParties.ADDRESS, Utils.toString(addressLines))
        properties
    }

    @Override
    TreeMap<String,String> getUniqueProperties(){
        TreeMap<String,String> keyMap = super.getUniqueProperties()
        for(BankAccount bankAccount:accountsList){
            keyMap.put(CounterParties.ACCOUNTNUMBER, bankAccount.getAccountNumber())
        }
        keyMap
    }

    void setAliases(ArrayList<String> aliases) {
        this.aliases = aliases
    }
}
