package be.dafke.BasicAccounting.Dao;

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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

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

            Transaction transaction = null;
            String abbreviation = journal.getAbbreviation();

            NodeList actions = doc.getElementsByTagName("Transaction");
            for (int i = 0; i < actions.getLength(); i++) {
                Element element = (Element)actions.item(i);
                String nr = Utils.getValue(element, "nr");
                String date = Utils.getValue(element, "date");
                String description = Utils.getValue(element, "description");
                if(nr!=null){
                    if(transaction != null){
                        journal.addBusinessObject(transaction);
                    }
                    transaction = new Transaction();
                    transaction.setJournal(journal);
                    transaction.setDate(Utils.toCalendar(date));
                    transaction.setDescription(description);
                }
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
                        booking.setMovement(new Movement(amount, true));
                        transaction.addBooking(booking);
                    }
                    if(credit!=null){
                        BigDecimal amount = Utils.parseBigDecimal(credit);
                        Booking booking = new Booking(account);
                        booking.setMovement(new Movement(amount, false));
                        transaction.addBooking(booking);
                    }
                }
                }
            if(transaction!=null){
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
                    + "  <name>" + journal.getName() + "</name>\r\n");
            for (Transaction transaction :journal.getBusinessObjects()) {
                ArrayList<Booking> list = transaction.getBookings();
                Booking booking = list.get(0);
                writer.write("  <Transaction id=\""+transaction.getId()+"\">\r\n");
                writer.write("    <nr>" + journal.getAbbreviation() + journal.getId(transaction) + "</nr>\r\n");
                writer.write("    <date>" + Utils.toString(transaction.getDate()) + "</date>\r\n");
                writer.write("    <Account>" + booking.getAccount() + "</Account>\r\n");
                writer.write("    <" + (booking.getMovement().isDebit() ? "debet" : "credit") + ">"
                                     + booking.getMovement().getAmount().toString()
                               + "</" + (booking.getMovement().isDebit() ? "debet" : "credit") + ">\r\n");
                writer.write("    <description>" + transaction.getDescription() + "</description>\r\n");
                writer.write("  </Transaction>\r\n");
                for(int i = 1; i < list.size(); i++) {
                    booking = list.get(i);
                    writer.write("  <Transaction>\r\n");
                    writer.write("    <Account>" + booking.getAccount() + "</Account>\r\n");
                    writer.write("    <" + (booking.getMovement().isDebit() ? "debet" : "credit") + ">"
                            + booking.getMovement().getAmount().toString()
                            + "</" + (booking.getMovement().isDebit() ? "debet" : "credit") + ">\r\n");
                    writer.write("  </Transaction>\r\n");
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
