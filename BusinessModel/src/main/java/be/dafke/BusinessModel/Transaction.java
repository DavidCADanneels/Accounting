package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.function.Predicate;

/**
 * Boekhoudkundige transactie Bevat minstens 2 boekingen
 * @author David Danneels
 * @since 01/10/2010
 * @see Booking
 */
public class Transaction extends BusinessCollection<Booking> implements Comparable<Transaction>{
    public static final String ID = "id";
    private Integer transactionId = 0;
    private BigDecimal debitTotal;
    private BigDecimal creditTotal;
    private Journal journal;
    private List<Journal> duplicateJournals = new ArrayList<>();
    private int nrOfDebits = 0;

    private String description;
    private Calendar date;

    private final ArrayList<Booking> bookings;
    private BigDecimal VATAmount;
    private BigDecimal turnOverAmount;
    private Contact contact = null;
    private Mortgage mortgage = null;
    private boolean balanceTransaction = false;
    private boolean registered = false;

    public Transaction(Calendar date, String description) {
        this.date = date==null?Calendar.getInstance():date;
        this.description = description;
        debitTotal = new BigDecimal(0).setScale(2);
        creditTotal = new BigDecimal(0).setScale(2);
        VATAmount = BigDecimal.ZERO.setScale(2);
        turnOverAmount = BigDecimal.ZERO.setScale(2);
        bookings = new ArrayList<>();
    }
    public void setRegistered() {
        registered = true;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public Mortgage getMortgage() {
        return mortgage;
    }

    public void setMortgage(Mortgage mortgage) {
        this.mortgage = mortgage;
    }

//    TODO: uncomment if saved per ID (ID must be the unique identifier)
//    (other options is to setName(id), but id is Integer (better save as int, not as String)
//    @Override
//    public TreeMap<String, String> getUniqueProperties(){
//        TreeMap<String,String> keyMap = new TreeMap<>();
//        keyMap.put(ID, transactionId.toString());
//        return keyMap;
//
//    }

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
        if(journal == null){
            return "NULL";
        }
        return journal.getAbbreviation();
    }

    public Integer getId(){
        if(journal == null){
            return 0;
        } else return journal.getId(this);
    }

    public int getTransactionId() {
        return transactionId;
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

    public ArrayList<VATBooking> getVatBookings(){
        ArrayList<VATBooking> result = new ArrayList<>();
        for(Booking booking: bookings){
            result.addAll(booking.getVatBookings());
        }
        return result;
    }

    public void setVATAmount(BigDecimal VATAmount) {
        this.VATAmount = VATAmount;
    }

//    public void increaseVATAmount(BigDecimal VATAmount) {
//        this.VATAmount = this.VATAmount.add(VATAmount);
//    }

    public void setTurnOverAmount(BigDecimal turnOverAmount) {
        this.turnOverAmount = turnOverAmount;
    }

    public void increaseTurnOverAmount(BigDecimal turnOverAmount) {
        this.turnOverAmount = this.turnOverAmount.add(turnOverAmount);
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

    public void setBalanceTransaction(boolean balanceTransaction) {
        this.balanceTransaction = balanceTransaction;
    }

    public boolean isBalanceTransaction() {
        return balanceTransaction;
    }

    public static Predicate<Transaction> ofYear(int year) {
        return transaction -> transaction.getDate()!=null && transaction.getDate().get(Calendar.YEAR)==year;
    }

    @Override
    public int compareTo(Transaction o) {
        int i = date.compareTo(o.date);
        if(i!=0)
            return i;
        else
            return transactionId-o.transactionId;
    }

    public void addDuplicateJournal(Journal journal) {
        duplicateJournals.add(journal);
    }

    public List<Journal> getDuplicateJournals() {
        return duplicateJournals;
    }
}