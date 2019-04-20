package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

import java.math.BigDecimal;

public class Contact extends BusinessObject{
    private String streetAndNumber = "";
    private String vatNumber = "";
    private String postalCode = "";
    private String city = "";
    private String countryCode = "";
    private String email = "";
    private String phone = "";
    private String officialName = "";
    private BigDecimal turnOver = BigDecimal.ZERO;
    private BigDecimal VATTotal = BigDecimal.ZERO;
    private Account customerAccount = null;
    private Account supplierAccount = null;

    public Contact() {

    }

    public Contact(Contact contact, Accounts accounts) {
        setName(contact.getName());
        vatNumber = contact.vatNumber;
        postalCode = contact.postalCode;
        city = contact.city;
        countryCode = contact.countryCode;
        email = contact.email;
        phone = contact.phone;
        officialName = contact.officialName;
        Account otherCustomerAccount = contact.getCustomerAccount();
        if(otherCustomerAccount!=null){
            String accountName = otherCustomerAccount.getName();
            customerAccount = accounts.getBusinessObject(accountName);
        }
        Account otherSupplierAccount = contact.getSupplierAccount();
        if(otherSupplierAccount!=null){
            String accountName = otherSupplierAccount.getName();
            supplierAccount = accounts.getBusinessObject(accountName);
        }
    }

    public enum ContactType{
        // TODO: add 'OWN' to store Own Company Details ? (or continue using ... )
        ALL, CUSTOMERS, SUPPLIERS;
    }

    public boolean isSupplier() {
        return supplierAccount!=null;
    }

    public boolean isCustomer() {
        return customerAccount!=null;
    }

    public String getStreetAndNumber() {
        return streetAndNumber;
    }

    public void setStreetAndNumber(String streetAndNumber) {
        this.streetAndNumber = streetAndNumber;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public String getOfficialName() {
        return officialName;
    }

    public void setOfficialName(String officialName) {
        this.officialName = officialName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public BigDecimal getTurnOver() {
        return turnOver;
    }

    public BigDecimal getVATTotal() {
        return VATTotal;
    }

    public Account getCustomerAccount() {
        return customerAccount;
    }

    public Account getSupplierAccount() {
        return supplierAccount;
    }

    public void increaseTurnOver(BigDecimal amount){
        turnOver = turnOver.add(amount);
        turnOver.setScale(2);
    }

    public void increaseVATTotal(BigDecimal amount){
        VATTotal = VATTotal.add(amount);
        VATTotal = VATTotal.setScale(2);
    }

    public void decreaseTurnOver(BigDecimal amount){
        turnOver = turnOver.subtract(amount);
        turnOver = turnOver.setScale(2);
    }

    public void decreaseVATTotal(BigDecimal amount){
        VATTotal = VATTotal.subtract(amount);
        VATTotal = VATTotal.setScale(2);
    }

    public void setTurnOver(BigDecimal turnOver) {
        this.turnOver = turnOver;
    }

    public void setVATTotal(BigDecimal VATTotal) {
        this.VATTotal = VATTotal;
    }

    public void setCustomerAccount(Account customerAccount) {
        this.customerAccount = customerAccount;
    }

    public void setSupplierAccount(Account supplierAccount) {
        this.supplierAccount = supplierAccount;
    }
}
