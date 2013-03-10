package be.dafke.Accounting.Objects.Accounting;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author David Danneels
 */
public class Accounting extends WriteableBusinessObject {
    private final AccountTypes accountTypes;
    private final Accounts accounts;
	private final Journals journals;
	private final Projects projects;
    private final JournalTypes journalTypes;
    private final Mortgages mortgages;
    private final CounterParties counterParties;
    private final Statements statements;
    private final Balances balances;
    private File xmlFolder, htmlFolder;
    private HashMap<String, WriteableBusinessCollection<WriteableBusinessObject>> collections;
    private ArrayList<String> keys;

    public Accounting() {
        // TODO use Accounts<Account> + modifiy Accounts file ... Accounts<T extends
        accountTypes = new AccountTypes();
        accounts = new Accounts();
        journalTypes = new JournalTypes();
        journalTypes.addDefaultType(accountTypes);
        journals = new Journals();
        balances = new Balances();
        mortgages = new Mortgages();
        counterParties = new CounterParties();
        statements = new Statements();
        projects = new Projects();
        balances.addDefaultBalances(this);

        collections = new HashMap<String, WriteableBusinessCollection<WriteableBusinessObject>>();
        // TODO unchecked assignment: use put(..., (WriteableBusinessCollection<WriteableBusinessObject>) accounts)
//        collections.put(accountTypes.getType(),(WriteableBusinessCollection)accountTypes);
        collections.put(accounts.getType(),(WriteableBusinessCollection)accounts);
        collections.put(journals.getType(),(WriteableBusinessCollection)journals);
        collections.put(balances.getType(),(WriteableBusinessCollection)balances);
        collections.put(mortgages.getType(),(WriteableBusinessCollection)mortgages);
        collections.put(statements.getType(),(WriteableBusinessCollection) statements);
        collections.put(counterParties.getType(),(WriteableBusinessCollection)counterParties);

        keys = new ArrayList<String>();
//        keys.add(accountTypes.getType());
        keys.add(accounts.getType());
        keys.add(mortgages.getType());
        keys.add(journals.getType());
        keys.add(balances.getType());
        keys.add(counterParties.getType());
        keys.add(statements.getType());
	}

    @Override
    public void setName(String name){
        super.setName(name);
        setXmlFolder();
    }

    public ArrayList<String> getKeys() {
        return keys;
    }

    public WriteableBusinessCollection<WriteableBusinessObject> getCollection(String key) {
        return collections.get(key);
    }

    // Collections
    //

    public AccountTypes getAccountTypes() {
        return accountTypes;
    }

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
    public Statements getStatements() {
        return statements;
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
//        statements.setHtmlFolder(htmlFolder);
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
        statements.setXmlFolder(xmlFolder);
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
//        statements.createXmlFolder();
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
//            statements.createHtmlFolder();
//            counterParties.createHtmlFolder();
        }
    }
}