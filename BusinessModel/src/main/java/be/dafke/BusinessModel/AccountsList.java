package be.dafke.BusinessModel;

import java.util.function.Predicate;

/**
 * Created by ddanneels on 26/05/2017.
 */
public class AccountsList {
    private boolean singleAccount = false;
    private Account account = null;
    private Accounts accounts = null;
    private Predicate<Account> filter = null;

    public AccountsList(Accounts accounts) {
        this.accounts = accounts;
    }

    public void setSingleAccount(boolean singleAccount) {
        this.singleAccount = singleAccount;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
