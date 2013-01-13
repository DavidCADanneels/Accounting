package be.dafke.Accounting.Objects;

import be.dafke.Mortgage.Mortgage;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

/**
 * Boekhoudkundige transactie Bevat minstens 2 boekingen
 * @author David Danneels
 * @since 01/10/2010
 * @see Booking
 */
public class Transaction implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Transaction trans = null;
	private BigDecimal debettotaal, credittotaal;
	private String descr = "";
	private Calendar datum = null;
	private final ArrayList<Booking> debitBookings, creditBookings, boekingen;
	private boolean booked;
	private final ArrayList<Mortgage> mortgages;

	private Transaction() {
		booked = false;
		debettotaal = new BigDecimal(0);
		debettotaal = debettotaal.setScale(2);
		credittotaal = new BigDecimal(0);
		credittotaal = credittotaal.setScale(2);
		debitBookings = new ArrayList<Booking>();
		creditBookings = new ArrayList<Booking>();
		boekingen = new ArrayList<Booking>();
		mortgages = new ArrayList<Mortgage>();
	}

	public BigDecimal getDebetTotaal() {
		return debettotaal;
	}

	public BigDecimal getCreditTotaal() {
		return credittotaal;
	}

	public void setDescription(String description) {
		descr = description;
		for(int i = 0; i < boekingen.size(); i++) {
			boekingen.get(i).setDescription(description);
		}
	}

	protected void lowerID() {
		for(int i = 0; i < boekingen.size(); i++)
			boekingen.get(i).lowerId();
	}

	protected void raiseID() {
		for(int i = 0; i < boekingen.size(); i++)
			boekingen.get(i).raiseId();
	}

	public Calendar getDate() {
		return datum;
	}

	public void setDate(Calendar date) {
		datum = date;
		for(int i = 0; i < boekingen.size(); i++) {
			boekingen.get(i).setDate(date);
		}
	}

	public static Transaction getInstance() {
		if (trans == null) trans = new Transaction();
		return trans;
	}

	public static void newInstance(Calendar date, String description) {
		trans = new Transaction();
		trans.setDate(date);
		trans.setDescription(description);
	}

	public void addMortgage(Mortgage mortgage) {
		mortgages.add(mortgage);
	}

	public void debiteer(Account rek, BigDecimal amount) {
		Booking booking = new Booking(this, descr, rek, amount, true, datum);
		boekingen.add(booking);
		debitBookings.add(booking);
		debettotaal = debettotaal.add(amount);
		debettotaal = debettotaal.setScale(2);
	}

	public void crediteer(Account rek, BigDecimal amount) {
		Booking booking = new Booking(this, descr, rek, amount, false, datum);
		boekingen.add(booking);
		creditBookings.add(booking);
		credittotaal = credittotaal.add(amount);
		credittotaal = credittotaal.setScale(2);
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
		booked = true;
		for(Mortgage mortgage : mortgages) {
			mortgage.increasePayed();
		}
	}

	public void setId(int nr) {
		Iterator<Booking> it = boekingen.iterator();
		while (it.hasNext()) {
			Booking b = it.next();
			b.setID(nr);
		}
	}

	public ArrayList<Booking> getBookings() {
		ArrayList<Booking> result = new ArrayList<Booking>();
		result.addAll(debitBookings);
		result.addAll(creditBookings);
		return result;
	}
}