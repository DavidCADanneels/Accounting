package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.ObjectModel.MustBeRead;

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
        String[] debits = debitTypes.split(",");
        String[] credits = creditTypes.split(",");
        for(String s:debits) {
            if(!"".equals(s)) {
                AccountType accountType = accountTypes.getBusinessObject(s);
                if (accountType != null) {
                    try {
                        journalType.addDebetType(accountType);
                    } catch (EmptyNameException e) {
                        e.printStackTrace();
                    } catch (DuplicateNameException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        for(String s:credits) {
            if(!"".equals(s)) {
                AccountType accountType = accountTypes.getBusinessObject(s);
                if(accountType!=null) {
                    try {
                        journalType.addCreditType(accountType);
                    } catch (EmptyNameException e) {
                        e.printStackTrace();
                    } catch (DuplicateNameException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
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
        defaultType.setDebetTypes(accountTypes);
        defaultType.setCreditTypes(accountTypes);
        try {
            addBusinessObject(defaultType);
        } catch (EmptyNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (DuplicateNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
