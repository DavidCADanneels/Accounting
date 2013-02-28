package be.dafke.Accounting.Dao.XML;

import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Accountings;
import be.dafke.Accounting.Objects.Accounting.Accounts;
import be.dafke.Accounting.Objects.Accounting.Balance;
import be.dafke.Accounting.Objects.Accounting.Balances;
import be.dafke.Accounting.Objects.Accounting.Booking;
import be.dafke.Accounting.Objects.Accounting.Journal;
import be.dafke.Accounting.Objects.Accounting.Journals;
import be.dafke.Accounting.Objects.Accounting.Transaction;
import be.dafke.Accounting.Objects.Coda.BankAccount;
import be.dafke.Accounting.Objects.Coda.CounterParty;
import be.dafke.Accounting.Objects.Coda.Movement;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Vector;
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
                BalancesSAXParser.readBalances(accounting, balancesFile);
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
            //writeAccounting(accounting); // TODO write extra XML file to link at Accounts, Journals, ...
            writeAccounts(accounting);
            writeBalances(accounting);
            writeJournals(accounting);
            writeMortgages(accounting);
            writeCounterparties(accounting);
            writeMovements(accounting);

            Accounts accounts = accounting.getAccounts();
            File xmlFolder = FileSystemView.getFileSystemView().getChild(accounting.getXmlFolder(), accounts.getFolder());
            if(!xmlFolder.exists()){
                xmlFolder.mkdir();
            }
            for(Account account:accounts.getAllAccounts()){
//            TODO: add isSavedXML
//            if(account.isSavedXML()){
                toXML(account);
//            }
            }

            Balances balances = accounting.getBalances();
            xmlFolder = FileSystemView.getFileSystemView().getChild(accounting.getXmlFolder(), balances.getFolder());
            if(!xmlFolder.exists()) {
                xmlFolder.mkdir();
            }
            for(Balance balance : balances.getBalances()){
                toXML(balance, balances, accounting);
            }

            Journals journals = accounting.getJournals();
            xmlFolder = FileSystemView.getFileSystemView().getChild(accounting.getXmlFolder(), journals.getFolder());
            if(!xmlFolder.exists()){
                xmlFolder.mkdir();
            }
            for(Journal journal:journals.getAllJournals()){
//            TODO: add isSavedXML
//            if(journal.isSavedXML()){
                toXML(journal);
//            }
            }

            Mortgages mortgages = accounting.getMortgages();
            xmlFolder = FileSystemView.getFileSystemView().getChild(accounting.getXmlFolder(), mortgages.getFolder());
            if(!xmlFolder.exists()){
                xmlFolder.mkdir();
            }
            for(Mortgage mortgage:mortgages.getMortgagesTables()){
//            TODO: add isSavedXML
//            if(journal.isSavedXML()){
                toXML(mortgage, mortgages, accounting);
//            }
            }
            //toHtml(accounting);
        }
    }

    private static void writeBalances(Accounting accounting) {
        System.out.println("Balances.TOXML(" + accounting.toString() + ")");
        File xmlFile = FileSystemView.getFileSystemView().getChild(accounting.getXmlFolder(), "Balances.xml");
        File xslFile = FileSystemView.getFileSystemView().getChild(accounting.getXslFolder(), "Balances.xsl");
        File dtdFile = FileSystemView.getFileSystemView().getChild(accounting.getXslFolder(), "Balances.dtd");
        try {
            Writer writer = new FileWriter(xmlFile);
            writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" + "<!DOCTYPE Balances SYSTEM \""
                    + dtdFile.getCanonicalPath() + "\">\r\n" + "<?xml-stylesheet type=\"text/xsl\" href=\""
                    + xslFile.getCanonicalPath() + "\"?>\r\n" + "<Balances>\r\n");
            writer.write("  <location>" + accounting.getBalances().getFolder() + "</location>\r\n");
            writer.write("</Balances>\r\n");
            writer.flush();
            writer.close();
//			setSaved(true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void writeAccounts(Accounting accounting) {
        System.out.println("Accounts.TOXML(" + accounting.toString() + ")");
        File xmlFile = FileSystemView.getFileSystemView().getChild(accounting.getXmlFolder(), "Accounts.xml");
        File xslFile = FileSystemView.getFileSystemView().getChild(accounting.getXslFolder(), "Accounts.xsl");
        File dtdFile = FileSystemView.getFileSystemView().getChild(accounting.getXslFolder(), "Accounts.dtd");
        try {
            Writer writer = new FileWriter(xmlFile);
            writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" + "<!DOCTYPE Accounts SYSTEM \""
                    + dtdFile.getCanonicalPath() + "\">\r\n" + "<?xml-stylesheet type=\"text/xsl\" href=\""
                    + xslFile.getCanonicalPath() + "\"?>\r\n" + "<Accounts>\r\n");
            writer.write("  <location>" + accounting.getAccounts().getFolder() + "</location>\r\n");
            for(Account account : accounting.getAccounts().getAllAccounts()) {
                writer.write("  <Account>\r\n");
                writer.write("    <account_name>" + account.getName() + "</account_name>\r\n");
                writer.write("    <account_type>" + account.getType() + "</account_type>\r\n");
                writer.write((account.getProject() == null ? "" : "      <account_project>"
                        + account.getProject() + "</account_project>\r\n"));
                writer.write("  </Account>\r\n");
            }
            writer.write("</Accounts>\r\n");
            writer.flush();
            writer.close();
//			setSaved(true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void writeJournals(Accounting accounting) {
        System.out.println("Journals.TOXML(" + accounting.toString() + ")");
        File xmlFile = FileSystemView.getFileSystemView().getChild(accounting.getXmlFolder(), "Journals.xml");
        File xslFile = FileSystemView.getFileSystemView().getChild(accounting.getXslFolder(), "Journals.xsl");
        File dtdFile = FileSystemView.getFileSystemView().getChild(accounting.getXslFolder(), "Journals.dtd");
        try {
            Writer writer = new FileWriter(xmlFile);
            writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" + "<!DOCTYPE Journals SYSTEM \""
                    + dtdFile.getCanonicalPath() + "\">\r\n" + "<?xml-stylesheet type=\"text/xsl\" href=\""
                    + xslFile.getCanonicalPath() + "\"?>\r\n" + "<Journals>\r\n");
            writer.write("  <location>" + accounting.getJournals().getFolder() + "</location>\r\n");
            for(Journal journal : accounting.getJournals().getAllJournals()) {
                writer.write("  <Journal>\r\n");
                writer.write("    <journal_name>" + journal.getName() + "</journal_name>\r\n");
                writer.write("    <journal_short>" + journal.getAbbreviation() + "</journal_short>\r\n");
                writer.write("    <journal_type>" + journal.getType().toString() + "</journal_type>\r\n");
                writer.write("  </Journal>\r\n");
            }
            writer.write("</Journals>\r\n");
            writer.flush();
            writer.close();
//			setSaved(true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void writeMovements(Accounting accounting) {
        System.out.println("Movements.TOXML(" + accounting.toString() + ")");
        File xmlFile = FileSystemView.getFileSystemView().getChild(accounting.getXmlFolder(), "Movements.xml");
        File xslFile = FileSystemView.getFileSystemView().getChild(accounting.getXslFolder(), "Movements.xsl");
        File dtdFile = FileSystemView.getFileSystemView().getChild(accounting.getXslFolder(), "Movements.dtd");
        try {
            Writer writer = new FileWriter(xmlFile);
            writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" + "<!DOCTYPE Movements SYSTEM \""
                    + dtdFile.getCanonicalPath() + "\">\r\n" + "<?xml-stylesheet type=\"text/xsl\" href=\""
                    + xslFile.getCanonicalPath() + "\"?>\r\n" + "<Movements xsl=\"" + accounting.getXslFolder() + "\">\r\n");
            writer.write("  <location>" + accounting.getMovements().getFolder() + "</location>\r\n");
            for(Movement movement : accounting.getMovements().getAllMovements()) {
                writer.write("  <Movement>\r\n");
                writer.write("    <Statement>"+movement.getStatementNr()+"</Statement>\r\n");
                writer.write("    <Sequence>"+movement.getSequenceNr()+"</Sequence>\r\n");
                writer.write("    <Date>"+Utils.toString(movement.getDate())+"</Date>\r\n");
                writer.write("    <Sign>"+(movement.isDebit()?"D":"C")+"</Sign>\r\n");
                writer.write("    <Amount>"+movement.getAmount()+"</Amount>\r\n");
                writer.write("    <CounterParty>"+movement.getCounterParty()+"</CounterParty>\r\n");
                writer.write("    <TransactionCode>" + movement.getTransactionCode() + "</TransactionCode>\r\n");
                writer.write("    <Communication>"+movement.getCommunication()+"</Communication>\r\n");
                writer.write("  </Movement>\r\n");
            }
            writer.write("</Movements>\r\n");
            writer.flush();
            writer.close();
//			setSaved(true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void writeCounterparties(Accounting accounting) {
        System.out.println("Counterparties.TOXML(" + accounting.toString() + ")");
        File xmlFile = FileSystemView.getFileSystemView().getChild(accounting.getXmlFolder(), "Counterparties.xml");
        File xslFile = FileSystemView.getFileSystemView().getChild(accounting.getXslFolder(), "Counterparties.xsl");
        File dtdFile = FileSystemView.getFileSystemView().getChild(accounting.getXslFolder(), "Counterparties.dtd");
        try {
            Writer writer = new FileWriter(xmlFile);
            writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" + "<!DOCTYPE Counterparties SYSTEM \""
                    + dtdFile.getCanonicalPath() + "\">\r\n" + "<?xml-stylesheet type=\"text/xsl\" href=\""
                    + xslFile.getCanonicalPath() + "\"?>\r\n" + "<Counterparties xsl=\"" + accounting.getXslFolder() + "\">\r\n");
            writer.write("  <location>" + accounting.getCounterParties().getFolder() + "</location>\r\n");
            for(CounterParty counterParty : accounting.getCounterParties().getCounterParties()) {
                writer.write("  <Counterparty name =\""+counterParty.getName()+"\">\r\n");
                if(counterParty.getAliases()!=null){
                    for(String alias : counterParty.getAliases()){
                        writer.write("      <Alias>"+alias+"</Alias>\r\n");
                    }
                }
                if(counterParty.getAccount()!=null){
                    writer.write("      <AccountName>" + counterParty.getAccount().getName() + "</AccountName>\r\n");
                }
                if(counterParty.getBankAccounts()!=null){
                    for(BankAccount account : counterParty.getBankAccounts().values()) {
                        if(account.getAccountNumber()!=null){
                            writer.write("      <BankAccount>" + account.getAccountNumber() + "</BankAccount>\r\n");
                        }
                        if(account.getBic()!=null){
                            writer.write("      <BIC>" + account.getBic() + "</BIC>\r\n");
                        }
                        if(account.getCurrency()!=null){
                            writer.write("      <Currency>" + account.getCurrency() + "</Currency>\r\n");
                        }
                    }
                }
                writer.write("  </Counterparty>\r\n");
            }
            writer.write("</Counterparties>\r\n");
            writer.flush();
            writer.close();
//			setSaved(true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void writeMortgages(Accounting accounting) {
        System.out.println("Mortgages.TOXML(" + accounting.toString() + ")");
        File xmlFile = FileSystemView.getFileSystemView().getChild(accounting.getXmlFolder(), "Mortgages.xml");
        File xslFile = FileSystemView.getFileSystemView().getChild(accounting.getXslFolder(), "Mortgages.xsl");
        File dtdFile = FileSystemView.getFileSystemView().getChild(accounting.getXslFolder(), "Mortgages.dtd");
        try {
            Writer writer = new FileWriter(xmlFile);
            writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" + "<!DOCTYPE Mortgages SYSTEM \""
                    + dtdFile.getCanonicalPath() + "\">\r\n" + "<?xml-stylesheet type=\"text/xsl\" href=\""
                    + xslFile.getCanonicalPath() + "\"?>\r\n" + "<Mortgages>\r\n");
            writer.write("  <location>" + accounting.getMortgages().getFolder() + "</location>\r\n");
            for(Mortgage mortgage : accounting.getMortgages().getMortgagesTables()) {
                writer.write("  <Mortgage name=\"" + mortgage.toString() + "\" total=\"" + mortgage.getStartCapital() + "\">\r\n");
                writer.write("    <nrPayed>" + mortgage.getNrPayed() + "</nrPayed>\r\n");
                writer.write("    <capital_account>" + mortgage.getCapitalAccount() + "</capital_account>\r\n");
                writer.write("    <intrest_account>" + mortgage.getIntrestAccount() + "</intrest_account>\r\n");
                writer.write("  </Mortgage>\r\n");
            }
            writer.write("</Mortgages>\r\n");
            writer.flush();
            writer.close();
//			setSaved(true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void toXML(Account account){
        try {
            Writer writer = new FileWriter(account.getXmlFile());
            writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n"
                    + "<?xml-stylesheet type=\"text/xsl\" href=\"" + account.getXslFile().getCanonicalPath() + "\"?>\r\n"
                    + "<account>\r\n" + "  <name>" + account.getName() + "</name>\r\n");
            for(Booking booking : account.getBookings()){
                writer.write("  <action>\r\n" + "    <nr>" + booking.getAbbreviation() + booking.getId() + "</nr>\r\n"
                        + "    <date>" + Utils.toString(booking.getDate()) + "</date>\r\n" + "    <"
                        + (booking.isDebit() ? "debit" : "credit") + ">" + booking.getAmount().toString() + "</"
                        + (booking.isDebit() ? "debit" : "credit") + ">\r\n" + "    <description>"
                        + booking.getDescription() + "</description>\r\n  </action>\r\n");
            }
            BigDecimal saldo = account.saldo();
            String resultType =(saldo.compareTo(BigDecimal.ZERO)<0)?"credit":"debit";
            writer.write("  <closed type = \"" + resultType + "\">\r\n" + "    <debitTotal>" + account.getDebetTotal() + "</debitTotal>\r\n"
                    + "    <creditTotal>" + account.getCreditTotal() + "</creditTotal>\r\n"
                    + "    <saldo>" + saldo.abs() + "</saldo>\r\n  </closed>\r\n");
            writer.write("</account>");
            writer.flush();
            writer.close();
//			setSaved(true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void toXML(Journal journal) {
//        xmlFile = FileSystemView.getFileSystemView().getChild(accounting.getJournalLocationXml(), name + ".xml");
        try {
            Writer writer = new FileWriter(journal.getXmlFile());
            writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n"
                    + "<?xml-stylesheet type=\"text/xsl\" href=\"" + journal.getXslFile() + "\"?>\r\n" + "<journal>\r\n"
                    + "  <name>" + journal.getName() + "</name>\r\n");
            for (Transaction transaction :journal.getTransactions()) {
                ArrayList<Booking> list = transaction.getBookings();
                Booking booking = list.get(0);
                writer.write("  <action>\r\n" + "    <nr>" + journal.getAbbreviation() + booking.getId() + "</nr>\r\n"
                        + "    <date>" + Utils.toString(booking.getDate()) + "</date>\r\n" + "    <account>"
                        + booking.getAccount() + "</account>\r\n" + "    <" + (booking.isDebit() ? "debet" : "credit")
                        + ">" + booking.getAmount().toString() + "</" + (booking.isDebit() ? "debet" : "credit")
                        + ">\r\n" + "    <description>" + booking.getDescription()
                        + "</description>\r\n  </action>\r\n");
                for(int i = 1; i < list.size(); i++) {
                    booking = list.get(i);
                    writer.write("  <action>\r\n" + "    <account>" + booking.getAccount() + "</account>\r\n" + "    <"
                            + (booking.isDebit() ? "debet" : "credit") + ">" + booking.getAmount().toString() + "</"
                            + (booking.isDebit() ? "debet" : "credit") + ">\r\n" + "  </action>\r\n");
                }
            }
            writer.write("</journal>");
            writer.flush();
            writer.close();
//			save = true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void toXML(Mortgage mortgage, Mortgages mortgages, Accounting accounting) {
        System.out.println("Mortgages.TOXML(" + mortgage.toString() + ")");
        File styleSheet = FileSystemView.getFileSystemView().getChild(accounting.getXslFolder(), "Mortgage.xsl");
        File xmlFolder = FileSystemView.getFileSystemView().getChild(accounting.getXmlFolder(), mortgages.getFolder());
        File xmlFile = FileSystemView.getFileSystemView().getChild(xmlFolder,mortgage.toString() + ".xml");
        try {
            Writer writer = new FileWriter(xmlFile);
            writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n"
                    + "<?xml-stylesheet type=\"text/xsl\" href=\"" + styleSheet.getCanonicalPath() + "\"?>\r\n"
                    + "<mortgageTable name=\"" + mortgage.toString() + "\">\r\n");
            int teller = 1;
            for(Vector<BigDecimal> vector : mortgage.getTable()) {
                writer.write("  <line>\r\n" + "    <nr>" + teller + "</nr>\r\n" + "    <mensuality>"
                        + vector.get(0) + "</mensuality>\r\n" + "    <intrest>" + vector.get(1) + "</intrest>\r\n"
                        + "    <capital>" + vector.get(2) + "</capital>\r\n" + "    <restCapital>" + vector.get(3)
                        + "</restCapital>\r\n  </line>\r\n");
                teller++;
            }
            writer.write("</mortgageTable>");
            writer.flush();
            writer.close();
            // setSaved(true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void toXML(Balance balance, Balances balances, Accounting accounting){
        System.out.println("Balances.TOXML(" + balance.toString() + ")");
        File styleSheet = FileSystemView.getFileSystemView().getChild(accounting.getXslFolder(), "Balance.xsl");
        File xmlFolder = FileSystemView.getFileSystemView().getChild(accounting.getXmlFolder(), balances.getFolder());
        File xmlFile = FileSystemView.getFileSystemView().getChild(xmlFolder,balance.getName() + ".xml");
        try {
            Writer writer = new FileWriter(xmlFile);
            writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n"
                    + "<?xml-stylesheet type=\"text/xsl\" href=\"" + styleSheet.getCanonicalPath() + "\"?>\r\n"
                    + "<balance>\r\n  <name>" + balance.getName() //$NON-NLS-1$ //$NON-NLS-2$
                    + "</name>\r\n  <left>" + balance.getLeftName() + "</left>\r\n  <right>" + balance.getRightName() + "</right>\r\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            ArrayList<Account> leftAccounts = balance.getLeftAccounts();
            ArrayList<Account> rightAccounts = balance.getRightAccounts();

            int nrLeft = leftAccounts.size();
            int nrRight = rightAccounts.size();
            int max;
            if (nrLeft > nrRight) {
                max = nrLeft;
            } else {
                max = nrRight;
            }
            for(int i = 0; i < max; i++) {
                writer.write("  <line>\r\n"); //$NON-NLS-1$
                if (i < nrLeft) {
                    writer.write("    <name1>" + leftAccounts.get(i).toString() + "</name1>\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
                    writer.write("    <amount1>" + leftAccounts.get(i).saldo() + "</amount1>\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
                } else {
                    writer.write("    <name1></name1>\r\n"); //$NON-NLS-1$
                    writer.write("    <amount1></amount1>\r\n"); //$NON-NLS-1$
                }
                if (i < nrRight) {
                    writer.write("    <amount2>" + BigDecimal.ZERO.subtract(rightAccounts.get(i).saldo()) //$NON-NLS-1$
                            + "</amount2>\r\n"); //$NON-NLS-1$
                    writer.write("    <name2>" + rightAccounts.get(i).toString() + "</name2>\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
                } else {
                    writer.write("    <amount2></amount2>\r\n"); //$NON-NLS-1$
                    writer.write("    <name2></name2>\r\n"); //$NON-NLS-1$
                }
                writer.write("  </line>\r\n"); //$NON-NLS-1$
            }
            writer.write("</balance>"); //$NON-NLS-1$
            writer.flush();
            writer.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }

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
