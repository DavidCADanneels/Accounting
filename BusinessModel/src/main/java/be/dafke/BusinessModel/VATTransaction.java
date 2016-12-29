package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by ddanneels on 28/12/2016.
 */
public class VATTransaction extends BusinessCollection<VATBooking>{

    public static final String VATBOOKING = "VATBooking";
    private VATFields vatFields;

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


    private HashMap<Integer,BigDecimal> data;

    public enum PurchaseType{
        GOODS, SERVICES, INVESTMENTS;
    }

    public enum VATType{
        SALE, PURCHASE, NONE;
    }
}
