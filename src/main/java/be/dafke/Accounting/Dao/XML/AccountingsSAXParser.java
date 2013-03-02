package be.dafke.Accounting.Dao.XML;

import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Accountings;
import be.dafke.Accounting.Objects.Accounting.Accounts;
import be.dafke.Accounting.Objects.Accounting.Balance;
import be.dafke.Accounting.Objects.Accounting.Balances;
import be.dafke.Accounting.Objects.Accounting.Journal;
import be.dafke.Accounting.Objects.Accounting.Journals;
import be.dafke.Accounting.Objects.Coda.CounterParties;
import be.dafke.Accounting.Objects.Coda.Movements;
import be.dafke.Accounting.Objects.Mortgage.Mortgage;
import be.dafke.Accounting.Objects.Mortgage.Mortgages;
import be.dafke.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.swing.filechooser.FileSystemView;
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

    private static File getXmlFile() {
        String folderName = "Accounting";
        String fileName = "Accountings.xml";
        File home = new File(System.getProperty("user.home"));
        File folder = new File(home, folderName);
        if (folder.exists() && !folder.isDirectory()) {
            File renamed = FileSystemView.getFileSystemView().createFileObject(home, folderName + "_file");
            folder.renameTo(renamed);
            folder = new File(home, folderName);
        }
        if (!folder.isDirectory()) {
            folder.mkdir();
        }
        File file = FileSystemView.getFileSystemView().getChild(folder, fileName);
        if (!file.exists()) {
            file = FileSystemView.getFileSystemView().createFileObject(folder, fileName);
        }
        return file;
    }

    private static File getHtmlFile() {
        String folderName = "Accounting";
        String fileName = "Accountings.html";
        File home = new File(System.getProperty("user.home"));
        File folder = new File(home, folderName);
        if (folder.exists() && !folder.isDirectory()) {
            File renamed = FileSystemView.getFileSystemView().createFileObject(home, folderName + "_file");
            folder.renameTo(renamed);
            folder = new File(home, folderName);
        }
        if (!folder.isDirectory()) {
            folder.mkdir();
        }
        File file = FileSystemView.getFileSystemView().getChild(folder, fileName);
        if (!file.exists()) {
            file = FileSystemView.getFileSystemView().createFileObject(folder, fileName);
        }
        return file;
    }

    private static File getXsl2XmlFile() {
        String folderName = "Accounting";
        File home = new File(System.getProperty("user.home"));
        File folder = new File(home, folderName);
        File xslFolder = new File(folder, "xsl");
        return new File(xslFolder, "Accountings2xml.xsl");
    }

    private static File getXsl2HtmlFile() {
        String folderName = "Accounting";
        File home = new File(System.getProperty("user.home"));
        File folder = new File(home, folderName);
        File xslFolder = new File(folder, "xsl");
        return new File(xslFolder, "Accountings2html.xsl");
    }

    private static File getDtdFile() {
        String folderName = "Accounting";
        File home = new File(System.getProperty("user.home"));
        File folder = new File(home, folderName);
        File xslFolder = new File(folder, "xsl");
        return new File(xslFolder, "Accountings.dtd");
    }

    public static Accountings readAccountings() {
        Accountings accountings = new Accountings();
        System.out.println("fromXML");
        File file = getXmlFile();
        if(file == null || !file.exists()){
            System.err.println(file.getAbsolutePath() + "not found");
        }
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(true);
            DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file.getAbsolutePath());
            doc.getDocumentElement().normalize();

            NodeList accountingNodes = doc.getElementsByTagName("Accounting");
            for(int i=0;i<accountingNodes.getLength();i++){
                Element element = (Element)accountingNodes.item(i);
                String name = element.getElementsByTagName("name").item(0).getChildNodes().item(0).getNodeValue();
                String xmlFolder = element.getElementsByTagName("xmlFolder").item(0).getChildNodes().item(0).getNodeValue();
                String xslFolder = element.getElementsByTagName("xslFolder").item(0).getChildNodes().item(0).getNodeValue();
                String htmlFolder = element.getElementsByTagName("htmlFolder").item(0).getChildNodes().item(0).getNodeValue();
                String xmlFile = element.getElementsByTagName("xml").item(0).getChildNodes().item(0).getNodeValue();
                String htmlFile = element.getElementsByTagName("html").item(0).getChildNodes().item(0).getNodeValue();
                String xsl2XmlFile = element.getElementsByTagName("xsl2xml").item(0).getChildNodes().item(0).getNodeValue();
                String xsl2HtmlFile = element.getElementsByTagName("xsl2html").item(0).getChildNodes().item(0).getNodeValue();
                Accounting acc = new Accounting(name);
                acc.setXmlFolder(new File(xmlFolder));
                acc.setXslFolder(new File(xslFolder));
                acc.setHtmlFolder(new File(htmlFolder));
                acc.setXmlFile(new File(xmlFile));
                acc.setHtmlFile(new File(htmlFile));
                acc.setXsl2XmlFile(new File(xsl2XmlFile));
                acc.setXsl2HtmlFile(new File(xsl2HtmlFile));
                accountings.addAccounting(acc);
            }
            NodeList current = doc.getElementsByTagName("CurrentAccounting");
            if(current.getLength()>0){
                String currentAccountName = current.item(0).getChildNodes().item(0).getNodeValue();
                accountings.setCurrentAccounting(currentAccountName);
            }
        } catch (IOException io) {
            // FileSystemView.getFileSystemView().createFileObject(home, "TESTFILE");
            // System.out.println(pad + " has been created");
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(Accounting accounting : accountings.getAccountings()) {
            readAccounting(accounting);
        }
        return accountings;
    }

    private static void readAccounting(Accounting accounting){
        File file = accounting.getXmlFile();
        if(file == null || !file.exists()){
            System.err.println(file.getAbsolutePath() + "not found");
        }
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(true);
            DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file.getAbsolutePath());
            doc.getDocumentElement().normalize();

            Accounts accounts = accounting.getAccounts();
            Element element = (Element)doc.getElementsByTagName("Accounts").item(0);
