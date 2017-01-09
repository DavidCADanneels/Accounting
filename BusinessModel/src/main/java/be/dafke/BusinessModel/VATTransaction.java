package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by ddanneels on 28/12/2016.
 */
public class VATTransaction extends BusinessCollection<VATBooking>{

    public static final String VATBOOKING = "VATBooking";
    private ArrayList<VATBooking> vatBookings = new ArrayList<>();

    @Override
    public String getChildType() {
        return VATBOOKING;
    }

    @Override
    public VATBooking createNewChild(TreeMap<String, String> properties) {
//        VATBooking vatBooking = new VATBooking();
//        return vatBooking;
        return null;
    }

    public enum PurchaseType{
        GOODS, SERVICES, INVESTMENTS;
    }

    public enum VATType{
        SALE, PURCHASE, NONE;
    }

    public ArrayList<VATBooking> getBusinessObjects(){
        return vatBookings;
    }

    @Override
    public VATBooking addBusinessObject(VATBooking value) {
        vatBookings.add(value);
        return value;
    }
}
