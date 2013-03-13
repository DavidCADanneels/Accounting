package be.dafke.Accounting.Dao.XML;

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

        accountings.readCollection("Accounting");

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

            accounting.getAccounts().readCollection("Account");
//            accounting.getAccounts().readCollection("Mortgage");
            accounting.getMortgages().readCollection("Mortgage");
            accounting.getJournals().readCollection("Journal");
            accounting.getCounterParties().readCollection("CounterParty");
//            accounting.getStatements().readCollection("be.dafke.Accounting.Objects.Accounting.Statements");

            StatementsSAXParser.readStatements(accounting.getStatements(), accounting.getCounterParties());

            for(Mortgage mortgage : accounting.getMortgages().getBusinessObjects()){
                MortgagesSAXParser.readMortgage(mortgage);
            }

            for(Journal journal : accounting.getJournals().getBusinessObjects()){
                JournalsSAXParser.readJournal(journal, accounting.getAccounts());
            }
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
//                writer.write("    <xsl2xml>" + acc.getXsl2XmlFile() + "</xsl2xml>\r\n");
//                writer.write("    <xsl2html>" + acc.getXsl2HtmlFile() + "</xsl2html>\r\n");
                writer.write("  </Accounting>\r\n");
            }
            if(accountings.getCurrentObject()!=null){
                writer.write("  <CurrentObject>" + accountings.getCurrentObject().getName() + "</CurrentObject>\r\n");
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
        accounting.getAccounts().writeCollection();
        for(Account account : accounting.getAccounts().getBusinessObjects()){
            AccountsSAXParser.writeAccount(account);
        }

        System.out.println("Balances.TOXML(" + accounting.toString() + ")");
        accounting.getBalances().writeCollection();
        for(Balance balance : accounting.getBalances().getBusinessObjects()){
            BalancesSAXParser.writeBalance(balance);
        }

        System.out.println("Journals.TOXML(" + accounting.toString() + ")");
        accounting.getJournals().writeCollection();
        for(Journal journal : accounting.getJournals().getBusinessObjects()){
            JournalsSAXParser.writeJournal(journal);
        }

        System.out.println("Mortgages.TOXML(" + accounting.toString() + ")");
        accounting.getMortgages().writeCollection();
        for(Mortgage mortgage:accounting.getMortgages().getBusinessObjects()){
            MortgagesSAXParser.writeMortgage(mortgage);
        }

        System.out.println("Counterparties.TOXML(" + accounting.toString() + ")");
        accounting.getCounterParties().writeCollection();

        System.out.println("Statements.TOXML(" + accounting.toString() + ")");
        StatementsSAXParser.writeStatements(accounting.getStatements());
//        accounting.getStatements().writeCollection();
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
