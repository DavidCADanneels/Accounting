package be.dafke.BasicAccounting.Objects;

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
    private final JournalTypes journalTypes;
    private final Balances balances;
    private final Mortgages mortgages;
    private ArrayList<String> keys;

    @Override
    public String getChildType(){
        return Accountings.ACCOUNTING;
    }

    public Accounting() {
        accountTypes = new AccountTypes();

        accounts = new Accounts();
        accounts.setBusinessTypeCollection(accountTypes);

        journalTypes = new JournalTypes();
        journalTypes.addDefaultType(accountTypes);

        journals = new Journals();
        journals.setBusinessTypeCollection(journalTypes);
        journals.setBusinessCollection(accounts);

        balances = new Balances();
        balances.setBusinessCollection(accounts);
        balances.setBusinessTypeCollection(accountTypes);
        balances.addDefaultBalances(accountTypes);

        mortgages = new Mortgages();
        mortgages.setBusinessTypeCollection(accountTypes);
        mortgages.setBusinessCollection(accounts);

        accounts.setName(accounts.getBusinessObjectType());
        journals.setName(journals.getBusinessObjectType());

        try {
            addBusinessObject((BusinessCollection)accounts);
            addBusinessObject((BusinessCollection)journals);
            addBusinessObject((BusinessCollection)balances);
            addBusinessObject((BusinessCollection)mortgages);
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
        keys.add(mortgages.getBusinessObjectType());
	}

    public void addKey(String key){
        keys.add(key);
    }

    public String toString(){
        return getName();
    }

    @Override
    public BusinessCollection createNewChild() {
       System.err.println("Never called ??");
        return null;
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
}