package be.dafke.BasicAccounting.Objects;

import be.dafke.BasicAccounting.AccountingExtension;
import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessObject;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author David Danneels
 */
public class Accounting extends BusinessCollection<BusinessCollection<BusinessObject>> {
    private final AccountTypes accountTypes;
    private final Accounts accounts;
	private final Journals journals;
    private final JournalTypes journalTypes;
    private ArrayList<String> keys;
    private List<AccountingExtension> extensions;

    public void addExtension(AccountingExtension extension){
        extensions.add(extension);
        extension.extendConstructor(this);
    }
    public List<AccountingExtension> getExtensions() {
        return extensions;
    }
    @Override
    public String getChildType(){
        return "Accounting";
    }

    public Accounting(String name) {
        setName(name);
        extensions = new ArrayList<AccountingExtension>();
        // TODO use Accounts<Account> + modifiy Accounts file ... Accounts<T extends

        accountTypes = new AccountTypes();

        accounts = new Accounts();
        accounts.setBusinessTypeCollection(accountTypes);

        journalTypes = new JournalTypes();
        journalTypes.addDefaultType(accountTypes);

        journals = new Journals();
        journals.setBusinessTypeCollection(journalTypes);
        journals.setBusinessCollection(accounts);

        accounts.setName(accounts.getBusinessObjectType());
        journals.setName(journals.getBusinessObjectType());

        try {
            addBusinessObject((BusinessCollection)accounts);
            addBusinessObject((BusinessCollection)journals);
        } catch (EmptyNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (DuplicateNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        keys = new ArrayList<String>();
//        keys.add(accountTypes.getBusinessObjectType());
        keys.add(accounts.getBusinessObjectType());
        keys.add(journals.getBusinessObjectType());
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
//        BusinessCollection<BusinessObject> collection = collections.get(name);
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
    public Journals getJournals() {
        return journals;
    }
    //
    public JournalTypes getJournalTypes() {
        return journalTypes;
    }
}