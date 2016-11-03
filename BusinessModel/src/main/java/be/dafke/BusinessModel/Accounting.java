package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessObject;
import be.dafke.ObjectModel.ChildrenNeedSeparateFile;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.ObjectModel.MustBeRead;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.stream.Collectors;

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

    @Override
    public String getChildType(){
        return Accountings.ACCOUNTING;
    }

    public Accounting() {
        accountTypes = new AccountTypes();
        accounts = new Accounts(this);

        journalTypes = new JournalTypes();
        journalTypes.addDefaultType(accountTypes);

        journals = new Journals(accounts, journalTypes);

        balances = new Balances(accounts, accountTypes);

        mortgages = new Mortgages();
        mortgages.setAccountTypes(accountTypes);
        mortgages.setAccounts(accounts);

        projects = new Projects(this);

        accounts.setName(accounts.getBusinessObjectType());
        journals.setName(journals.getBusinessObjectType());

        try {
            addBusinessObject((BusinessCollection)accounts);
            addBusinessObject((BusinessCollection)journals);
            addBusinessObject((BusinessCollection)balances);
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
        keys.add(journals.getBusinessObjectType());
        keys.add(balances.getBusinessObjectType());
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
        return keys.stream().map(this::getBusinessObject).collect(Collectors.toCollection(ArrayList::new));
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
}