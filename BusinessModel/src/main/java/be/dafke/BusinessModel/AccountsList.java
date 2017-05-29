package be.dafke.BusinessModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Created by ddanneels on 26/05/2017.
 */
public class AccountsList {
    private boolean singleAccount = false;
    private Account account = null;
    private Predicate<Account> filter = null;
    private HashMap<AccountType, Boolean> availableAccountTypes = new HashMap<>();

    public AccountsList(AccountTypes accountTypes) {
        this(accountTypes, false);
    }

    public AccountsList(AccountTypes accountTypes, boolean enabled) {
        accountTypes.getBusinessObjects().forEach(accountType -> {
            availableAccountTypes.put(accountType,enabled);
        });
    }

    public void setTypeAvailable(AccountType accountType, boolean available){
        availableAccountTypes.put(accountType, available);
    }

    public boolean isTypeAvailable(AccountType accountType){
        return availableAccountTypes.get(accountType);
    }

    public void setSingleAccount(boolean singleAccount) {
        this.singleAccount = singleAccount;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public boolean isSingleAccount() {
        return singleAccount;
    }

    public Account getAccount() {
        return account;
    }

    public ArrayList<AccountType> getAccountTypes() {
        ArrayList<AccountType> accountTypes = new ArrayList<>();
        for(Map.Entry<AccountType, Boolean> entry : availableAccountTypes.entrySet()){
            if(entry.getValue()){
                accountTypes.add(entry.getKey());
            }
        }
        return accountTypes;
    }
}
