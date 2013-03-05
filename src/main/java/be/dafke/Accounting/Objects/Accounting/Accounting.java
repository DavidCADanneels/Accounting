package be.dafke.Accounting.Objects.Accounting;

import java.io.File;

/**
 * @author David Danneels
 */
public class Accounting extends BusinessObject{
	private final Accounts accounts;
	private final Journals journals;
	private final Projects projects;
    private final JournalTypes journalTypes;
    private final Mortgages mortgages;
    private final CounterParties counterParties;
    private final Movements movements;
    private final Balances balances;
    private File xmlFolder, htmlFolder;

    public Accounting(String name) {
        super(name, "Accounting");
		accounts = new Accounts();
		journals = new Journals();
        balances = new Balances();
        mortgages = new Mortgages();
        counterParties = new CounterParties();
        movements = new Movements();
        projects = new Projects();
        journalTypes = new JournalTypes();
        balances.addDefaultBalances(this);

        setXmlFolder();
	}

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
    // Getters
    public File getHtmlFolder() {
        return htmlFolder;
    }
    //
    // Setters
    //
    public void setHtmlFolder(File htmlFolder) {
        this.htmlFolder = htmlFolder;
        setHtmlFile(new File(htmlFolder, "Accounting.html"));
//        setHtmlFile(new File(htmlFolder, name + ".html"));
        accounts.setHtmlFolder(htmlFolder);
        journals.setHtmlFolder(htmlFolder);
        balances.setHtmlFolder(htmlFolder);
        mortgages.setHtmlFolder(htmlFolder);
//        movements.setHtmlFolder(htmlFolder);
//        counterParties.setHtmlFolder(htmlFolder);
    }

    private void setXmlFolder(){
        File homeFolder = new File(System.getProperty("Accountings_xml"));
        xmlFolder = new File(homeFolder, getName());
        setXmlFile(new File(xmlFolder, "Accounting.xml"));

        accounts.setXmlFolder(xmlFolder);
        journals.setXmlFolder(xmlFolder);
        balances.setXmlFolder(xmlFolder);
        mortgages.setXmlFolder(xmlFolder);
        movements.setXmlFolder(xmlFolder);
        counterParties.setXmlFolder(xmlFolder);
    }

    public void createXmlFolders() {
        if(xmlFolder.mkdirs()){
            System.out.println(xmlFolder.getPath() + " has been created");
        }
        accounts.createXmlFolder();
        journals.createXmlFolder();
        balances.createXmlFolder();
        mortgages.createXmlFolder();
//        movements.createXmlFolder();
//        counterParties.createXmlFolder();
    }

    public void createHtmlFolders(){
        if(htmlFolder!=null){
            if(htmlFolder.mkdirs()){
                System.out.println(htmlFolder.getPath() + " has been created");
            }
            accounts.createHtmlFolder();
            journals.createHtmlFolder();
            balances.createHtmlFolder();
            mortgages.createHtmlFolder();
//            movements.createHtmlFolder();
//            counterParties.createHtmlFolder();
        }
    }
}