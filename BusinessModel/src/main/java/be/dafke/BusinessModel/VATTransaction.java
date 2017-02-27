package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.NotEmptyException;

import java.util.ArrayList;

/**
 * Created by ddanneels on 28/12/2016.
 */
public class VATTransaction extends BusinessCollection<VATBooking>{
    private static int count = 0;
    private ArrayList<VATBooking> vatBookings = new ArrayList<>();
    private Integer id;

    public Integer getId() {
        return id;
    }

    public VATTransaction() {
        this(++count);
    }

    public VATTransaction(int id) {
        this.id = id;
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
    public void removeBusinessObject(VATBooking value) throws NotEmptyException {
        vatBookings.remove(value);
    }

        @Override
    public VATBooking addBusinessObject(VATBooking value) {
        vatBookings.add(value);
        return value;
    }
}
