package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessObject;
import be.dafke.ObjectModel.ChildrenNeedSeparateFile;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.ObjectModel.MustBeRead;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * @author David Danneels
 */
public class Accounting extends BusinessCollection<BusinessCollection<BusinessObject>> implements MustBeRead, ChildrenNeedSeparateFile {
    private final AccountTypes accountTypes;
    private final Accounts accounts;
	private final Journals journals;
    private final JournalTypes journalTypes;
    private final Balances balances;
    private final Mortgages mortgages;
    private ArrayList<String> keys;
    private Projects projects;
    private Contacts contacts;

    @Override
    public String getChildType(){
        return Accountings.ACCOUNTING;
    }

    public Accounting() {
        accountTypes = new AccountTypes();
        accounts = new Accounts(accountTypes);

        journalTypes = new JournalTypes();
        journalTypes.addDefaultType(accountTypes);

        journals = new Journals(accounts, journalTypes);

        balances = new Balances(accounts, accountTypes);

        contacts = new Contacts();

        mortgages = new Mortgages(accounts);

        projects = new Projects(accounts, accountTypes);

        try {
//            addBusinessObject((BusinessCollection)accountTypes);
            addBusinessObject((BusinessCollection)accounts);
//            addBusinessObject((BusinessCollection)journalTypes);
            addBusinessObject((BusinessCollection)journals);
            addBusinessObject((BusinessCollection)balances);
            addBusinessObject((BusinessCollection)contacts);
            addBusinessObject((BusinessCollection)mortgages);
            addBusinessObject((BusinessCollection)projects);
        } catch (EmptyNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (DuplicateNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        keys = new ArrayList<>();
//        keys.add(accountTypes.getBusinessObjectType());
        keys.add(accounts.getBusinessObjectType());
//        keys.add(journalTypes.getBusinessObjectType());
        keys.add(journals.getBusinessObjectType());
        keys.add(balances.getBusinessObjectType());
        keys.add(contacts.getBusinessObjectType());
        keys.add(mortgages.getBusinessObjectType());
        keys.add(projects.getBusinessObjectType());
	}

    public void addKey(String key){
        keys.add(key);
    }

    public String toString(){
        return getName();
    }

    @Override
    public BusinessCollection createNewChild(TreeMap<String, String> properties) {
       System.err.println("Never called ??");
        return null;
    }

    @Override
    public ArrayList<BusinessCollection<BusinessObject>> getBusinessObjects(){
        ArrayList<BusinessCollection<BusinessObject>> objects = new ArrayList<>();
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
    public Journals getJournals() {
        return journals;
    }
    //
    public JournalTypes getJournalTypes() {
        return journalTypes;
    }

    public Balances getBalances() {
        return balances;
    }

    public Mortgages getMortgages() {
        return mortgages;
    }

    public Projects getProjects() {
        return projects;
    }

    public Contacts getContacts() {
        return contacts;
    }
}