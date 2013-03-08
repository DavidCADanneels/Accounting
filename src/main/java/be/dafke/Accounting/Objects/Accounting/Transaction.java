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
	private BigDecimal debettotaal, credittotaal;
	private String description = "";
	private Calendar datum = null;
    private int id;
    private String abbreviation;
    private final ArrayList<Booking> bookings;
    private boolean booked;
	private final ArrayList<Mortgage> mortgages;
    private Journal journal;
    private int nrOfDebits = 0;

    public Transaction() {
		booked = false;
		debettotaal = new BigDecimal(0);
		debettotaal = debettotaal.setScale(2);
		credittotaal = new BigDecimal(0);
		credittotaal = credittotaal.setScale(2);
        bookings = new ArrayList<Booking>();
		mortgages = new ArrayList<Mortgage>();
	}

	public BigDecimal getDebetTotaal() {
		return debettotaal;
	}

	public BigDecimal getCreditTotaal() {
		return credittotaal;
	}

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getDescription(){
        return description;
    }

	public void setDescription(String description) {
		this.description = description;
	}

	protected void lowerID() {
        id--;
	}

	protected void raiseID() {
        id++;
	}

	public Calendar getDate() {
		return datum;
	}

	public void setDate(Calendar date) {
		datum = date;
	}

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
            debettotaal = debettotaal.add(amount);
            debettotaal = debettotaal.setScale(2);
        } else {
            bookings.add(booking);
            credittotaal = credittotaal.add(amount);
            credittotaal = credittotaal.setScale(2);
        }
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
        // TODO: null check on journal OR Disable "Debit" and "Credit" button if there are no Journals
        // if(journal == null) throw Exception --> catch Exception in GUI
		journal.book(this);
        this.journal = journal;
		booked = true;
		for(Mortgage mortgage : mortgages) {
			mortgage.increasePayed();
		}
	}

    public int getId(){
        return id;
    }

	public void setId(int nr) {
        id = nr;
	}

	public ArrayList<Booking> getBookings() {
        return bookings;
    }

    public Journal getJournal() {
        return journal;
    }
}