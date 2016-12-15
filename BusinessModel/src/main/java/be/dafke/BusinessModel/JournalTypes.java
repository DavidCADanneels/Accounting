package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.ObjectModel.MustBeRead;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import static be.dafke.BusinessModel.JournalType.CREDIT_TYPES;
import static be.dafke.BusinessModel.JournalType.DEBIT_TYPES;

public class JournalTypes extends BusinessCollection<JournalType> implements MustBeRead {
    private AccountTypes accountTypes;

    public JournalTypes(AccountTypes accountTypes) {
        setName("JournalTypes");
        this.accountTypes = accountTypes;
    }

    @Override
    public String getChildType(){
        return "JournalType";
    }

    @Override
    public JournalType createNewChild(TreeMap<String, String> properties) {
        String name = properties.get(NAME)==null?"":properties.get(NAME);
        JournalType journalType = new JournalType(name);
        String debitTypes = properties.get(DEBIT_TYPES)==null?"":properties.get(DEBIT_TYPES);
        String creditTypes = properties.get(CREDIT_TYPES)==null?"":properties.get(CREDIT_TYPES);
        ArrayList<AccountType> debitAccountTypes = new ArrayList<>();
        ArrayList<AccountType> creditAccountTypes = new ArrayList<>();
        for(String s:debitTypes.split(",")) {
            debitAccountTypes.add(accountTypes.getBusinessObject(s));
        }
        for(String s:creditTypes.split(",")) {
            creditAccountTypes.add(accountTypes.getBusinessObject(s));
        }
        journalType.setDebetTypes(debitAccountTypes);
        journalType.setCreditTypes(creditAccountTypes);
        return journalType;
    }

    @Override
    public Set<String> getInitKeySet(){
        Set<String> keySet = new TreeSet<>();
        keySet.add(NAME);
        keySet.add(CREDIT_TYPES);
        keySet.add(DEBIT_TYPES);
        return keySet;
    }

    public void addDefaultType(AccountTypes accountTypes) {
        JournalType defaultType = new JournalType("default");
        defaultType.setDebetTypes(accountTypes.getBusinessObjects());
        defaultType.setCreditTypes(accountTypes.getBusinessObjects());
        try {
            addBusinessObject(defaultType);
        } catch (EmptyNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (DuplicateNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
