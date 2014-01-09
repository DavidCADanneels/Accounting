package be.dafke.BasicAccounting.Dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.Accounts;
import be.dafke.BasicAccounting.Objects.Booking;
import be.dafke.BasicAccounting.Objects.Journal;
import be.dafke.BasicAccounting.Objects.Movement;
import be.dafke.BasicAccounting.Objects.Transaction;
import be.dafke.Utils.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * User: Dafke
 * Date: 28/02/13
 * Time: 5:02
 */
public class JournalsSAXParser {
    public static void readJournal(Journal journal, Accounts accounts, File xmlFile) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(true);
            DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile.getAbsoluteFile());
            doc.getDocumentElement().normalize();

//            String name = Utils.getValue(doc, "name");

            String abbreviation = journal.getAbbreviation();

            NodeList actions = doc.getElementsByTagName("Transaction");
            for (int i = 0; i < actions.getLength(); i++) {
                Transaction transaction = new Transaction();
                transaction.setJournal(journal);
                Element element = (Element)actions.item(i);
//                String nr = Utils.getValue(element, "id");
                String date = Utils.getValue(element, "date");
                String description = Utils.getValue(element, "description");
                transaction.setDate(Utils.toCalendar(date));
                transaction.setDescription(description);

                NodeList list = element.getElementsByTagName("Booking");
                for (int j = 0; j < list.getLength(); j++) {
//                    String id = Utils.getValue(element, "id");
                    element = (Element)list.item(j);
                    String accountName = Utils.getValue(element, "Account");
                    Account account = accounts.getBusinessObject(accountName);
                    String debit = Utils.getValue(element, "debet");
                    String credit = Utils.getValue(element, "credit");
                    if(transaction==null){
                        System.err.println("Journals.xml is bad structured: each transaction should have a \"nr\" tag !");
                    } else {
                        if(debit!=null){
                            BigDecimal amount = Utils.parseBigDecimal(debit);
                            Booking booking = new Booking(account);
                            booking.addBusinessObject(new Movement(amount, true));
                            transaction.addBusinessObject(booking);
                        }
                        if(credit!=null){
                            BigDecimal amount = Utils.parseBigDecimal(credit);
                            Booking booking = new Booking(account);
                            booking.addBusinessObject(new Movement(amount, false));
                            transaction.addBusinessObject(booking);
                        }
                    }
                }
                journal.addBusinessObject(transaction);
            }
        } catch (IOException io) {
            io.printStackTrace();
//				FileSystemView.getFileSystemView().createFileObject(subFolder, "Accounting.xml");
//				System.out.println(journalFile + " has been created");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeJournal(Journal journal, File xmlFolder, String header) {
        try {
            File xmlFile = new File(xmlFolder, journal.getName()+".xml");
            Writer writer = new FileWriter(xmlFile);

            writer.write(header);

            writer.write("<Journal>\r\n"
                    + "  <name>" + journal.getName() + "</name>\r\n"
                    + "  <abbr>" + journal.getAbbreviation() + "</abbr>\r\n");
            for (Transaction transaction :journal.getBusinessObjects()) {
                writer.write("  <Transaction>\r\n");
                writer.write("    <id>" + journal.getId(transaction) + "</id>\r\n");
                writer.write("    <description>" + transaction.getDescription() + "</description>\r\n");
                writer.write("    <date>" + Utils.toString(transaction.getDate()) + "</date>\r\n");

                for (Booking booking : transaction.getBookings()){
                writer.write("    <Booking>\r\n");
                writer.write("      <id>" + booking.getMovement().getId() + "</id>\r\n");
                writer.write("      <Account>" + booking.getAccount() + "</Account>\r\n");
                writer.write("      <" + (booking.getMovement().isDebit() ? "debet" : "credit") + ">"
                                     + booking.getMovement().getAmount().toString()
                               + "</" + (booking.getMovement().isDebit() ? "debet" : "credit") + ">\r\n");
                writer.write("    </Booking>\r\n");
                }
                writer.write("  </Transaction>\r\n");
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
