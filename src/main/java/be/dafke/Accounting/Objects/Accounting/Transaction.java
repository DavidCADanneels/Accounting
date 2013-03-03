package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Objects.Mortgage.Mortgage;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;

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
	private BigDecimal debettotaal, credittotaal;
	private String description = "";
	private Calendar datum = null;
    private int id;
    private String abbreviation;
    private final HashMap<Account,Vector<Booking>> debitBookings, creditBookings, allBookings;
    private boolean booked;
	private final ArrayList<Mortgage> mortgages;
    private boolean sort = true; // for later use: default = true --> First all Debits, then all Credits
    private Journal journal;

    public Transaction() {
		booked = false;
		debettotaal = new BigDecimal(0);
		debettotaal = debettotaal.setScale(2);
		credittotaal = new BigDecimal(0);
		credittotaal = credittotaal.setScale(2);
        allBookings = new HashMap<Account,Vector<Booking>>();
		debitBookings = new HashMap<Account,Vector<Booking>>();
		creditBookings = new HashMap<Account,Vector<Booking>>();
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

    public void addBooking(Account account, BigDecimal amount, boolean debit, boolean merge){
        Booking booking = new Booking(this, account, amount, debit);
        if(!merge){
            // all Bookings
            Vector<Booking> allVector = allBookings.get(account);
            if(allVector==null){
                allVector = new Vector<Booking>();
            }
            allVector.add(booking);
            allBookings.put(account, allVector);

            // Specific Bookings: Debit or Credit
            if(debit){
                // Debit
                Vector<Booking> debitVector = debitBookings.get(account);
                if(debitVector==null){
                    debitVector = new Vector<Booking>();
                }
                debitVector.add(booking);
                debitBookings.put(account,debitVector);
                debettotaal = debettotaal.add(amount);
                debettotaal = debettotaal.setScale(2);
            } else {
                // Credit
                Vector<Booking> creditVector = creditBookings.get(account);
                if(creditVector==null){
                    creditVector = new Vector<Booking>();
                }
                creditVector.add(booking);
                creditBookings.put(account,creditVector);
                credittotaal = credittotaal.add(amount);
                credittotaal = credittotaal.setScale(2);
            }
        } else{
            // MERGE !!!
            Vector<Booking> vector = new Vector<Booking>();
            Vector<Booking> debitVector = debitBookings.get(account);
            Vector<Booking> creditVector = creditBookings.get(account);
            vector.add(booking);
            if(debitVector!=null){
                vector.addAll(debitVector);
            }
            if(creditVector!=null){
                vector.addAll(creditVector);
            }
            booking = merge(vector);
            vector = new Vector<Booking>();
            vector.add(booking);
            if(booking.getAmount().compareTo(BigDecimal.ZERO) == 0){
                debitBookings.remove(account);
                creditBookings.remove(account);
                allBookings.remove(account);
            } else {
                if(booking.isDebit()){
                    debitBookings.put(account, vector);
                    creditBookings.remove(account);
                } else {
                    creditBookings.put(account, vector);
                    debitBookings.remove(account);
                }
                allBookings.put(account, vector);
            }
        }
    }

    private Booking merge(Vector<Booking> vector) {
        if(vector.size()==1){
            return vector.firstElement();
        }
        Booking result = vector.get(0);
        BigDecimal amount = result.getAmount();
        for(int i=1;i<vector.size();i++){
            Booking nextBooking = vector.get(i);
            if(result.isDebit() == nextBooking.isDebit()){
                amount = amount.add(nextBooking.getAmount());
//              amount.setScale(2);
            }else{
                amount = amount.subtract(nextBooking.getAmount());
//              amount.setScale(2);
            }
            result.setAmount(amount);
        }
        if(amount.compareTo(BigDecimal.ONE)<0){
            amount = amount.negate();
            result.setDebit(!result.isDebit());
            result.setAmount(amount);
        }

        return result;
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

    public int size(){
        return getBookings().size();
    }

    public boolean isEmpty(){
        return getBookings().isEmpty();
    }

    public Booking get(int i){
        return getBookings().get(i);
    }

	public ArrayList<Booking> getBookings() {
        ArrayList<Booking> result = new ArrayList<Booking>();
        if(sort){
            for(Vector<Booking> vector: debitBookings.values()){
                for(Booking booking: vector){
                    result.add(booking);
                }
            }
            for(Vector<Booking> vector: creditBookings.values()){
                for(Booking booking: vector){
                    result.add(booking);
                }
            }
        } else {
            for(Vector<Booking> vector: allBookings.values()){
                for(Booking booking: vector){
                    result.add(booking);
                }
            }
        }
        return result;
    }

    public boolean contains(Account account) {
        return allBookings.containsKey(account);
    }

    public Journal getJournal() {
        return journal;
    }
}