package be.dafke.BusinessActions;

import be.dafke.BusinessModel.Accounts;

import java.util.EventListener;

/**
 * Created by ddanneels on 5/11/2016.
 */
public interface AccountsListener extends EventListener{
    void setAccounts(Accounts accounts);
}
