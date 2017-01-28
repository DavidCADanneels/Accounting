package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.Utils.MultiValueMap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TreeMap;

/**
 * Boekhoudkundig dagboek
 * @author David Danneels
 * @since 01/10/2010
 */
public class Journal extends BusinessCollection<Transaction> {
    private String abbreviation;
    protected final MultiValueMap<Calendar,Transaction> transactions;
    private JournalType type;
    private Transaction currentTransaction;

    public Journal(Journal journal) {
        this(journal.getName(), journal.abbreviation);
        setType(journal.type);
    }

    public Journal(String name, String abbreviation) {
        setName(name);
        setAbbreviation(abbreviation);
        currentTransaction = new Transaction(Calendar.getInstance(),"");
        transactions = new MultiValueMap<>();
	}

    @Override
    public boolean isDeletable(){
        return transactions.isEmpty();
    }

    public Transaction getCurrentObject() {
        return currentTransaction;
    }

    public void setCurrentObject(Transaction currentTransaction) {
        this.currentTransaction = currentTransaction;
    }

    public JournalType getType() {
        return type;
    }

    public void setType(JournalType type) {
        this.type = type;
    }

	@Override
	public String toString() {
		return getName() + " (" + abbreviation + ")";
	}

    @Override
	public ArrayList<Transaction> getBusinessObjects() {
        return transactions.values();
    }

	public int getId() {
		return transactions.values().size()+1;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public int getId(Transaction transaction){
        return transactions.values().indexOf(transaction)+1;
    }

    public void changeDate(Transaction transaction, Calendar newDate){
        removeBusinessObject(transaction);
        transaction.setDate(newDate);
        addBusinessObject(transaction);
    }

	public void removeBusinessObject(Transaction transaction) {
        Calendar date = transaction.getDate();
        transactions.removeValue(date, transaction);
        ArrayList<Booking> bookings = transaction.getBusinessObjects();
        for (Booking booking : bookings) {
            Account account = booking.getAccount();
            account.removeBusinessObject(booking.getMovement());
        }

    }

	public Transaction addBusinessObject(Transaction transaction) {
        Calendar date = transaction.getDate();
        transaction.setJournal(this);

        for(Booking booking : transaction.getBusinessObjects()) {
            Account account = booking.getAccount();
            account.addBusinessObject(booking.getMovement());
        }
        transactions.addValue(date, transaction);
        return transaction;
	}

    @Override
    public TreeMap<String,String> getUniqueProperties(){
        TreeMap<String,String> keyMap = super.getUniqueProperties();
        keyMap.put(Journals.ABBREVIATION, abbreviation);
        return keyMap;
    }
}