package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Objects.BusinessTypeCollection;
import be.dafke.Accounting.Objects.BusinessTyped;
import be.dafke.Accounting.Objects.WriteableBusinessObject;
import be.dafke.MultiValueMap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;
import java.util.TreeMap;

/**
 * Boekhoudkundig dagboek
 * @author David Danneels
 * @since 01/10/2010
 */
public class Journal extends WriteableBusinessObject implements BusinessTyped<JournalType> {
    private static final String TYPE = "type";
    protected static final String ABBREVIATION = "abbreviation";
    private String abbreviation;
    private int id;
    private final MultiValueMap<Calendar,Transaction> transactions;
    private JournalType type;
    private Transaction currentTransaction = new Transaction();
    private BusinessTypeCollection businessTypeCollection;

    public Journal() {
		transactions = new MultiValueMap<Calendar,Transaction>();
		id = 1;
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

    public JournalType getType() {
        return type;
    }

    @Override
    public void setBusinessTypeCollection(BusinessTypeCollection businessTypeCollection) {
        this.businessTypeCollection = businessTypeCollection;
    }

    @Override
    public BusinessTypeCollection getBusinessTypeCollection() {
        return businessTypeCollection;
    }

    public void setType(JournalType type) {
        this.type = type;
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
        transaction.setJournal(this);
        transaction.setAbbreviation(abbreviation);

        for(Booking booking : transaction.getBookings()) {
            Account account = booking.getAccount();
            account.book(date, booking.getMovement());
        }
        addTransaction(date, transaction);
        id++;
	}

    @Override
    public Set<String> getInitKeySet(){
        Set<String> keySet = super.getInitKeySet();
        keySet.add(ABBREVIATION);
        keySet.add(TYPE);
        return keySet;
    }

    @Override
    public void setInitProperties(TreeMap<String, String> properties) {
        super.setInitProperties(properties);
        abbreviation = properties.get(ABBREVIATION);
        String typeName = properties.get(TYPE);
        if(typeName!=null){
            type = (JournalType) businessTypeCollection.getBusinessObject(typeName);
        }
    }


    @Override
    public TreeMap<String,String> getInitProperties() {
        TreeMap<String,String> outputMap = super.getInitProperties();
        outputMap.put(TYPE, getType().getName());
        outputMap.put(ABBREVIATION, getAbbreviation());
        return outputMap;
    }

    @Override
    public TreeMap<String,String> getUniqueProperties(){
        TreeMap<String,String> keyMap = super.getUniqueProperties();
        keyMap.put(ABBREVIATION, abbreviation);
        return keyMap;
    }

    @Override
    public TreeMap<String,String> getProperties() {
        TreeMap<String,String> outputMap = super.getProperties();
        return outputMap;
    }

    @Override
    public void setProperties(TreeMap<String, String> properties) {
        super.setProperties(properties);
    }
}