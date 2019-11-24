package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessObject

import java.util.function.Predicate

class Movement extends BusinessObject{
    private static int count = 0
    static final String DATE = "date"
    static final String ID = "id"
    static final String DEBIT = "debit"
    static final String CREDIT = "credit"
    private BigDecimal amount
    private boolean debit
    private Booking booking
    private Integer id

    Movement(BigDecimal amount, boolean debit){
        this.amount = amount
        this.debit = debit
        this.id = ++count
    }

    Movement(BigDecimal amount, boolean debit, int id) {
        this.amount = amount
        this.debit = debit
        this.id = id
        count++
    }

    Movement(Movement movement) {
        this.amount = movement.amount
        this.debit = movement.debit
    }

    @Override
    TreeMap<String, String> getUniqueProperties(){
        TreeMap<String,String> properties = new TreeMap<String, String>()
        properties.put(ID,id.toString())
        properties
    }

    Booking getBooking() {
        booking
    }

    Transaction getTransaction(){
        booking.getTransaction()
    }

    Journal getJournal(){
        getTransaction().getJournal()
    }

    String getTransactionString(){
        booking.getTransaction().getAbbreviation() + booking.getTransaction().getId()
    }

    void setBooking(Booking booking) {
        this.booking = booking
    }

    BigDecimal getAmount() {
        amount
    }

    boolean isDebit() {
        debit
    }

    void setDebit(boolean debit) {
        this.debit = debit
    }

    void setAmount(BigDecimal amount) {
        this.amount = amount
    }

    Integer getId() {
        id
    }

    Calendar getDate() {
        booking.getTransaction().getDate()
    }

    void setDate(Calendar date){
        booking.getTransaction().setDate(date)
    }

    String getDescription(){
        booking.getTransaction().getDescription()
    }

    void setDescription(String description){
        booking.getTransaction().setDescription(description)
    }

    static Predicate<Movement> ofYear(int year) {
        { movement -> movement.getDate() != null && movement.getDate().get(Calendar.YEAR) == year }
    }
}
