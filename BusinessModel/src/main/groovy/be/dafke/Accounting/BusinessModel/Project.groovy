package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessCollection
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.Accounting.ObjectModel.Exceptions.NotEmptyException

class Project extends BusinessCollection<Account> {
    /**
     *
     */
    private final ProjectAccounts projectAccounts
    private final Balance resultBalance, relationsBalance

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
        projectAccounts.getBusinessObjects()
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
        for(Account account:projectAccounts.getBusinessObjects()){
            for(Movement movement :account.getBusinessObjects()){
                Transaction transaction = movement.getBooking().getTransaction()
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
