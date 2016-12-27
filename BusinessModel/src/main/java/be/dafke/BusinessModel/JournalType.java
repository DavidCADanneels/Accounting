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
    public static final String VATTYPE = "vatType";

    private AccountTypes debetTypes, creditTypes;
    private VAT.VATType vatType = VAT.VATType.NONE;

    public JournalType(String name){
        setName(name);
        debetTypes = new AccountTypes();
        creditTypes = new AccountTypes();
    }

//    public VAT getVat() {
//        return vat;
//    }
//
//    public void setVat(VAT vat) {
//        this.vat = vat;
//    }


    public VAT.VATType getVatType() {
        return vatType;
    }

    public void setVatType(VAT.VATType vatType) {
        this.vatType = vatType;
    }

    public Properties getOutputProperties(){
        Properties properties = super.getOutputProperties();
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