package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.TreeMap;

/**
 * User: Dafke
 * Date: 9/03/13
 * Time: 17:01
 */
public class Movement extends BusinessObject{
    private static int count = 0;
    public static final String JOURNAL_NAME = "journalName";
    public static final String DATE = "date";
    public static final String DESCRIPTION = "description";
    public static final String JOURNAL_ID = "journalId";
    public static final String JOURNAL_ABBR = "journalAbbr";
    public static final String ID = "id";
    public static final String DEBIT = "debit";
    public static final String CREDIT = "credit";
    private BigDecimal amount;
    private boolean debit;
    private Booking booking;
    private Integer id;

    public Movement(BigDecimal amount, boolean debit){
        this.amount = amount;
        this.debit = debit;
        this.id = ++count;
    }

    public Movement(BigDecimal amount, boolean debit, int id) {
        this.amount = amount;
        this.debit = debit;
        this.id = id;
        count++;
    }

    @Override
    public TreeMap<String, String> getUniqueProperties(){
        TreeMap<String,String> properties = new TreeMap<String, String>();
        properties.put(ID,id.toString());
        return properties;
    }

    public Booking getBooking() {
        return booking;
    }

    public Transaction getTransaction(){
        return booking.getTransaction();
    }

    public Journal getJournal(){
        return getTransaction().getJournal();
    }

    public String getTransactionString(){
        return booking.getTransaction().getAbbreviation() + booking.getTransaction().getId();
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

    public void setDebit(boolean debit) {
        this.debit = debit;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public Calendar getDate() {
        return booking.getTransaction().getDate();
    }

    public void setDate(Calendar date){
        booking.getTransaction().setDate(date);
    }

    public String getDescription(){
        return booking.getTransaction().getDescription();
    }

    public void setDescription(String description){
        booking.getTransaction().setDescription(description);
    }
}
