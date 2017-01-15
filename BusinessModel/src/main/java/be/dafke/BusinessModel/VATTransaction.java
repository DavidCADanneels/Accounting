package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.NotEmptyException;

import java.util.ArrayList;

/**
 * Created by ddanneels on 28/12/2016.
 */
public class VATTransaction extends BusinessCollection<VATBooking>{

    public static final String VATBOOKING = "VATBooking";
    private ArrayList<VATBooking> vatBookings = new ArrayList<>();

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
    public void removeBusinessObject(VATBooking value) throws NotEmptyException {
        vatBookings.remove(value);
    }

        @Override
    public VATBooking addBusinessObject(VATBooking value) {
        vatBookings.add(value);
        return value;
    }
}
