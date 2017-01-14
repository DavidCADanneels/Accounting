package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.ObjectModel.Exceptions.NotEmptyException;
import be.dafke.ObjectModel.MustBeRead;

import java.util.ArrayList;
import java.util.Properties;
import java.util.TreeMap;

/**
 * @author David Danneels
 */
public class Project extends BusinessCollection<Account> implements MustBeRead {
	/**
	 * 
	 */
	private final Accounts allAccounts;  // needed to lookup existing accounts when adding them to the project
	private final ProjectAccounts projectAccounts;
	private final Balance resultBalance, relationsBalance;

	@Override
	public String getChildType() {
		return "Account";
	}

	public Project(String name, Accounts accounts, AccountTypes accountTypes) {
		setName(name);
		allAccounts = accounts;
		projectAccounts = new ProjectAccounts(accountTypes);
		Balances balances = new Balances(accounts, accountTypes);
		resultBalance = balances.createResultBalance(projectAccounts);
		relationsBalance = balances.createRelationsBalance(projectAccounts);
	}

	@Override
	public Account createNewChild(TreeMap<String, String> properties) {
		return allAccounts.getBusinessObject(properties.get(NAME));
	}

	@Override
	public Properties getOutputProperties() {
		Properties outputMap = new Properties();
		outputMap.put(NAME,getName());
		return outputMap;
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
		ProjectJournal journal = new ProjectJournal(allAccounts, getName(), "TMP");
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
