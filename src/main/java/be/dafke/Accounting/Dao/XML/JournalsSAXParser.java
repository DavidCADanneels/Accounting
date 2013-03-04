package be.dafke.Accounting.Dao.XML;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;
import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Accounts;
import be.dafke.Accounting.Objects.Accounting.Booking;
import be.dafke.Accounting.Objects.Accounting.Journal;
import be.dafke.Accounting.Objects.Accounting.JournalTypes;
import be.dafke.Accounting.Objects.Accounting.Journals;
import be.dafke.Accounting.Objects.Accounting.Transaction;
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
    public static void readJournals(Journals journals, JournalTypes journalTypes, Accounts accounts){
        try {
            File file = journals.getXmlFile();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(true);
            DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file.getAbsolutePath());
            doc.getDocumentElement().normalize();

            Element rootElement = (Element) doc.getElementsByTagName("Journals").item(0);
            NodeList nodeList = rootElement.getElementsByTagName("Journal");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element)nodeList.item(i);
                String xmlFile = element.getElementsByTagName("xml").item(0).getChildNodes().item(0).getNodeValue();
                String htmlFile = element.getElementsByTagName("html").item(0).getChildNodes().item(0).getNodeValue();
                String journal_name = element.getElementsByTagName("journal_name").item(0).getChildNodes().item(0).getNodeValue();
                String journal_short = element.getElementsByTagName("journal_short").item(0).getChildNodes().item(0).getNodeValue();
                String journal_type = element.getElementsByTagName("journal_type").item(0).getChildNodes().item(0).getNodeValue();
                System.out.println("Journal: "+journal_name+" | "+journal_short+" | "+journal_type);
                try{
                    Journal journal = journals.addJournal(journal_name, journal_short, journalTypes.get(journal_type));
                    journal.setXmlFile(new File(xmlFile));
                    journal.setHtmlFile(new File(htmlFile));
                    readJournal(journal, accounts);
                } catch (DuplicateNameException e) {
                    System.err.println("There is already an journal with the name \""+journal_name+"\" and/or abbreviation \""+journal_short+"\".");
                } catch (EmptyNameException e) {
                    System.err.println("Journal name and abbreviation cannot be empty.");
                }
            }
        } catch (IOException io) {
            io.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //
    private static void readJournal(Journal journal, Accounts accounts) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(true);
            DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(journal.getXmlFile().getAbsolutePath());
            doc.getDocumentElement().normalize();

//            String name = doc.getElementsByTagName("name").item(0).getChildNodes().item(0).getNodeValue();

            Transaction transaction = null;
            String abbreviation = journal.getAbbreviation();

            NodeList actions = doc.getElementsByTagName("action");
            for (int i = 0; i < actions.getLength(); i++) {
                Element element = (Element)actions.item(i);
                NodeList nodeListNr = element.getElementsByTagName("nr");
                NodeList nodeListDate = element.getElementsByTagName("date");
                NodeList nodeListDescription = element.getElementsByTagName("description");
                if(nodeListNr.getLength()!=0 && nodeListDate.getLength()!=0 && nodeListDescription.getLength()!=0){
                    if(transaction != null){
                        transaction.book(journal);
                    }
                    transaction = new Transaction();
                    transaction.setAbbreviation(abbreviation);

                    String nr = nodeListNr.item(0).getChildNodes().item(0).getNodeValue();
                    transaction.setId(Utils.parseInt(nr.replaceAll(abbreviation,"")));

                    String date = nodeListDate.item(0).getChildNodes().item(0).getNodeValue();
                    transaction.setDate(Utils.toCalendar(date));

                    if(nodeListDescription.item(0).getChildNodes().getLength()>0){
                        String description = nodeListDescription.item(0).getChildNodes().item(0).getNodeValue();
                        transaction.setDescription(description);
                    }
                }
                String accountName = element.getElementsByTagName("account_name").item(0).getChildNodes().item(0).getNodeValue();
                Account account = accounts.get(accountName);
                NodeList nodeListDebet = element.getElementsByTagName("debet");
                NodeList nodeListCredit = element.getElementsByTagName("credit");
                if(nodeListDebet.getLength()!=0){
                    String debet = nodeListDebet.item(0).getChildNodes().item(0).getNodeValue();
                    transaction.addBooking(account, Utils.parseBigDecimal(debet),true, false);
                }
                if(nodeListCredit.getLength()!=0){
                    String credit = nodeListCredit.item(0).getChildNodes().item(0).getNodeValue();
                    transaction.addBooking(account, Utils.parseBigDecimal(credit),false, false);
                }
            }
            if(transaction!=null){
                transaction.book(journal);
            }
        } catch (IOException io) {
//				FileSystemView.getFileSystemView().createFileObject(subFolder, "Accounting.xml");
//				System.out.println(journalFile + " has been created");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // WRITE
    //
    public static void writeJournals(Journals journals) {
        try {
            Writer writer = new FileWriter(journals.getXmlFile());

            writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n");
            writer.write("<!DOCTYPE Journals SYSTEM \"" + journals.getDtdFile().getCanonicalPath() + "\">\r\n");
            writer.write("<?xml-stylesheet type=\"text/xsl\" href=\"" + journals.getXsl2XmlFile().getCanonicalPath() + "\"?>\r\n");

            writer.write("<Journals>\r\n");
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
            writeJournal(journal);
//            }
        }
    }
    //
    private static void writeJournal(Journal journal) {
        try {
            Writer writer = new FileWriter(journal.getXmlFile());

            writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n");
            writer.write("<!DOCTYPE Journal SYSTEM \"" + journal.getDtdFile().getCanonicalPath() + "\">\r\n");
            writer.write("<?xml-stylesheet type=\"text/xsl\" href=\"" + journal.getXsl2XmlFile().getCanonicalPath() + "\"?>\r\n");

            writer.write("<Journal>\r\n"
                    + "  <name>" + journal.getName() + "</name>\r\n");
            for (Transaction transaction :journal.getTransactions()) {
                ArrayList<Booking> list = transaction.getBookings();
                Booking booking = list.get(0);
                writer.write("  <action id=\""+booking.getId()+"\">\r\n");
                writer.write("    <nr>" + journal.getAbbreviation() + booking.getId() + "</nr>\r\n");
                writer.write("    <date>" + Utils.toString(booking.getDate()) + "</date>\r\n");
                writer.write("    <account_name>" + booking.getAccount() + "</account_name>\r\n");
                writer.write("    <account_xml>" + booking.getAccount().getXmlFile() + "</account_xml>\r\n");
                writer.write("    <account_html>" + booking.getAccount().getHtmlFile() + "</account_html>\r\n");
                writer.write("    <" + (booking.isDebit() ? "debet" : "credit") + ">"
                                     + booking.getAmount().toString()
                               + "</" + (booking.isDebit() ? "debet" : "credit") + ">\r\n");
                writer.write("    <description>" + booking.getDescription() + "</description>\r\n");
                writer.write("  </action>\r\n");
                for(int i = 1; i < list.size(); i++) {
                    booking = list.get(i);
                    writer.write("  <action>\r\n");
                    writer.write("    <account_name>" + booking.getAccount() + "</account_name>\r\n");
                    writer.write("    <account_xml>" + booking.getAccount().getXmlFile() + "</account_xml>\r\n");
                    writer.write("    <account_html>" + booking.getAccount().getHtmlFile() + "</account_html>\r\n");
                    writer.write("    <" + (booking.isDebit() ? "debet" : "credit") + ">"
                                         + booking.getAmount().toString()
                                   + "</" + (booking.isDebit() ? "debet" : "credit") + ">\r\n");
                    writer.write("  </action>\r\n");
                }
            }
            writer.write("</Journal>");
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
