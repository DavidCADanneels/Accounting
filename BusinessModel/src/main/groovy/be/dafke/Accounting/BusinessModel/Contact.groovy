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

    Account supplierAccount = null

    Customer customer = null

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
        if(contact.customer){
            customer = new Customer()
            String accountName = contact.customer.customerAccount
            customer.customerAccount = accounts.getBusinessObject(accountName)
        }
        Account otherSupplierAccount = contact.supplierAccount
        if(otherSupplierAccount){
            String accountName = otherSupplierAccount.name
            supplierAccount = accounts.getBusinessObject(accountName)
        }
    }

    Customer getCustomer() {
        return customer
    }

    void setCustomer(Customer customer) {
        this.customer = customer
    }

    enum ContactType{
        // TODO: add 'OWN' to store Own Company Details ? (or continue using ... )
        ALL, CUSTOMERS, SUPPLIERS
    }

    boolean isSupplier() {
        supplierAccount!=null
    }

    void setCustomerAccount(Account customerAccount) {
        if(!customer) customer = new Customer()
        customer.customerAccount = customerAccount
    }

    void setSupplierAccount(Account supplierAccount) {
        this.supplierAccount = supplierAccount
    }
}
