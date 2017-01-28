package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.ObjectModel.Exceptions.NotEmptyException;

public class JournalType extends BusinessObject {
    private AccountTypes debetTypes, creditTypes;
    private Account singleAccount = null;
    private VATTransaction.VATType vatType = VATTransaction.VATType.NONE;

    public JournalType(JournalType journalType){
        this(journalType.getName());
        vatType = journalType.getVatType();
        for(AccountType accountType:journalType.debetTypes.getBusinessObjects()){
            try {
                debetTypes.addBusinessObject(accountType);
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }
        for(AccountType accountType:journalType.creditTypes.getBusinessObjects()){
            try {
                creditTypes.addBusinessObject(accountType);
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }
    }

    public JournalType(String name){
        setName(name);
        debetTypes = new AccountTypes();
        creditTypes = new AccountTypes();
    }

    public Account getSingleAccount() {
        return singleAccount;
    }

    public void setSingleAccount(Account singleAccount) {
        this.singleAccount = singleAccount;
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