//            String name = element.getElementsByTagName("name").item(0).getChildNodes().item(0).getNodeValue();
            String location = element.getElementsByTagName("location").item(0).getChildNodes().item(0).getNodeValue();
            String xmlFile = element.getElementsByTagName("xml").item(0).getChildNodes().item(0).getNodeValue();
            String htmlFile = element.getElementsByTagName("html").item(0).getChildNodes().item(0).getNodeValue();
            accounts.setFolder(location);
            accounts.setXmlFile(new File(xmlFile));
            accounts.setHtmlFile(new File(htmlFile));
            AccountsSAXParser.readAccounts(accounting.getAccounts(), accounting.getProjects());

            Journals journals = accounting.getJournals();
            element = (Element)doc.getElementsByTagName("Journals").item(0);
//            name = element.getElementsByTagName("name").item(0).getChildNodes().item(0).getNodeValue();
            location = element.getElementsByTagName("location").item(0).getChildNodes().item(0).getNodeValue();
            xmlFile = element.getElementsByTagName("xml").item(0).getChildNodes().item(0).getNodeValue();
            htmlFile = element.getElementsByTagName("html").item(0).getChildNodes().item(0).getNodeValue();
            journals.setFolder(location);
            journals.setXmlFile(new File(xmlFile));
            journals.setHtmlFile(new File(htmlFile));
            JournalsSAXParser.readJournals(accounting.getJournals(), accounting.getJournalTypes(), accounting.getAccounts());
            NodeList current = doc.getElementsByTagName("CurrentJournal");
            if(current.getLength()>0){
                String currentJournalName = current.item(0).getChildNodes().item(0).getNodeValue();
                Journal currentJournal = journals.get(currentJournalName);
                accounting.getJournals().setCurrentJournal(currentJournal);
            }

            Balances balances = accounting.getBalances();
            element = (Element)doc.getElementsByTagName("Balances").item(0);
