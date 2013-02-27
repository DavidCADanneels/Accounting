package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Objects.Coda.CounterParties;
import be.dafke.Accounting.Objects.Coda.Movements;
import be.dafke.Accounting.Objects.Mortgage.Mortgages;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.Serializable;

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
    private final Mortgages mortgages;
    private final CounterParties counterParties;
    private final Movements movements;
    private final Balances balances;

    private final String name;
    private File xslFolder, xmlFolder, htmlFolder;
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
        mortgages = new Mortgages();
		counterParties = new CounterParties();
		movements = new Movements();
        balances = new Balances();
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
			htmlFolder = chooser.getSelectedFile();
		} else {
			File parent = FileSystemView.getFileSystemView().getHomeDirectory();
			htmlFolder = FileSystemView.getFileSystemView().getChild(parent, name);
		}
        htmlFile = FileSystemView.getFileSystemView().getChild(htmlFolder, "Accounting.html");
        balances.setHtmlFolder(FileSystemView.getFileSystemView().getChild(htmlFolder, "Balances"));
        mortgages.setHtmlFolder(FileSystemView.getFileSystemView().getChild(htmlFolder, "Mortgages"));
        accounts.setHtmlFolder(FileSystemView.getFileSystemView().getChild(htmlFolder, "Accounts"));
        journals.setHtmlFolder(FileSystemView.getFileSystemView().getChild(htmlFolder, "Journals"));
		movements.setHtmlFolder(FileSystemView.getFileSystemView().getChild(htmlFolder, "Movements"));
		counterParties.setHtmlFolder(FileSystemView.getFileSystemView().getChild(htmlFolder, "CounterParties"));
	}

	private void createXMLFolders() {
		File home = new File(System.getProperty("user.home"));
		File folder = FileSystemView.getFileSystemView().getChild(home, "Accounting");
		xmlFolder = FileSystemView.getFileSystemView().getChild(folder, name);
        balances.setXmlFolder(FileSystemView.getFileSystemView().getChild(xmlFolder, "Balances"));
        mortgages.setXmlFolder(FileSystemView.getFileSystemView().getChild(xmlFolder, "Mortgages"));
        accounts.setXmlFolder(FileSystemView.getFileSystemView().getChild(xmlFolder, "Accounts"));
        journals.setXmlFolder(FileSystemView.getFileSystemView().getChild(xmlFolder, "Journals"));
		movements.setXmlFolder(FileSystemView.getFileSystemView().getChild(xmlFolder, "Movements"));
		counterParties.setXmlFolder(FileSystemView.getFileSystemView().getChild(xmlFolder, "CounterParties"));
	}

    public Mortgages getMortgages(){
        return mortgages;
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

    public Accounts getAccounts() {
        return accounts;
    }

    public JournalTypes getJournalTypes() {
        return journalTypes;
    }

    public Balances getBalances(){
        return balances;
    }

	public Journals getJournals() {
		return journals;
	}

	public Projects getProjects() {
		return projects;
	}

    // Current Objects
	public Journal getCurrentJournal() {
		return currentJournal;
	}

	public void setCurrentJournal(Journal journal) {
		currentJournal = journal;
	}

    public void setCurrentAccount(Account currentAccount) {
        this.currentAccount = currentAccount;
    }

    public Account getCurrentAccount() {
        return currentAccount;
    }

	// Folders
	public void setHtmlFolder(File htmlFolder) {
		this.htmlFolder = htmlFolder;
		this.htmlFolder.mkdir();
		htmlFile = FileSystemView.getFileSystemView().getChild(this.htmlFolder, "Accounting.html");
	}

	public void setXmlFolder(File xmlFolder) {
		this.xmlFolder = xmlFolder;
		this.xmlFolder.mkdir();
		xmlFile = FileSystemView.getFileSystemView().getChild(this.xmlFolder, "Accounting.xml");
	}

    public void setXslFolder(File xslFolder) {
        this.xslFolder = xslFolder;
        xslFile = FileSystemView.getFileSystemView().getChild(this.xslFolder, "Accounting.xsl");
    }

    public File getHtmlFolder() {
        return htmlFolder;
    }

    public File getXmlFolder() {
        return xmlFolder;
    }

    public File getXslFolder() {
        return xslFolder;
    }

    // Files
    public File getHTMLFile(){
        return htmlFile;
    }

    public File getXMLFile(){
        return xmlFile;
    }

    public File getXSLFile(){
        return xslFile;
    }

//	public void setSaved(boolean save) {
//		setSavedHTML(save);
//		setSavedXML(save);
//	}
}
