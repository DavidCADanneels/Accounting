package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

public class JournalType extends BusinessObject {
    private VATTransaction.VATType vatType = null;
    private AccountsList left, right;

    public JournalType(JournalType journalType, AccountTypes accountTypes){
        this(journalType.getName(), accountTypes);
        vatType = journalType.getVatType();
        for(AccountType accountType:journalType.left.getAccountTypes()){
            addLeftType(accountType);
        }
        for(AccountType accountType:journalType.right.getAccountTypes()){
            addRightType(accountType);
        }
    }

    public JournalType(String name, AccountTypes accountTypes){
        setName(name);
        left = new AccountsList(accountTypes);
        right = new AccountsList(accountTypes);
    }

    public AccountsList getLeft() {
        return left;
    }

    public AccountsList getRight() {
        return right;
    }

    public VATTransaction.VATType getVatType() {
        return vatType;
    }

    public void setVatType(VATTransaction.VATType vatType) {
        this.vatType = vatType;
    }

    public void addLeftType(AccountType accountType) {
        if (accountType != null) {
            left.setTypeAvailable(accountType, Boolean.TRUE);
        }
    }

    public void addRightType(AccountType accountType) {
        if (accountType != null) {
            right.setTypeAvailable(accountType, Boolean.TRUE);
        }
    }
}