package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.Utils.MultiValueMap;

import java.math.BigDecimal;
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
    private Accounting accounting;
    private boolean master = false;

    public Journal(Journal journal) {
        this(journal.getName(), journal.abbreviation);
        setType(journal.type);
    }

    public Journal(String name, String abbreviation) {
        this(name, abbreviation, false);
    }
    public Journal(String name, String abbreviation, boolean master) {
        this.master = master;
        setName(name);
        setAbbreviation(abbreviation);
        currentTransaction = new Transaction(Calendar.getInstance(),"");
        transactions = new MultiValueMap<>();
	}

    public Accounting getAccounting() {
        return accounting;
    }

    public void setAccounting(Accounting accounting) {
        this.accounting = accounting;
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
        transaction.setForced(true);
        addBusinessObject(transaction);
    }

	public void removeBusinessObject(Transaction transaction) {
        Calendar date = transaction.getDate();
        transactions.removeValue(date, transaction);

        if(!master) {
            ArrayList<Booking> bookings = transaction.getBusinessObjects();
            for (Booking booking : bookings) {
                Account account = booking.getAccount();
                account.removeBusinessObject(booking.getMovement());
            }

            if (accounting.isVatAccounting() && accounting.getVatTransactions() != null) {
                VATTransaction vatTransaction = transaction.getVatTransaction();
                if (vatTransaction != null) {
                    VATTransactions vatTransactions = accounting.getVatTransactions();
                    vatTransactions.removeBusinessObject(vatTransaction);
                }
                Contact contact = transaction.getContact();
                BigDecimal turnOverAmount = transaction.getTurnOverAmount();
                BigDecimal vatAmount = transaction.getVATAmount();
                if (contact != null && turnOverAmount != null && vatAmount != null) {
                    contact.decreaseTurnOver(turnOverAmount);
                    contact.decreaseVATTotal(vatAmount);
                }
            }
        }
    }

	public Transaction addBusinessObject(Transaction transaction) {
        Calendar date = transaction.getDate();
        transactions.addValue(date, transaction);

        if(!master){
            transaction.setJournal(this);
            for (Booking booking : transaction.getBusinessObjects()) {
                Account account = booking.getAccount();
                account.addBusinessObject(booking.getMovement());
            }

            if (accounting.isVatAccounting() && accounting.getVatTransactions() != null) {
                VATTransaction vatTransaction = transaction.getVatTransaction();
                if (vatTransaction != null) {
                    VATTransactions vatTransactions = accounting.getVatTransactions();
                    // TODO: raise count here, not when creating the VATTransaction (+ set ID)
                    int count = VATTransaction.raiseCount();
                    vatTransaction.setId(count);
                    vatTransactions.addBusinessObject(vatTransaction, transaction.isForced());
                }
                Contact contact = transaction.getContact();
                BigDecimal turnOverAmount = transaction.getTurnOverAmount();
                BigDecimal vatAmount = transaction.getVATAmount();
                if (contact != null && turnOverAmount != null && vatAmount != null) {
                    contact.increaseTurnOver(turnOverAmount);
                    contact.increaseVATTotal(vatAmount);
                }
            }
        }
        transaction.setForced(false);
        return transaction;
	}

    @Override
    public TreeMap<String,String> getUniqueProperties(){
        TreeMap<String,String> keyMap = super.getUniqueProperties();
        keyMap.put(Journals.ABBREVIATION, abbreviation);
        return keyMap;
    }
}