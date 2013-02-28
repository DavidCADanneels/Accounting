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

    private static File getFile() {
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

    public static Accountings readAccountings() {
        Accountings accountings = new Accountings();
        System.out.println("fromXML");
        File file = getFile();
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
        Accounting currentAccounting = accountings.getCurrentAccounting();
        try {
            Writer writer = new FileWriter(getFile());
            writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n"
                    + "<!DOCTYPE Accountings SYSTEM \"C:\\Users\\Dafke\\Accounting\\xsl\\Accountings.dtd\">\r\n"
                    + "<?xml-stylesheet type=\"text/xsl\" href=\"Accountings.xsl\"?>\r\n" + "<Accountings>\r\n");
            for(Accounting acc : accountings.getAccountings()) {
                writer.write("  <Accounting name=\"" + acc.toString()
                        + "\" current=\"" + (acc == currentAccounting?"true":"false")
                        + "\" xml=\"" + acc.getXmlFolder()
                        + "\" xsl=\"" + acc.getXslFolder()
                        + "\" html=\"" + acc.getHtmlFolder()
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
        if(htmlFile == null){
            htmlFile = FileSystemView.getFileSystemView().getChild(rootFolder, "Accounting.html");
            accounting.setHtmlFile(htmlFile);
        }

        // ACCOUNTS
        //
        Accounts accounts = accounting.getAccounts();
        htmlFile = accounts.getHtmlFile();
        if(htmlFile == null){
            htmlFile = FileSystemView.getFileSystemView().getChild(rootFolder, "Accounts.html");
            accounts.setHtmlFile(htmlFile);
        }
        File htmlFolder = FileSystemView.getFileSystemView().getChild(rootFolder, accounts.getFolder());
        htmlFolder.mkdirs();

        // JOURNALS
        //
        Journals journals = accounting.getJournals();
        htmlFile = journals.getHtmlFile();
        if(htmlFile == null){
            htmlFile = FileSystemView.getFileSystemView().getChild(rootFolder, "Journals.html");
            journals.setHtmlFile(htmlFile);
        }
        htmlFolder = FileSystemView.getFileSystemView().getChild(rootFolder, journals.getFolder());
        htmlFolder.mkdirs();

        // BALANCES
        //
        Balances balances = accounting.getBalances();
        htmlFile = balances.getHtmlFile();
        if(htmlFile == null){
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
        if(htmlFile == null){
            htmlFile = FileSystemView.getFileSystemView().getChild(rootFolder, "Mortgages.html");
            mortgages.setHtmlFile(htmlFile);
        }
        htmlFolder = FileSystemView.getFileSystemView().getChild(rootFolder, mortgages.getFolder());
        htmlFolder.mkdirs();

        // MOVEMENTS
        //
        Movements movements = accounting.getMovements();
        htmlFile = movements.getHtmlFile();
        if(htmlFile == null){
            htmlFile = FileSystemView.getFileSystemView().getChild(rootFolder, "Movements.html");
            movements.setHtmlFile(htmlFile);
        }
        htmlFolder = FileSystemView.getFileSystemView().getChild(rootFolder, movements.getFolder());
        htmlFolder.mkdirs();

        // COUNTERPARTIES
        //
        CounterParties counterParties = accounting.getCounterParties();
        htmlFile = counterParties.getHtmlFile();
        if(htmlFile == null){
            htmlFile = FileSystemView.getFileSystemView().getChild(rootFolder, "CounterParties.html");
            counterParties.setHtmlFile(htmlFile);
        }
        htmlFolder = FileSystemView.getFileSystemView().getChild(rootFolder, counterParties.getFolder());
        htmlFolder.mkdirs();
    }

    public static void createXMLFolders(Accounting accounting) {
        File home = new File(System.getProperty("user.home"));
        File accountingFolder = FileSystemView.getFileSystemView().getChild(home, "Accounting");

        // ACCOUNTING
        //
        File xmlFolder = accounting.getXmlFolder();
        if(xmlFolder == null){
            xmlFolder = FileSystemView.getFileSystemView().getChild(accountingFolder, accounting.getName());
            accounting.setXmlFolder(xmlFolder);
        }
        File xmlFile = accounting.getXmlFile();
        if(xmlFile == null){
            xmlFile = FileSystemView.getFileSystemView().getChild(xmlFolder, "Accounting.xml");
            accounting.setXmlFile(xmlFile);
        }
        File xslFolder = accounting.getXslFolder();
        if(xslFolder == null){
            xslFolder = FileSystemView.getFileSystemView().getChild(accountingFolder, "xsl");
            accounting.setXslFolder(xslFolder);
        }
        File xslFile = accounting.getXslFile();
        if(xslFile == null){
            xslFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Accounting.xsl");
            accounting.setXslFile(xslFile);
        }
        File dtdFile = accounting.getDtdFile();
        if(dtdFile == null){
            dtdFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Accounting.dtd");
            accounting.setDtdFile(dtdFile);
        }

        // ACCOUNTS
        //
        Accounts accounts = accounting.getAccounts();
        String folder = accounts.getFolder();
        if(folder == null){
            folder = "Accounts";
            accounts.setFolder(folder);
        }
        xmlFile = accounts.getXmlFile();
        if(xmlFile == null){
            xmlFile = FileSystemView.getFileSystemView().getChild(xmlFolder, "Accounts.xml");
            accounts.setXmlFile(xmlFile);
        }
        xslFile = accounts.getXslFile();
        if(xslFile == null){
            xslFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Accounts.xsl");
            accounts.setXslFile(xslFile);
        }
        dtdFile = accounts.getDtdFile();
        if(dtdFile == null){
            dtdFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Accounts.dtd");
            accounts.setDtdFile(dtdFile);
        }

        // JOURNALS
        //
        Journals journals = accounting.getJournals();
        folder = journals.getFolder();
        if(folder == null){
            folder = "Journals";
            journals.setFolder(folder);
        }
        xmlFile = journals.getXmlFile();
        if(xmlFile == null){
            xmlFile = FileSystemView.getFileSystemView().getChild(xmlFolder, "Journals.xml");
            journals.setXmlFile(xmlFile);
        }
        xslFile = journals.getXslFile();
        if(xslFile == null){
            xslFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Journals.xsl");
            journals.setXslFile(xslFile);
        }
        dtdFile = journals.getDtdFile();
        if(dtdFile == null){
            dtdFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Journals.dtd");
            journals.setDtdFile(dtdFile);
        }

        // BALANCES
        //
        Balances balances = accounting.getBalances();
        folder = balances.getFolder();
        if(folder == null){
            folder = "Balances";
            balances.setFolder(folder);
        }
        xmlFile = balances.getXmlFile();
        if(xmlFile == null){
            xmlFile = FileSystemView.getFileSystemView().getChild(xmlFolder, "Balances.xml");
            balances.setXmlFile(xmlFile);
        }
        xslFile = balances.getXslFile();
        if(xslFile == null){
            xslFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Balances.xsl");
            balances.setXslFile(xslFile);
        }
        dtdFile = balances.getDtdFile();
        if(dtdFile == null){
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
        if(folder == null){
            folder = "Mortgages";
            mortgages.setFolder(folder);
        }
        xmlFile = mortgages.getXmlFile();
        if(xmlFile == null){
            xmlFile = FileSystemView.getFileSystemView().getChild(xmlFolder, "Mortgages.xml");
            mortgages.setXmlFile(xmlFile);
        }
        xslFile = mortgages.getXslFile();
        if(xslFile == null){
            xslFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Mortgages.xsl");
            mortgages.setXslFile(xslFile);
        }
        dtdFile = mortgages.getDtdFile();
        if(dtdFile == null){
            dtdFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Mortgages.dtd");
            mortgages.setDtdFile(dtdFile);
        }

        // MOVEMENTS
        //
        Movements movements = accounting.getMovements();
        folder = movements.getFolder();
        if(folder == null){
            folder = "Movements";
            movements.setFolder(folder);
        }
        xmlFile = movements.getXmlFile();
        if(xmlFile == null){
            xmlFile = FileSystemView.getFileSystemView().getChild(xmlFolder, "Movements.xml");
            movements.setXmlFile(xmlFile);
        }
        xslFile = movements.getXslFile();
        if(xslFile == null){
            xslFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Movements.xsl");
            movements.setXslFile(xslFile);
        }
        dtdFile = movements.getDtdFile();
        if(dtdFile == null){
            dtdFile = FileSystemView.getFileSystemView().getChild(xslFolder, "Movements.dtd");
            movements.setDtdFile(dtdFile);
        }

        // COUNTERPARTIES
        //
        CounterParties counterParties = accounting.getCounterParties();
        folder = counterParties.getFolder();
        if(folder == null){
            folder = "CounterParties";
            counterParties.setFolder(folder);
        }
        xmlFile = counterParties.getXmlFile();
        if(xmlFile == null){
            xmlFile = FileSystemView.getFileSystemView().getChild(xmlFolder, "CounterParties.xml");
            counterParties.setXmlFile(xmlFile);
        }
        xslFile = counterParties.getXslFile();
        if(xslFile == null){
            xslFile = FileSystemView.getFileSystemView().getChild(xslFolder, "CounterParties.xsl");
            counterParties.setXslFile(xslFile);
        }
        dtdFile = counterParties.getDtdFile();
        if(dtdFile == null){
            dtdFile = FileSystemView.getFileSystemView().getChild(xslFolder, "CounterParties.dtd");
            counterParties.setDtdFile(dtdFile);
        }
    }

    private static void writeAccounting(Accounting accounting){
        createXMLFolders(accounting);

        writeAccountingFile(accounting); // TODO write extra XML file to link at Accounts, Journals, ...

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
                    + accounting.getDtdFile().getCanonicalPath() + "\">\r\n" + "<?xml-stylesheet type=\"text/xsl\" href=\""
                    + accounting.getXslFile().getCanonicalPath() + "\"?>\r\n" + "<Accounting>\r\n");
            writer.write("  <name>" + accounting.getName() + "</name>\r\n");
            writer.write("  <link>\r\n");
            writer.write("    <name>Accounts</name>\r\n");
            writer.write("    <location>" + accounting.getAccounts().getFolder() + "</location>\r\n");
            writer.write("  </link>\r\n");
            writer.write("  <link>\r\n");
            writer.write("    <name>Journals</name>\r\n");
            writer.write("    <location>" + accounting.getJournals().getFolder() + "</location>\r\n");
            writer.write("  </link>\r\n");
            writer.write("  <link>\r\n");
            writer.write("    <name>Balances</name>\r\n");
            writer.write("    <location>" + accounting.getBalances().getFolder() + "</location>\r\n");
            writer.write("  </link>\r\n");
            writer.write("  <link>\r\n");
            writer.write("    <name>Mortgages</name>\r\n");
            writer.write("    <location>" + accounting.getMortgages().getFolder() + "</location>\r\n");
            writer.write("  </link>\r\n");
            writer.write("  <link>\r\n");
            writer.write("    <name>CounterParties</name>\r\n");
            writer.write("    <location>" + accounting.getCounterParties().getFolder() + "</location>\r\n");
            writer.write("  </link>\r\n");
            writer.write("  <link>\r\n");
            writer.write("    <name>Movements</name>\r\n");
            writer.write("    <location>" + accounting.getMovements().getFolder() + "</location>\r\n");
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
            Utils.xmlToHtml(accounting.getXmlFile(), accounting.getXslFile(), accounting.getHtmlFile(), null);
            Utils.xmlToHtml(accounts.getXmlFile(), accounts.getXslFile(), accounts.getHtmlFile(), null);
            Utils.xmlToHtml(journals.getXmlFile(), journals.getXslFile(), journals.getHtmlFile(), null);
            Utils.xmlToHtml(balances.getXmlFile(), balances.getXslFile(), balances.getHtmlFile(), null);
            Utils.xmlToHtml(mortgages.getXmlFile(), mortgages.getXslFile(), mortgages.getHtmlFile(), null);
            Utils.xmlToHtml(movements.getXmlFile(), movements.getXslFile(), movements.getHtmlFile(), null);
            Utils.xmlToHtml(counterParties.getXmlFile(), counterParties.getXslFile(), counterParties.getHtmlFile(), null);
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
