package be.dafke.Accounting.Dao.XML;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;
import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Accountings;
import be.dafke.Accounting.Objects.Accounting.Balance;
import be.dafke.Accounting.Objects.Accounting.Journal;
import be.dafke.Accounting.Objects.Accounting.Mortgage;
import be.dafke.Accounting.Objects.WriteableBusinessCollection;
import be.dafke.Accounting.Objects.WriteableBusinessObject;
import be.dafke.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

/**
 * User: Dafke
 * Date: 20/01/13
 * Time: 10:22
 */
public class AccountingsSAXParser {

    public static Accountings readAccountings() {
        Accountings accountings = new Accountings();
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(true);
            DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(accountings.getXmlFile().getAbsolutePath());
            doc.getDocumentElement().normalize();

            NodeList accountingNodes = doc.getElementsByTagName("Accounting");
            for(int i=0;i<accountingNodes.getLength();i++){
                Element element = (Element)accountingNodes.item(i);
                Accounting accounting = new Accounting();
                String name = Utils.getValue(element, "name");
                accounting.setName(name);
                accounting.setHtmlFolder(Utils.getFile(element, "htmlFolder"));
                accounting.setXmlFile(Utils.getFile(element, "xml"));
                accounting.setHtmlFile(Utils.getFile(element, "html"));
                accounting.setXsl2XmlFile(Utils.getFile(element, "xsl2xml"));
                accounting.setXsl2HtmlFile(Utils.getFile(element, "xsl2html"));
                try{
                    accountings.addBusinessObject(accounting);
                } catch (DuplicateNameException e) {
                    System.err.println("There is already an accounting with the name \""+name+"\"");
                } catch (EmptyNameException e) {
                    System.err.println("The name cannot be empty.");
                }
            }
            String currentAccountName = Utils.getValue(doc, "CurrentAccounting");
            if(currentAccountName!=null){
                accountings.setCurrentAccounting(currentAccountName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(Accounting accounting : accountings.getBusinessObjects()) {
            readAccounting(accounting);
        }
        return accountings;
    }

    private static void readAccounting(Accounting accounting){

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(true);
            DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(accounting.getXmlFile().getAbsolutePath());
            doc.getDocumentElement().normalize();

            for(String key:accounting.getKeys()) {
                WriteableBusinessCollection<WriteableBusinessObject> writeableBusinessObject = accounting.getCollection(key);
                Element element = (Element)doc.getElementsByTagName(key).item(0);
                String name = Utils.getValue(element, "name");
                System.out.println("parsing: " + name);
                File xmlFile = Utils.getFile(element, "xml");
                File htmlFile = Utils.getFile(element, "html");
                writeableBusinessObject.setXmlFile(xmlFile);
                writeableBusinessObject.setHtmlFile(htmlFile);
            }

            CollectionSAXParser.readCollection(accounting.getCollection("Accounts"),
                    "be.dafke.Accounting.Objects.Accounting.Account");
            MortgagesSAXParser.readMortgages(accounting.getMortgages(), accounting.getAccounts(), accounting.getAccountTypes());
            CollectionSAXParser.readCollection(accounting.getCollection("Journals"),
                    "be.dafke.Accounting.Objects.Accounting.Journal");
            for(Journal journal : accounting.getJournals().getBusinessObjects()){
                JournalsSAXParser.readJournal(journal, accounting.getAccounts());
            }
            CounterPartiesSAXParser.readCounterparties(accounting.getCounterParties(), accounting.getAccounts());
            StatementsSAXParser.readStatements(accounting.getStatements(), accounting.getCounterParties());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeAccountings(Accountings accountings) {
        accountings.createDefaultValuesIfNull();
//        CollectionSAXParser.writeCollection(accountings);
        toXml(accountings);
        toHtml(accountings);
    }

    private static void toXml(Accountings accountings){
        try {
            Writer writer = new FileWriter(accountings.getXmlFile());

            writer.write(accountings.getXmlHeader());

            writer.write("<Accountings>\r\n");
            for(Accounting acc : accountings.getBusinessObjects()) {
                writer.write("  <Accounting>\r\n");
                writer.write("    <name>" + acc.toString() + "</name>\r\n");
                if(acc.getHtmlFolder()!=null){
                    writer.write("    <htmlFolder>" + acc.getHtmlFolder() + "</htmlFolder>\r\n");
                }
                writer.write("    <xml>" + acc.getXmlFile() + "</xml>\r\n");
                if(acc.getHtmlFile()!=null){
                    writer.write("    <html>" + acc.getHtmlFile() + "</html>\r\n");
                }
                writer.write("    <xsl2xml>" + acc.getXsl2XmlFile() + "</xsl2xml>\r\n");
                writer.write("    <xsl2html>" + acc.getXsl2HtmlFile() + "</xsl2html>\r\n");
                writer.write("  </Accounting>\r\n");
            }
            if(accountings.getCurrentAccounting()!=null){
                writer.write("  <CurrentAccounting>" + accountings.getCurrentAccounting().getName() + "</CurrentAccounting>\r\n");
            }
            writer.write("</Accountings>");
            writer.flush();
            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        for(Accounting accounting : accountings.getBusinessObjects()) {
            writeAccounting(accounting);
        }
    }

    private static void writeAccounting(Accounting accounting){
        writeAccountingFile(accounting);

        System.out.println("Accounts.TOXML(" + accounting.toString() + ")");
        CollectionSAXParser.writeCollection(accounting.getCollection("Accounts"));
        for(Account account : accounting.getAccounts().getBusinessObjects()){
            AccountsSAXParser.writeAccount(account);
        }

        System.out.println("Balances.TOXML(" + accounting.toString() + ")");
        CollectionSAXParser.writeCollection(accounting.getCollection("Balances"));
        for(Balance balance : accounting.getBalances().getBusinessObjects()){
            BalancesSAXParser.writeBalance(balance);
        }

        System.out.println("Journals.TOXML(" + accounting.toString() + ")");
        CollectionSAXParser.writeCollection(accounting.getCollection("Journals"));
        for(Journal journal : accounting.getJournals().getBusinessObjects()){
            JournalsSAXParser.writeJournal(journal);
        }

        System.out.println("Mortgages.TOXML(" + accounting.toString() + ")");
//        MortgagesSAXParser.writeMortgages(accounting.getMortgages());
        CollectionSAXParser.writeCollection(accounting.getCollection("Mortgages"));
        for(Mortgage mortgage:accounting.getMortgages().getBusinessObjects()){
            MortgagesSAXParser.writeMortgage(mortgage);
        }

        System.out.println("Counterparties.TOXML(" + accounting.toString() + ")");
//        CounterPartiesSAXParser.writeCounterparties(accounting.getCounterParties());
        CollectionSAXParser.writeCollection(accounting.getCollection("CounterParties"));

        System.out.println("Statements.TOXML(" + accounting.toString() + ")");
        StatementsSAXParser.writeStatements(accounting.getStatements());
//        CollectionSAXParser.writeCollection(accounting.getCollection("Statements"));
    }

    private static void writeAccountingFile(Accounting accounting) {
        try {
            Writer writer = new FileWriter(accounting.getXmlFile());
            writer.write(accounting.getXmlHeader());
            writer.write("<Accounting>\r\n");
            writer.write("  <name>" + accounting.getName() + "</name>\r\n");
            for(String key:accounting.getKeys()) {
                WriteableBusinessCollection<WriteableBusinessObject> writeableBusinessCollection = accounting.getCollection(key);
                System.out.println("writing: " + key);
                writer.write("  <" + key + ">\r\n");
                writer.write("    <name>" + key + "</name>\r\n");
                writer.write("    <xml>" + writeableBusinessCollection.getXmlFile() + "</xml>\r\n");
                if(writeableBusinessCollection.getHtmlFile()!=null){
                    writer.write("    <html>" + writeableBusinessCollection.getHtmlFile() + "</html>\r\n");
                }
                writer.write("  </" + key + ">\r\n");
            }
            writer.write("</Accounting>\r\n");
            writer.flush();
            writer.close();
//			setSaved(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void toHtml(Accountings accountings){
        accountings.xmlToHtml();
        for(Accounting accounting:accountings.getBusinessObjects()){
            if(accounting.getHtmlFolder()!=null && !accounting.getHtmlFolder().getPath().equals("null")){
                toHtml(accounting);
            }
        }
    }

    private static void toHtml(Accounting accounting){
        if(accounting.getHtmlFolder() != null){

            accounting.xmlToHtml();

            for(String key : accounting.getKeys()){
                WriteableBusinessCollection<WriteableBusinessObject> collection = accounting.getCollection(key);
                collection.xmlToHtml();
                if(collection.getHtmlFolder()!=null){
                    for(WriteableBusinessObject writeableBusinessObject : collection.getBusinessObjects()){
//                        TODO: add isSavedHTML
//                        if(writeableBusinessObject.isSavedHTML()){
                            writeableBusinessObject.xmlToHtml();
//                        }
                    }
                }
            }
        }
    }
}
