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
import org.xml.sax.XMLReader;

import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
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
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException io) {
            }
        } else {
            try {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                factory.setValidating(true);
                SAXParser parser = factory.newSAXParser();
                XMLReader reader = parser.getXMLReader();
                reader.setContentHandler(new AccountingsContentHandler(accountings));
                reader.setErrorHandler(new FoutHandler());
                reader.parse(file.getAbsolutePath());
            } catch (IOException io) {
                // FileSystemView.getFileSystemView().createFileObject(home, "TESTFILE");
                // System.out.println(pad + " has been created");
            } catch (Exception e) {
                e.printStackTrace();
            }
            for(Accounting accounting : accountings.getAccountings()) {
                readAccounting(accounting, file);
            }
        }
        return accountings;
    }

    private static void readAccounting(Accounting accounting, File file){
        String name = accounting.toString();
        File subFolder = FileSystemView.getFileSystemView().getChild(file.getParentFile(), name);
        if (!subFolder.isDirectory()) {
            System.err.println(name + " not found or no directory");
        } else {
            // TODO read the names of these files from the index file "Accounting.xml"
            File accountsFile = FileSystemView.getFileSystemView().getChild(subFolder, "Accounts.xml");
            File journalsFile = FileSystemView.getFileSystemView().getChild(subFolder, "Journals.xml");
            File balancesFile = FileSystemView.getFileSystemView().getChild(subFolder, "Balances.xml");
            File mortgagesFile = FileSystemView.getFileSystemView().getChild(subFolder, "Mortgages.xml");
            File counterpartiesFile = FileSystemView.getFileSystemView().getChild(subFolder, "Counterparties.xml");
            File movementsFile = FileSystemView.getFileSystemView().getChild(subFolder, "Movements.xml");
            if (!accountsFile.exists()) {
                System.err.println("no Accounts.xml file found in " + name);
            } else {
                AccountsSAXParser.readAccounts(accounting.getAccounts(), accounting.getProjects(), accountsFile);
            }
            if (!journalsFile.exists()) {
                System.err.println("no Journals.xml file found in " + name);
            } else {
                JournalsSAXParser.readJournals(accounting, journalsFile);
            }
            if (!balancesFile.exists()) {
                System.err.println("no Balances.xml file found in " + name);
            } else {
                BalancesSAXParser.readBalances(accounting.getBalances(), balancesFile);
            }
            if (!mortgagesFile.exists()) {
                System.err.println("no Mortgages.xml file found in " + name);
            } else {
                MortgagesSAXParser.readMortgages(accounting, mortgagesFile);
            }
            if (!counterpartiesFile.exists()) {
                System.err.println("no Counterparties.xml file found in " + name);
            } else {
                CounterPartiesSAXParser.readCounterparties(accounting.getCounterParties(), accounting.getAccounts(), counterpartiesFile);
            }
            if (!movementsFile.exists()) {
                System.err.println("no Movements.xml file found in " + name);
            } else {
                MovementsSAXParser.readMovements(accounting.getMovements(), accounting.getCounterParties(), movementsFile);
            }
        }
    }

    public static void writeAccountings(Accountings accountings) {
        createDefaultValuesIfNull(accountings);
        Accounting currentAccounting = accountings.getCurrentAccounting();
        try {
            Writer writer = new FileWriter(getXmlFile());
            writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n"
                    + "<!DOCTYPE Accountings SYSTEM \"" + getDtdFile() +"\">\r\n"
                    + "<?xml-stylesheet type=\"text/xsl\" href=\"" + getXsl2XmlFile() +"\"?>\r\n" + "<Accountings>\r\n");
            for(Accounting acc : accountings.getAccountings()) {
                writer.write("  <Accounting name=\"" + acc.toString()
                        + "\" current=\"" + (acc == currentAccounting?"true":"false")
                        + "\" xmlFolder=\"" + acc.getXmlFolder()
                        + "\" xslFolder=\"" + acc.getXslFolder()
                        + "\" htmlFolder=\"" + acc.getHtmlFolder()
                        + "\" xml=\"" + acc.getXmlFile()
                        + "\" html=\"" + acc.getHtmlFile()
                        + "\" xsl2xml=\"" + acc.getXsl2XmlFile()
                        + "\" xsl2html=\"" + acc.getXsl2HtmlFile()
                        + "\"/>\r\n");
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

    private static void createHtmlFolders(Accounting accounting, File rootFolder) {
        rootFolder.mkdirs();
        // ACCOUNTING
        //
        File htmlFile = accounting.getHtmlFile();
        if(htmlFile == null || htmlFile.getPath().equals("null")){
            htmlFile = FileSystemView.getFileSystemView().getChild(rootFolder, "Accounting.html");
            accounting.setHtmlFile(htmlFile);
        }

        // ACCOUNTS
        //
        Accounts accounts = accounting.getAccounts();
        htmlFile = accounts.getHtmlFile();
        if(htmlFile == null || htmlFile.getPath().equals("null")){
            htmlFile = FileSystemView.getFileSystemView().getChild(rootFolder, "Accounts.html");
            accounts.setHtmlFile(htmlFile);
        }
        File htmlFolder = FileSystemView.getFileSystemView().getChild(rootFolder, accounts.getFolder());
        htmlFolder.mkdirs();

        // JOURNALS
        //
        Journals journals = accounting.getJournals();
        htmlFile = journals.getHtmlFile();
        if(htmlFile == null || htmlFile.getPath().equals("null")){
            htmlFile = FileSystemView.getFileSystemView().getChild(rootFolder, "Journals.html");
            journals.setHtmlFile(htmlFile);
        }
        htmlFolder = FileSystemView.getFileSystemView().getChild(rootFolder, journals.getFolder());
        htmlFolder.mkdirs();

        // BALANCES
        //
        Balances balances = accounting.getBalances();
        htmlFile = balances.getHtmlFile();
        if(htmlFile == null || htmlFile.getPath().equals("null")){
            htmlFile = FileSystemView.getFileSystemView().getChild(rootFolder, "Balances.html");
            balances.setHtmlFile(htmlFile);
        }
        htmlFolder = FileSystemView.getFileSystemView().getChild(rootFolder, balances.getFolder());
        htmlFolder.mkdirs();
        for(Balance balance: balances.getBalances()){
            htmlFile = FileSystemView.getFileSystemView().getChild(htmlFolder, balance.getName() + ".xml");
            balance.setHtmlFile(htmlFile);
        }

        // MORTGAGES
        //
        Mortgages mortgages = accounting.getMortgages();
        htmlFile = mortgages.getHtmlFile();
        if(htmlFile == null || htmlFile.getPath().equals("null")){
            htmlFile = FileSystemView.getFileSystemView().getChild(rootFolder, "Mortgages.html");
            mortgages.setHtmlFile(htmlFile);
        }
        htmlFolder = FileSystemView.getFileSystemView().getChild(rootFolder, mortgages.getFolder());
        htmlFolder.mkdirs();

        // MOVEMENTS
        //
        Movements movements = accounting.getMovements();
        htmlFile = movements.getHtmlFile();
        if(htmlFile == null || htmlFile.getPath().equals("null")){
            htmlFile = FileSystemView.getFileSystemView().getChild(rootFolder, "Movements.html");
            movements.setHtmlFile(htmlFile);
        }
        htmlFolder = FileSystemView.getFileSystemView().getChild(rootFolder, movements.getFolder());
        htmlFolder.mkdirs();

        // COUNTERPARTIES
        //
        CounterParties counterParties = accounting.getCounterParties();
        htmlFile = counterParties.getHtmlFile();
        if(htmlFile == null || htmlFile.getPath().equals("null")){
            htmlFile = FileSystemView.getFileSystemView().getChild(rootFolder, "CounterParties.html");
            counterParties.setHtmlFile(htmlFile);
        }
        htmlFolder = FileSystemView.getFileSystemView().getChild(rootFolder, counterParties.getFolder());
        htmlFolder.mkdirs();
    }

    private static void createDefaultValuesIfNull(Accountings accountings){
        // ACCOUNTINGS



        for(Accounting accounting:accountings.getAccountings()){
            createXMLFolders(accounting);
        }
    }

    public static void createXMLFolders(Accounting accounting) {
        File home = new File(System.getProperty("user.home"));
        File accountingFolder = FileSystemView.getFileSystemView().getChild(home, "Accounting");

        // ACCOUNTING
        //
        File xmlFolder = accounting.getXmlFolder();
        if(xmlFolder == null || xmlFolder.getPath().equals("null")){
            xmlFolder = FileSystemView.getFileSystemView().getChild(accountingFolder, accounting.getName());
            accounting.setXmlFolder(xmlFolder);
        }
        File xmlFile = accounting.getXmlFile();
        if(xmlFile == null || xmlFile.getPath().equals("null")){
            xmlFile = FileSystemView.getFileSystemView().getChild(xmlFolder, "Accounting.xml");
            accounting.setXmlFile(xmlFile);
        }
        File xslFolder = accounting.getXslFolder();
        if(xslFolder == null || xslFolder.getPath().equals("null")){
            xslFolder = FileSystemView.getFileSystemView().getChild(accountingFolder, "xsl");
            accounting.setXslFolder(xslFolder);
        }
        File xsl2XmlFile = accounting.getXsl2XmlFile();
        if(xsl2XmlFile == null || xsl2XmlFile.getPath().equals("null")){
            xsl2XmlFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Accounting2xml.xsl");
            accounting.setXsl2XmlFile(xsl2XmlFile);
        }
        File xsl2HtmlFile = accounting.getXsl2HtmlFile();
        if(xsl2HtmlFile == null || xsl2HtmlFile.getPath().equals("null")){
            xsl2HtmlFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Accounting2html.xsl");
            accounting.setXsl2HtmlFile(xsl2HtmlFile);
        }
        File dtdFile = accounting.getDtdFile();
        if(dtdFile == null || dtdFile.getPath().equals("null")){
            dtdFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Accounting.dtd");
            accounting.setDtdFile(dtdFile);
        }

        // ACCOUNTS
        //
        Accounts accounts = accounting.getAccounts();
        String folder = accounts.getFolder();
        if(folder == null || folder.equals("null")){
            folder = "Accounts";
            accounts.setFolder(folder);
        }
        xmlFile = accounts.getXmlFile();
        if(xmlFile == null || xmlFile.getPath().equals("null")){
            xmlFile = FileSystemView.getFileSystemView().getChild(xmlFolder, "Accounts.xml");
            accounts.setXmlFile(xmlFile);
        }
        xsl2XmlFile = accounts.getXsl2XmlFile();
        if(xsl2XmlFile == null || xsl2XmlFile.getPath().equals("null")){
            xsl2XmlFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Accounts2xml.xsl");
            accounts.setXsl2XmlFile(xsl2XmlFile);
        }
        xsl2HtmlFile = accounts.getXsl2HtmlFile();
        if(xsl2HtmlFile == null || xsl2HtmlFile.getPath().equals("null")){
            xsl2HtmlFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Accounts2html.xsl");
            accounts.setXsl2HtmlFile(xsl2HtmlFile);
        }
        dtdFile = accounts.getDtdFile();
        if(dtdFile == null || dtdFile.getPath().equals("null")){
            dtdFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Accounts.dtd");
            accounts.setDtdFile(dtdFile);
        }

        // JOURNALS
        //
        Journals journals = accounting.getJournals();
        folder = journals.getFolder();
        if(folder == null || folder.equals("null")){
            folder = "Journals";
            journals.setFolder(folder);
        }
        xmlFile = journals.getXmlFile();
        if(xmlFile == null || xmlFile.getPath().equals("null")){
            xmlFile = FileSystemView.getFileSystemView().getChild(xmlFolder, "Journals.xml");
            journals.setXmlFile(xmlFile);
        }
        xsl2XmlFile = journals.getXsl2XmlFile();
        if(xsl2XmlFile == null || xsl2XmlFile.getPath().equals("null")){
            xsl2XmlFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Journals2xml.xsl");
            journals.setXsl2XmlFile(xsl2XmlFile);
        }
        xsl2HtmlFile = journals.getXsl2HtmlFile();
        if(xsl2HtmlFile == null || xsl2HtmlFile.getPath().equals("null")){
            xsl2HtmlFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Journals2html.xsl");
            journals.setXsl2HtmlFile(xsl2HtmlFile);
        }
        dtdFile = journals.getDtdFile();
        if(dtdFile == null || dtdFile.getPath().equals("null")){
            dtdFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Journals.dtd");
            journals.setDtdFile(dtdFile);
        }

        // BALANCES
        //
        Balances balances = accounting.getBalances();
        folder = balances.getFolder();
        if(folder == null || folder.equals("null")){
            folder = "Balances";
            balances.setFolder(folder);
        }
        xmlFile = balances.getXmlFile();
        if(xmlFile == null || xmlFile.getPath().equals("null")){
            xmlFile = FileSystemView.getFileSystemView().getChild(xmlFolder, "Balances.xml");
            balances.setXmlFile(xmlFile);
        }
        xsl2XmlFile = balances.getXsl2XmlFile();
        if(xsl2XmlFile == null || xsl2XmlFile.getPath().equals("null")){
            xsl2XmlFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Balances2xml.xsl");
            balances.setXsl2XmlFile(xsl2XmlFile);
        }
        xsl2HtmlFile = balances.getXsl2HtmlFile();
        if(xsl2HtmlFile == null || xsl2HtmlFile.getPath().equals("null")){
            xsl2HtmlFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Balances2html.xsl");
            balances.setXsl2HtmlFile(xsl2HtmlFile);
        }
        dtdFile = balances.getDtdFile();
        if(dtdFile == null || dtdFile.getPath().equals("null")){
            dtdFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Balances.dtd");
            balances.setDtdFile(dtdFile);
        }
        //
        for(Balance balance: balances.getBalances()){
            File xmlFolderBalances = FileSystemView.getFileSystemView().getChild(xmlFolder, folder);
//            File htmlFolderBalances = FileSystemView.getFileSystemView().getChild(htmlFolder, folder);

            balance.setXmlFile(FileSystemView.getFileSystemView().getChild(xmlFolderBalances, balance.getName() + ".xml"));
            balance.setXslFile(FileSystemView.getFileSystemView().getChild(xslFolder, "Balance.xsl"));
            //htmlFile = FileSystemView.getFileSystemView().getChild(htmlFolder, name + ".html");
        }


        // MORTGAGES
        //
        Mortgages mortgages = accounting.getMortgages();
        folder = mortgages.getFolder();
        if(folder == null || folder.equals("null")){
            folder = "Mortgages";
            mortgages.setFolder(folder);
        }
        xmlFile = mortgages.getXmlFile();
        if(xmlFile == null || xmlFile.getPath().equals("null")){
            xmlFile = FileSystemView.getFileSystemView().getChild(xmlFolder, "Mortgages.xml");
            mortgages.setXmlFile(xmlFile);
        }
        xsl2XmlFile = mortgages.getXsl2XmlFile();
        if(xsl2XmlFile == null || xsl2XmlFile.getPath().equals("null")){
            xsl2XmlFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Mortgages2xml.xsl");
            mortgages.setXsl2XmlFile(xsl2XmlFile);
        }
        xsl2HtmlFile = mortgages.getXsl2HtmlFile();
        if(xsl2HtmlFile == null || xsl2HtmlFile.getPath().equals("null")){
            xsl2HtmlFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Mortgages2html.xsl");
            mortgages.setXsl2HtmlFile(xsl2HtmlFile);
        }
        dtdFile = mortgages.getDtdFile();
        if(dtdFile == null || dtdFile.getPath().equals("null")){
            dtdFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Mortgages.dtd");
            mortgages.setDtdFile(dtdFile);
        }

        // MOVEMENTS
        //
        Movements movements = accounting.getMovements();
        folder = movements.getFolder();
        if(folder == null || folder.equals("null")){
            folder = "Movements";
            movements.setFolder(folder);
        }
        xmlFile = movements.getXmlFile();
        if(xmlFile == null || xmlFile.getPath().equals("null")){
            xmlFile = FileSystemView.getFileSystemView().getChild(xmlFolder, "Movements.xml");
            movements.setXmlFile(xmlFile);
        }
        xsl2XmlFile = movements.getXsl2XmlFile();
        if(xsl2XmlFile == null || xsl2XmlFile.getPath().equals("null")){
            xsl2XmlFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Movements2xml.xsl");
            movements.setXsl2XmlFile(xsl2XmlFile);
        }
        xsl2HtmlFile = movements.getXsl2HtmlFile();
        if(xsl2HtmlFile == null || xsl2HtmlFile.getPath().equals("null")){
            xsl2HtmlFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Movements2html.xsl");
            movements.setXsl2HtmlFile(xsl2HtmlFile);
        }

        dtdFile = movements.getDtdFile();
        if(dtdFile == null || dtdFile.getPath().equals("null")){
            dtdFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Movements.dtd");
            movements.setDtdFile(dtdFile);
        }

        // COUNTERPARTIES
        //
        CounterParties counterParties = accounting.getCounterParties();
        folder = counterParties.getFolder();
        if(folder == null || folder.equals("null")){
            folder = "CounterParties";
            counterParties.setFolder(folder);
        }
        xmlFile = counterParties.getXmlFile();
        if(xmlFile == null || xmlFile.getPath().equals("null")){
            xmlFile = FileSystemView.getFileSystemView().getChild(xmlFolder, "CounterParties.xml");
            counterParties.setXmlFile(xmlFile);
        }
        xsl2XmlFile = counterParties.getXsl2XmlFile();
        if(xsl2XmlFile == null || xsl2XmlFile.getPath().equals("null")){
            xsl2XmlFile = FileSystemView.getFileSystemView().getChild(xslFolder, "CounterParties2xml.xsl");
            counterParties.setXsl2XmlFile(xsl2XmlFile);
        }
        xsl2HtmlFile = counterParties.getXsl2HtmlFile();
        if(xsl2HtmlFile == null || xsl2HtmlFile.getPath().equals("null")){
            xsl2HtmlFile = FileSystemView.getFileSystemView().getChild(xslFolder, "CounterParties2html.xsl");
            counterParties.setXsl2HtmlFile(xsl2HtmlFile);
        }
        dtdFile = counterParties.getDtdFile();
        if(dtdFile == null || dtdFile.getPath().equals("null")){
            dtdFile = FileSystemView.getFileSystemView().getChild(xslFolder, "CounterParties.dtd");
            counterParties.setDtdFile(dtdFile);
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

        toHtml(accounting);
    }

    private static void writeAccountingFile(Accounting accounting) {
        try {
            Writer writer = new FileWriter(accounting.getXmlFile());
            writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" + "<!DOCTYPE Accounts SYSTEM \""
                    + accounting.getDtdFile() + "\">\r\n" + "<?xml-stylesheet type=\"text/xsl\" href=\""
                    + accounting.getXsl2XmlFile() + "\"?>\r\n" + "<Accounting>\r\n");
            writer.write("  <name>" + accounting.getName() + "</name>\r\n");
            writer.write("  <link>\r\n");
            writer.write("    <name>Accounts</name>\r\n");
            writer.write("    <location>" + accounting.getAccounts().getFolder() + "</location>\r\n");
            writer.write("    <xml>" + accounting.getAccounts().getXmlFile() + "</xml>\r\n");
            writer.write("    <html>" + accounting.getAccounts().getHtmlFile() + "</html>\r\n");
            writer.write("  </link>\r\n");
            writer.write("  <link>\r\n");
            writer.write("    <name>Journals</name>\r\n");
            writer.write("    <location>" + accounting.getJournals().getFolder() + "</location>\r\n");
            writer.write("    <xml>" + accounting.getJournals().getXmlFile() + "</xml>\r\n");
            writer.write("    <html>" + accounting.getJournals().getHtmlFile() + "</html>\r\n");
            writer.write("  </link>\r\n");
            writer.write("  <link>\r\n");
            writer.write("    <name>Balances</name>\r\n");
            writer.write("    <location>" + accounting.getBalances().getFolder() + "</location>\r\n");
            writer.write("    <xml>" + accounting.getBalances().getXmlFile() + "</xml>\r\n");
            writer.write("    <html>" + accounting.getBalances().getHtmlFile() + "</html>\r\n");
            writer.write("  </link>\r\n");
            writer.write("  <link>\r\n");
            writer.write("    <name>Mortgages</name>\r\n");
            writer.write("    <location>" + accounting.getMortgages().getFolder() + "</location>\r\n");
            writer.write("    <xml>" + accounting.getMortgages().getXmlFile() + "</xml>\r\n");
            writer.write("    <html>" + accounting.getMortgages().getHtmlFile() + "</html>\r\n");
            writer.write("  </link>\r\n");
            writer.write("  <link>\r\n");
            writer.write("    <name>CounterParties</name>\r\n");
            writer.write("    <location>" + accounting.getCounterParties().getFolder() + "</location>\r\n");
            writer.write("    <xml>" + accounting.getCounterParties().getXmlFile() + "</xml>\r\n");
            writer.write("    <html>" + accounting.getCounterParties().getHtmlFile() + "</html>\r\n");
            writer.write("  </link>\r\n");
            writer.write("  <link>\r\n");
            writer.write("    <name>Movements</name>\r\n");
            writer.write("    <location>" + accounting.getMovements().getFolder() + "</location>\r\n");
            writer.write("    <xml>" + accounting.getMovements().getXmlFile() + "</xml>\r\n");
            writer.write("    <html>" + accounting.getMovements().getHtmlFile() + "</html>\r\n");
            writer.write("  </link>\r\n");
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

    private static void toHtml(Accounting accounting){
        if(accounting.getHtmlFolder() != null){
            createHtmlFolders(accounting, accounting.getHtmlFolder());

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
