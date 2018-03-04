package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

import java.math.BigDecimal;

/**
 * Created by ddanneels on 15/11/2016.
 */
public class Contact extends BusinessObject{
    private boolean supplier, customer;
    private String streetAndNumber = "";
    private String vatNumber = "";
    private String postalCode = "";
    private String city = "";
    private String countryCode = "";
    private String email = "";
    private String phone = "";
    private BigDecimal turnOver = BigDecimal.ZERO;
    private BigDecimal VATTotal = BigDecimal.ZERO;

    public enum ContactType{
        ALL, CUSTOMERS, SUPPLIERS;
    }

    public boolean isSupplier() {
        return supplier;
    }

    public void setSupplier(boolean supplier) {
        this.supplier = supplier;
    }

    public boolean isCustomer() {
        return customer;
    }

    public void setCustomer(boolean customer) {
        this.customer = customer;
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

    public void increaseTurnOver(BigDecimal amount){
        turnOver = turnOver.add(amount);
        turnOver.setScale(2);
    }

    public void increaseVATTotal(BigDecimal amount){
        VATTotal = VATTotal.add(amount);
        VATTotal.setScale(2);
    }

    public void decreaseTurnOver(BigDecimal amount){
        turnOver = turnOver.subtract(amount);
        turnOver.setScale(2);
    }

    public void decreaseVATTotal(BigDecimal amount){
        VATTotal = VATTotal.subtract(amount);
        VATTotal.setScale(2);
    }

    public void setTurnOver(BigDecimal turnOver) {
        this.turnOver = turnOver;
    }

    public void setVATTotal(BigDecimal VATTotal) {
        this.VATTotal = VATTotal;
    }
}
