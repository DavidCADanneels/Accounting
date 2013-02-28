package be.dafke.Accounting.Objects.Accounting;

import be.dafke.Accounting.Objects.Coda.CounterParties;
import be.dafke.Accounting.Objects.Coda.Movements;
import be.dafke.Accounting.Objects.Mortgage.Mortgages;

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
    private File dtdFile;

    public Accounting(String name) {
		this.name = name;
//		savedXML = true;
//		savedHTML = false;// TODO: true ?
		accounts = new Accounts(this);
		journals = new Journals(this);
        balances = new Balances();
        mortgages = new Mortgages(this);
        counterParties = new CounterParties();
        movements = new Movements();
        projects = new Projects();
        journalTypes = new JournalTypes();
        balances.addDefaultBalances(this);
	}

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

//	private void setSavedXML(boolean saved) {
//		savedXML = saved;
//	}
//
//	public boolean isSavedXML() {
//		return savedXML;
//	}
//
//	private void setSavedHTML(boolean saved) {
//		savedHTML = saved;
//	}
//
//	public boolean isSaved() {
//		return savedHTML;
//	}
//
//	public void setSaved(boolean save) {
//		setSavedHTML(save);
//		setSavedXML(save);
//	}


    // Collections
    //
    public Accounts getAccounts() {
        return accounts;
    }
    //
    public Projects getProjects() {
        return projects;
    }
    //
    public Journals getJournals() {
        return journals;
    }
    //
    public JournalTypes getJournalTypes() {
        return journalTypes;
    }
    //
    public Balances getBalances(){
        return balances;
    }
    //
    public Mortgages getMortgages(){
        return mortgages;
    }
    //
    public CounterParties getCounterParties() {
        return counterParties;
    }
    //
    public Movements getMovements() {
        return movements;
    }

    // Current Objects
    //
    // Transaction
    public Transaction getCurrentTransaction(){
        return currentTransaction;
    }

    public void setCurrentTransaction(Transaction transaction){
        currentTransaction = transaction;
    }
    //
    // Journal
	public Journal getCurrentJournal() {
		return currentJournal;
	}
    //
	public void setCurrentJournal(Journal journal) {
		currentJournal = journal;
	}
    //
    // Account
    public void setCurrentAccount(Account currentAccount) {
        this.currentAccount = currentAccount;
    }
    //
    public Account getCurrentAccount() {
        return currentAccount;
    }

	// Folders
    //
    // Setters
	public void setHtmlFolder(File htmlFolder) {
		this.htmlFolder = htmlFolder;
		this.htmlFolder.mkdir();
	}
    //
	public void setXmlFolder(File xmlFolder) {
		this.xmlFolder = xmlFolder;
		this.xmlFolder.mkdir();
	}
    //
    public void setXslFolder(File xslFolder) {
        this.xslFolder = xslFolder;
    }
    //
    // Getters
    public File getHtmlFolder() {
        return htmlFolder;
    }
    //
    public File getXmlFolder() {
        return xmlFolder;
    }
    //
    public File getXslFolder() {
        return xslFolder;
    }

    // Files
    //
    // Setters
    public void setHtmlFile(File htmlFile) {
        this.htmlFile = htmlFile;
    }
    //
    public void setXmlFile(File xmlFile) {
        this.xmlFile = xmlFile;
    }
    //
    public void setXslFile(File xslFile) {
        this.xslFile = xslFile;
    }
    //
    // Getters
    public File getHtmlFile(){
        return htmlFile;
    }
    //
    public File getXmlFile(){
        return xmlFile;
    }
    //
    public File getXslFile(){
        return xslFile;
    }

    public File getDtdFile() {
        return dtdFile;
    }

    public void setDtdFile(File dtdFile) {
        this.dtdFile = dtdFile;
    }
}