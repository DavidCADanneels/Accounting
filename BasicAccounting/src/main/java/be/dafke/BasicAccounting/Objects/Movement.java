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
    private static int count = 0;
    private static final String JOURNAL_NAME = "journalName";
    private static final String DATE = "date";
    private static final String DEBIT = "debit";
    private static final String CREDIT = "credit";
    private static final String DESCRIPTION = "description";
    private static final String JOURNAL_ID = "journalId";
    private static final String JOURNAL_ABBR = "journalAbbr";
    private static final String ID = "id";
    private BigDecimal amount;
    private boolean debit;
    private Booking booking;
    private Integer id;

    public Movement(BigDecimal amount, boolean debit){
        this.amount = amount;
        this.debit = debit;
        id = ++count;
    }

    @Override
    public TreeMap<String,String> getInitProperties() {
        TreeMap<String,String> properties = super.getInitProperties();
        Transaction transaction = booking.getTransaction();
        properties.put(ID,id.toString());
        properties.put(JOURNAL_NAME,transaction.getJournal().getName());
        properties.put(JOURNAL_ID,transaction.getId().toString());
        properties.put(JOURNAL_ABBR,transaction.getJournal().getAbbreviation());
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

    public int getId() {
        return id;
    }
}
