package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.ObjectModel.Exceptions.NotEmptyException;

import java.util.ArrayList;

/**
 * @author David Danneels
 */
public class Project extends BusinessCollection<Account> {
	/**
	 * 
	 */
	private final ProjectAccounts projectAccounts;
	private final Balance resultBalance, relationsBalance;

	public Project(String name, Accounts accounts, AccountTypes accountTypes) {
		setName(name);
		projectAccounts = new ProjectAccounts();
		Balances balances = new Balances(accounts, accountTypes);
		resultBalance = balances.createResultBalance(projectAccounts);
		relationsBalance = balances.createRelationsBalance(projectAccounts);
	}

	@Override
	public Account addBusinessObject(Account account) throws EmptyNameException, DuplicateNameException {
		projectAccounts.addBusinessObject(account);
		return account;
	}

	@Override
	public ArrayList<Account> getBusinessObjects(){
		return projectAccounts.getBusinessObjects();
	}

	@Override
	public Account getBusinessObject(String name){
		return projectAccounts.getBusinessObject(name);
	}

	@Override
	public void removeBusinessObject(Account account) throws NotEmptyException {
		projectAccounts.removeBusinessObject(account);
	}

	public ProjectJournal getJournal() {
		ProjectJournal journal = new ProjectJournal(getName(), "TMP");
		for(Account account:projectAccounts.getBusinessObjects()){
			for(Movement movement :account.getBusinessObjects()){
				Transaction transaction = movement.getBooking().getTransaction();
				journal.addBusinessObject(transaction);
			}
		}
		return journal;
	}

	public Balance getResultBalance() {
		return resultBalance;
	}

	public Balance getRelationsBalance() {
		return relationsBalance;
	}
}
