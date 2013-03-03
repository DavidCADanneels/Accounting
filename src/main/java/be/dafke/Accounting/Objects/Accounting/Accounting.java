package be.dafke.Accounting.Objects.Accounting;

import javax.swing.filechooser.FileSystemView;
import java.io.File;

/**
 * @author David Danneels
 */
public class Accounting {
	/**
	 * 
	 */
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
    private File xmlFile, htmlFile, xsl2XmlFile, xsl2HtmlFile;
    private File dtdFile;

    public Accounting(String name) {
		this.name = name;
//		savedXML = true;
//		savedHTML = false;// TODO: true ?
		accounts = new Accounts();
		journals = new Journals();
        balances = new Balances();
        mortgages = new Mortgages();
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

	// Folders
    //
    // Setters
	public void setHtmlFolder(File htmlFolder) {
		this.htmlFolder = htmlFolder;
	}
    //
	public void setXmlFolder(File xmlFolder) {
		this.xmlFolder = xmlFolder;
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
    public void setXsl2XmlFile(File xsl2XmlFile) {
        this.xsl2XmlFile = xsl2XmlFile;
    }
    //
    public void setXsl2HtmlFile(File xsl2HtmlFile) {
        this.xsl2HtmlFile = xsl2HtmlFile;
    }
    //
    public void setDtdFile(File dtdFile) {
        this.dtdFile = dtdFile;
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
    public File getXsl2XmlFile(){
        return xsl2XmlFile;
    }
    //
    public File getXsl2HtmlFile() {
        return xsl2HtmlFile;
    }
    //
    public File getDtdFile() {
        return dtdFile;
    }

    public void setDefaultHtmlFoldersAndFiles(boolean overwrite) {
        if(htmlFolder!=null && !htmlFolder.getPath().equals("null")){// && htmlFolder.exists()){
            htmlFolder.mkdirs();
            if(htmlFile == null || htmlFile.getPath().equals("null")){
                htmlFile = FileSystemView.getFileSystemView().getChild(htmlFolder, "Accounting.html");
//                htmlFile = FileSystemView.getFileSystemView().getChild(htmlFolder, name + ".html");
            }
            accounts.setDefaultHtmlFolderAndFiles(this,"Accounts", overwrite);
            journals.setDefaultHtmlFolderAndFiles(this,"Journals", overwrite);
            balances.setDefaultHtmlFolderAndFiles(this, "Balances", overwrite);
            mortgages.setDefaultHtmlFolderAndFiles(this, "Mortgages", overwrite);
            movements.setDefaultHtmlFolderAndFiles(this, "Movements", overwrite);
            counterParties.setDefaultHtmlFolderAndFiles(this,"CounterParties", overwrite);
        }
    }

    public void setDefaultXmlFoldersAndFiles(boolean overwrite){
        File home = new File(System.getProperty("user.home"));
        File accountingFolder = FileSystemView.getFileSystemView().getChild(home, "Accounting");

        // ACCOUNTING
        //
        if(overwrite || xmlFolder == null || xmlFolder.getPath().equals("null")){
            xmlFolder = FileSystemView.getFileSystemView().getChild(accountingFolder, name);
        }
        if(overwrite || xmlFile == null || xmlFile.getPath().equals("null")){
            xmlFile = FileSystemView.getFileSystemView().getChild(xmlFolder, "Accounting.xml");
        }
        if(overwrite || xslFolder == null || xslFolder.getPath().equals("null")){
            xslFolder = FileSystemView.getFileSystemView().getChild(accountingFolder, "xsl");
        }
        if(overwrite || xsl2XmlFile == null || xsl2XmlFile.getPath().equals("null")){
            xsl2XmlFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Accounting2xml.xsl");
        }
        if(overwrite || xsl2HtmlFile == null || xsl2HtmlFile.getPath().equals("null")){
            xsl2HtmlFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Accounting2html.xsl");
        }
        if(overwrite || dtdFile == null || dtdFile.getPath().equals("null")){
            dtdFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Accounting.dtd");
        }

        accounts.setDefaultXmlFolderAndFiles(this, "Accounts", overwrite);
        journals.setDefaultXmlFolderAndFiles(this, "Journals", overwrite);
        balances.setDefaultXmlFolderAndFiles(this, "Balances", overwrite);
        mortgages.setDefaultXmlFolderAndFiles(this, "Mortgages", overwrite);
        movements.setDefaultXmlFolderAndFiles(this, "Movements", overwrite);
        counterParties.setDefaultXmlFolderAndFiles(this, "CounterParties", overwrite);
    }
}