package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessTyped;
import be.dafke.ObjectModel.MustBeRead;
import be.dafke.Utils.MultiValueMap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

/**
 * Boekhoudkundig dagboek
 * @author David Danneels
 * @since 01/10/2010
 */
public class Journal extends BusinessCollection<Transaction> implements BusinessTyped<JournalType>, MustBeRead {
    private static final String TYPE = "type";
    protected static final String ABBREVIATION = "abbr";// TODO: 'abbr' or 'abbreviation'
    private String abbreviation;
    private final MultiValueMap<Calendar,Transaction > transactions;
    private JournalType type;
    private Accounting accounting;
    private Transaction currentTransaction;

    public Journal(Accounting accounting) {
        this.accounting = accounting;
        currentTransaction = new Transaction(accounting.getAccounts());
        transactions = new MultiValueMap<Calendar,Transaction>();
	}

    public Journals getJournals() {
        return accounting.getJournals();
    }

    @Override
    public Transaction createNewChild(){
        return new Transaction(accounting.getAccounts());
    }

    @Override
    public String getChildType(){
        return "Transaction";
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

	public void removeBusinessObject(Transaction transaction) {
        Calendar date = transaction.getDate();
        transactions.removeValue(date, transaction);
		ArrayList<Booking> bookings = transaction.getBusinessObjects();
		for(Booking booking : bookings) {
			Account account = booking.getAccount();
			account.removeBusinessObject(booking.getMovement());
		}
        if (transaction instanceof MortgageTransaction){
            MortgageTransaction mortgageTransaction = (MortgageTransaction) transaction;
            Mortgage mortgage = mortgageTransaction.getMortgage();
            mortgage.removeBusinessObject(mortgageTransaction);
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

        if (transaction instanceof MortgageTransaction){
            MortgageTransaction mortgageTransaction = (MortgageTransaction) transaction;
            Mortgage mortgage = mortgageTransaction.getMortgage();
            mortgage.addBusinessObject(mortgageTransaction);
        }

        return transaction;
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
            type = accounting.getJournalTypes().getBusinessObject(typeName);
        }
    }

    @Override
    public Properties getInitProperties() {
        Properties outputMap = super.getInitProperties();
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