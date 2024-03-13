package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessObject

//enum JournalTypeName{
//
//}


class JournalType extends BusinessObject {

    static String DEFAULT_TYPE = "default"
    static String PAYMENT_TYPE = "Payments"
    static String SALE_TYPE = "Sale"
    static String PURCHASE_TYPE = "Purchase"

    VATTransaction.TransactionType transactionType = null
    AccountsList left, right

    JournalType(JournalType journalType){
        this(journalType.name)
        transactionType = journalType.getTransactionType()
        for(AccountType accountType:journalType.left.accountTypes){
            left.setTypeAvailable(accountType, Boolean.TRUE)
        }
        for(AccountType accountType:journalType.right.accountTypes){
            right.setTypeAvailable(accountType, Boolean.TRUE)
        }
    }

    JournalType(String name){
        setName(name)
        left = new AccountsList()
        right = new AccountsList()
    }

    static JournalType createJournalType(String name, String type, AccountTypes accountTypes){
//        if(type == JournalType.DEFAULT_TYPE) {
            JournalType journalType = new JournalType(name)
            journalType.addAllAccountTypes(accountTypes)
            return journalType
//        } else if(type == JournalType.PURCHASE_TYPE){
//
//        }
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

    VATTransaction.TransactionType getTransactionType() {
        transactionType
    }

    void setTransactionType(VATTransaction.TransactionType vatType) {
        this.transactionType = vatType
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

    static VATTransaction.VATType calculateLeftVatType(VATTransaction.TransactionType transactionType){
        if (transactionType == VATTransaction.TransactionType.SALE) {
            VATTransaction.VATType.SALE
        } else if (transactionType == VATTransaction.TransactionType.PURCHASE) {
            VATTransaction.VATType.PURCHASE
        } else {
            null
        }
    }

    static VATTransaction.VATType calculateRightVatType(VATTransaction.TransactionType transactionType){
        if (transactionType == VATTransaction.TransactionType.SALE) {
            VATTransaction.VATType.CUSTOMER
        } else {
            null
        }
    }
}