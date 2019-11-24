package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessObject

class Contact extends BusinessObject{
    private String streetAndNumber = ""
    private String vatNumber = ""
    private String postalCode = ""
    private String city = ""
    private String countryCode = ""
    private String email = ""
    private String phone = ""
    private String officialName = ""
    private BigDecimal turnOver = BigDecimal.ZERO
    private BigDecimal VATTotal = BigDecimal.ZERO
    private Account customerAccount = null
    private Account supplierAccount = null

    Contact() {

    }

    Contact(Contact contact, Accounts accounts) {
        setName(contact.getName())
        vatNumber = contact.vatNumber
        postalCode = contact.postalCode
        city = contact.city
        countryCode = contact.countryCode
        email = contact.email
        phone = contact.phone
        officialName = contact.officialName
        Account otherCustomerAccount = contact.getCustomerAccount()
        if(otherCustomerAccount!=null){
            String accountName = otherCustomerAccount.getName()
            customerAccount = accounts.getBusinessObject(accountName)
        }
        Account otherSupplierAccount = contact.getSupplierAccount()
        if(otherSupplierAccount!=null){
            String accountName = otherSupplierAccount.getName()
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

    String getStreetAndNumber() {
        streetAndNumber
    }

    void setStreetAndNumber(String streetAndNumber) {
        this.streetAndNumber = streetAndNumber
    }

    void setPostalCode(String postalCode) {
        this.postalCode = postalCode
    }

    void setCity(String city) {
        this.city = city
    }

    void setCountryCode(String countryCode) {
        this.countryCode = countryCode
    }

    void setEmail(String email) {
        this.email = email
    }

    void setPhone(String phone) {
        this.phone = phone
    }

    String getVatNumber() {
        vatNumber
    }

    void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber
    }

    String getOfficialName() {
        officialName
    }

    void setOfficialName(String officialName) {
        this.officialName = officialName
    }

    String getPostalCode() {
        postalCode
    }

    String getCity() {
        city
    }

    String getCountryCode() {
        countryCode
    }

    String getEmail() {
        email
    }

    String getPhone() {
        phone
    }

    BigDecimal getTurnOver() {
        turnOver
    }

    BigDecimal getVATTotal() {
        VATTotal
    }

    Account getCustomerAccount() {
        customerAccount
    }

    Account getSupplierAccount() {
        supplierAccount
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
