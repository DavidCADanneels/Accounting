package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;
import be.dafke.ObjectModel.MustBeRead;

import java.util.ArrayList;
import java.util.Properties;
import java.util.stream.Collectors;

public class JournalType extends BusinessObject implements MustBeRead {

    public static final String DEBIT_TYPES = "debitTypes";
    public static final String CREDIT_TYPES = "creditTypes";

    private ArrayList<AccountType> debetTypes, creditTypes;

    public JournalType(String name){
        setName(name);
        debetTypes = new ArrayList<>();
        creditTypes = new ArrayList<>();
    }

    public Properties getOutputProperties(){
        Properties properties = super.getOutputProperties();
        String debitStream = debetTypes.stream().map(AccountType::getName).collect(Collectors.joining(","));
        String creditStream = creditTypes.stream().map(AccountType::getName).collect(Collectors.joining(","));
        System.out.println(debitStream);
        System.out.println(debetTypes.toString());
        properties.put(DEBIT_TYPES,debitStream);
        properties.put(CREDIT_TYPES,creditStream);
        return properties;
    }

    public void addDebetType(AccountType accountType){
        debetTypes.add(accountType);
    }

    public void addCreditType(AccountType accountType){
        creditTypes.add(accountType);
    }

    public ArrayList<AccountType> getDebetTypes() {
        return debetTypes;
    }

    public void setDebetTypes(ArrayList<AccountType> debetTypes) {
        this.debetTypes = debetTypes;
    }

    public ArrayList<AccountType> getCreditTypes() {
        return creditTypes;
    }

    public void setCreditTypes(ArrayList<AccountType> creditTypes) {
        this.creditTypes = creditTypes;
    }
}