package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Objects.Coda.CounterParties;
import be.dafke.Accounting.Objects.Coda.CounterParty;
import be.dafke.Accounting.Objects.Coda.Movements;
import be.dafke.Accounting.Objects.Mortgage.Mortgage;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author David Danneels
 */
public class Accounting implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// private boolean savedXML;
//	private boolean savedHTML;
	private final Accounts rekeningen;
	private final Journals journals;
	private Journal currentJournal;
	private final Projects projects;
	private final JournalTypes journalTypes;
	private final HashMap<String, Mortgage> mortgageTables;
	private final CounterParties counterParties;
	private final Movements movements;
	private final String name;
	private File accountLocationXML, journalLocationXML, balanceLocationXML;
	private File mortgageLocationXML, movementLocationXML, counterpartyLocationXML;
	private File accountLocationHTML, journalLocationHTML, balanceLocationHTML;
	private File mortgageLocationHTML, movementLocationHTML, counterpartyLocationHTML;
	private File locationXSL, locationXML, locationHTML;
	private File xmlFile, htmlFile, xslFile;

    private Transaction currentTransaction = new Transaction();

	public Accounting(String name) {
		this.name = name;
//		savedXML = true;
//		savedHTML = false;// TODO: true ?
		rekeningen = new Accounts();
		journals = new Journals();
		projects = new Projects();
		journalTypes = new JournalTypes();
		mortgageTables = new HashMap<String, Mortgage>();
		counterParties = new CounterParties();
		movements = new Movements();
		createXMLFolders();
//		createHTMLFolders();
	}

    public Transaction getCurrentTransaction(){
        return currentTransaction;
    }

    public void setCurrentTransaction(Transaction transaction){
        currentTransaction = transaction;
    }

	public CounterParties getCounterParties() {
		return counterParties;
	}

	public Movements getMovements() {
		return movements;
	}

	public void createHTMLFolders() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			locationHTML = chooser.getSelectedFile();
		} else {
			File parent = FileSystemView.getFileSystemView().getHomeDirectory();
			locationHTML = createSubFolderIfNotExist(parent, name);
		}
        htmlFile = FileSystemView.getFileSystemView().getChild(locationHTML,"Accounting.html");
		accountLocationHTML = createSubFolderIfNotExist(locationHTML, "Accounts");
		journalLocationHTML = createSubFolderIfNotExist(locationHTML, "Journals");
		balanceLocationHTML = createSubFolderIfNotExist(locationHTML, "Balances");
		mortgageLocationHTML = createSubFolderIfNotExist(locationHTML, "Mortgages");
		mortgageLocationHTML = createSubFolderIfNotExist(locationHTML, "Movements");
		counterpartyLocationHTML = createSubFolderIfNotExist(locationHTML, "CounterParties");
	}

	private void createXMLFolders() {
		File home = new File(System.getProperty("user.home"));
		File folder = createSubFolderIfNotExist(home, "Accounting");
		locationXML = createSubFolderIfNotExist(folder, name);
		accountLocationXML = createSubFolderIfNotExist(locationXML, "Accounts");
		journalLocationXML = createSubFolderIfNotExist(locationXML, "Journals");
		balanceLocationXML = createSubFolderIfNotExist(locationXML, "Balances");
		mortgageLocationXML = createSubFolderIfNotExist(locationXML, "Mortgages");
		mortgageLocationXML = createSubFolderIfNotExist(locationXML, "Movements");
		counterpartyLocationXML = createSubFolderIfNotExist(locationXML, "CounterParties");
		//
//		locationXSL = createSubFolderIfNotExist(folder, "xsl");
//		File defaultFolder = new File("xsl");
////		File folder = FileSystemView.getFileSystemView().get
//		File[] files = FileSystemView.getFileSystemView().getFiles(defaultFolder, true);
//		// File[] files = FileSystemView.getFileSystemView().getFiles(locationXSL,true);
//		for(File file : files) {
//			File newFile = FileSystemView.getFileSystemView().createFileObject(locationXSL, file.getName());
//			try {
//				newFile.createNewFile();
////				newFile.
//			} catch (IOException io) {
//
//			}
//			newFile.renameTo(file);
//			FileSystemView.getFileSystemView().createFileObject(file, file.getName());
//		}
	}

	private File createSubFolderIfNotExist(File folder, String folderName) {
		File subFolder = FileSystemView.getFileSystemView().getChild(folder, folderName);
		if (!subFolder.exists()) {
			File newFolder = FileSystemView.getFileSystemView().createFileObject(folder, folderName);
			newFolder.mkdir();
		}
		return subFolder;
	}

