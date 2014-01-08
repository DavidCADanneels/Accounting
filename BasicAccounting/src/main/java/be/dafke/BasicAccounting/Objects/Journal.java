package be.dafke.BasicAccounting.Objects;

import be.dafke.ObjectModel.BusinessObject;
import be.dafke.ObjectModel.BusinessTypeCollection;
import be.dafke.ObjectModel.BusinessTypeCollectionDependent;
import be.dafke.ObjectModel.BusinessTyped;
import be.dafke.Utils.MultiValueMap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;
import java.util.TreeMap;

/**
 * Boekhoudkundig dagboek
 * @author David Danneels
 * @since 01/10/2010
 */
public class Journal extends BusinessObject implements BusinessTypeCollectionDependent<JournalType>, BusinessTyped<JournalType> {
    private static final String TYPE = "type";
    protected static final String ABBREVIATION = "abbreviation";
    private String abbreviation;
    private final MultiValueMap<Calendar,Transaction> transactions;
    private JournalType type;
    private Transaction currentTransaction = new Transaction();
    private BusinessTypeCollection businessTypeCollection;

    public Journal() {
		transactions = new MultiValueMap<Calendar,Transaction>();
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

    @Override
    public void setBusinessTypeCollection(BusinessTypeCollection businessTypeCollection) {
        this.businessTypeCollection = businessTypeCollection;
    }

    public void setType(JournalType type) {
        this.type = type;
    }

	@Override
	public String toString() {
		return getName() + " (" + abbreviation + ")";
	}

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

	public void removeBusinessObject(Transaction transaction) {
        Calendar date = transaction.getDate();
        transactions.removeValue(date, transaction);
		ArrayList<Booking> bookings = transaction.getBookings();
		for(Booking booking : bookings) {
			Account account = booking.getAccount();
			account.unbook(date, booking.getMovement());
		}
	}

	public void addBusinessObject(Transaction transaction) {
        Calendar date = transaction.getDate();
        transaction.setJournal(this);

        for(Booking booking : transaction.getBookings()) {
            Account account = booking.getAccount();
            account.book(date, booking.getMovement());
        }
        transactions.addValue(date, transaction);
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
}