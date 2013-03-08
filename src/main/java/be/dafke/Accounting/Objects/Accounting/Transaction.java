package be.dafke.Accounting.Objects.Accounting;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Boekhoudkundige transactie Bevat minstens 2 boekingen
 * @author David Danneels
 * @since 01/10/2010
 * @see Booking
 */
public class Transaction {
	private BigDecimal debitTotal;
    private BigDecimal creditTotal;
    private Journal journal;

    private boolean booked;
    private int nrOfDebits = 0;

    private String description = "";
    private Calendar date = null;
    private int id;
    private String abbreviation;

    private final ArrayList<Booking> bookings;
    private final ArrayList<Mortgage> mortgages;

    public Transaction() {
		booked = false;
		debitTotal = new BigDecimal(0);
		debitTotal = debitTotal.setScale(2);
		creditTotal = new BigDecimal(0);
		creditTotal = creditTotal.setScale(2);
        bookings = new ArrayList<Booking>();
		mortgages = new ArrayList<Mortgage>();
	}

    protected void lowerID() {
        id--;
    }

    protected void raiseID() {
        id++;
    }

    public void moveTransaction(Journal oldJournal, Journal newJournal) {
        if (booked) {
            oldJournal.unbook(this);
        }
        newJournal.book(this);
    }

    public void deleteTransaction(Journal journal) {
        if (booked) journal.unbook(this);
    }

    public void book(Journal journal) {
        journal.book(this);
    }

    // Getters (without setters)
    public ArrayList<Mortgage> getMortgages() {
        return mortgages;
    }

    public ArrayList<Booking> getBookings() {
        return bookings;
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

    public boolean isBooked() {
        return booked;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public int getId(){
        return id;
    }

    public String getDescription(){
        return description;
    }

    public Calendar getDate() {
        return date;
    }

    // Setters

    public void setJournal(Journal journal) {
        this.journal = journal;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    public void setId(int nr) {
        id = nr;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

    // Adders

	public void addMortgage(Mortgage mortgage) {
		mortgages.add(mortgage);
	}

    public void addBooking(Booking booking){
        booking.setTransaction(this);
        boolean debit = booking.isDebit();
        BigDecimal amount = booking.getAmount();

        if(debit){
            bookings.add(nrOfDebits, booking);
            nrOfDebits++;
            debitTotal = debitTotal.add(amount);
            debitTotal = debitTotal.setScale(2);
        } else {
            bookings.add(booking);
            creditTotal = creditTotal.add(amount);
            creditTotal = creditTotal.setScale(2);
        }
    }
}