package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessObject

import java.util.function.Predicate

class Movement extends BusinessObject{
    static int count = 0
    static final String DATE = "date"
    static final String ID = "id"
    static final String DEBIT = "debit"
    static final String CREDIT = "credit"
    BigDecimal amount
    boolean debit
    Booking booking
    Integer id

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

    Transaction getTransaction(){
        booking.transaction
    }

    Journal getJournal(){
        getTransaction().journal
    }

    String getTransactionString(){
        booking.transaction.abbreviation + booking.transaction.id
    }

    Calendar getDate() {
        booking.transaction.date
    }

    void setDate(Calendar date){
        booking.transaction.date = date
    }

    String getDescription(){
        booking.transaction.description
    }

    void setDescription(String description){
        booking.transaction.description = description
    }

    static Predicate<Movement> ofYear(int year) {
        { movement -> movement.date != null && movement.date.get(Calendar.YEAR) == year }
    }
}
