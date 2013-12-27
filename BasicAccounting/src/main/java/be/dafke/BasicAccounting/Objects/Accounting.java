package be.dafke.BasicAccounting.Objects;

import be.dafke.BasicAccounting.Dao.JournalsSAXParser;
import be.dafke.Coda.Objects.CounterParties;
import be.dafke.Coda.Objects.Statements;
import be.dafke.Mortgage.Dao.MortgagesSAXParser;
import be.dafke.Mortgage.Objects.Mortgage;
import be.dafke.Mortgage.Objects.Mortgages;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.ObjectModel.WriteableBusinessCollection;
import be.dafke.ObjectModel.WriteableBusinessObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author David Danneels
 */
public class Accounting extends WriteableBusinessCollection<WriteableBusinessCollection<WriteableBusinessObject>> {
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
    private ArrayList<String> keys;

    public Accounting(File xmlFolder) {
        super(new File(xmlFolder,"Accounting"));
        // TODO use Accounts<Account> + modifiy Accounts file ... Accounts<T extends

        accountTypes = new AccountTypes();

        accounts = new Accounts(new File(xmlFolder,"Accounts"));
        accounts.setBusinessTypeCollection(accountTypes);

        journalTypes = new JournalTypes();
        journalTypes.addDefaultType(accountTypes);

        journals = new Journals(new File(xmlFolder,"Journals"));
        journals.setBusinessTypeCollection(journalTypes);

        balances = new Balances(new File(xmlFolder,"Balances"));
        balances.setBusinessCollection(accounts);
        balances.setBusinessTypeCollection(accountTypes);

        mortgages = new Mortgages(new File(xmlFolder,"Mortgages"));
        mortgages.setBusinessTypeCollection(accountTypes);
        mortgages.setBusinessCollection(accounts);

        counterParties = new CounterParties(new File(xmlFolder,"CounterParties"));

        statements = new Statements(new File(xmlFolder,"Statements"));
        statements.setBusinessCollection(counterParties);

        projects = new Projects();

        accounts.setName(accounts.getBusinessObjectType());
        journals.setName(journals.getBusinessObjectType());
        balances.setName(balances.getBusinessObjectType());
        mortgages.setName(mortgages.getBusinessObjectType());
        counterParties.setName(counterParties.getBusinessObjectType());
        statements.setName(statements.getBusinessObjectType());

        try {
            addBusinessObject((WriteableBusinessCollection)accounts);
            addBusinessObject((WriteableBusinessCollection)journals);
            addBusinessObject((WriteableBusinessCollection)balances);
            addBusinessObject((WriteableBusinessCollection)mortgages);
            addBusinessObject((WriteableBusinessCollection)statements);
            addBusinessObject((WriteableBusinessCollection)counterParties);
        } catch (EmptyNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (DuplicateNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        keys = new ArrayList<String>();
//        keys.add(accountTypes.getBusinessObjectType());
        keys.add(accounts.getBusinessObjectType());
        keys.add(mortgages.getBusinessObjectType());
        keys.add(journals.getBusinessObjectType());
        keys.add(balances.getBusinessObjectType());
        keys.add(counterParties.getBusinessObjectType());
        keys.add(statements.getBusinessObjectType());
	}

    public String toString(){
        return getName();
    }

    @Override
    public WriteableBusinessCollection createNewChild(String name) {
        WriteableBusinessCollection<WriteableBusinessObject> collection = getBusinessObject(name);
//        WriteableBusinessCollection<WriteableBusinessObject> collection = collections.get(name);
        if(collection==null){
//            collection =
            System.err.println("Accounting does not have a collection with the name: " + name);
        }
        return collection;
    }

    @Override
    public void readCollection() {
        readCollection(keys, true);
        for(Mortgage mortgage : mortgages.getBusinessObjects()){
            MortgagesSAXParser.readMortgage(mortgage);
        }

        for(Journal journal : journals.getBusinessObjects()){
            JournalsSAXParser.readJournal(journal, accounts);
        }

    }

    @Override
    public void setName(String name){
        super.setName(name);
        setXmlFolder();
    }

    @Override
    public ArrayList<WriteableBusinessCollection<WriteableBusinessObject>> getBusinessObjects(){
        ArrayList<WriteableBusinessCollection<WriteableBusinessObject>> objects = new ArrayList<WriteableBusinessCollection<WriteableBusinessObject>>();
        for(String key:keys){
            objects.add(getBusinessObject(key));
        }
        return objects;
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
        if(htmlFolder!=null){
            setHtmlFile(new File(htmlFolder, "Accounting.html"));
    //        setHtmlFile(new File(htmlFolder, name + ".html"));
            accounts.setHtmlFolder(htmlFolder);
            journals.setHtmlFolder(htmlFolder);
            balances.setHtmlFolder(htmlFolder);
            mortgages.setHtmlFolder(htmlFolder);
    //        statements.setHtmlFolder(htmlFolder);
    //        counterParties.setHtmlFolder(htmlFolder);
        }
    }

    private void setXmlFolder(){
        xmlFolder = new File(System.getProperty("Accountings_xml"), getName());
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
    @Override
    public Set<String> getInitKeySet() {
        Set<String> keySet = new TreeSet<String>();
        keySet.add(NAME);
        keySet.add(XML);
        keySet.add(HTML);
        return keySet;
    }

    @Override
    public TreeMap<String,String> getInitProperties() {
        TreeMap<String,String> properties = new TreeMap<String, String>();
        properties.put(NAME, getName());
        if(getXmlFile()!=null){
            properties.put(XML, getXmlFile().getPath());
        }
        if(getHtmlFile()!=null){
            properties.put(HTML, getHtmlFile().getPath());
        }
        return properties;
    }

    @Override
    public void setInitProperties(TreeMap<String, String> properties) {
        setName(properties.get(NAME));
        String xmlPath = properties.get(XML);
        String htmlPath = properties.get(HTML);
        if(xmlPath!=null){
            setXmlFile(new File(xmlPath));
        }
        if(htmlPath!=null){
            setHtmlFile(new File(htmlPath));
        }
    }
}