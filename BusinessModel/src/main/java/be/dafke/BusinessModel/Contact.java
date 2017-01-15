package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

import java.math.BigDecimal;
import java.util.Properties;

import static be.dafke.BusinessModel.Contacts.ADDRESS_LINE_1;
import static be.dafke.BusinessModel.Contacts.ADDRESS_LINE_2;
import static be.dafke.BusinessModel.Contacts.TVA_NUMBER;

/**
 * Created by ddanneels on 15/11/2016.
 */
public class Contact extends BusinessObject{
    private boolean supplier, customer;
    private String addressLine1 = "";
    private String addressLine2 = "";
    private String vatNumber = "";
    private String postalCode = "";
    private String city = "";
    private String countryCode = "";
    private String email = "";
    private String phone = "";
    private BigDecimal turnOver = BigDecimal.ZERO;
    private BigDecimal VATTotal = BigDecimal.ZERO;

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

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public Properties getOutputProperties(){
        Properties properties = super.getOutputProperties();
        if(addressLine1!=null) properties.put(ADDRESS_LINE_1,addressLine1);
        if(addressLine2!=null) properties.put(ADDRESS_LINE_2,addressLine2);
        if(vatNumber !=null) properties.put(TVA_NUMBER, vatNumber);
        return properties;
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
}
