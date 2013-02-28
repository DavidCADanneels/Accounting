package be.dafke.Accounting.Dao.XML;

import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Accountings;
import be.dafke.Accounting.Objects.Accounting.Accounts;
import be.dafke.Accounting.Objects.Accounting.Balances;
import be.dafke.Accounting.Objects.Accounting.Journal;
import be.dafke.Accounting.Objects.Accounting.Journals;
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

    private static void writeAccounting(Accounting accounting){
        //writeAccounting(accounting); // TODO write extra XML file to link at Accounts, Journals, ...

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

        //toHtml(accounting);
    }

    private static void toHtml(Accounting accounting){
        if(accounting.getHtmlFolder() == null){
            accounting.createHTMLFolders();
        }
        Utils.xmlToHtml(accounting.getXMLFile(), accounting.getXSLFile(), accounting.getHTMLFile(), null);
        for(Account account:accounting.getAccounts().values()){
//            TODO: add isSavedHTML
//            if(account.isSavedHTML()){
            Utils.xmlToHtml(account.getXmlFile(), account.getXslFile(), account.getHtmlFile(), null);
//            }
        }
        for(Journal journal:accounting.getJournals().values()){
//            TODO: add isSavedHTML
//            if(journal.isSavedHTML()){
            Utils.xmlToHtml(journal.getXmlFile(), journal.getXslFile(), journal.getHtmlFile(), null);
//            }
        }
    }
}
