package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.Utils.Utils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Boekhoudkundige transactie Bevat minstens 2 boekingen
 * @author David Danneels
 * @since 01/10/2010
 * @see Booking
 */
public class Transaction extends BusinessCollection<Booking> {
    private static final String ID = "id";
    private BigDecimal debitTotal;
    private BigDecimal creditTotal;
    private Journal journal;
    public static final String DATE = "date";
    public static final String DESCRIPTION = "description";
    public static final String BOOKINGS = "Bookings";
    private int nrOfDebits = 0;

    private String description = "";
    private Calendar date = null;

    private final ArrayList<Booking> bookings;
    private Accounts accounts;
    private Mortgage mortgage = null;

    public Transaction(Accounts accounts, Calendar date, String description) {
        this.accounts = accounts;
        this.date = date==null?Calendar.getInstance():date;
        this.description = description;
		debitTotal = new BigDecimal(0);
		debitTotal = debitTotal.setScale(2);
		creditTotal = new BigDecimal(0);
		creditTotal = creditTotal.setScale(2);
        bookings = new ArrayList<>();
	}

    public Mortgage getMortgage() {
        return mortgage;
    }

    public void setMortgage(Mortgage mortgage) {
        this.mortgage = mortgage;
    }

    @Override
    public TreeMap<String, String> getUniqueProperties(){
        return new TreeMap<>();
    }

    @Override
    public Booking createNewChild(TreeMap<String, String> properties){
        Account account = accounts.getBusinessObject(properties.get(Booking.ACCOUNT));
        String debitString = properties.get(Booking.DEBIT);
        String creditString = properties.get(Booking.CREDIT);
        boolean debit= true;
        BigDecimal amount = BigDecimal.ZERO;
        if(debitString!=null){
            debit = true;
            amount = new BigDecimal(debitString);
            if(creditString!=null){
                System.err.println("Movement cannot contain both 'debit' and 'credit' !!!");
            }
        } else if(creditString!=null){
            debit = false;
            amount = new BigDecimal(creditString);
        } else {
            System.err.println("No 'debit' or 'credit' tag found in Movement !!!");
        }
        return new Booking(account, amount, debit);
//        return new Booking(accounts, properties);
    }

    @Override
    public String getChildType(){
        return "Booking";
    }

    @Override
    public Properties getOutputProperties() {
        Properties properties = new Properties();
        properties.put(ID, new Integer(journal.getId(this)).toString());
        properties.put(DATE, Utils.toString(date));
        properties.put(DESCRIPTION, getDescription());
        properties.put(BOOKINGS, bookings);

        return properties;
    }
    // FOR READING
    // Define keys to read from xml, required to initialize Object attributes
    public Set<String> getInitKeySet(){
        Set<String> keySet = new TreeSet<String>();
        keySet.add(Booking.ACCOUNT);
        keySet.add(Booking.DEBIT);
        keySet.add(Booking.CREDIT);
        return keySet;
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