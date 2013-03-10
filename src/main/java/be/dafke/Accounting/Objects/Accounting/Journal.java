package be.dafke.Accounting.Objects.Accounting;

import be.dafke.MultiValueMap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Boekhoudkundig dagboek
 * @author David Danneels
 * @since 01/10/2010
 */
public class Journal extends WriteableBusinessObject {
	private String abbreviation;
	private int id;
	private final MultiValueMap<Calendar,Transaction> transactions;
	private JournalType journalType;
    private Transaction currentTransaction = new Transaction();
    protected static final String ABBREVIATION = "abbreviation";

    public Journal() {
		transactions = new MultiValueMap<Calendar,Transaction>();
		id = 1;
	}

    @Override
    public Map<String,String> getKeyMap(){
        Map<String,String> keyMap = new HashMap<String, String>();
        keyMap.put(NAME, getName());
        keyMap.put(ABBREVIATION, abbreviation);
        return keyMap;
    }

    @Override
    public boolean isDeletable(){
        return transactions.isEmpty();
    }

    public Booking getBooking(int row){
        ArrayList<Booking> boekingen = new ArrayList<Booking>();
        for(Transaction transaction : getTransactions()){
            boekingen.addAll(transaction.getBookings());
        }
        return boekingen.get(row);
    }

    public Transaction getTransaction(int row){
        return getBooking(row).getTransaction();
    }

    public Transaction getCurrentTransaction() {
        return currentTransaction;
    }

    public void setCurrentTransaction(Transaction currentTransaction) {
        this.currentTransaction = currentTransaction;
    }

    public JournalType getJournalType() {
        return journalType;
    }

    public void setJournalType(JournalType journalType) {
        this.journalType = journalType;
    }

	@Override
	public String toString() {
		return getName() + " (" + abbreviation + ")";
	}

	public ArrayList<Transaction> getTransactions() {
        return transactions.values();
    }

	public int getId() {
		return id;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

    public void setAbbreviation(String newAbbreviation) {
        abbreviation = newAbbreviation;
    }

	private void deleteTransaction(Calendar date, Transaction transaction) {
        // Remove Key-Value Pair
        transactions.removeValue(date, transaction);

        // Lower the remaining ID's
        int id = transaction.getId();  // ID's of transactions start with 1 (in list start with 0)
        ArrayList<Transaction> list = transactions.values();
        for(Transaction trans : list.subList(id-1, list.size())){
            trans.lowerID();
        }
	}

	private void addTransaction(Calendar date, Transaction transaction) {
        // Add Key-Value Pair
        transactions.addValue(date, transaction);

        // Set ID
        ArrayList<Transaction> list = transactions.values();
        int id = list.indexOf(transaction);
        transaction.setId(id);

        // Raise remaining ID's
        for(Transaction trans : list.subList(id, list.size())){
            trans.raiseID();
        }
	}

	public void unbook(Transaction transaction) {
        Calendar date = transaction.getDate();
		deleteTransaction(date, transaction);
		ArrayList<Booking> bookings = transaction.getBookings();
		for(Booking booking : bookings) {
			Account account = booking.getAccount();
			account.unbook(date, booking.getMovement());
		}
		id--;
	}

	public void book(Transaction transaction) {
        Calendar date = transaction.getDate();
        addTransaction(date, transaction);
        transaction.setJournal(this);
        transaction.setAbbreviation(abbreviation);
		ArrayList<Booking> bookings = transaction.getBookings();
		for(Booking booking : bookings) {
			Account account = booking.getAccount();
			account.book(date, booking.getMovement());
		}
        id++;
	}
}