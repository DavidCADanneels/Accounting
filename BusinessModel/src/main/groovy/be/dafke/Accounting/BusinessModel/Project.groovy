package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessCollection
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.Accounting.ObjectModel.Exceptions.NotEmptyException

class Project extends BusinessCollection<Account> {
    /**
     *
     */
    final ProjectAccounts projectAccounts
    final Balance resultBalance, relationsBalance

    Project(String name, Accounts accounts, AccountTypes accountTypes) {
        setName(name)
        projectAccounts = new ProjectAccounts()
        Balances balances = new Balances(accounts, accountTypes)
        resultBalance = balances.createResultBalance(projectAccounts)
        relationsBalance = balances.createRelationsBalance(projectAccounts)
    }

    @Override
    Account addBusinessObject(Account account) throws EmptyNameException, DuplicateNameException {
        projectAccounts.addBusinessObject(account)
        account
    }

    @Override
    ArrayList<Account> getBusinessObjects(){
        projectAccounts.businessObjects
    }

    @Override
    Account getBusinessObject(String name){
        projectAccounts.getBusinessObject(name)
    }

    @Override
    void removeBusinessObject(Account account) throws NotEmptyException {
        projectAccounts.removeBusinessObject(account)
    }

    ProjectJournal getJournal() {
        ProjectJournal journal = new ProjectJournal(getName(), "TMP")
        for(Account account:projectAccounts.businessObjects){
            for(Movement movement :account.businessObjects){
                Transaction transaction = movement.booking.transaction
                journal.addBusinessObject(transaction)
            }
        }
        journal
    }

    Balance getResultBalance() {
        resultBalance
    }

    Balance getRelationsBalance() {
        relationsBalance
    }
}
