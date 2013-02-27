package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Objects.Coda.CounterParties;
import be.dafke.Accounting.Objects.Coda.CounterParty;
import be.dafke.Accounting.Objects.Coda.Movements;
import be.dafke.Accounting.Objects.Mortgage.Mortgage;
import be.dafke.Utils;

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
	private final Accounts accounts;
	private final Journals journals;
	private final Projects projects;
    private final JournalTypes journalTypes;
    private final HashMap<String, Mortgage> mortgageTables;
    private final CounterParties counterParties;
    private final Movements movements;

    private File balanceLocationXML;
    private File mortgageLocationXML;
    private File balanceLocationHTML;
    private File mortgageLocationHTML;

    private final String name;
    private File locationXSL, locationXML, locationHTML;
    private File xmlFile, htmlFile, xslFile;

    private Journal currentJournal;
    private Transaction currentTransaction = new Transaction();
    private Account currentAccount;

    public Accounting(String name) {
		this.name = name;
//		savedXML = true;
//		savedHTML = false;// TODO: true ?
		accounts = new Accounts(this);
		journals = new Journals(this);
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
			locationHTML = Utils.createSubFolderIfNotExist(parent, name);
		}
        htmlFile = FileSystemView.getFileSystemView().getChild(locationHTML, "Accounting.html");
        balanceLocationHTML = Utils.createSubFolderIfNotExist(locationHTML, "Balances");
        mortgageLocationHTML = Utils.createSubFolderIfNotExist(locationHTML, "Mortgages");
        accounts.setLocationHtml(Utils.createSubFolderIfNotExist(locationHTML, "Accounts"));
        journals.setLocationHtml(Utils.createSubFolderIfNotExist(locationHTML, "Journals"));
		movements.setLocationHtml(Utils.createSubFolderIfNotExist(locationHTML, "Movements"));
		counterParties.setLocationHtml(Utils.createSubFolderIfNotExist(locationHTML, "CounterParties"));
	}

	private void createXMLFolders() {
		File home = new File(System.getProperty("user.home"));
		File folder = Utils.createSubFolderIfNotExist(home, "Accounting");
		locationXML = Utils.createSubFolderIfNotExist(folder, name);
        balanceLocationXML = Utils.createSubFolderIfNotExist(locationXML, "Balances");
        mortgageLocationXML = Utils.createSubFolderIfNotExist(locationXML, "Mortgages");
        accounts.setLocationXml(Utils.createSubFolderIfNotExist(locationXML, "Accounts"));
        journals.setLocationXml(Utils.createSubFolderIfNotExist(locationXML, "Journals"));
		movements.setLocationXml(Utils.createSubFolderIfNotExist(locationXML, "Movements"));
		counterParties.setLocationXml(Utils.createSubFolderIfNotExist(locationXML, "CounterParties"));
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
		return accounts;
	}

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

	public void setLocationXsl(File location) {
		locationXSL = location;
		xslFile = FileSystemView.getFileSystemView().getChild(locationXSL, "Accounting.xsl");
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

    public void setCurrentAccount(Account currentAccount) {
        this.currentAccount = currentAccount;
    }

    public Account getCurrentAccount() {
        return currentAccount;
    }

//	public void setSaved(boolean save) {
//		setSavedHTML(save);
//		setSavedXML(save);
//	}
}
