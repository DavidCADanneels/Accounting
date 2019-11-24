package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessCollection
import java.util.function.Predicate

class Transaction extends BusinessCollection<Booking> implements Comparable<Transaction>{
    static final String ID = "id"
    private Integer transactionId = 0
    private BigDecimal debitTotal
    private BigDecimal creditTotal
    private Journal journal
    private List<Journal> duplicateJournals = new ArrayList()
    private int nrOfDebits = 0

    private String description
    private Calendar date

    private final ArrayList<Booking> bookings
    private BigDecimal VATAmount
    private BigDecimal turnOverAmount
    private Contact contact = null
    private Mortgage mortgage = null
    private boolean balanceTransaction = false
    private boolean registered = false

    Transaction(Calendar date, String description) {
        this.date = date==null?Calendar.getInstance():date
        this.description = description
        debitTotal = new BigDecimal(0).setScale(2)
        creditTotal = new BigDecimal(0).setScale(2)
        VATAmount = BigDecimal.ZERO.setScale(2)
        turnOverAmount = BigDecimal.ZERO.setScale(2)
        bookings = new ArrayList()
    }
    void setRegistered() {
        registered = true
    }

    boolean isRegistered() {
        registered
    }

    void setTransactionId(int transactionId) {
        this.transactionId = transactionId
    }

    Mortgage getMortgage() {
        mortgage
    }

    void setMortgage(Mortgage mortgage) {
        this.mortgage = mortgage
    }

//    TODO: uncomment if saved per ID (ID must be the unique identifier)
//    (other options is to setName(id), but id is Integer (better save as int, not as String)
//    @Override
//    TreeMap<String, String> getUniqueProperties(){
//        TreeMap<String,String> keyMap = new TreeMap()
//        keyMap.put(ID, transactionId.toString())
//        keyMap
//
//    }

    BigDecimal getDebetTotaal() {
        debitTotal
    }

    BigDecimal getCreditTotaal() {
        creditTotal
    }

    // Getters

    Journal getJournal() {
        journal
    }

    String getAbbreviation() {
        if(journal == null){
            "NULL"
        }
        journal.getAbbreviation()
    }

    Integer getId(){
        if(journal == null){
            0
        } else journal.getId(this)
    }

    int getTransactionId() {
        transactionId
    }

    String getDescription(){
        (description==null)?"":description
    }

    Calendar getDate() {
        date
    }

    // Setters

    void setJournal(Journal journal) {
        this.journal = journal
    }

    void setDescription(String description) {
        this.description = description
    }

    void setDate(Calendar date) {
        this.date = date
    }

    @Override
    ArrayList<Booking> getBusinessObjects(){
        bookings
    }

    // Adders
    @Override
    Booking addBusinessObject(Booking booking){
        booking.setTransaction(this)
        BigDecimal amount = booking.getAmount()

        if(booking.isDebit()){
            bookings.add(nrOfDebits, booking)
            nrOfDebits++
            debitTotal = debitTotal.add(amount)
            debitTotal = debitTotal.setScale(2)
        } else {
            bookings.add(booking)
            creditTotal = creditTotal.add(amount)
            creditTotal = creditTotal.setScale(2)
        }
        booking
    }

    @Override
    void removeBusinessObject(Booking booking){
        booking.setTransaction(null)
        BigDecimal amount = booking.getAmount()

        bookings.remove(booking)
        if(booking.isDebit()){
            nrOfDebits--
            debitTotal = debitTotal.subtract(amount)
            debitTotal = debitTotal.setScale(2)
        } else {
            creditTotal = creditTotal.subtract(amount)
            creditTotal = creditTotal.setScale(2)
        }

    }

    boolean isBookable() {
        !getBusinessObjects().isEmpty() && debitTotal.compareTo(creditTotal) == 0 && debitTotal.compareTo(BigDecimal.ZERO) != 0
    }

    ArrayList<Account> getAccounts() {
        ArrayList<Account> accountsList = new ArrayList()
        for (Booking booking : getBusinessObjects()) {
            accountsList.add(booking.getAccount())
        }
        accountsList
// Can be very brief but unreadable with collect construction
//        getBusinessObjects().stream().map(Booking::getAccount).collect(Collectors.toCollection(ArrayList::new))
    }

    ArrayList<VATBooking> getVatBookings(){
        ArrayList<VATBooking> result = new ArrayList()
        for(Booking booking: bookings){
            result.addAll(booking.getVatBookings())
        }
        result
    }

    ArrayList<VATBooking> getMergedVatBookings(){
        HashMap<String, VATBooking> hashMap = new HashMap()
        bookings.forEach({ booking ->
            ArrayList<VATBooking> vatBookings = booking.getVatBookings()
            vatBookings.forEach({ vatBooking ->
                VATField vatField = vatBooking.getVatField()
                VATBooking foundBooking = hashMap.get(vatField.getName())
                if (foundBooking == null) {
                    foundBooking = vatBooking
                } else {
                    VATMovement movement = vatBooking.getVatMovement()
                    VATMovement foundMovement = foundBooking.getVatMovement()
                    BigDecimal totalAmount = foundMovement.getAmount().add(movement.getAmount()).setScale(2, BigDecimal.ROUND_HALF_DOWN)
                    VATMovement totalMovement = new VATMovement(totalAmount)
                    foundBooking = new VATBooking(vatField, totalMovement)
                }
                hashMap.put(vatField.getName(), foundBooking)
            })
        })
        ArrayList<VATBooking> result = new ArrayList()
        result.addAll(hashMap.values())
        result
    }

    void setVATAmount(BigDecimal VATAmount) {
        this.VATAmount = VATAmount
    }

//    void increaseVATAmount(BigDecimal VATAmount) {
//        this.VATAmount = this.VATAmount.add(VATAmount)
//    }

    void setTurnOverAmount(BigDecimal turnOverAmount) {
        this.turnOverAmount = turnOverAmount
    }

    void increaseTurnOverAmount(BigDecimal turnOverAmount) {
        this.turnOverAmount = this.turnOverAmount.add(turnOverAmount)
    }

    BigDecimal getVATAmount() {
        VATAmount
    }

    BigDecimal getTurnOverAmount() {
        turnOverAmount
    }

    void setContact(Contact contact) {
        this.contact = contact
    }

    Contact getContact() {
        contact
    }

    void setBalanceTransaction(boolean balanceTransaction) {
        this.balanceTransaction = balanceTransaction
    }

    boolean isBalanceTransaction() {
        balanceTransaction
    }

    static Predicate<Transaction> ofYear(int year) {
        { transaction -> transaction.getDate() != null && transaction.getDate().get(Calendar.YEAR) == year }
    }

    @Override
    int compareTo(Transaction o) {
        int i = date.compareTo(o.date)
        if(i!=0)
            i
        else
            transactionId-o.transactionId
    }

    void addDuplicateJournal(Journal journal) {
        duplicateJournals.add(journal)
    }

    List<Journal> getDuplicateJournals() {
        duplicateJournals
    }
}