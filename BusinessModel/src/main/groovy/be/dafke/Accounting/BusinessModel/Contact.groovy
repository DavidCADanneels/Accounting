package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessObject

class Contact extends BusinessObject{
    String streetAndNumber = ""
    String vatNumber = ""
    String postalCode = ""
    String city = ""
    String countryCode = ""
    String email = ""
    String phone = ""
    String officialName = ""
    BigDecimal turnOver = BigDecimal.ZERO
    BigDecimal VATTotal = BigDecimal.ZERO
    Account customerAccount = null
    Account supplierAccount = null

    Contact() {

    }

    Contact(Contact contact, Accounts accounts) {
        setName(contact.name)
        vatNumber = contact.vatNumber
        postalCode = contact.postalCode
        city = contact.city
        countryCode = contact.countryCode
        email = contact.email
        phone = contact.phone
        officialName = contact.officialName
        Account otherCustomerAccount = contact.customerAccount
        if(otherCustomerAccount){
            String accountName = otherCustomerAccount.name
            customerAccount = accounts.getBusinessObject(accountName)
        }
        Account otherSupplierAccount = contact.supplierAccount
        if(otherSupplierAccount){
            String accountName = otherSupplierAccount.name
            supplierAccount = accounts.getBusinessObject(accountName)
        }
    }

    enum ContactType{
        // TODO: add 'OWN' to store Own Company Details ? (or continue using ... )
        ALL, CUSTOMERS, SUPPLIERS
    }

    boolean isSupplier() {
        supplierAccount!=null
    }

    boolean isCustomer() {
        customerAccount!=null
    }

    boolean withDebts(){
        // Customer
        isCustomer() && customerAccount.saldo != 0
    }

    boolean withCredits(){
        // Supplier
        isSupplier() && supplierAccount.saldo != 0
    }

    void increaseTurnOver(BigDecimal amount){
        turnOver = turnOver.add(amount)
        turnOver = turnOver.setScale(2)
    }

    void increaseVATTotal(BigDecimal amount){
        VATTotal = VATTotal.add(amount)
        VATTotal = VATTotal.setScale(2)
    }

    void decreaseTurnOver(BigDecimal amount){
        turnOver = turnOver.subtract(amount)
        turnOver = turnOver.setScale(2)
    }

    void decreaseVATTotal(BigDecimal amount){
        VATTotal = VATTotal.subtract(amount)
        VATTotal = VATTotal.setScale(2)
    }

    void setTurnOver(BigDecimal turnOver) {
        this.turnOver = turnOver
    }

    void setVATTotal(BigDecimal VATTotal) {
        this.VATTotal = VATTotal
    }

    void setCustomerAccount(Account customerAccount) {
        this.customerAccount = customerAccount
    }

    void setSupplierAccount(Account supplierAccount) {
        this.supplierAccount = supplierAccount
    }
}
