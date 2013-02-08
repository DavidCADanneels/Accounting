package be.dafke.Accounting.Dao.XML;

import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Accountings;
import be.dafke.Accounting.Objects.Accounting.Booking;
import be.dafke.Accounting.Objects.Accounting.Journal;
import be.dafke.Accounting.Objects.Accounting.Project;
import be.dafke.Accounting.Objects.Accounting.Transaction;
import be.dafke.Accounting.Objects.Coda.BankAccount;
import be.dafke.Accounting.Objects.Coda.CounterParty;
import be.dafke.Accounting.Objects.Coda.Movement;
import be.dafke.Accounting.Objects.Mortgage.Mortgage;
import be.dafke.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.XMLReader;

import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Dafke
 * Date: 20/01/13
 * Time: 10:22
 */
public class AccountingSAXParser {

    private static File getFile() {
        String folderName = "Accounting";
        String fileName = "Accountings.xml";
        File home = // FileSystemView.getFileSystemView().getDefaultDirectory();
                new File(System.getProperty("user.home"));
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

    public static Accountings fromXML() {
        Accountings accountings = new Accountings();
        System.out.println("fromXML");
        File file = getFile();
        String pad = file.getAbsolutePath();
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
                reader.setContentHandler(new AccountingsContentHandler(file, accountings));
                reader.setErrorHandler(new FoutHandler());
                reader.parse(pad);
            } catch (IOException io) {
                // FileSystemView.getFileSystemView().createFileObject(home, "TESTFILE");
                // System.out.println(pad + " has been created");
            } catch (Exception e) {
                e.printStackTrace();
            }
//			Accounting tmpCurrent = currentAccounting;
            for(Accounting accounting : accountings.getAccountings()) {
//				currentAccounting = accounting;
                String name = accounting.toString();
                File subFolder = FileSystemView.getFileSystemView().getChild(file.getParentFile(), name);
                if (!subFolder.isDirectory()) {
                    System.err.println(name + " not found or no directory");
                } else {
                    // Accountings.openObject(subFile);
                    File subFile = FileSystemView.getFileSystemView().getChild(subFolder, "Accounting.xml");
                    if (!subFile.exists()) {
                        System.err.println("no XML file found in " + name);
                        return accountings;
                    } else {
                        try {
                            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                            DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();
                            Document doc = dBuilder.parse(subFile.getAbsolutePath());
                            doc.getDocumentElement().normalize();
                            Node accountsNode = doc.getElementsByTagName("Accounts").item(0);
                            Node journalsNode = doc.getElementsByTagName("Journals").item(0);
                            Node balancesNode = doc.getElementsByTagName("Balances").item(0);
                            Node mortgagesNode = doc.getElementsByTagName("Mortgages").item(0);
                            Node counterpartiesNode = doc.getElementsByTagName("Counterparties").item(0);
                            Node movementsNode = doc.getElementsByTagName("Movements").item(0);

                            if(accountsNode!=null){
                                String htmlLocation = accountsNode.getAttributes().getNamedItem("html").getNodeValue();
                                accounting.setAccountLocationHtml(new File(htmlLocation));
                                String xmlLocation = accountsNode.getAttributes().getNamedItem("xml").getNodeValue();
                                accounting.setAccountLocationXml(new File(xmlLocation));
                            }
                            if(journalsNode!=null){
                                String htmlLocation = journalsNode.getAttributes().getNamedItem("html").getNodeValue();
                                accounting.setJournalLocationHtml(new File(htmlLocation));
                                String xmlLocation = journalsNode.getAttributes().getNamedItem("xml").getNodeValue();
                                accounting.setJournalLocationXml(new File(xmlLocation));
                            }
                            if(balancesNode!=null){
                                String htmlLocation = balancesNode.getAttributes().getNamedItem("html").getNodeValue();
                                accounting.setBalanceLocationHtml(new File(htmlLocation));
                                String xmlLocation = balancesNode.getAttributes().getNamedItem("xml").getNodeValue();
                                accounting.setBalanceLocationXml(new File(xmlLocation));
                            }
                            if(mortgagesNode!=null){
                                String htmlLocation = mortgagesNode.getAttributes().getNamedItem("html").getNodeValue();
                                accounting.setMortgageLocationHtml(new File(htmlLocation));
                                String xmlLocation = mortgagesNode.getAttributes().getNamedItem("xml").getNodeValue();
                                accounting.setMortgageLocationXml(new File(xmlLocation));
                            }
                            if(counterpartiesNode!=null){
                                String htmlLocation = counterpartiesNode.getAttributes().getNamedItem("html").getNodeValue();
                                accounting.setCounterpartyLocationHtml(new File(htmlLocation));
                                String xmlLocation = counterpartiesNode.getAttributes().getNamedItem("xml").getNodeValue();
                                accounting.setCounterpartyLocationXml(new File(xmlLocation));
                            }
                            if(movementsNode!=null){
                                String htmlLocation = movementsNode.getAttributes().getNamedItem("html").getNodeValue();
                                accounting.setMovementLocationHtml(new File(htmlLocation));
                                String xmlLocation = movementsNode.getAttributes().getNamedItem("xml").getNodeValue();
                                accounting.setMovementLocationXml(new File(xmlLocation));
                            }

                            // Handle Accounts
                            NodeList accounts = ((Element)accountsNode).getElementsByTagName("Account");
                            for (int i = 0; i < accounts.getLength(); i++) {
                                Element element = (Element)accounts.item(i);
                                String account_name = element.getElementsByTagName("account_name").item(0).getChildNodes().item(0).getNodeValue();
                                String account_type = element.getElementsByTagName("account_type").item(0).getChildNodes().item(0).getNodeValue();
                                System.out.println("Account: "+account_name+" | "+account_type);

                                Account.AccountType type = Account.AccountType.valueOf(account_type);
                                Account account = new Account(account_name, type);
                                account.setAccounting(accounting);
                                accounting.getAccounts().add(account);

                                NodeList projectNodeList = element.getElementsByTagName("account_project");
                                if(projectNodeList.getLength()>0){
                                    String account_project = projectNodeList.item(0).getChildNodes().item(0).getNodeValue();
                                    Project project = accounting.getProjects().get(account_project);
                                    if (project == null) {
                                        project = new Project(account_project);
                                        accounting.getProjects().put(account_project, project);
                                    }
                                    project.addAccount(account);
                                }
                            }

                            SAXParserFactory factory = SAXParserFactory.newInstance();
                            factory.setValidating(true);
                            SAXParser parser = factory.newSAXParser();
                            XMLReader reader = parser.getXMLReader();
                            reader.setContentHandler(new AccountingContentHandler(accounting));
                            reader.setErrorHandler(new FoutHandler());
                            reader.parse(subFile.getAbsolutePath());
                        } catch (IOException io) {
                            io.printStackTrace();
                            FileSystemView.getFileSystemView().createFileObject(subFolder, "Accounting.xml");
                            System.out.println(subFile.getAbsolutePath() + " has been created");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    handleJournals(accounting);
                    handleMortgages(accounting);
                }
            }
//			currentAccounting = tmpCurrent;
        }
        return accountings;
    }

    private static void handleJournals(Accounting accounting) {
        File journalFiles[] = FileSystemView.getFileSystemView().getFiles(accounting.getJournalLocationXml(), false);
        for(File journalFile : journalFiles) {
            String journalName = journalFile.getName().replaceAll(".xml", "");
            Journal journal = accounting.getJournals().get(journalName);
            accounting.setCurrentJournal(journal);
            try {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                factory.setValidating(false);
                SAXParser parser = factory.newSAXParser();
                XMLReader reader = parser.getXMLReader();
                reader.setContentHandler(new JournalContentHandler(accounting, journal));
                reader.setErrorHandler(new FoutHandler());
                reader.parse(journalFile.getAbsolutePath());
            } catch (IOException io) {
//				FileSystemView.getFileSystemView().createFileObject(subFolder, "Accounting.xml");
//				System.out.println(journalFile + " has been created");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void handleMortgages(Accounting accounting) {
        File mortgagesFiles[] = FileSystemView.getFileSystemView().getFiles(accounting.getMortgageLocationXml(), false);
        for(File mortgagesFile : mortgagesFiles) {
            String mortgageName = mortgagesFile.getName().replaceAll(".xml", "");
            Mortgage mortgage = accounting.getMortgage(mortgageName);
            try {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                factory.setValidating(false);
                SAXParser parser = factory.newSAXParser();
                XMLReader reader = parser.getXMLReader();
                reader.setContentHandler(new MortgageContentHandler(mortgage));
                reader.setErrorHandler(new FoutHandler());
                reader.parse(mortgagesFile.getAbsolutePath());
            } catch (IOException io) {
//				FileSystemView.getFileSystemView().createFileObject(subFolder, "Accounting.xml");
//				System.out.println(journalFile + " has been created");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void toXML(Accountings accountings) {
        Accounting currentAccounting = accountings.getCurrentAccounting();
        if (currentAccounting != null) {
            try {
                Writer writer = new FileWriter(getFile());
                writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n"
                        + "<!DOCTYPE Accountings SYSTEM \"C:\\Users\\Dafke\\Accounting\\xsl\\Accountings.dtd\">\r\n"
                        + "<?xml-stylesheet type=\"text/xsl\" href=\"Accountings.xsl\"?>\r\n" + "<Accountings>\r\n");
                for(Accounting acc : accountings.getAccountings()) {
                    writer.write("  <Accounting name=\"" + acc.toString() + "\" xml=\"" + acc.getLocationXml()
                            + "\" html=\"" + acc.getLocationHtml() + "\"/>\r\n");
                }
                writer.write("  <CurrentAccounting name=\"" + currentAccounting.toString() + "\" xml=\""
                        + currentAccounting.getLocationXml() + "\" html=\"" + currentAccounting.getLocationHtml()
                        + "\"/>\r\n");
                writer.write("</Accountings>");
                writer.flush();
                writer.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for(Accounting accounting : accountings.getAccountings()) {
            toXML(accounting);
        }
    }

    private static void toXML(Accounting accounting) {
        System.out.println("Accounting.TOXML()");
        File xmlFile = FileSystemView.getFileSystemView().getChild(accounting.getLocationXml(), "Accounting.xml");
        File xslFile = FileSystemView.getFileSystemView().getChild(accounting.getLocationXSL(), "Accounting.xsl");
        File dtdFile = FileSystemView.getFileSystemView().getChild(accounting.getLocationXSL(), "Accounting.dtd");
        try {
            Writer writer = new FileWriter(xmlFile);
            writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" + "<!DOCTYPE Accounting SYSTEM \""
                    + dtdFile.getCanonicalPath() + "\">\r\n" + "<?xml-stylesheet type=\"text/xsl\" href=\""
                    + xslFile.getCanonicalPath() + "\"?>\r\n" + "<Accounting xsl=\"" + accounting.getLocationXSL() + "\">\r\n");
            writer.write("  <Accounts xml=\"" + accounting.getAccountLocationXml() + "\" html=\"" + accounting.getAccountLocationHtml()
                    + "\">\r\n");
            for(Account account : accounting.getAccounts().getAccounts()) {
                writer.write("    <Account>\r\n"
                        + "      <account_name>"
                        + account.getName()
                        + "</account_name>\r\n"
                        + "      <account_type>"
                        + account.getType()
                        + "</account_type>\r\n"
                        + (account.getProject() == null ? "" : "      <account_project>" + account.getProject()
                        + "</account_project>\r\n") + "    </Account>\r\n");
            }
            writer.write("  </Accounts>\r\n");
            writer.write("  <Journals xml=\"" + accounting.getJournalLocationXml() + "\" html=\"" + accounting.getJournalLocationHtml()
                    + "\">\r\n");
            for(Journal journal : accounting.getJournals().getAllJournals()) {
                writer.write("    <Journal>\r\n" + "      <journal_name>" + journal.getName() + "</journal_name>\r\n"
                        + "      <journal_short>" + journal.getAbbreviation() + "</journal_short>\r\n"
                        + "    </Journal>\r\n");
            }
            writer.write("  </Journals>\r\n");
            writer.write("  <Balances xml=\"" + accounting.getBalanceLocationXml() + "\" html=\"" + accounting.getBalanceLocationHtml()
                    + "\">\r\n");
            writer.write("  </Balances>\r\n");
            writer.write("  <Mortgages xml=\"" + accounting.getMortgageLocationXml() + "\" html=\"" + accounting.getMortgageLocationHtml()
                    + "\">\r\n");
            for(Mortgage mortgage : accounting.getMortgagesTables()) {
                writer.write("    <Mortgage name=\"" + mortgage.toString() + "\" total=\"" + mortgage.getStartCapital()
                        + "\">\r\n" + "      <nrPayed>" + mortgage.getNrPayed() + "</nrPayed>\r\n"
                        + "      <capital_account>" + mortgage.getCapitalAccount() + "</capital_account>\r\n"
                        + "      <intrest_account>" + mortgage.getIntrestAccount() + "</intrest_account>\r\n"
                        + "    </Mortgage>\r\n");
            }
            writer.write("  </Mortgages>\r\n");
            writer.write("  <Movements xml=\"" + accounting.getMovementLocationXml() + "\" html=\"" + accounting.getMovementLocationHtml()
                    + "\">\r\n");
            for(Movement movement : accounting.getMovements().getAllMovements()) {

            }
            writer.write("  </Movements>\r\n");
            writer.write("  <Counterparties xml=\"" + accounting.getCounterPartyLocationXml() + "\" html=\""
                    + accounting.getCounterPartyLocationHtml() + "\">\r\n");
            for(CounterParty counterParty : accounting.getCounterParties().getCounterParties()) {
                writer.write("    <Counterparty name =\""+counterParty.getName()+"\">\r\n");
                writer.write("      <AccountName>");
                if(counterParty.getAccount()!=null){
                    writer.write(counterParty.getAccount().getName());
                }
                writer.write("</AccountName>\r\n");
                if(counterParty.getBankAccounts()!=null){
                    for(BankAccount account : counterParty.getBankAccounts().values()) {
                        writer.write("      <BankAccount>" + account.getAccountNumber() + "</BankAccount>\r\n");
                        writer.write("      <BIC>" + account.getBic().trim() + "</BIC>\r\n");
                        writer.write("      <Currency>" + account.getCurrency() + "</Currency>\r\n");
                    }
                }
                writer.write("    </Counterparty>\r\n");
            }
            writer.write("  </Counterparties>\r\n");
            writer.write("</Accounting>\r\n");
            writer.flush();
            writer.close();
//			setSaved(true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
        writeMortgages(accounting);

        for(Account account:accounting.getAccounts().values()){
//            TODO: add isSavedXML
//            if(account.isSavedXML()){
            toXML(account);
//            }
        }
        for(Journal journal:accounting.getJournals().values()){
//            TODO: add isSavedXML
//            if(journal.isSavedXML()){
            toXML(journal);
//            }
        }
        toHtml(accounting);
    }

    private static void writeMortgages(Accounting accounting) {
        for(Mortgage mortgage : accounting.getMortgagesTables()) {
            File styleSheet = FileSystemView.getFileSystemView().getChild(accounting.getLocationXSL(), "Mortgage.xsl");
            File xmlFile = FileSystemView.getFileSystemView().getChild(accounting.getMortgageLocationXml(),
                    mortgage.toString() + ".xml");
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
    }

    private static void toXML(Account account){
        try {
            Writer writer = new FileWriter(account.getXmlFile());
            writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n"
                    + "<?xml-stylesheet type=\"text/xsl\" href=\"" + account.getXslFile().getCanonicalPath() + "\"?>\r\n"
                    + "<account>\r\n" + "  <name>" + account.getName() + "</name>\r\n");
            Iterator<Booking> it = account.getBookings().iterator();
            while (it.hasNext()) {
                Booking booking = it.next();
                writer.write("  <action>\r\n" + "    <nr>" + booking.getAbbreviation() + booking.getId() + "</nr>\r\n"
                        + "    <date>" + Utils.toString(booking.getDate()) + "</date>\r\n" + "    <"
                        + (booking.isDebit() ? "debet" : "credit") + ">" + booking.getAmount().toString() + "</"
                        + (booking.isDebit() ? "debet" : "credit") + ">\r\n" + "    <description>"
                        + booking.getDescription() + "</description>\r\n  </action>\r\n");
            }
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


    private static void toHtml(Accounting accounting){
        if(accounting.getLocationHtml() == null){
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
