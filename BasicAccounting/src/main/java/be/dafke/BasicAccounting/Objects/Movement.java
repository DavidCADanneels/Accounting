package be.dafke.BasicAccounting.Objects;

import java.math.BigDecimal;

/**
 * User: Dafke
 * Date: 9/03/13
 * Time: 17:01
 */
public class Movement {
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

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