//            name = element.getElementsByTagName("name").item(0).getChildNodes().item(0).getNodeValue();
            location = element.getElementsByTagName("location").item(0).getChildNodes().item(0).getNodeValue();
            xmlFile = element.getElementsByTagName("xml").item(0).getChildNodes().item(0).getNodeValue();
            htmlFile = element.getElementsByTagName("html").item(0).getChildNodes().item(0).getNodeValue();
            balances.setFolder(location);
            balances.setXmlFile(new File(xmlFile));
            balances.setHtmlFile(new File(htmlFile));
            BalancesSAXParser.readBalances(accounting.getBalances());

            Mortgages mortgages = accounting.getMortgages();
            element = (Element)doc.getElementsByTagName("Mortgages").item(0);
//            name = element.getElementsByTagName("name").item(0).getChildNodes().item(0).getNodeValue();
            location = element.getElementsByTagName("location").item(0).getChildNodes().item(0).getNodeValue();
            xmlFile = element.getElementsByTagName("xml").item(0).getChildNodes().item(0).getNodeValue();
            htmlFile = element.getElementsByTagName("html").item(0).getChildNodes().item(0).getNodeValue();
            mortgages.setFolder(location);
            mortgages.setXmlFile(new File(xmlFile));
            mortgages.setHtmlFile(new File(htmlFile));
            MortgagesSAXParser.readMortgages(accounting);

            CounterParties counterParties = accounting.getCounterParties();
            element = (Element)doc.getElementsByTagName("CounterParties").item(0);
//            name = element.getElementsByTagName("name").item(0).getChildNodes().item(0).getNodeValue();
            location = element.getElementsByTagName("location").item(0).getChildNodes().item(0).getNodeValue();
            xmlFile = element.getElementsByTagName("xml").item(0).getChildNodes().item(0).getNodeValue();
            htmlFile = element.getElementsByTagName("html").item(0).getChildNodes().item(0).getNodeValue();
            counterParties.setFolder(location);
            counterParties.setXmlFile(new File(xmlFile));
            counterParties.setHtmlFile(new File(htmlFile));
            CounterPartiesSAXParser.readCounterparties(accounting.getCounterParties(), accounting.getAccounts());

            Movements movements = accounting.getMovements();
            element = (Element)doc.getElementsByTagName("Movements").item(0);