//	public static Accounting fromXML(){
//		
//	}
    public void addCounterparty(CounterParty counterParty){
        counterParties.addCounterParty(counterParty);
    }

	public void addMortgageTable(String mortgageName, Mortgage table) {
		mortgageTables.put(mortgageName, table);
	}

	public boolean containsMortgageName(String mortgageName) {
		return mortgageTables.containsKey(mortgageName);
	}

	public Mortgage getMortgage(String mortgageName) {
		return mortgageTables.get(mortgageName);
	}

	public ArrayList<Mortgage> getMortgagesTables() {
		return new ArrayList<Mortgage>(mortgageTables.values());
	}

	public void removeMortgageTable(Mortgage selectedMortgage) {
		mortgageTables.remove(selectedMortgage.toString());
	}

//	private void setSavedXML(boolean saved) {
//		savedXML = saved;
//	}

//	public boolean isSavedXML() {
//		return savedXML;
//	}

//	private void setSavedHTML(boolean saved) {
//		savedHTML = saved;
//	}

//	public boolean isSaved() {
//		return savedHTML;
//	}

	@Override
	public String toString() {
		return name;
	}

//	public Journal addNewJournal() {
//		// TODO: implement a gui to create journals (using journalType) see NewAccountGUI
//		String name = JOptionPane.showInputDialog(java.util.ResourceBundle.getBundle("Accounting").getString(
//				"GEEF_NAAM_DAGBOEK"));
//		while (name == null || name.equals(""))
//			name = JOptionPane.showInputDialog(java.util.ResourceBundle.getBundle("Accounting").getString(
//					"GEEF_NAAM_DAGBOEK"));
//		String abbr = JOptionPane.showInputDialog(java.util.ResourceBundle.getBundle("Accounting").getString(
//				"GEEF_AFKORTING_DAGBOEK"));
//		while (abbr == null || abbr.equals(""))
//			abbr = JOptionPane.showInputDialog(java.util.ResourceBundle.getBundle("Accounting").getString(
//					"GEEF_AFKORTING_DAGBOEK"));
//		Journal journal = new Journal(name, abbr);
//		journal.setAccounting(this);
//		journals.put(journal.toString(),journal);
//		return journal;
//	}

	public Journals getJournals() {
		return journals;
	}

	public Projects getProjects() {
		return projects;
	}

	public Journal getCurrentJournal() {
		return currentJournal;
	}

	public void setCurrentJournal(Journal journal) {
		currentJournal = journal;
	}

	public Accounts getAccounts() {
		return rekeningen;
	}

//	public static Accounting newInstance(boolean transfer) {
//		// closeInstance();
//		if (transfer) {
//			// OUDE REKENINGEN OVERNEMEN
//			// later implementeren
//			JOptionPane.showMessageDialog(
//					null,
//					java.util.ResourceBundle.getBundle("Accounting").getString(
//							"NOG_NIET_GEIMPLEMENTEERD"));
//			accounting = new Accounting("Transferred");
//		} else {
//			// VOLLEDIG NIEUW
//			String name = JOptionPane.showInputDialog(null, "Enter a name");
//			while (Accountings.contains(name)) {
//				name = JOptionPane.showInputDialog(null, "Name already used, enter another name");
//			}
//			accounting = new Accounting(name);
//		}
//		return accounting;
//	}

//	public Accounting saveAs() {
//		JFileChooser kiezer = new JFileChooser();
//		kiezer.setDialogTitle(java.util.ResourceBundle.getBundle("Accounting").getString("WAAR_OPSLAAN"));
//		int result = kiezer.showSaveDialog(null);
//		while (result != JFileChooser.APPROVE_OPTION)
//			result = kiezer.showSaveDialog(null);
//		File bestand = kiezer.getSelectedFile();
//		location = bestand.getPath();
//		saveToXML();
//		saveToHTML();
	// serialiseer();
