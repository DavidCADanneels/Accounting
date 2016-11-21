package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

import java.util.Properties;

import static be.dafke.BusinessModel.Contacts.ADDRESS_LINE_1;
import static be.dafke.BusinessModel.Contacts.ADDRESS_LINE_2;
import static be.dafke.BusinessModel.Contacts.TVA_NUMBER;

/**
 * Created by ddanneels on 15/11/2016.
 */
public class Contact extends BusinessObject{
    private String addressLine1, addressLine2, tvaNumber;

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

    public String getTvaNumber() {
        return tvaNumber;
    }

    public void setTvaNumber(String tvaNumber) {
        this.tvaNumber = tvaNumber;
    }

    public Properties getOutputProperties(){
        Properties properties = super.getOutputProperties();
        if(addressLine1!=null) properties.put(ADDRESS_LINE_2,addressLine2);
        if(addressLine2!=null) properties.put(ADDRESS_LINE_1,addressLine1);
        if(tvaNumber!=null) properties.put(TVA_NUMBER, tvaNumber);
        return properties;
    }
}
