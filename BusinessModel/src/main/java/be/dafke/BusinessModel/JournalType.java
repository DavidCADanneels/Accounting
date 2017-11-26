package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

public class JournalType extends BusinessObject {
    private VATTransaction.VATType vatType = null;
    private AccountsList left, right;

    public JournalType(JournalType journalType, AccountTypes accountTypes){
        this(journalType.getName(), accountTypes);
        vatType = journalType.getVatType();
        for(AccountType accountType:journalType.left.getAccountTypes()){
            left.setTypeAvailable(accountType, Boolean.TRUE);
        }
        for(AccountType accountType:journalType.right.getAccountTypes()){
            right.setTypeAvailable(accountType, Boolean.TRUE);
        }
    }

    public JournalType(String name, AccountTypes accountTypes){
        setName(name);
        left = new AccountsList(accountTypes, false);
        right = new AccountsList(accountTypes, false);
    }

    public AccountsList getLeft() {
        return left;
    }

    public AccountsList getRight() {
        return right;
    }

    public void setLeft(AccountsList left) {
        this.left = left;
    }

    public void setRight(AccountsList right) {
        this.right = right;
    }

    public VATTransaction.VATType getVatType() {
        return vatType;
    }

    public void setVatType(VATTransaction.VATType vatType) {
        this.vatType = vatType;
        left.setVatType(calculateLeftVatType(vatType));
        right.setVatType(calculateRightVatType(vatType));
    }

    public void switchVatTypes(){
        VATTransaction.VATType tmp = left.getVatType();
        left.setVatType(right.getVatType());
        right.setVatType(tmp);
    }

    public VATTransaction.VATType getLeftVatType() {
        return left.getVatType();
    }

    public VATTransaction.VATType getRightVatType() {
        return right.getVatType();
    }

    public static VATTransaction.VATType calculateLeftVatType(VATTransaction.VATType journalVatType){
        if (journalVatType == VATTransaction.VATType.SALE) {
            return VATTransaction.VATType.CUSTOMER;
        } else if (journalVatType == VATTransaction.VATType.PURCHASE) {
            return VATTransaction.VATType.PURCHASE;
        } else {
            return null;
        }
    }

    public static VATTransaction.VATType calculateRightVatType(VATTransaction.VATType journalVatType){
        if (journalVatType == VATTransaction.VATType.SALE) {
            return VATTransaction.VATType.SALE;
        } else {
            return null;
        }
    }
}