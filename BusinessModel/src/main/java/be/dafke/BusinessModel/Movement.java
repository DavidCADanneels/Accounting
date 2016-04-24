package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;
import be.dafke.Utils.Utils;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Properties;
import java.util.Set;
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
    public static final String DEBIT = "debit";
    public static final String CREDIT = "credit";
    public static final String DESCRIPTION = "description";
    public static final String JOURNAL_ID = "journalId";
    public static final String JOURNAL_ABBR = "journalAbbr";
    public static final String ID = "id";
    private BigDecimal amount;
    private boolean debit;
    private Booking booking;
    private Integer id;

    public Movement(){
        id = ++count;
    }

    public Movement(BigDecimal amount, boolean debit){
        this.amount = amount;
        this.debit = debit;
        id = ++count;
    }

    @Override
    public TreeMap<String, String> getUniqueProperties(){
        TreeMap<String,String> properties = new TreeMap<String, String>();
        properties.put(ID,id.toString());
        return properties;
    }

    @Override
    public Set<String> getInitKeySet(){
        Set<String> keySet = super.getInitKeySet();
        keySet.add(ID);
        keySet.add(DEBIT);
        keySet.add(CREDIT);
        return keySet;
    }

    @Override
    public Properties getInitProperties() {
        Properties properties = new Properties();
        Transaction transaction = booking.getTransaction();
        properties.put(ID,id.toString());
        if(debit){
            properties.put(DEBIT, amount.toString());
        } else {
            properties.put(CREDIT, amount.toString());
        }
        properties.put(JOURNAL_NAME,transaction.getJournal().getName());
        properties.put(JOURNAL_ID,transaction.getId().toString());
        properties.put(JOURNAL_ABBR,transaction.getJournal().getAbbreviation());
        properties.put(DATE, Utils.toString(transaction.getDate()));
        properties.put(DESCRIPTION, transaction.getDescription());
        return properties;
    }

    public Booking getBooking() {
        return booking;
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
