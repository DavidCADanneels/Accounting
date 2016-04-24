package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.Utils.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Boekhoudkundige transactie Bevat minstens 2 boekingen
 * @author David Danneels
 * @since 01/10/2010
 * @see Booking
 */
public class Transaction extends BusinessCollection<Booking> {
    private static final String ID = "id";
    private static final String DATE = "date";
    private static final String DESCRIPTION = "description";
	private BigDecimal debitTotal;
    private BigDecimal creditTotal;
    private Journal journal;

    private int nrOfDebits = 0;

    private String description = "";
    private Calendar date = null;

    private final ArrayList<Booking> bookings;
    private Accounts accounts;

    public Transaction(Accounts accounts) {
        this.accounts = accounts;
		debitTotal = new BigDecimal(0);
		debitTotal = debitTotal.setScale(2);
		creditTotal = new BigDecimal(0);
		creditTotal = creditTotal.setScale(2);
        bookings = new ArrayList<Booking>();
        date = Calendar.getInstance();
	}

    @Override
    public boolean writeChildren(){
        return true;
    }

    @Override
    public TreeMap<String, String> getUniqueProperties(){
        return new TreeMap<String, String>();
    }

    @Override
    public Booking createNewChild(){
        return new Booking(accounts);
    }

    @Override
    public String getChildType(){
        return "Booking";
    }

    @Override
    public Properties getInitProperties() {
        Properties properties = new Properties();
        properties.put(ID, new Integer(journal.getId(this)).toString());
        properties.put(DATE, Utils.toString(date));
        properties.put(DESCRIPTION, getDescription());

        return properties;
    }

    public Set<String> getInitKeySet(){
        Set<String> keySet = new TreeSet<String>();
        keySet.add(DATE);
        keySet.add(DESCRIPTION);
        return keySet;
    }
    //
    public void setInitProperties(TreeMap<String, String> properties){
        date = Utils.toCalendar(properties.get(DATE));
        description = properties.get(DESCRIPTION);
    }

	public BigDecimal getDebetTotaal() {
		return debitTotal;
	}

	public BigDecimal getCreditTotaal() {
		return creditTotal;
	}

    // Getters

    public Journal getJournal() {
        return journal;
    }

    public String getAbbreviation() {
        return journal.getAbbreviation();
    }

    public Integer getId(){
        return journal.getId(this);
    }

    public String getDescription(){
        return (description==null)?"":description;
    }

    public Calendar getDate() {
        return date;
    }

    // Setters

    public void setJournal(Journal journal) {
        this.journal = journal;
    }

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

    @Override
    public ArrayList<Booking> getBusinessObjects(){
        return bookings;
    }

    // Adders
    @Override
    public Booking addBusinessObject(Booking booking){
        booking.setTransaction(this);
        BigDecimal amount = booking.getAmount();

        if(booking.isDebit()){
            bookings.add(nrOfDebits, booking);
            nrOfDebits++;
            debitTotal = debitTotal.add(amount);
            debitTotal = debitTotal.setScale(2);
        } else {
            bookings.add(booking);
            creditTotal = creditTotal.add(amount);
            creditTotal = creditTotal.setScale(2);
        }
        return booking;
    }

    @Override
    public void removeBusinessObject(Booking booking){
        booking.setTransaction(null);
        BigDecimal amount = booking.getAmount();

        bookings.remove(booking);
        if(booking.isDebit()){
            nrOfDebits--;
            debitTotal = debitTotal.subtract(amount);
            debitTotal = debitTotal.setScale(2);
        } else {
            creditTotal = creditTotal.subtract(amount);
            creditTotal = creditTotal.setScale(2);
        }

    }

    public boolean isBookable() {
        return !getBusinessObjects().isEmpty() && debitTotal.compareTo(creditTotal) == 0 && debitTotal.compareTo(BigDecimal.ZERO) != 0;
    }
}