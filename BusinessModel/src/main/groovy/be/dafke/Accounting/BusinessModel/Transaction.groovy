package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessCollection
import java.util.function.Predicate

class Transaction extends BusinessCollection<Booking> implements Comparable<Transaction>{
    static final String ID = "id"
    Integer transactionId = 0
    BigDecimal debitTotal
    BigDecimal creditTotal
    Journal journal
    List<Journal> duplicateJournals = new ArrayList()
    int nrOfDebits = 0

    String description = ""
    Calendar date

    final ArrayList<Booking> bookings
    BigDecimal VATAmount
    BigDecimal turnOverAmount
    Contact contact = null
    Mortgage mortgage = null
    boolean balanceTransaction = false
    boolean registered = false

    Transaction(Calendar date, String description) {
        this.date = date==null?Calendar.getInstance():date
        this.description = description
        debitTotal = new BigDecimal(0).setScale(2)
        creditTotal = new BigDecimal(0).setScale(2)
        VATAmount = BigDecimal.ZERO.setScale(2)
        turnOverAmount = BigDecimal.ZERO.setScale(2)
        bookings = new ArrayList()
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

    // Getters

    String getAbbreviation() {
        if(journal == null){
            "NULL"
        }
        journal.abbreviation
    }

    Integer getId(){
        if(journal == null){
            0
        } else journal.getId(this)
    }

//    String getDescription(){
//        (description==null)?"":description
//    }


    @Override
    ArrayList<Booking> getBusinessObjects(){
        bookings
    }

    // Adders
    @Override
    Booking addBusinessObject(Booking booking){
        booking.setTransaction(this)
        BigDecimal amount = booking.amount

        if(booking.debit){
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
        BigDecimal amount = booking.amount

        bookings.remove(booking)
        if(booking.debit){
            nrOfDebits--
            debitTotal = debitTotal.subtract(amount)
            debitTotal = debitTotal.setScale(2)
        } else {
            creditTotal = creditTotal.subtract(amount)
            creditTotal = creditTotal.setScale(2)
        }

    }

    boolean isBookable() {
        !getBusinessObjects().empty && debitTotal.compareTo(creditTotal) == 0 && debitTotal.compareTo(BigDecimal.ZERO) != 0
    }

    ArrayList<Account> getAccounts() {
        ArrayList<Account> accountsList = new ArrayList()
        for (Booking booking : getBusinessObjects()) {
            accountsList.add(booking.account)
        }
        accountsList
// Can be very brief but unreadable with collect construction
//        getBusinessObjects().stream().map(Booking::getAccount).collect(Collectors.toCollection(ArrayList::new))
    }

    ArrayList<VATBooking> getVatBookings(){
        ArrayList<VATBooking> result = new ArrayList()
        for(Booking booking: bookings){
            result.addAll(booking.vatBookings)
        }
        result
    }

    ArrayList<VATBooking> getMergedVatBookings(){
        HashMap<String, VATBooking> hashMap = new HashMap()
        bookings.forEach({ booking ->
            ArrayList<VATBooking> vatBookings = booking.vatBookings
            vatBookings.forEach({ vatBooking ->
                VATField vatField = vatBooking.vatField
                VATBooking foundBooking = hashMap.get(vatField.name)
                if (foundBooking == null) {
                    foundBooking = vatBooking
                } else {
                    VATMovement movement = vatBooking.vatMovement
                    VATMovement foundMovement = foundBooking.vatMovement
                    BigDecimal totalAmount = foundMovement.amount.add(movement.amount).setScale(2, BigDecimal.ROUND_HALF_DOWN)
                    VATMovement totalMovement = new VATMovement(totalAmount)
                    foundBooking = new VATBooking(vatField, totalMovement)
                }
                hashMap.put(vatField.name, foundBooking)
            })
        })
        ArrayList<VATBooking> result = new ArrayList()
        result.addAll(hashMap.values())
        result
    }


//    void increaseVATAmount(BigDecimal VATAmount) {
//        this.VATAmount = this.VATAmount.add(VATAmount)
//    }

    void increaseTurnOverAmount(BigDecimal turnOverAmount) {
        this.turnOverAmount = this.turnOverAmount.add(turnOverAmount)
    }

    static Predicate<Transaction> ofYear(int year) {
        { transaction -> transaction.date != null && transaction.date.get(Calendar.YEAR) == year }
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
}