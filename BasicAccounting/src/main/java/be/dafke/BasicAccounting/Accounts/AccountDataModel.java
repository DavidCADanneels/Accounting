package be.dafke.BasicAccounting.Accounts;

import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.AccountType;

import java.util.List;
import java.util.function.Predicate;

/**
 * Created by ddanneels on 7/05/2017.
 */
public interface AccountDataModel {
    void setFilter(Predicate<Account> filter);
    void setAccountTypes(List<AccountType> accountTypes);
}
