package be.dafke.Accounting.Objects.Accounting;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Boekhoudkundige boeking Is onderdeel van een Transactie
 * @author David Danneels
 * @since 01/10/2010
 * @see Transaction
 */
public class Booking {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Account account;
	private BigDecimal amount;
	private boolean debit;
	private final Transaction transaction;

	/**
	 * Constructor
	 * @param transaction de transaction waartoe deze boeking behoort
	 * @param account de account horende bij deze boeking
	 * @param amount het amount waarmee de account gedebiteerd/gecrediteerd moet worden
	 * @param debit of het amount gedebiteerd of gecrediteerd moet worden
	 * <ul>
	 * <li><i><b>true</b></i>: debiteren</li>
	 * <li><i><b>false</b></i>: crediteren</li>
	 * </ul>
	 */
	public Booking(Transaction transaction, Account account, BigDecimal amount, boolean debit) {
		this.transaction = transaction;
		this.account = account;
		this.amount = amount;
		this.debit = debit;
	}

	/**
	 * Geeft de transaction terug waartoe deze boeking hoort
	 * @return de transaction waartoe deze boeking hoort
	 */
	public Transaction getTransaction() {
		return transaction;
	}

	/**
	 * Geeft de afkorting van de boeking terug
	 * @return de afkorting van de boeking
	 */
	public String getAbbreviation() {
		return transaction.getAbbreviation();
	}

	/**
	 * Geeft het id-nummer van de boeking terug
	 * @return het id-nummer van de boeking
	 */
	public int getId() {
		return transaction.getId();
	}

	/**
	 * Geeft de date terug waarop deze boeking plaatsvond
	 * @return de date waarop deze boeking plaatsvond
	 */
	public Calendar getDate() {
		return transaction.getDate();
	}

	/**
	 * Geeft de omschrijving van de boeking terug
	 * @return de omschrijving van de boeking
	 */
	public String getDescription() {
		return transaction.getDescription();
	}

	/**
	 * Geeft de account van de boeking terug
	 * @return de account van de boeking
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * Geeft het amount van de boeking terug
	 * @return het amount van de boeking
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * Geeft aan of de boeking gedebiteerd of gecrediteerd moet worden
	 * @return of de boeking gedebiteerd of gecrediteerd moet worden
	 * <ul>
	 * <li><i><b>true</b></i>: debiteren</li>
	 * <li><i><b>false</b></i>: crediteren</li>
	 * </ul>
	 */
	public boolean isDebit() {
		return debit;
	}

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setDebit(boolean debit) {
        this.debit = debit;
    }

    public boolean isFirstBooking() {
        return this == transaction.getBookings().get(0);
    }
}