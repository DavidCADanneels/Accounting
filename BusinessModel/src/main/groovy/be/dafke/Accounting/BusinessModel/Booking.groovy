package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessObject

import java.util.function.Predicate

class Booking extends BusinessObject {
    static final String ID = "id"
    Account account
    Movement movement
    Transaction transaction

    static final String DEBIT = "debit"
    static final String CREDIT = "credit"
    static final String ACCOUNT = "Account"
    ArrayList<VATBooking> vatBookings = new ArrayList()

    Booking(Account account, BigDecimal amount, boolean debit, int id) {
        this.account = account
        movement = new Movement(amount, debit, id)
        movement.booking = this
    }
    Booking(Account account, BigDecimal amount, boolean debit) {
        this.account = account
        movement = new Movement(amount, debit)
        movement.booking = this
    }

    Booking(Booking booking) {
        this.account = booking.account
        this.movement = new Movement(booking.movement)
        booking.vatBookings.forEach({ vatBooking ->
            VATBooking newVatBooking = new VATBooking(vatBooking)
            this.vatBookings.add(newVatBooking)
        })
        movement.booking = this
    }

    static Predicate<Booking> withAccount(Account account){
        { booking -> booking.account == account }
    }

    static Predicate<Booking> vatBooking(){
        { booking -> !booking.vatBookings.isEmpty() }
    }

    boolean isDebit(){
        movement.debit
    }

    boolean isCredit(){
        !movement.debit
    }

    void setDebit(boolean debit){
        movement.debit = debit
    }

    BigDecimal getAmount(){
        movement.amount
    }

    void setAmount(BigDecimal amount){
        movement.amount = amount
    }

    Movement getMovement(){
        movement
    }

    Integer getId(){
        movement.id
    }

    @Override
    TreeMap<String, String> getUniqueProperties(){
        new TreeMap()
    }

    // Getters

    Transaction getTransaction() {
        transaction
    }

    void setAccount(Account account) {
        this.account = account
    }

    Account getAccount() {
        account
    }

    // Setters

    void setTransaction(Transaction transaction) {
        this.transaction = transaction
    }

    void addVatBooking(VATBooking vatBooking) {
        vatBookings.add(vatBooking)
    }

    ArrayList<VATBooking> getVatBookings() {
        vatBookings
    }

    String getVATBookingsString(){
        if(vatBookings == null || vatBookings.isEmpty()){
            ""
        } else {
            StringBuffer buffer = new StringBuffer("(")
            for (VATBooking vatBooking:vatBookings) {
                VATField vatField = vatBooking.vatField
                if(vatField != null){
                    VATMovement vatMovement = vatBooking.vatMovement
                    if(vatMovement != null){
                        BigDecimal amount = vatMovement.amount
                        boolean plus = amount.compareTo(BigDecimal.ZERO) >= 0
                        buffer.append(plus ? "+" : "-")
                        buffer.append(vatField.name)
                    }
                }
            }
            buffer.append(")")
            buffer.toString()
        }

    }

    String getMergedVATBookingsString(){
        if(vatBookings == null || vatBookings.isEmpty()){
            ""
        } else {
            StringBuffer buffer = new StringBuffer("(")
            ArrayList<String> vatFieldNames = new ArrayList()
            for (VATBooking vatBooking:vatBookings) {
                VATField vatField = vatBooking.vatField
                String vatFieldName = vatField.name
                if(!vatFieldNames.contains(vatFieldName)){
                    VATMovement vatMovement = vatBooking.vatMovement
                    if(vatMovement != null){
                        BigDecimal amount = vatMovement.amount
                        boolean plus = amount.compareTo(BigDecimal.ZERO) >= 0
                        buffer.append(plus ? "+" : "-")
                        buffer.append(vatFieldName)
                    }
                    vatFieldNames.add(vatFieldName)
                }
            }
            buffer.append(")")
            buffer.toString()
        }

    }
}
