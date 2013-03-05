package be.dafke.Accounting.Dao.XML;

import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Accountings;
import be.dafke.Accounting.Objects.Accounting.Accounts;
import be.dafke.Accounting.Objects.Accounting.Balance;
import be.dafke.Accounting.Objects.Accounting.Balances;
import be.dafke.Accounting.Objects.Accounting.BusinessObject;
import be.dafke.Accounting.Objects.Accounting.CounterParties;
import be.dafke.Accounting.Objects.Accounting.Journal;
import be.dafke.Accounting.Objects.Accounting.Journals;
import be.dafke.Accounting.Objects.Accounting.Mortgage;
import be.dafke.Accounting.Objects.Accounting.Mortgages;
import be.dafke.Accounting.Objects.Accounting.Movements;
import be.dafke.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

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
                Accounting acc = new Accounting(Utils.getValue(element, "name"));
                acc.setHtmlFolder(Utils.getFile(element, "htmlFolder"));
                acc.setXmlFile(Utils.getFile(element, "xml"));
                acc.setHtmlFile(Utils.getFile(element, "html"));
                acc.setXsl2XmlFile(Utils.getFile(element, "xsl2xml"));
                acc.setXsl2HtmlFile(Utils.getFile(element, "xsl2html"));
                accountings.addAccounting(acc);
            }
            String currentAccountName = Utils.getValue(doc, "CurrentAccounting");
            if(currentAccountName!=null){
                accountings.setCurrentAccounting(currentAccountName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(Accounting accounting : accountings.getAccountings()) {
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
                BusinessObject businessObject = accounting.getCollection(key);
                Element element = (Element)doc.getElementsByTagName(key).item(0);
                String name = Utils.getValue(element, "name");
                System.out.println("parsing: " + name);
                File xmlFile = Utils.getFile(element, "xml");
                File htmlFile = Utils.getFile(element, "html");
                businessObject.setXmlFile(xmlFile);
                businessObject.setHtmlFile(htmlFile);
            }

            AccountsSAXParser.readAccounts(accounting.getAccounts(), accounting.getProjects());
            JournalsSAXParser.readJournals(accounting.getJournals(), accounting.getJournalTypes(), accounting.getAccounts());
            MortgagesSAXParser.readMortgages(accounting.getMortgages(), accounting.getAccounts());
            CounterPartiesSAXParser.readCounterparties(accounting.getCounterParties(), accounting.getAccounts());
            MovementsSAXParser.readMovements(accounting.getMovements(), accounting.getCounterParties());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeAccountings(Accountings accountings) {
        accountings.createDefaultValuesIfNull();
        toXml(accountings);
        toHtml(accountings);
    }

    private static void toXml(Accountings accountings){
        try {
            Writer writer = new FileWriter(accountings.getXmlFile());
            writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n"
                    + "<!DOCTYPE Accountings SYSTEM \"" + accountings.getDtdFile() +"\">\r\n"
                    + "<?xml-stylesheet type=\"text/xsl\" href=\"" + accountings.getXsl2XmlFile() +"\"?>\r\n" + "<Accountings>\r\n");
            for(Accounting acc : accountings.getAccountings()) {
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
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(Accounting accounting : accountings.getAccountings()) {
            writeAccounting(accounting);
        }
    }

    private static void writeAccounting(Accounting accounting){
        writeAccountingFile(accounting);

        System.out.println("Accounts.TOXML(" + accounting.toString() + ")");
        AccountsSAXParser.writeAccounts(accounting.getAccounts());

        System.out.println("Balances.TOXML(" + accounting.toString() + ")");
        BalancesSAXParser.writeBalances(accounting.getBalances());

        System.out.println("Journals.TOXML(" + accounting.toString() + ")");
        JournalsSAXParser.writeJournals(accounting.getJournals());

        System.out.println("Mortgages.TOXML(" + accounting.toString() + ")");
        MortgagesSAXParser.writeMortgages(accounting.getMortgages());

        System.out.println("Counterparties.TOXML(" + accounting.toString() + ")");
        CounterPartiesSAXParser.writeCounterparties(accounting.getCounterParties());

        System.out.println("Movements.TOXML(" + accounting.toString() + ")");
        MovementsSAXParser.writeMovements(accounting.getMovements());
    }

    private static void writeAccountingFile(Accounting accounting) {
        try {
            Writer writer = new FileWriter(accounting.getXmlFile());
            writer.write(Utils.getXmlHeader(accounting));
            writer.write("<Accounting>\r\n");
            writer.write("  <name>" + accounting.getName() + "</name>\r\n");
            for(String key:accounting.getKeys()) {
                BusinessObject businessObject = accounting.getCollection(key);
                System.out.println("writing: " + key);
                writer.write("  <" + key + ">\r\n");
                writer.write("    <name>" + key + "</name>\r\n");
                writer.write("    <xml>" + businessObject.getXmlFile() + "</xml>\r\n");
                if(businessObject.getHtmlFile()!=null){
                    writer.write("    <html>" + businessObject.getHtmlFile() + "</html>\r\n");
                }
                writer.write("  </" + key + ">\r\n");
            }
            writer.write("</Accounting>\r\n");
            writer.flush();
            writer.close();
//			setSaved(true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void toHtml(Accountings accountings){
        Utils.xmlToHtml(accountings.getXmlFile(),accountings.getXsl2HtmlFile(),accountings.getHtmlFile(),null);
        for(Accounting accounting:accountings.getAccountings()){
            if(accounting.getHtmlFolder()!=null && !accounting.getHtmlFolder().getPath().equals("null")){
                toHtml(accounting);
            }
        }
    }

    private static void toHtml(Accounting accounting){
        if(accounting.getHtmlFolder() != null){
            Accounts accounts = accounting.getAccounts();
            Journals journals = accounting.getJournals();
            Balances balances = accounting.getBalances();
            Mortgages mortgages = accounting.getMortgages();
            Movements movements = accounting.getMovements();
            CounterParties counterParties = accounting.getCounterParties();

            // TODO path of HtmlFile cannot contain spaces
            Utils.xmlToHtml(accounting.getXmlFile(), accounting.getXsl2HtmlFile(), accounting.getHtmlFile(), null);
            Utils.xmlToHtml(accounts.getXmlFile(), accounts.getXsl2HtmlFile(), accounts.getHtmlFile(), null);
            Utils.xmlToHtml(journals.getXmlFile(), journals.getXsl2HtmlFile(), journals.getHtmlFile(), null);
            Utils.xmlToHtml(balances.getXmlFile(), balances.getXsl2HtmlFile(), balances.getHtmlFile(), null);
            Utils.xmlToHtml(mortgages.getXmlFile(), mortgages.getXsl2HtmlFile(), mortgages.getHtmlFile(), null);
            Utils.xmlToHtml(movements.getXmlFile(), movements.getXsl2HtmlFile(), movements.getHtmlFile(), null);
            Utils.xmlToHtml(counterParties.getXmlFile(), counterParties.getXsl2HtmlFile(), counterParties.getHtmlFile(), null);
            for(Account account:accounting.getAccounts().getBusinessObjects()){
//                TODO: add isSavedHTML
//                if(account.isSavedHTML()){
                Utils.xmlToHtml(account.getXmlFile(), account.getXsl2HtmlFile(), account.getHtmlFile(), null);
//                }
            }
            for(Journal journal:accounting.getJournals().getBusinessObjects()){
//                TODO: add isSavedHTML
//                if(journal.isSavedHTML()){
                    Utils.xmlToHtml(journal.getXmlFile(), journal.getXsl2HtmlFile(), journal.getHtmlFile(), null);
//                }
            }
            for(Balance balance:accounting.getBalances().getBusinessObjects()){
//                TODO: add isSavedHTML
//              if(balance.isSavedHTML()){
                    Utils.xmlToHtml(balance.getXmlFile(), balance.getXsl2HtmlFile(), balance.getHtmlFile(), null);
//                }
            }
            for(Mortgage mortgage:accounting.getMortgages().getBusinessObjects()){
//                TODO: add isSavedHTML
//                if(mortgage.isSavedHTML()){
                    Utils.xmlToHtml(mortgage.getXmlFile(), mortgage.getXsl2HtmlFile(), mortgage.getHtmlFile(), null);
//                }
            }
        }
    }
}
