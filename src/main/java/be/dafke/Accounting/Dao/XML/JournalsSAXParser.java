package be.dafke.Accounting.Dao.XML;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;
import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Booking;
import be.dafke.Accounting.Objects.Accounting.Journal;
import be.dafke.Accounting.Objects.Accounting.Journals;
import be.dafke.Accounting.Objects.Accounting.Transaction;
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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Dafke
 * Date: 28/02/13
 * Time: 5:02
 */
public class JournalsSAXParser {
    // READ
    //
    public static void readJournals(Accounting accounting, File accountingFile){
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(true);
            DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(accountingFile.getAbsolutePath());
            doc.getDocumentElement().normalize();

            Node journalsNode = doc.getElementsByTagName("Journals").item(0);

            String xmlLocation = doc.getElementsByTagName("location").item(0).getChildNodes().item(0).getNodeValue();
            String xmlFile = doc.getElementsByTagName("xml").item(0).getChildNodes().item(0).getNodeValue();
            String htmlFile = doc.getElementsByTagName("html").item(0).getChildNodes().item(0).getNodeValue();
            Journals journals = accounting.getJournals();
            journals.setFolder(xmlLocation);
            journals.setXmlFile(new File(xmlFile));
            journals.setHtmlFile(new File(htmlFile));

            journalsFromXML(accounting, (Element) journalsNode);

        } catch (IOException io) {
            io.printStackTrace();
            FileSystemView.getFileSystemView().createFileObject("Journals.xml");
            System.out.println(accountingFile.getAbsolutePath() + " has been created");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //
    private static void journalsFromXML(Accounting accounting, Element journalsElement) {
        NodeList journalsNode = journalsElement.getElementsByTagName("Journal");
        for (int i = 0; i < journalsNode.getLength(); i++) {
            Element element = (Element)journalsNode.item(i);
            String xmlFile = element.getElementsByTagName("xml").item(0).getChildNodes().item(0).getNodeValue();
            String htmlFile = element.getElementsByTagName("html").item(0).getChildNodes().item(0).getNodeValue();
            String journal_name = element.getElementsByTagName("journal_name").item(0).getChildNodes().item(0).getNodeValue();
            String journal_short = element.getElementsByTagName("journal_short").item(0).getChildNodes().item(0).getNodeValue();
            String journal_type = element.getElementsByTagName("journal_type").item(0).getChildNodes().item(0).getNodeValue();
            System.out.println("Journal: "+journal_name+" | "+journal_short+" | "+journal_type);
            try{
                Journal journal = accounting.getJournals().addJournal(journal_name, journal_short, accounting.getJournalTypes().get(journal_type));
                journal.setXmlFile(new File(xmlFile));
                journal.setHtmlFile(new File(htmlFile));
            } catch (DuplicateNameException e) {
                System.err.println("There is already an journal with the name \""+journal_name+"\" and/or abbreviation \""+journal_short+"\".");
            } catch (EmptyNameException e) {
                System.err.println("Journal name and abbreviation cannot be empty.");
            }
        }
        Journals journals = accounting.getJournals();
        File xmlFolder = FileSystemView.getFileSystemView().getChild(accounting.getXmlFolder(), journals.getFolder());
        File journalFiles[] = FileSystemView.getFileSystemView().getFiles(xmlFolder, false);
        for(File journalFile : journalFiles) {
            String journalName = journalFile.getName().replaceAll(".xml", "");
            Journal journal = accounting.getJournals().get(journalName);
            accounting.setCurrentJournal(journal);
            try {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                factory.setValidating(false);
                SAXParser parser = factory.newSAXParser();
                XMLReader reader = parser.getXMLReader();
                reader.setContentHandler(new JournalContentHandler(accounting.getAccounts(), journal));
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

    // WRITE
    //
    public static void writeJournals(Journals journals) {
        try {
            Writer writer = new FileWriter(journals.getXmlFile());
            writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n" + "<!DOCTYPE Journals SYSTEM \""
                    + journals.getDtdFile().getCanonicalPath() + "\">\r\n" + "<?xml-stylesheet type=\"text/xsl\" href=\""
                    + journals.getXsl2XmlFile().getCanonicalPath() + "\"?>\r\n" + "<Journals>\r\n");
            writer.write("  <location>" + journals.getFolder() + "</location>\r\n");
            writer.write("  <xml>" + journals.getXmlFile() + "</xml>\r\n");
            writer.write("  <html>" + journals.getHtmlFile() + "</html>\r\n");
            for(Journal journal : journals.getAllJournals()) {
                writer.write("  <Journal>\r\n");
                writer.write("    <xml>" + journal.getXmlFile() + "</xml>\r\n");
                writer.write("    <html>" + journal.getHtmlFile() + "</html>\r\n");
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
        for(Journal journal:journals.getAllJournals()){
//            TODO: add isSavedXML
//            if(journal.isSavedXML()){
            toXML(journal);
//            }
        }
    }
    //
    private static void toXML(Journal journal) {
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
}
