package be.dafke.Accounting.Objects.Accounting;

import java.math.BigDecimal;

/**
 * User: Dafke
 * Date: 9/03/13
 * Time: 17:01
 */
public class Movement implements Comparable<Movement>{
    private BigDecimal amount;
    private boolean debit;
    private Booking booking;

    public Movement(BigDecimal amount, boolean debit){
        this.amount = amount;
        this.debit = debit;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public boolean isDebit() {
        return debit;
    }

    @Override
    public int compareTo(Movement o) {
        Integer id1 = booking.getTransaction().getId();
        Integer id2 = o.booking.getTransaction().getId();
        return id1.compareTo(id2);
    }
}
