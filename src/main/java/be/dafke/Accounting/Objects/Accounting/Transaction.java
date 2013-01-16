package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Objects.Mortgage.Mortgage;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

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
	private String description = "";
	private Calendar datum = null;
    private int id;
    private String abbreviation;
	private final ArrayList<Booking> debitBookings, creditBookings, boekingen;
//    private final HashMap<Account,Vector<Booking>> allBookings;//debitBookings, creditBookings;
    private boolean booked;
	private final ArrayList<Mortgage> mortgages;

	private Transaction() {
		booked = false;
		debettotaal = new BigDecimal(0);
		debettotaal = debettotaal.setScale(2);
		credittotaal = new BigDecimal(0);
		credittotaal = credittotaal.setScale(2);
//        allBookings = new HashMap<Account,Vector<Booking>>();
//		debitBookings = new HashMap<Account,Vector<Booking>>();
        debitBookings = new ArrayList<Booking>();
//		creditBookings = new HashMap<Account,Vector<Booking>>();
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
//		for(int i = 0; i < boekingen.size(); i++) {
//			boekingen.get(i).setDescription(description);
//		}
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
//		for(int i = 0; i < boekingen.size(); i++) {
//			boekingen.get(i).setDate(date);
//		}
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
        Booking booking = new Booking(this, rek, amount, true);
//        addBooking(rek, amount, true);
        debitBookings.add(booking);
		debettotaal = debettotaal.add(amount);
		debettotaal = debettotaal.setScale(2);
	}

	public void crediteer(Account rek, BigDecimal amount) {
        Booking booking = new Booking(this, rek, amount, false);
//        addBooking(rek, amount, false);
        creditBookings.add(booking);
		credittotaal = credittotaal.add(amount);
		credittotaal = credittotaal.setScale(2);
	}

    private void addBooking(Account rek, BigDecimal amount, boolean debit){
        Booking booking = new Booking(this, rek, amount, debit);
        boekingen.add(booking);
//        Vector<Booking> vector;
        // TODO: optimise this code:
        // 1. vector = get
        //    if vector == null vector=new
        // 2. do we have to reput the vector if vector was not null ?
//        if(allBookings.containsKey(rek)){
//            vector = allBookings.get(rek);
//        }else{
//            vector = new Vector<Booking>();
//        }
//        vector.add(booking);
//        allBookings.put(rek,vector);
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

    public int getId(){
        return id;
    }

	public void setId(int nr) {
        id = nr;
//		Iterator<Booking> it = boekingen.iterator();
//		while (it.hasNext()) {
//			Booking b = it.next();
//			b.setID(nr);
//		}
	}

	public ArrayList<Booking> getBookings() {
		ArrayList<Booking> result = new ArrayList<Booking>();
		result.addAll(debitBookings);
		result.addAll(creditBookings);
		return result;
	}
}