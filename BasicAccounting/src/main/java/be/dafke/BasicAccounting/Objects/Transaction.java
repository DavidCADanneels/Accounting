package be.dafke.BasicAccounting.Objects;

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
    // TODO: transform this into BusinessCollection<Booking>
	private BigDecimal debitTotal;
    private BigDecimal creditTotal;
    private Journal journal;

    private int nrOfDebits = 0;

    private String description = "";
    private Calendar date = null;

    private final ArrayList<Booking> bookings;

    public Transaction() {
		debitTotal = new BigDecimal(0);
		debitTotal = debitTotal.setScale(2);
		creditTotal = new BigDecimal(0);
		creditTotal = creditTotal.setScale(2);
        bookings = new ArrayList<Booking>();
        date = Calendar.getInstance();
	}

    // Getters (without setters)
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

    public String getAbbreviation() {
        return journal.getAbbreviation();
    }

    public int getId(){
        return journal.getId(this);
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

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

    // Adders

   public void addBooking(Booking booking){
        booking.setTransaction(this);
        boolean debit = booking.getMovement().isDebit();
        BigDecimal amount = booking.getMovement().getAmount();

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

    public void removeBooking(Booking booking){
        booking.setTransaction(null);
        boolean debit = booking.getMovement().isDebit();
        BigDecimal amount = booking.getMovement().getAmount();

        bookings.remove(booking);
        if(debit){
            nrOfDebits--;
            debitTotal = debitTotal.subtract(amount);
            debitTotal = debitTotal.setScale(2);
        } else {
            creditTotal = creditTotal.subtract(amount);
            creditTotal = creditTotal.setScale(2);
        }

    }
}