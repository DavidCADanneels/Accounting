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
    private Calendar date;
    private ArrayList<VATBooking> vatBookings = new ArrayList<>();
    private Transaction transaction;
    private Integer id;

    public Integer getId() {
        return id;
    }

    public VATTransaction(Calendar date) {
        this(++count, date);
    }

    public VATTransaction(int id, Calendar date) {
        this.id = id;
        this.date = date;
        count++;
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
    public void removeBusinessObject(VATBooking value) throws NotEmptyException {
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

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }
}
