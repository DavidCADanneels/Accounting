package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessObject;
import be.dafke.ObjectModel.ChildrenNeedSeparateFile;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.ObjectModel.MustBeRead;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author David Danneels
 */
public class Accounting extends BusinessCollection<BusinessCollection<BusinessObject>> implements MustBeRead, ChildrenNeedSeparateFile {
    public static final String DEBIT_ACCOUNT = "DebitAccount";
    public static final String CREDIT_ACCOUNT = "CreditAccount";
    private final AccountTypes accountTypes;
    private final Accounts accounts;
	private final Journals journals;
    private final JournalTypes journalTypes;
    private final Balances balances;
    private final Mortgages mortgages;
    private Projects projects;
    private Contacts contacts;
    private VATTransactions vatTransactions;

    @Override
    public String getChildType(){
        return Accountings.ACCOUNTING;
    }

    public Accounting() {
        accountTypes = new AccountTypes();
//        TODO: remove this line once we safe AccountTypes in a separate file
        accountTypes.addDefaultTypes();
        accounts = new Accounts(accountTypes);

        journalTypes = new JournalTypes(accountTypes);
//        journalTypes.addDefaultType(accountTypes);

        vatTransactions = new VATTransactions();

        journals = new Journals(accounts, journalTypes, vatTransactions);

        balances = new Balances(accounts, accountTypes);

        contacts = new Contacts();

        mortgages = new Mortgages(accounts);

        projects = new Projects(accounts, accountTypes);

        try {
//            addBusinessObject((BusinessCollection)accountTypes);
            addBusinessObject((BusinessCollection)accounts);
            addBusinessObject((BusinessCollection)journalTypes);
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
	}

    public Properties getOutputProperties(){
        Properties outputProperties = super.getOutputProperties();
        for(Map.Entry<Integer, BigDecimal> entry : vatTransactions.getVatAccounts().entrySet()){
            outputProperties.put("VAT"+entry.getKey(),entry.getValue());
        }
        Account vatDebitAccount = vatTransactions.getDebitAccount();
        if(vatDebitAccount!=null) {
            outputProperties.put(DEBIT_ACCOUNT, vatDebitAccount);
        }
        Account vatCreditAccount = vatTransactions.getCreditAccount();
        if(vatCreditAccount!=null) {
            outputProperties.put(CREDIT_ACCOUNT, vatCreditAccount);
        }
        return outputProperties;
    }

    public String toString(){
        return getName();
    }

    @Override
    public BusinessCollection createNewChild(TreeMap<String, String> properties) {
       System.err.println("Never called ??");
        return null;
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

    public VATTransactions getVatTransactions() {
        return vatTransactions;
    }

    public Set<String> getInitKeySet(){
        Set<String> keySet = super.getInitKeySet();
        keySet.add("VAT1");
        keySet.add("VAT2");
        keySet.add("VAT3");
        keySet.add("VAT81");
        keySet.add("VAT82");
        keySet.add("VAT83");
        keySet.add("VAT54");
        keySet.add("VAT59");
        return keySet;
    }
}