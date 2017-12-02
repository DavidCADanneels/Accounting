package be.dafke.BusinessModel;

import be.dafke.ObjectModel.Exceptions.NotEmptyException;

/**
 * Created by ddanneels on 25/12/2016.
 */
public class ProjectAccounts extends Accounts {

    // Need to override remove function
    // In Accounts we check if Account.isDeletable(), not needed here.
    public void removeBusinessObject(Account account) throws NotEmptyException {
        removeBusinessObject(account.getUniqueProperties());
    }
}
