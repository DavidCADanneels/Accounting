package be.dafke.Accounting.Objects.Accounting;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Boekhoudkundig dagboek
 * @author David Danneels
 * @since 01/10/2010
 */
public class Journal implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String abbreviation;
	private int id;
	private final ArrayList<Transaction> transacties;
//	private boolean save;
	private JournalType journalType;
//	private final String DEFAULT_XSL = "../../xsl/Journal.xsl";
//	private final String xslFile = DEFAULT_XSL;
	private File xmlFile;
	private File htmlFile;
	private File xslFile;

	/**
	 * Constructor
	 * @param name naam van het dagboek
	 * @param abbreviation afkorting van het dagboek Deze afkorting wordt gebruikt in de transacties horende bij dit
	 * dagboek
	 * @param journalType the type of journal, e.g. purchase, sales, finance
	 */
	protected Journal(String name, String abbreviation, JournalType journalType) {
//		save = true;
		transacties = new ArrayList<Transaction>();
		this.name = name;
		this.abbreviation = abbreviation;
		id = 1;
		this.journalType = journalType;
	}

    public Booking getBooking(int row){
        ArrayList<Booking> boekingen = new ArrayList<Booking>();
        for(Transaction transaction : transacties){
            boekingen.addAll(transaction.getBookings());
        }
        return boekingen.get(row);
    }

    public Transaction getTransaction(int row){
        return getBooking(row).getTransaction();
    }

    /**
	 * Geeft de naam van het dagboek en de bijhorende afkorting terug
	 * @return de naam van het dagboek en de bijhorende afkorting <b><i>naam dagboek(afkorting)</i></b>
	 */

	@Override
	public String toString() {
		return name + " (" + abbreviation + ")";
	}

	/**
	 * Geeft de transacties terug die bij dit dagboek horen
	 * @return de transacties die bij dit dagboek horen
	 */
	public ArrayList<Transaction> getTransactions() {
		return transacties;
	}

	/**
	 * Geeft het id van de volgende transactie terug
	 * @return het id van de volgende transactie
	 */
	public int getId() {
		return id;
	}

	/**
	 * Geeft de afkorting van het dagboek terug
	 * @return de afkorting van het dagboek
	 */
	public String getAbbreviation() {
		return abbreviation;
	}

	/**
	 * Deelt mee of de laatste wijzigingen aan het dagboek reeds werden uitgeschreven naar een XML bestand
	 * @return of de laatste wijzigingen aan het dagboek reeds werden uitgeschreven naar een XML bestand
	 */
//	public boolean isSaved() {
//		return save;
//	}

	/**
	 * Stelt in of de laatste wijzigingen aan het dagboek reeds werden uitgeschreven naar een XML bestand
	 * @param s of de laatste wijzigingen aan het dagboek reeds werden uitgeschreven naar een XML bestand
	 */
//	protected void setSaved(boolean s) {
//		save = s;
//	}

	/**
	 * Verwijdert de gegeven transactie
	 * @param transaction de te verwijderen transactie
	 */
	private void deleteTransaction(Transaction transaction) {
		boolean found = false;
		for(Transaction trans : transacties) {
			if (found) {
				trans.lowerID();
			} else if (trans == transaction) {
				found = true;
			}
		}
		transacties.remove(transaction);
	}

	/**
	 * Voegt een transactie toe
	 * @param transaction de toe te voegen transactie
	 */
	private void addTransaction(Transaction transaction) {
		Calendar datum = transaction.getDate();
		int plaats = 0;
		if (transacties.size() == 0 || datum.compareTo(transacties.get(transacties.size() - 1).getDate()) >= 0) {
			transaction.setId(id);
			transacties.add(transaction);
		} else {
			boolean found = false;
			for(int i = 0; i < transacties.size(); i++) {
				Transaction transactie = transacties.get(i);
				Calendar date = transactie.getDate();
				if (found) {
					transactie.raiseID();
				} else if (date.compareTo(datum) > 0) {
					transactie.raiseID();
					plaats = i;
					found = true;
				}
			}
			if (!found) {
				// hier kom je nooit
				transaction.setId(id);
				transacties.add(transaction);
			} else {
				transaction.setId(plaats + 1);
				transacties.add(plaats, transaction);
			}
		}
	}

	/**
	 * Verwijdert de gegeven transactie
	 * @param transaction de te verwijderen transactie
	 */
	protected void unbook(Transaction transaction) {
		deleteTransaction(transaction);
		ArrayList<Booking> boekingen = transaction.getBookings();
		for(Booking boeking : boekingen) {
			Account account = boeking.getAccount();
			account.unbook(boeking);
		}
		id--;
//		accounting.setSavedXML(false);
//		accounting.setSavedHTML(false);
//		save = false;
	}

	/**
	 * Boek de gegeven transactie
	 * @param transaction de te boeken transactie
	 */
	protected void book(Transaction transaction) {
        transaction.setAbbreviation(abbreviation);
		addTransaction(transaction);
		ArrayList<Booking> boekingen = transaction.getBookings();
		for(Booking boeking : boekingen) {
			Account rek = boeking.getAccount();
			rek.book(boeking);
		}
		id++;
//		accounting.setSavedXML(false);
//		accounting.setSavedHTML(false);
//		save = false;
	}

	public JournalType getType() {
		return journalType;
	}

	public void setName(String newName) {
		name = newName;
	}

	public void setType(JournalType journalType) {
		this.journalType = journalType;
	}

	public String getName() {
		return name;
	}

	public void setAbbreviation(String newAbbreviation) {
		abbreviation = newAbbreviation;
	}

    public File getXslFile() {
        return xslFile;
    }

    public File getHtmlFile() {
        return htmlFile;
    }

    public File getXmlFile() {
        return xmlFile;
    }

    public void setXslFile(File xslFile) {
        this.xslFile = xslFile;
    }

    public void setHtmlFile(File htmlFile) {
        this.htmlFile = htmlFile;
    }

    public void setXmlFile(File xmlFile) {
        this.xmlFile = xmlFile;
    }
}