package be.dafke.Accounting.Objects.Accounting;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Boekhoudkundige boeking Is onderdeel van een Transactie
 * @author David Danneels
 * @since 01/10/2010
 * @see Transaction
 */
public class Booking implements Comparable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String afkorting;
	private String descr;
	private Calendar datum;
	private final Account rekening;
	private final BigDecimal bedrag;
	private final boolean isDebet;
	private final Transaction transactie;

	/**
	 * Constructor
	 * @param transaction de transactie waartoe deze boeking behoort
	 * @param account de rekening horende bij deze boeking
	 * @param amount het bedrag waarmee de rekening gedebiteerd/gecrediteerd moet worden
	 * @param debit of het bedrag gedebiteerd of gecrediteerd moet worden
	 * <ul>
	 * <li><i><b>true</b></i>: debiteren</li>
	 * <li><i><b>false</b></i>: crediteren</li>
	 * </ul>
	 */
	public Booking(Transaction transaction, Account account, BigDecimal amount, boolean debit) {
		transactie = transaction;
//		descr = description;
		rekening = account;
		bedrag = amount;
		isDebet = debit;
		//datum = date;
	}

	/**
	 * Geeft de transactie terug waartoe deze boeking hoort
	 * @return de transactie waartoe deze boeking hoort
	 */
	public Transaction getTransaction() {
		return transactie;
	}

//	/**
//	 * Stel de datum van de boeking in
//	 * @param date de datum van de boeking
//	 */
//	public void setDate(Calendar date) {
//		datum = date;
//	}

//	/**
//	 * Stelt het id-nummer van de boeking in
//	 * @param s het id-nummer van de boeking
//	 */
//	protected void setID(int s) {
//		id = s;
//	}

//	/**
//	 * Stelt de afkorting van de boeking in
//	 * @param abbrev de afkorting van de boeking
//	 */
//	public void setAbbreviation(String abbrev) {
//		afkorting = abbrev;
//	}

	/**
	 * Geeft de afkorting van de boeking terug
	 * @return de afkorting van de boeking
	 */
	public String getAbbreviation() {
		return transactie.getAbbreviation();
	}

//	/**
//	 * Stelt de omschrijving van de boeking in
//	 * @param description de omschrijving van de boeking
//	 */
//	public void setDescription(String description) {
//		descr = description;
//	}

	/**
	 * Geeft het id-nummer van de boeking terug
	 * @return het id-nummer van de boeking
	 */
	public int getId() {
		return transactie.getId();
	}

	/**
	 * Verlaag het id-nummer van de boeking
	 */
	protected void lowerId() {
		id--;
	}

	/**
	 * Verhoog het id-nummer van de boeking
	 */
	protected void raiseId() {
		id++;
	}

	/**
	 * Geeft de datum terug waarop deze boeking plaatsvond
	 * @return de datum waarop deze boeking plaatsvond
	 */
	public Calendar getDate() {
		return transactie.getDate();
	}

	/**
	 * Geeft de omschrijving van de boeking terug
	 * @return de omschrijving van de boeking
	 */
	public String getDescription() {
		return transactie.getDescription();
	}

	/**
	 * Geeft de rekening van de boeking terug
	 * @return de rekening van de boeking
	 */
	public Account getAccount() {
		return rekening;
	}

	/**
	 * Geeft het bedrag van de boeking terug
	 * @return het bedrag van de boeking
	 */
	public BigDecimal getAmount() {
		return bedrag;
	}

	/**
	 * Geeft aan of de boeking gedebiteerd of gecrediteerd moet worden
	 * @return of de boeking gedebiteerd of gecrediteerd moet worden
	 * <ul>
	 * <li><i><b>true</b></i>: debiteren</li>
	 * <li><i><b>false</b></i>: crediteren</li>
	 * </ul>
	 */
	public boolean isDebet() {
		return isDebet;
	}

	/**
	 * Functie om boekingen te ordenen
	 * @param t de boeking waarmee vergeleken wordt
	 * @return <ul>
	 * <li><b>kleiner dan 0</b>: het id-nummer van deze boeking is kleiner dan dat van <i>t</i></li>
	 * <li><b>groter dan 0</b>: het id-nummer van deze boeking is groter dan dat van <i>t</i></li>
	 * <li><b>0</b>: de id-nummers van beide boekingen zijn gelijk. Dit kan enkel wanneer de boekingen tot verschillende
	 * dagboeken horen</li>
	 * </ul>
	 * @see Comparable
	 */
	@Override
	public int compareTo(Object t) {
		return id - ((Booking) t).id;
	}
}