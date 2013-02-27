package be.dafke.Accounting.Objects.Accounting;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

@XmlRootElement()
/**
 * Boekhoudkundige rekening
 * @author David Danneels
 * @since 01/10/2010
 */
public class Account implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

//	private static final ResourceBundle bundle = ResourceBundle.getBundle("Accounting");
	private String name;
	private AccountType type;
	private BigDecimal debettotaal, credittotaal;
	private final ArrayList<Booking> boekingen;
//	private boolean save;
	private Project project;
	private File xmlFile;
	private File htmlFile;
	private File xslFile;

	/**
	 * Constructor
	 * @param name naam van de rekening
	 * @param type nr die het soort rekening aanduidt
	 * <p>
	 * <ul>
	 * <li>0 : Actief</li>
	 * <li>1 : Passief</li>
	 * <li>2 : Kost</li>
	 * <li>3 : Opbrengst</li>
	 * <li>4 : Tegoed van Klant</li>
	 * <li>5 : Schuld aan Leverancier</li>
	 * </ul>
	 * In balansen worden rekeningen van het even type steeds links weergegeven, die van het oneven type rechts
	 */
	protected Account(String name, AccountType type) {
		project = null;
		this.name = name;
		this.type = type;
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

//	/**
//	 * Deelt mee of de laatste wijzigingen aan de rekening reeds werden uitgeschreven naar een XML bestand
//	 * @return of de laatste wijzigingen aan de rekening reeds werden uitgeschreven naar een XML bestand
//	 */
//	public boolean isSaved() {
//		return save;
//	}
//
//	/**
//	 * Stelt in of de laatste wijzigingen aan de rekening reeds werden uitgeschreven naar een XML bestand
//	 * @param save of de laatste wijzigingen aan de rekening reeds werden uitgeschreven naar een XML bestand
//	 */
//	protected void setSaved(boolean save) {
//		accounting.setSaved(save);
//		this.save = save;
//	}

	@XmlList
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
	public AccountType getType() {
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

    public File getXmlFile(){
        return xmlFile;
    }

    public File getXslFile(){
        return xslFile;
    }

    public File getHtmlFile(){
        return htmlFile;
    }

    public void setXmlFile(File xmlFile) {
        this.xmlFile = xmlFile;
    }

    public void setXslFile(File xslFile) {
        this.xslFile = xslFile;
    }

    public void setHtmlFile(File htmlFile) {
        this.htmlFile = htmlFile;
    }

    /**
	 * Geeft de naam van de rekening en het ev. bijhorende project terug
	 * @return de naam van de rekening <b><i>naam rekening</i></b> of <b><i>naam rekening(naam project)</i></b> indien
	 * het project niet <b>null</b> is.
	 */

	@Override
	public String toString() {
		if (project == null) return name;
		return name + " [" + project.toString() + "]";
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

	public void setName(String newName) {
		name = newName;
//		setSaved(false);
	}

	public void setType(AccountType type) {
		this.type = type;
	}

	@XmlElement
	public String getName() {
		return name;
	}
}