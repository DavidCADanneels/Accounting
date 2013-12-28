package be.dafke.BasicAccounting.Objects;

import be.dafke.Coda.Objects.CounterParties;
import be.dafke.Coda.Objects.Statements;
import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessObject;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import java.util.ArrayList;

/**
 * @author David Danneels
 */
public class Accounting extends BusinessCollection<BusinessCollection<BusinessObject>> {
    private final AccountTypes accountTypes;
    private final Accounts accounts;
	private final Journals journals;
	private final Projects projects;
    private final JournalTypes journalTypes;
    private final CounterParties counterParties;
    private final Statements statements;
    private final Balances balances;
    private ArrayList<String> keys;

    @Override
    public String getChildType(){
        return "";
    }

    public Accounting(String name) {
        setName(name);
        // TODO use Accounts<Account> + modifiy Accounts file ... Accounts<T extends

        accountTypes = new AccountTypes();

        accounts = new Accounts();
        accounts.setBusinessTypeCollection(accountTypes);

        journalTypes = new JournalTypes();
        journalTypes.addDefaultType(accountTypes);

        journals = new Journals();
        journals.setBusinessTypeCollection(journalTypes);

        balances = new Balances();
        balances.setBusinessCollection(accounts);
        balances.setBusinessTypeCollection(accountTypes);
//        balances.addDefaultBalances(this);

        counterParties = new CounterParties();

        statements = new Statements();
        statements.setBusinessCollection(counterParties);

        projects = new Projects();

        accounts.setName(accounts.getBusinessObjectType());
        journals.setName(journals.getBusinessObjectType());
        balances.setName(balances.getBusinessObjectType());
        counterParties.setName(counterParties.getBusinessObjectType());
        statements.setName(statements.getBusinessObjectType());

        try {
            addBusinessObject((BusinessCollection)accounts);
            addBusinessObject((BusinessCollection)journals);
            addBusinessObject((BusinessCollection)balances);
            addBusinessObject((BusinessCollection)statements);
            addBusinessObject((BusinessCollection)counterParties);
        } catch (EmptyNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (DuplicateNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        keys = new ArrayList<String>();
//        keys.add(accountTypes.getBusinessObjectType());
        keys.add(accounts.getBusinessObjectType());
        keys.add(journals.getBusinessObjectType());
        keys.add(balances.getBusinessObjectType());
        keys.add(counterParties.getBusinessObjectType());
        keys.add(statements.getBusinessObjectType());
	}

    public void addKey(String key){
        keys.add(key);
    }

    public String toString(){
        return getName();
    }

    @Override
    public BusinessCollection createNewChild(String name) {
        BusinessCollection<BusinessObject> collection = getBusinessObject(name);
//        WriteableBusinessCollection<WriteableBusinessObject> collection = collections.get(name);
        if(collection==null){
//            collection =
            System.err.println("Accounting does not have a collection with the name: " + name);
        }
        return collection;
    }

    @Override
    public ArrayList<BusinessCollection<BusinessObject>> getBusinessObjects(){
        ArrayList<BusinessCollection<BusinessObject>> objects = new ArrayList<BusinessCollection<BusinessObject>>();
        for(String key:keys){
            objects.add(getBusinessObject(key));
        }
        return objects;
    }

    // Collections
    //
    public AccountTypes getAccountTypes() {
        return accountTypes;
    }

    public Accounts getAccounts() {
        return accounts;
    }
    //
    public Projects getProjects() {
        return projects;
    }
    //
    public Journals getJournals() {
        return journals;
    }
    //
    public JournalTypes getJournalTypes() {
        return journalTypes;
    }
    //
    public Balances getBalances(){
        return balances;
    }
    //
    public CounterParties getCounterParties() {
        return counterParties;
    }
    //
    public Statements getStatements() {
        return statements;
    }
}