package be.dafke.Accounting.Objects.Accounting;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Boekhoudkundig dagboek
 * @author David Danneels
 * @since 01/10/2010
 */
public class Journal extends BusinessObject{
	private String abbreviation;
	private int id;
	private final TreeMap<Calendar,List<Transaction>> transactions;
//	private boolean save;
	private JournalType journalType;
    private Transaction currentTransaction = new Transaction();
    private Account currentAccount;
    protected static final String ABBREVIATION = "abbreviation";

    public Journal() {
//		save = true;
		transactions = new TreeMap<Calendar,List<Transaction>>();
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
	public Collection<Transaction> getTransactions() {
        ArrayList<Transaction> result = new ArrayList<Transaction>();
        for(List<Transaction> list : transactions.values()){
            result.addAll(list);
        }
        return result;

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
        Calendar date = transaction.getDate();
        List<Transaction> list = transactions.get(date);
        int index = list.indexOf(transaction);
        list.remove(transaction);
        if(list.isEmpty()){
            transactions.remove(date);
        }
        // lower ID's
        if(index<list.size()){
            List<Transaction> subList = list.subList(index,list.size());
            for(Transaction trans:subList){
                trans.lowerID();
            }
        }
        lowerIds(transactions.higherKey(date));
	}

    private void lowerIds(Calendar date){
        while(date != null){
            List<Transaction> list = transactions.get(date);
            for(Transaction transaction:list){
                transaction.lowerID();
            }
            date = transactions.higherKey(date);
        }
    }

    private void raiseIds(Calendar date){
        while(date != null){
            List<Transaction> list = transactions.get(date);
            for(Transaction transaction:list){
                transaction.raiseID();
            }
            date = transactions.higherKey(date);
        }
    }

    /**
	 * Voegt een transactie toe
	 * @param transaction de toe te voegen transactie
	 */
	private void addTransaction(Transaction transaction) {
        Calendar datum = transaction.getDate();
        if(!transactions.containsKey(datum)){
            transactions.put(datum, new ArrayList<Transaction>());
        }
        List<Transaction> list = transactions.get(datum);
        list.add(transaction);
        // raise ID's
        raiseIds(transactions.higherKey(datum));
	}

	/**
	 * Verwijdert de gegeven transactie
	 * @param transaction de te verwijderen transactie
	 */
	protected void unbook(Transaction transaction) {
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
	protected void book(Transaction transaction) {
        transaction.setAbbreviation(abbreviation);
		addTransaction(transaction);
		ArrayList<Booking> boekingen = transaction.getBookings();
		for(Booking boeking : boekingen) {
			Account rek = boeking.getAccount();
			rek.book(boeking);
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