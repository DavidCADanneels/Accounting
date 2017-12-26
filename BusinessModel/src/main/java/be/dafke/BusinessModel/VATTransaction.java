package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.NotEmptyException;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ddanneels on 28/12/2016.
 */
public class VATTransaction extends BusinessCollection<VATBooking>{
    private static int count = 0;
    private ArrayList<VATBooking> vatBookings = new ArrayList<>();
    private Transaction transaction;
    private Integer id;
    private boolean registered = false;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public static int raiseCount(){
        return ++count;
    }

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        VATTransaction.count = count;
    }

    public void setRegistered() {
        registered = true;
    }

    public boolean isRegistered() {
        return registered;
    }

    public enum PurchaseType{
        GOODS, SERVICES, INVESTMENTS;
    }

    public enum VATType{
        SALE, PURCHASE, CUSTOMER;
    }

    public ArrayList<VATBooking> getBusinessObjects(){
        return vatBookings;
    }

    @Override
    public void removeBusinessObject(VATBooking value) {
        vatBookings.remove(value);
    }

    @Override
    public VATBooking addBusinessObject(VATBooking value) {
        vatBookings.add(value);
        value.setVatTransaction(this);
        return value;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

}
