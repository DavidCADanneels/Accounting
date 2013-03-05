package be.dafke.Accounting.Objects.Accounting;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

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
    private HashMap<String, BusinessCollection<BusinessObject>> collections;
    private ArrayList<String> keys;

    public Accounting() {
        // TODO use Accounts<Account> + modifiy Accounts file ... Accounts<T extends
		accounts = new Accounts();
		journals = new Journals();
        balances = new Balances();
        mortgages = new Mortgages();
        counterParties = new CounterParties();
        movements = new Movements();
        projects = new Projects();
        journalTypes = new JournalTypes();
        balances.addDefaultBalances(this);

        collections = new HashMap<String, BusinessCollection<BusinessObject>>();
        // TODO unchecked assignment: use put(..., (BusinessCollection<BusinessObject>) accounts)
        collections.put(accounts.getType(),(BusinessCollection)accounts);
        collections.put(journals.getType(),(BusinessCollection)journals);
        collections.put(balances.getType(),(BusinessCollection)balances);
        collections.put(mortgages.getType(),(BusinessCollection)mortgages);
        collections.put(movements.getType(),(BusinessCollection)movements);
        collections.put(counterParties.getType(),(BusinessCollection)counterParties);

        keys = new ArrayList<String>();
        keys.add(accounts.getType());
        keys.add(journals.getType());
        keys.add(balances.getType());
        keys.add(mortgages.getType());
        keys.add(counterParties.getType());
        keys.add(movements.getType());
	}

    @Override
    public void setName(String name){
        super.setName(name);
        setXmlFolder();
    }

    public ArrayList<String> getKeys() {
        return keys;
    }

    public BusinessObject getCollection(String key) {
        return collections.get(key);
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