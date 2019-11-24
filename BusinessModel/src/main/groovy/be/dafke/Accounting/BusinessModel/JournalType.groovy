package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessObject

class JournalType extends BusinessObject {
    private VATTransaction.VATType vatType = null
    private AccountsList left, right

    JournalType(JournalType journalType){
        this(journalType.getName())
        vatType = journalType.getVatType()
        for(AccountType accountType:journalType.left.getAccountTypes()){
            left.setTypeAvailable(accountType, Boolean.TRUE)
        }
        for(AccountType accountType:journalType.right.getAccountTypes()){
            right.setTypeAvailable(accountType, Boolean.TRUE)
        }
    }

    JournalType(String name){
        setName(name)
        left = new AccountsList()
        right = new AccountsList()
    }

    void addAllAccountTypes(AccountTypes accountTypes){
        left.addAllTypes(accountTypes, false)
        right.addAllTypes(accountTypes, false)
    }

    AccountsList getLeft() {
        left
    }

    AccountsList getRight() {
        right
    }

    void setLeft(AccountsList left) {
        this.left = left
    }

    void setRight(AccountsList right) {
        this.right = right
    }

    VATTransaction.VATType getVatType() {
        vatType
    }

    void setVatType(VATTransaction.VATType vatType) {
        this.vatType = vatType
    }

    void switchVatTypes(){
        VATTransaction.VATType tmp = left.getVatType()
        left.setVatType(right.getVatType())
        right.setVatType(tmp)
    }

    VATTransaction.VATType getLeftVatType() {
        left.getVatType()
    }

    VATTransaction.VATType getRightVatType() {
        right.getVatType()
    }

    static VATTransaction.VATType calculateLeftVatType(VATTransaction.VATType journalVatType){
        if (journalVatType == VATTransaction.VATType.SALE) {
            VATTransaction.VATType.SALE
        } else if (journalVatType == VATTransaction.VATType.PURCHASE) {
            VATTransaction.VATType.PURCHASE
        } else {
            null
        }
    }

    static VATTransaction.VATType calculateRightVatType(VATTransaction.VATType journalVatType){
        if (journalVatType == VATTransaction.VATType.SALE) {
            VATTransaction.VATType.CUSTOMER
        } else {
            null
        }
    }
}