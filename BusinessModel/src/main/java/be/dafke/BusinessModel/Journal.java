package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.MustBeRead;
import be.dafke.Utils.MultiValueMap;
import be.dafke.Utils.Utils;

import java.util.*;

/**
 * Boekhoudkundig dagboek
 * @author David Danneels
 * @since 01/10/2010
 */
public class Journal extends BusinessCollection<Transaction> implements MustBeRead {
    private String abbreviation;
    private final MultiValueMap<Calendar,Transaction > transactions;
    private JournalType type;
    private Accounting accounting;
    private Transaction currentTransaction;

    public Journal(Accounting accounting, String name, String abbreviation) {
        this.accounting = accounting;
        setName(name);
        setAbbreviation(abbreviation);
        currentTransaction = new Transaction(accounting.getAccounts(),Calendar.getInstance(),"open transaction");
        transactions = new MultiValueMap<Calendar,Transaction>();
	}

    @Override
    public Properties getOutputProperties() {
        Properties outputMap = new Properties();
        outputMap.put(NAME,getName());
        outputMap.put(Journals.TYPE, getType().getName());
        outputMap.put(Journals.ABBREVIATION, getAbbreviation());
        return outputMap;
    }

    public Journals getJournals() {
        return accounting.getJournals();
    }

    @Override
    public Set<String> getInitKeySet(){
        Set<String> keySet = new TreeSet<String>();
        keySet.add(NAME);
        keySet.add(Transaction.DATE);
        keySet.add(Transaction.DESCRIPTION);
        return keySet;
    }

    @Override
    public Transaction createNewChild(TreeMap<String, String> properties){
        Calendar date = Utils.toCalendar(properties.get(Transaction.DATE));
        String description = properties.get(Transaction.DESCRIPTION);
        return new Transaction(accounting.getAccounts(), date, description);
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
    public TreeMap<String,String> getUniqueProperties(){
        TreeMap<String,String> keyMap = super.getUniqueProperties();
        keyMap.put(Journals.ABBREVIATION, abbreviation);
        return keyMap;
    }
}