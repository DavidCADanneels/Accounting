package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.ObjectModel.Exceptions.NotEmptyException;

import java.util.Properties;
import java.util.stream.Collectors;

public class JournalType extends BusinessObject {

    public static final String DEBIT_TYPES = "debitTypes";
    public static final String CREDIT_TYPES = "creditTypes";
    public static final String VATTYPE = "vatType";

    private AccountTypes debetTypes, creditTypes;
    private VATTransaction.VATType vatType = VATTransaction.VATType.NONE;

    public JournalType(String name){
        setName(name);
        debetTypes = new AccountTypes();
        creditTypes = new AccountTypes();
    }

//    public VATTransactions getVatTransactions() {
//        return vat;
//    }
//
//    public void setVat(VATTransactions vat) {
//        this.vat = vat;
//    }


    public VATTransaction.VATType getVatType() {
        return vatType;
    }

    public void setVatType(VATTransaction.VATType vatType) {
        this.vatType = vatType;
    }

    public Properties getOutputProperties(){
        Properties properties = new Properties();
        properties.put(NAME,getName());
        String debitStream = debetTypes.getBusinessObjects().stream().map(AccountType::getName).collect(Collectors.joining(","));
        String creditStream = creditTypes.getBusinessObjects().stream().map(AccountType::getName).collect(Collectors.joining(","));
        properties.put(DEBIT_TYPES,debitStream);
        properties.put(CREDIT_TYPES,creditStream);
        properties.put(VATTYPE,vatType.toString());
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