//            name = element.getElementsByTagName("name").item(0).getChildNodes().item(0).getNodeValue();
            location = element.getElementsByTagName("location").item(0).getChildNodes().item(0).getNodeValue();
            xmlFile = element.getElementsByTagName("xml").item(0).getChildNodes().item(0).getNodeValue();
            htmlFile = element.getElementsByTagName("html").item(0).getChildNodes().item(0).getNodeValue();
            movements.setFolder(location);
            movements.setXmlFile(new File(xmlFile));
            movements.setHtmlFile(new File(htmlFile));
            MovementsSAXParser.readMovements(accounting.getMovements(), accounting.getCounterParties());
        } catch (IOException io) {
            io.printStackTrace();
//            FileSystemView.getFileSystemView().createFileObject(file.getPath());
//            System.out.println(file.getAbsolutePath() + " has been created");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeAccountings(Accountings accountings) {
        createDefaultValuesIfNull(accountings);
        toXml(accountings);
        toHtml(accountings);
    }

    private static void toXml(Accountings accountings){
        try {
            Writer writer = new FileWriter(getXmlFile());
            writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n"
                    + "<!DOCTYPE Accountings SYSTEM \"" + getDtdFile() +"\">\r\n"
                    + "<?xml-stylesheet type=\"text/xsl\" href=\"" + getXsl2XmlFile() +"\"?>\r\n" + "<Accountings>\r\n");
            for(Accounting acc : accountings.getAccountings()) {
                writer.write("  <Accounting>\r\n");
                writer.write("    <name>" + acc.toString() + "</name>\r\n");
                writer.write("    <xmlFolder>" + acc.getXmlFolder() + "</xmlFolder>\r\n");
                writer.write("    <xslFolder>" + acc.getXslFolder() + "</xslFolder>\r\n");
                writer.write("    <htmlFolder>" + acc.getHtmlFolder() + "</htmlFolder>\r\n");
                writer.write("    <xml>" + acc.getXmlFile() + "</xml>\r\n");
                writer.write("    <html>" + acc.getHtmlFile() + "</html>\r\n");
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

    private static void createDefaultValuesIfNull(Accountings accountings){
        // ACCOUNTINGS



        for(Accounting accounting:accountings.getAccountings()){
            accounting.setDefaultXmlFoldersAndFiles(false);
            accounting.setDefaultHtmlFoldersAndFiles(false);
        }
    }

    private static void writeAccounting(Accounting accounting){
//        createXMLFolders(accounting);

        writeAccountingFile(accounting);

        System.out.println("Accounts.TOXML(" + accounting.toString() + ")");
        Accounts accounts = accounting.getAccounts();
        File xmlFolder = FileSystemView.getFileSystemView().getChild(accounting.getXmlFolder(), accounts.getFolder());
        if(!xmlFolder.exists()){
            xmlFolder.mkdir();
        }
        AccountsSAXParser.writeAccounts(accounts);

        System.out.println("Balances.TOXML(" + accounting.toString() + ")");
        Balances balances = accounting.getBalances();
        xmlFolder = FileSystemView.getFileSystemView().getChild(accounting.getXmlFolder(), balances.getFolder());
        if(!xmlFolder.exists()) {
            xmlFolder.mkdir();
        }
        BalancesSAXParser.writeBalances(balances);

        System.out.println("Journals.TOXML(" + accounting.toString() + ")");
        Journals journals = accounting.getJournals();
        xmlFolder = FileSystemView.getFileSystemView().getChild(accounting.getXmlFolder(), journals.getFolder());
        if(!xmlFolder.exists()){
            xmlFolder.mkdir();
        }
        JournalsSAXParser.writeJournals(journals);

        System.out.println("Mortgages.TOXML(" + accounting.toString() + ")");
        Mortgages mortgages = accounting.getMortgages();
        xmlFolder = FileSystemView.getFileSystemView().getChild(accounting.getXmlFolder(), mortgages.getFolder());
        if(!xmlFolder.exists()){
            xmlFolder.mkdir();
        }
        MortgagesSAXParser.writeMortgages(mortgages);

        System.out.println("Counterparties.TOXML(" + accounting.toString() + ")");
        CounterPartiesSAXParser.writeCounterparties(accounting.getCounterParties());

        System.out.println("Movements.TOXML(" + accounting.toString() + ")");
        MovementsSAXParser.writeMovements(accounting.getMovements());
    }

    private static void writeAccountingFile(Accounting accounting) {
        try {
            Writer writer = new FileWriter(accounting.getXmlFile());
            writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" + "<!DOCTYPE Accounting SYSTEM \""
                    + accounting.getDtdFile() + "\">\r\n" + "<?xml-stylesheet type=\"text/xsl\" href=\""
                    + accounting.getXsl2XmlFile() + "\"?>\r\n" + "<Accounting>\r\n");
            writer.write("  <name>" + accounting.getName() + "</name>\r\n");
            writer.write("  <Accounts>\r\n");
            writer.write("    <name>Accounts</name>\r\n");
            writer.write("    <location>" + accounting.getAccounts().getFolder() + "</location>\r\n");
            writer.write("    <xml>" + accounting.getAccounts().getXmlFile() + "</xml>\r\n");
            writer.write("    <html>" + accounting.getAccounts().getHtmlFile() + "</html>\r\n");
            writer.write("  </Accounts>\r\n");
            writer.write("  <Journals>\r\n");
            writer.write("    <name>Journals</name>\r\n");
            writer.write("    <location>" + accounting.getJournals().getFolder() + "</location>\r\n");
            writer.write("    <xml>" + accounting.getJournals().getXmlFile() + "</xml>\r\n");
            writer.write("    <html>" + accounting.getJournals().getHtmlFile() + "</html>\r\n");
            writer.write("  </Journals>\r\n");
            if(accounting.getJournals().getCurrentJournal()!=null){
                writer.write("  <CurrentJournal>" + accounting.getJournals().getCurrentJournal().getName() + "</CurrentJournal>\r\n");
            }
            writer.write("  <Balances>\r\n");
            writer.write("    <name>Balances</name>\r\n");
            writer.write("    <location>" + accounting.getBalances().getFolder() + "</location>\r\n");
            writer.write("    <xml>" + accounting.getBalances().getXmlFile() + "</xml>\r\n");
            writer.write("    <html>" + accounting.getBalances().getHtmlFile() + "</html>\r\n");
            writer.write("  </Balances>\r\n");
            writer.write("  <Mortgages>\r\n");
            writer.write("    <name>Mortgages</name>\r\n");
            writer.write("    <location>" + accounting.getMortgages().getFolder() + "</location>\r\n");
            writer.write("    <xml>" + accounting.getMortgages().getXmlFile() + "</xml>\r\n");
            writer.write("    <html>" + accounting.getMortgages().getHtmlFile() + "</html>\r\n");
            writer.write("  </Mortgages>\r\n");
            writer.write("  <CounterParties>\r\n");
            writer.write("    <name>CounterParties</name>\r\n");
            writer.write("    <location>" + accounting.getCounterParties().getFolder() + "</location>\r\n");
            writer.write("    <xml>" + accounting.getCounterParties().getXmlFile() + "</xml>\r\n");
            writer.write("    <html>" + accounting.getCounterParties().getHtmlFile() + "</html>\r\n");
            writer.write("  </CounterParties>\r\n");
            writer.write("  <Movements>\r\n");
            writer.write("    <name>Movements</name>\r\n");
            writer.write("    <location>" + accounting.getMovements().getFolder() + "</location>\r\n");
            writer.write("    <xml>" + accounting.getMovements().getXmlFile() + "</xml>\r\n");
            writer.write("    <html>" + accounting.getMovements().getHtmlFile() + "</html>\r\n");
            writer.write("  </Movements>\r\n");
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
        Utils.xmlToHtml(getXmlFile(),getXsl2HtmlFile(),getHtmlFile(),null);
        for(Accounting accounting:accountings.getAccountings()){
            toHtml(accounting);
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
            for(Account account:accounting.getAccounts().getAllAccounts()){
//                TODO: add isSavedHTML
//                if(account.isSavedHTML()){
                Utils.xmlToHtml(account.getXmlFile(), account.getXslFile(), account.getHtmlFile(), null);
//                }
            }
            for(Journal journal:accounting.getJournals().getAllJournals()){
//                TODO: add isSavedHTML
//                if(journal.isSavedHTML()){
                    Utils.xmlToHtml(journal.getXmlFile(), journal.getXslFile(), journal.getHtmlFile(), null);
//                }
            }
            for(Balance balance:accounting.getBalances().getBalances()){
//                TODO: add isSavedHTML
//              if(balance.isSavedHTML()){
                    Utils.xmlToHtml(balance.getXmlFile(), balance.getXslFile(), balance.getHtmlFile(), null);
//                }
            }
            for(Mortgage mortgage:accounting.getMortgages().getMortgages()){
//                TODO: add isSavedHTML
//                if(mortgage.isSavedHTML()){
                    Utils.xmlToHtml(mortgage.getXmlFile(), mortgage.getXslFile(), mortgage.getHtmlFile(), null);
//                }
            }
        }
    }
}
