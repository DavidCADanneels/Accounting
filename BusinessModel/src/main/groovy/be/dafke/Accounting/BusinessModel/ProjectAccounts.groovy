package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.Exceptions.NotEmptyException

class ProjectAccounts extends Accounts {

    // Need to override remove function
    // In Accounts we check if Account.isDeletable(), not needed here.
    void removeBusinessObject(Account account) throws NotEmptyException {
        removeBusinessObject(account.getUniqueProperties())
    }
}