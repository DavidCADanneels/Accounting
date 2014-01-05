package be.dafke.BasicAccounting.Objects;

import be.dafke.ObjectModel.BusinessObject;
import be.dafke.Utils.Utils;

import java.math.BigDecimal;
import java.util.TreeMap;

/**
 * User: Dafke
 * Date: 9/03/13
 * Time: 17:01
 */
public class Movement extends BusinessObject{
    private static final String JOURNAL = "journal";
    private static final String DATE = "date";
    private static final String DEBIT = "debit";
    private static final String CREDIT = "credit";
    private static final String DESCRIPTION = "description";
    private static final String NR = "nr";
    private BigDecimal amount;
    private boolean debit;
    private Booking booking;

    public Movement(BigDecimal amount, boolean debit){
        this.amount = amount;
        this.debit = debit;
    }

    @Override
    public TreeMap<String,String> getInitProperties() {
        TreeMap<String,String> properties = super.getInitProperties();
        Transaction transaction = booking.getTransaction();
        properties.put(NR,transaction.getAbbreviation() + transaction.getId());
        properties.put(JOURNAL,transaction.getJournal().getName());
        properties.put(DATE, Utils.toString(transaction.getDate()));
        if(debit){
            properties.put(DEBIT, amount.toString());
        } else {
            properties.put(CREDIT, amount.toString());
        }
        properties.put(DESCRIPTION, Utils.toString(transaction.getDate()));

        return properties;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public boolean isDebit() {
        return debit;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
