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
public class Journal extends BusinessObject{
	private String abbreviation;
	private int id;
	private final MultiValueMap<Calendar,Transaction> transactions;
//	private boolean save;
	private JournalType journalType;
    private Transaction currentTransaction = new Transaction();
    protected static final String ABBREVIATION = "abbreviation";

    public Journal() {
//		save = true;
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

    /**
	 * Geeft de naam van het dagboek en de bijhorende afkorting terug
	 * @return de naam van het dagboek en de bijhorende afkorting <b><i>naam dagboek(afkorting)</i></b>
	 */

	@Override
	public String toString() {
		return getName() + " (" + abbreviation + ")";
	}

	/**
	 * Geeft de transacties terug die bij dit dagboek horen
	 * @return de transacties die bij dit dagboek horen
	 */
	public ArrayList<Transaction> getTransactions() {
        return transactions.values();
    }

	/**
	 * Geeft het id van de volgende transactie terug
	 * @return het id van de volgende transactie
	 */
	public int getId() {
		return id;
	}

	/**
	 * Geeft de afkorting van het dagboek terug
	 * @return de afkorting van het dagboek
	 */
	public String getAbbreviation() {
		return abbreviation;
	}

    /**
	 * Verwijdert de gegeven transactie
	 * @param transaction de te verwijderen transactie
	 */
	private void deleteTransaction(Transaction transaction) {
        // Remove Key-Value Pair
        Calendar date = transaction.getDate();
        transactions.removeValue(date, transaction);

        // Lower the remaining ID's
        ArrayList<Transaction> list = transactions.values();
        for(Transaction trans : list.subList(id, list.size())){
            trans.lowerID();
        }
	}

    /**
	 * Voegt een transactie toe
	 * @param transaction de toe te voegen transactie
	 */
	private void addTransaction(Transaction transaction) {
        // Add Key-Value Pair
        Calendar date = transaction.getDate();
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

	/**
	 * Verwijdert de gegeven transactie
	 * @param transaction de te verwijderen transactie
	 */
	public void unbook(Transaction transaction) {
		deleteTransaction(transaction);
		ArrayList<Booking> boekingen = transaction.getBookings();
		for(Booking boeking : boekingen) {
			Account account = boeking.getAccount();
			account.unbook(boeking);
		}
		id--;
//		accounting.setSavedXML(false);
//		accounting.setSavedHTML(false);
//		save = false;
	}

	/**
	 * Boek de gegeven transactie
	 * @param transaction de te boeken transactie
	 */
	public void book(Transaction transaction) {
        transaction.setJournal(this);
        transaction.setAbbreviation(abbreviation);
		addTransaction(transaction);
		ArrayList<Booking> boekingen = transaction.getBookings();
		for(Booking boeking : boekingen) {
			Account rek = boeking.getAccount();
			rek.book(boeking);
		}
        for(Mortgage mortgage : transaction.getMortgages()) {
            mortgage.increasePayed();
        }

        id++;
//		accounting.setSavedXML(false);
//		accounting.setSavedHTML(false);
//		save = false;
	}

	public void setAbbreviation(String newAbbreviation) {
		abbreviation = newAbbreviation;
	}
}