package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.ObjectModel.Exceptions.NotEmptyException;
import be.dafke.ObjectModel.MustBeRead;

import java.util.Properties;
import java.util.stream.Collectors;

public class JournalType extends BusinessObject implements MustBeRead {

    public static final String DEBIT_TYPES = "debitTypes";
    public static final String CREDIT_TYPES = "creditTypes";

    private AccountTypes debetTypes, creditTypes;
    private boolean tax = false;

    public JournalType(String name){
        setName(name);
        debetTypes = new AccountTypes();
        creditTypes = new AccountTypes();
    }

    public void setTax(boolean tax) {
        this.tax = tax;
    }

    public boolean isTax() {
        return tax;
    }

    public Properties getOutputProperties(){
        Properties properties = super.getOutputProperties();
        String debitStream = debetTypes.getBusinessObjects().stream().map(AccountType::getName).collect(Collectors.joining(","));
        String creditStream = creditTypes.getBusinessObjects().stream().map(AccountType::getName).collect(Collectors.joining(","));
        properties.put(DEBIT_TYPES,debitStream);
        properties.put(CREDIT_TYPES,creditStream);
        return properties;
    }

    public void addDebetType(AccountType accountType) throws EmptyNameException, DuplicateNameException {
        debetTypes.addBusinessObject(accountType);
    }

    public void addCreditType(AccountType accountType) throws EmptyNameException, DuplicateNameException {
        creditTypes.addBusinessObject(accountType);
    }

    public AccountTypes getDebetTypes() {
        return debetTypes;
    }

    public void setDebetTypes(AccountTypes debetTypes) {
        this.debetTypes = debetTypes;
    }

    public AccountTypes getCreditTypes() {
        return creditTypes;
    }

    public void setCreditTypes(AccountTypes creditTypes) {
        this.creditTypes = creditTypes;
    }

    public void removeCreditType(AccountType type) {
        try {
            creditTypes.removeBusinessObject(type);
        } catch (NotEmptyException e) {
            e.printStackTrace();
        }
    }

    public void removeDebitType(AccountType type){
        try {
            debetTypes.removeBusinessObject(type);
        } catch (NotEmptyException e) {
            e.printStackTrace();
        }
    }
}