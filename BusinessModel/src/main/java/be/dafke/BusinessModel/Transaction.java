package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TreeMap;

/**
 * Boekhoudkundige transactie Bevat minstens 2 boekingen
 * @author David Danneels
 * @since 01/10/2010
 * @see Booking
 */
public class Transaction extends BusinessCollection<Booking> {
    private static final String ID = "id";
    private BigDecimal debitTotal;
    private BigDecimal creditTotal;
    private Journal journal;
    private int nrOfDebits = 0;

    private String description = "";
    private Calendar date = null;

    private final ArrayList<Booking> bookings;
    private BigDecimal VATAmount;
    private BigDecimal turnOverAmount;
    private VATTransaction vatTransaction = null;
    private Contact contact = null;
    private Mortgage mortgage = null;
    private boolean forced = false;

    public Transaction(Calendar date, String description) {
        this.date = date==null?Calendar.getInstance():date;
        this.description = description;
		debitTotal = new BigDecimal(0).setScale(2);
		creditTotal = new BigDecimal(0).setScale(2);
		VATAmount = BigDecimal.ZERO.setScale(2);
		turnOverAmount = BigDecimal.ZERO.setScale(2);
        bookings = new ArrayList<>();
	}

    public Mortgage getMortgage() {
        return mortgage;
    }

    public void setMortgage(Mortgage mortgage) {
        this.mortgage = mortgage;
    }

    @Override
    public TreeMap<String, String> getUniqueProperties(){
        return new TreeMap<>();
    }

	public BigDecimal getDebetTotaal() {
		return debitTotal;
	}

	public BigDecimal getCreditTotaal() {
		return creditTotal;
	}

    // Getters

    public Journal getJournal() {
        return journal;
    }

    public String getAbbreviation() {
        return journal.getAbbreviation();
    }

    public Integer getId(){
        return journal.getId(this);
    }

    public String getDescription(){
        return (description==null)?"":description;
    }

    public Calendar getDate() {
        return date;
    }

    // Setters

    public void setJournal(Journal journal) {
        this.journal = journal;
    }

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDate(Calendar date) {
		this.date = date;
		if(vatTransaction!=null){
		    vatTransaction.setDate(date);
        }
	}

    @Override
    public ArrayList<Booking> getBusinessObjects(){
        return bookings;
    }

    // Adders
    @Override
    public Booking addBusinessObject(Booking booking){
        booking.setTransaction(this);
        BigDecimal amount = booking.getAmount();

        if(booking.isDebit()){
            bookings.add(nrOfDebits, booking);
            nrOfDebits++;
            debitTotal = debitTotal.add(amount);
            debitTotal = debitTotal.setScale(2);
        } else {
            bookings.add(booking);
            creditTotal = creditTotal.add(amount);
            creditTotal = creditTotal.setScale(2);
        }
        return booking;
    }

    @Override
    public void removeBusinessObject(Booking booking){
        booking.setTransaction(null);
        BigDecimal amount = booking.getAmount();

        bookings.remove(booking);
        if(booking.isDebit()){
            nrOfDebits--;
            debitTotal = debitTotal.subtract(amount);
            debitTotal = debitTotal.setScale(2);
        } else {
            creditTotal = creditTotal.subtract(amount);
            creditTotal = creditTotal.setScale(2);
        }

    }

    public boolean isBookable() {
        return !getBusinessObjects().isEmpty() && debitTotal.compareTo(creditTotal) == 0 && debitTotal.compareTo(BigDecimal.ZERO) != 0;
    }

    public ArrayList<Account> getAccounts() {
        ArrayList<Account> accountsList = new ArrayList<>();
        for (Booking booking : getBusinessObjects()) {
            accountsList.add(booking.getAccount());
        }
        return accountsList;
// Can be very brief but unreadable with collect construction
//        return getBusinessObjects().stream().map(Booking::getAccount).collect(Collectors.toCollection(ArrayList::new));
    }

    public VATTransaction getVatTransaction() {
        return vatTransaction;
    }

    // TODO: rename to setVatTransaction (or refactor)
    public void addVatTransaction(VATTransaction vatTransaction) {
        if(this.vatTransaction==null){
            this.vatTransaction = vatTransaction;
            vatTransaction.setTransaction(this);
            vatTransaction.setDate(date);
        } else {
            for (VATBooking vatBooking : vatTransaction.getBusinessObjects()) {
                this.vatTransaction.addBusinessObject(vatBooking);
            }
        }
    }

    public void setVATAmount(BigDecimal VATAmount) {
        this.VATAmount = VATAmount;
    }

    public void setTurnOverAmount(BigDecimal turnOverAmount) {
        this.turnOverAmount = turnOverAmount;
    }

    public BigDecimal getVATAmount() {
        return VATAmount;
    }

    public BigDecimal getTurnOverAmount() {
        return turnOverAmount;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Contact getContact() {
        return contact;
    }

    public void setForced(boolean forced) {
        this.forced = forced;
    }

    public boolean isForced() {
        return forced;
    }
}