//		return this;
//	}

	public JournalTypes getJournalTypes() {
		return journalTypes;
	}

	// Default Location
	public void setLocationHtml(File location) {
		locationHTML = location;
		locationHTML.mkdir();
		htmlFile = FileSystemView.getFileSystemView().getChild(locationHTML, "Accounting.html");
	}

	public void setLocationXml(File location) {
		locationXML = location;
		locationXML.mkdir();
		xmlFile = FileSystemView.getFileSystemView().getChild(locationXML, "Accounting.xml");
	}

	public File getLocationHtml() {
		return locationHTML;
	}

    public File getHTMLFile(){
        return htmlFile;
    }

    public File getXMLFile(){
        return xmlFile;
    }

    public File getXSLFile(){
        return xslFile;
    }

	public File getLocationXml() {
		return locationXML;
	}

	public File getLocationXSL() {
		return locationXSL;
	}

	// TODO call this function on load
	public void setLocationXSL(File location) {
		locationXSL = location;
		xslFile = FileSystemView.getFileSystemView().getChild(locationXSL, "Accounting.xsl");
	}

	// Account Locations
	public void setAccountLocationHtml(File location) {
		accountLocationHTML = location;
		accountLocationHTML.mkdir();
	}

	public void setAccountLocationXml(File location) {
		accountLocationXML = location;
		accountLocationXML.mkdir();
	}

	public File getAccountLocationHtml() {
		return accountLocationHTML;
	}

	public File getAccountLocationXml() {
		return accountLocationXML;
	}

	// Journal Location
	public void setJournalLocationHtml(File location) {
		journalLocationHTML = location;
		journalLocationHTML.mkdir();
	}

	public void setJournalLocationXml(File location) {
		journalLocationXML = location;
		journalLocationXML.mkdir();
	}

	public File getJournalLocationHtml() {
		return journalLocationHTML;
	}

	public File getJournalLocationXml() {
		return journalLocationXML;
	}

	// Balance Location
	public void setBalanceLocationHtml(File location) {
		balanceLocationHTML = location;
		balanceLocationHTML.mkdir();
	}

	public void setBalanceLocationXml(File location) {
		balanceLocationXML = location;
		balanceLocationXML.mkdir();
	}

	public File getBalanceLocationHtml() {
		return balanceLocationHTML;
	}

	public File getBalanceLocationXml() {
		return balanceLocationXML;
	}

	// Mortgage Location
	public void setMortgageLocationHtml(File location) {
		mortgageLocationHTML = location;
		mortgageLocationHTML.mkdir();
	}

	public void setMortgageLocationXml(File location) {
		mortgageLocationXML = location;
		mortgageLocationXML.mkdir();
	}

	public File getMortgageLocationHtml() {
		return mortgageLocationHTML;
	}

	public File getMortgageLocationXml() {
		return mortgageLocationXML;
	}

	// Movement Location
	public void setMovementLocationHtml(File location) {
		movementLocationHTML = location;
		movementLocationHTML.mkdir();
	}

	public void setMovementLocationXml(File location) {
		movementLocationXML = location;
		movementLocationXML.mkdir();
	}

	public File getMovementLocationHtml() {
		return movementLocationHTML;
	}

	public File getMovementLocationXml() {
		return movementLocationXML;
	}

	// Counterparty location
	public void setCounterpartyLocationHtml(File location) {
		counterpartyLocationHTML = location;
		counterpartyLocationHTML.mkdir();
	}

	public void setCounterpartyLocationXml(File location) {
		counterpartyLocationXML = location;
		counterpartyLocationXML.mkdir();
	}

	public File getCounterPartyLocationHtml() {
		return counterpartyLocationHTML;
	}

	public File getCounterPartyLocationXml() {
		return counterpartyLocationHTML;
	}

//	public void setSaved(boolean save) {
//		setSavedHTML(save);
//		setSavedXML(save);
//	}
}
