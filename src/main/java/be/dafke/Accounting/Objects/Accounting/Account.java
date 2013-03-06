package be.dafke.Accounting.Objects.Accounting;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

@XmlRootElement()
/**
 * Boekhoudkundige rekening
 * @author David Danneels
 * @since 01/10/2010
 */
public class Account extends BusinessObject{

    public enum AccountType {
		Active, Passive, Cost, Revenue, Credit, Debit;
		public static ArrayList<AccountType> getList() {
			ArrayList<AccountType> list = new ArrayList<AccountType>();
			for(AccountType type : values()) {
				list.add(type);
			}
			return list;
		}
	}

	private AccountType type;
    private Project project;
    private BigDecimal debettotaal, credittotaal;
    private final ArrayList<Booking> boekingen;
    //	private static final ResourceBundle bundle = ResourceBundle.getBundle("Accounting");
    //	private boolean save;

	public Account() {
		project = null;
		boekingen = new ArrayList<Booking>();
		debettotaal = new BigDecimal(0);
		debettotaal = debettotaal.setScale(2);
		credittotaal = new BigDecimal(0);
		credittotaal = credittotaal.setScale(2);
//		setSaved(false);
	}

	/**
	 * Geeft het project terug waartoe de rekening behoort
	 * @return het project waartoe de rekening behoort Dit kan <b><i>null</i></b> zijn !!!
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * Stelt het project in waartoe de rekening behoort
	 * @param p het project waartoe de rekening voortaan behoort
	 */
	public void setProject(Project p) {
		project = p;
	}

    @Override
    public boolean isDeletable(){
        return boekingen.isEmpty();
    }

    /**
     * Geeft de boekingen terug die bij deze rekening horen
     * @return de boekingen die bij deze rekening horen
     */
	public ArrayList<Booking> getBookings() {
		return boekingen;
	}

	/**
	 * Voeg een boeking toe
	 * @param booking de toe te voegen boeking
	 */
	private void addBooking(Booking booking) {
		Calendar datum = booking.getDate();
		if (boekingen.size() == 0 || datum.compareTo(boekingen.get(boekingen.size() - 1).getDate()) >= 0) boekingen.add(booking);
		else {
			int plaats = boekingen.size();
			boolean found = false;
			for(int i = 0; i < boekingen.size(); i++) {
				Booking transactie = boekingen.get(i);
				Calendar date = transactie.getDate();
				if (!found && date.compareTo(datum) > 0) {
					plaats = i;
					found = true;
				}
			}
			if (!found) {
				// hier kom je nooit
				boekingen.add(booking);
			} else {
				boekingen.add(plaats, booking);
			}
		}
	}

	/**
	 * Boek een nieuwe boeking
	 * @param booking de nieuwe boeking
	 */
	protected void book(Booking booking) {
        addBooking(booking);
		if (booking.isDebit()) {
            debettotaal = debettotaal.add(booking.getAmount());
            debettotaal = debettotaal.setScale(2);
		} else {
            credittotaal = credittotaal.add(booking.getAmount());
            credittotaal = credittotaal.setScale(2);
		}
//		setSaved(false);
	}

	/**
	 * Geeft het debettotaal van deze rekening terug
	 * @return het debettotaal (decimaal met 2 cijfers na de komma)
	 */
	public BigDecimal getDebetTotal() {
		return debettotaal;
	}

	/**
	 * Geeft het credittotaal van deze rekening terug
	 * @return het credittotaal (decimaal met 2 cijfers na de komma)
	 */
	public BigDecimal getCreditTotal() {
		return credittotaal;
	}

	/**
	 * Geeft het type van deze rekening terug
	 * @return het type van deze rekening
	 * <ul>
	 * <li>0 : Actief</li>
	 * <li>1 : Passief</li>
	 * <li>2 : Kost</li>
	 * <li>3 : Opbrengst</li>
	 * <li>4 : Tegoed van Klant</li>
	 * <li>5 : Schuld aan Leverancier</li>
	 * </ul>
	 */
	public AccountType getAccountType() {
		return type;
	}

	/**
	 * Geeft het saldo van deze rekening terug
	 * @return het saldo van deze rekening (decimaal met 2 cijfers na de komma) Is het type van deze rekening:
	 * <ul>
	 * <li>even: return (debettotaal-credittotaal)</li>
	 * <li>oneven: return (credittotaal-debettotaal)</li>
	 * </ul>
	 * Het resultaat kan dus negatief zijn
	 */
	public BigDecimal saldo() {
		BigDecimal result = debettotaal.subtract(credittotaal);
		result = result.setScale(2);
		return result;
	}

    /**
	 * Geeft de naam van de rekening en het ev. bijhorende project terug
	 * @return de naam van de rekening <b><i>naam rekening</i></b> of <b><i>naam rekening(naam project)</i></b> indien
	 * het project niet <b>null</b> is.
	 */

	@Override
	public String toString() {
		if (project == null) return getName();
		return getName() + " [" + project.toString() + "]";
	}

	/**
	 * Verwijdert de gegeven boeking
	 * @param booking de te verwijderen boeking
	 */
	protected void unbook(Booking booking) {
		if (booking.isDebit()) {
			debettotaal = debettotaal.subtract(booking.getAmount());
			debettotaal = debettotaal.setScale(2);
		} else {
			credittotaal = credittotaal.subtract(booking.getAmount());
			credittotaal = credittotaal.setScale(2);
		}
		boekingen.remove(booking);
//		setSaved(false);
	}


	public void setAccountType(AccountType type) {
		this.type = type;
	}
}