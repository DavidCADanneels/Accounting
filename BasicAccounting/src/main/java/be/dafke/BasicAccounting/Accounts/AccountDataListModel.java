package be.dafke.BasicAccounting.Accounts;

import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.AccountType;
import be.dafke.BusinessModel.Accounts;
import be.dafke.Utils.AlphabeticListModel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by ddanneels on 7/05/2017.
 */
public class AccountDataListModel extends AlphabeticListModel<Account> implements AccountDataModel{
    private Predicate<Account> filter;
    private Accounts accounts;
    private List<AccountType> accountTypes;

    public void setAccounts(Accounts accounts) {
        this.accounts = accounts;
    }

    @Override
    public void setFilter(Predicate<Account> filter) {
        this.filter=filter;
        filter();
    }

    @Override
    public void setAccountTypes(List<AccountType> accountTypes) {
        this.accountTypes=accountTypes;
        filter();
    }

    public void filter() {
        List<Account> filteredAccounts = getFilteredAccounts();
        removeAllElements();
        for(Account account:filteredAccounts){
            addElement(account);
        }
    }

    private List<Account> getFilteredAccounts(){
        if(filter==null){
            return accounts.getAccountsByType(accountTypes).stream().collect(Collectors.toCollection(ArrayList::new));
        }
        return accounts.getAccountsByType(accountTypes).stream().filter(filter).collect(Collectors.toCollection(ArrayList::new));
    }

}
