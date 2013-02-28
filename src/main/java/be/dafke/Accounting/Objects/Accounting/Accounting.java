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
        balances = new Balances();
        mortgages = new Mortgages(this);
        counterParties = new CounterParties();
        movements = new Movements();
        projects = new Projects();
        journalTypes = new JournalTypes();
		createXMLFolders();
        balances.addDefaultBalances(this);
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
//        htmlFile = FileSystemView.getFileSystemView().getChild(htmlFolder, "Accounting.html");
        accounts.setHtmlFile(FileSystemView.getFileSystemView().getChild(htmlFolder, "Accounts.html"));
        journals.setHtmlFile(FileSystemView.getFileSystemView().getChild(htmlFolder, "Journals.html"));
        balances.setHtmlFile(FileSystemView.getFileSystemView().getChild(htmlFolder, "Balances.html"));
        mortgages.setHtmlFile(FileSystemView.getFileSystemView().getChild(htmlFolder, "Mortgages.html"));
        movements.setHtmlFile(FileSystemView.getFileSystemView().getChild(htmlFolder, "Movements.html"));
        counterParties.setHtmlFile(FileSystemView.getFileSystemView().getChild(htmlFolder, "CounterParties.html"));
    }

	private void createXMLFolders() {
		File home = new File(System.getProperty("user.home"));
		File accountingFolder = FileSystemView.getFileSystemView().getChild(home, "Accounting");
		xmlFolder = FileSystemView.getFileSystemView().getChild(accountingFolder, name);
        xslFolder = FileSystemView.getFileSystemView().getChild(accountingFolder, "xsl");

        accounts.setFolder("Accounts");
        accounts.setXmlFile(FileSystemView.getFileSystemView().getChild(xmlFolder, "Accounts.xml"));
        accounts.setXslFile(FileSystemView.getFileSystemView().getChild(xslFolder, "Accounts.xsl"));
        accounts.setDtdFile(FileSystemView.getFileSystemView().getChild(xslFolder, "Accounts.dtd"));

        journals.setFolder("Journals");
        journals.setXmlFile(FileSystemView.getFileSystemView().getChild(xmlFolder, "Journals.xml"));
        journals.setXslFile(FileSystemView.getFileSystemView().getChild(xslFolder, "Journals.xsl"));
        journals.setDtdFile(FileSystemView.getFileSystemView().getChild(xslFolder, "Journals.dtd"));

        balances.setFolder("Balances");
        balances.setXmlFile(FileSystemView.getFileSystemView().getChild(xmlFolder, "Balances.xml"));
        balances.setXslFile(FileSystemView.getFileSystemView().getChild(xslFolder, "Balances.xsl"));
        balances.setDtdFile(FileSystemView.getFileSystemView().getChild(xslFolder, "Balances.dtd"));

        mortgages.setFolder("Mortgages");
        mortgages.setXmlFile(FileSystemView.getFileSystemView().getChild(xmlFolder, "Mortgages.xml"));
        mortgages.setXslFile(FileSystemView.getFileSystemView().getChild(xslFolder, "Mortgages.xsl"));
        mortgages.setDtdFile(FileSystemView.getFileSystemView().getChild(xslFolder, "Mortgages.dtd"));

        movements.setFolder("Movements");
        movements.setXmlFile(FileSystemView.getFileSystemView().getChild(xmlFolder, "Movements.xml"));
        movements.setXslFile(FileSystemView.getFileSystemView().getChild(xslFolder, "Movements.xsl"));
        movements.setDtdFile(FileSystemView.getFileSystemView().getChild(xslFolder, "Movements.dtd"));

        counterParties.setFolder("CounterParties");
        counterParties.setXmlFile(FileSystemView.getFileSystemView().getChild(xmlFolder, "CounterParties.xml"));
        counterParties.setXslFile(FileSystemView.getFileSystemView().getChild(xslFolder, "CounterParties.xsl"));
        counterParties.setDtdFile(FileSystemView.getFileSystemView().getChild(xslFolder, "CounterParties.dtd"));
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
