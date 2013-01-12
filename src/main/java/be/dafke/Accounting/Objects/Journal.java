package be.dafke.Accounting.Objects;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.filechooser.FileSystemView;

import be.dafke.Utils;

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
	private Accounting accounting;
	private JournalType journalType;
	private final String DEFAULT_XSL = "../../xsl/Journal.xsl";
	private final String styleSheet = DEFAULT_XSL;

	/**
	 * Constructor
	 * @param name naam van het dagboek
	 * @param abbreviation afkorting van het dagboek Deze afkorting wordt gebruikt in de transacties horende bij dit
	 * dagboek
	 */
	public Journal(String name, String abbreviation) {
		this(name, abbreviation, new JournalType());
	}

	/**
	 * Constructor
	 * @param name naam van het dagboek
	 * @param abbreviation afkorting van het dagboek Deze afkorting wordt gebruikt in de transacties horende bij dit
	 * dagboek
	 * @param journalType the type of journal, e.g. purchase, sales, finance
	 */
	public Journal(String name, String abbreviation, JournalType journalType) {
//		save = true;
		transacties = new ArrayList<Transaction>();
		this.name = name;
		this.abbreviation = abbreviation;
		id = 1;
		this.journalType = journalType;
	}

	public void setAccounting(Accounting accounting) {
		this.accounting = accounting;
	}

	public Accounting getAccounting() {
		return accounting;
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
	 * Schrijft het dagboek uit naar een XML-bestand: <b><i>naam dagboek</i>.xml</b> Alle XML bestanden horende bij
	 * dagboeken worden in 1 map bewaard. Wanneer voor de eerste keer een dagboek naar XML wordt uitgeschreven, wordt
	 * aan de gebruiker gevraagd deze map aan te duiden.
	 */
	public void toXML() {
//		File folder = FileSystemView.getFileSystemView().createFileObject(accounting.getXMLLocation());
//		File subFolder = FileSystemView.getFileSystemView().getChild(folder, "Journals");
		File file = FileSystemView.getFileSystemView().getChild(accounting.getXMLJournalLocation(), name + ".xml");
		try {
			Writer writer = new FileWriter(file);
			writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n"
					+ "<?xml-stylesheet type=\"text/xsl\" href=\"" + styleSheet + "\"?>\r\n" + "<journal>\r\n"
					+ "  <name>" + name + "</name>\r\n");
			Iterator<Transaction> it = transacties.iterator();
			while (it.hasNext()) {
				Transaction trans = it.next();
				ArrayList<Booking> list = trans.getBookings();
				Booking booking = list.get(0);
				writer.write("  <action>\r\n" + "    <nr>" + abbreviation + booking.getId() + "</nr>\r\n"
						+ "    <date>" + Utils.toString(booking.getDate()) + "</date>\r\n" + "    <account>"
						+ booking.getAccount() + "</account>\r\n" + "    <" + (booking.isDebet() ? "debet" : "credit")
						+ ">" + booking.getAmount().toString() + "</" + (booking.isDebet() ? "debet" : "credit")
						+ ">\r\n" + "    <description>" + booking.getDescription()
						+ "</description>\r\n  </action>\r\n");
				for(int i = 1; i < list.size(); i++) {
					booking = list.get(i);
					writer.write("  <action>\r\n" + "    <account>" + booking.getAccount() + "</account>\r\n" + "    <"
							+ (booking.isDebet() ? "debet" : "credit") + ">" + booking.getAmount().toString() + "</"
							+ (booking.isDebet() ? "debet" : "credit") + ">\r\n" + "  </action>\r\n");
				}
			}
			writer.write("</journal>");
			writer.flush();
			writer.close();
//			save = true;
		} catch (FileNotFoundException ex) {
			Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void toHTML() {
		// Main.startFOP(args);
		System.out.println("Journal to HTML canceled since startFOP (not implemented yet) closes the JVM");
	}

	/**
	 * Verwijdert de gegeven transactie
	 * @param transaction de te verwijderen transactie
	 */
	private void deleteTransaction(Transaction transaction) {
		boolean found = false;
		for(int i = 0; i < transacties.size(); i++) {
			Transaction trans = transacties.get(i);
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
		for(int i = 0; i < boekingen.size(); i++) {
			Booking boeking = boekingen.get(i);
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
		addTransaction(transaction);
		ArrayList<Booking> boekingen = transaction.getBookings();
		for(int i = 0; i < boekingen.size(); i++) {
			Booking boeking = boekingen.get(i);
			Account rek = boeking.getAccount();
			boeking.setAbbreviation(abbreviation);
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
}