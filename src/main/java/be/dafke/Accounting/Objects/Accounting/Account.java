package be.dafke.Accounting.Objects.Accounting;

import be.dafke.MultiValueMap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

/**
  * Boekhoudkundige rekening
  * @author David Danneels
  * @since 01/10/2010
 */
@XmlRootElement
public class Account extends WriteableBusinessObject {
	private AccountType type;
    private BigDecimal debitTotal, creditTotal;
    private final MultiValueMap<Calendar,Booking> bookings;

    public Account() {
        bookings = new MultiValueMap<Calendar,Booking>();
        debitTotal = BigDecimal.ZERO;
        debitTotal = debitTotal.setScale(2);
        creditTotal = BigDecimal.ZERO;
        creditTotal = creditTotal.setScale(2);
    }

    // Setters
    @XmlElement
    public void setAccountType(AccountType type) {
        this.type = type;
    }

    // Getters

    public AccountType getAccountType() {
        return type;
    }

    public BigDecimal getSaldo() {
        BigDecimal result = debitTotal.subtract(creditTotal);
        result = result.setScale(2);
        return result;
    }

    public BigDecimal getDebetTotal() {
        return debitTotal;
    }

    public BigDecimal getCreditTotal() {
        return creditTotal;
    }

    public ArrayList<Booking> getBookings() {
        return bookings.values();
    }

    @Override
    public boolean isDeletable(){
        return bookings.isEmpty();
    }

	protected void book(Booking booking) {
        Calendar date = booking.getTransaction().getDate();
        bookings.addValue(date, booking);
		if (booking.isDebit()) {
            debitTotal = debitTotal.add(booking.getAmount());
            debitTotal = debitTotal.setScale(2);
		} else {
            creditTotal = creditTotal.add(booking.getAmount());
            creditTotal = creditTotal.setScale(2);
		}
	}

	protected void unbook(Booking booking) {
		if (booking.isDebit()) {
			debitTotal = debitTotal.subtract(booking.getAmount());
			debitTotal = debitTotal.setScale(2);
		} else {
			creditTotal = creditTotal.subtract(booking.getAmount());
			creditTotal = creditTotal.setScale(2);
		}
        Calendar date = booking.getTransaction().getDate();
        bookings.removeValue(date, booking);
    }
}