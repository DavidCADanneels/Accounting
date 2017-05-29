package be.dafke.BusinessModelDao;

import be.dafke.BusinessModel.*;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.Utils.Utils;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static be.dafke.BusinessModelDao.XMLConstants.*;
import static be.dafke.BusinessModelDao.XMLReader.*;
import static be.dafke.BusinessModelDao.XMLWriter.getXmlHeader;
import static be.dafke.Utils.Utils.parseInt;
import static be.dafke.Utils.Utils.toCalendar;

/**
 * Created by ddanneels on 15/01/2017.
 */
public class JournalsIO {

    public static void readJournalTypes(JournalTypes journalTypes, AccountTypes accountTypes, File accountingFolder){
        File xmlFile = new File(accountingFolder, "JournalTypes.xml");
        Element rootElement = getRootElement(xmlFile, JOURNAL_TYPES);
        for (Element element : getChildren(rootElement, JOURNAL_TYPE)) {

            String name = getValue(element, NAME);
            JournalType journalType = new JournalType(name, accountTypes);

            String debitTypes = getValue(element, DEBIT_TYPES);
            String creditTypes = getValue(element, CREDIT_TYPES);
            String[] debits = debitTypes.split(",");
            String[] credits = creditTypes.split(",");
            AccountsList left = new AccountsList(accountTypes, false);
            AccountsList right = new AccountsList(accountTypes, false);
            for(String s:debits) {
                if(!"".equals(s)) {
                    AccountType accountType = accountTypes.getBusinessObject(s);
                    if (accountType != null) {
                        left.setTypeAvailable(accountType, Boolean.TRUE);
                    }
                }
            }
            for(String s:credits) {
                if(!"".equals(s)) {
                    AccountType accountType = accountTypes.getBusinessObject(s);
                    if (accountType != null) {
                        right.setTypeAvailable(accountType, Boolean.TRUE);
                    }
                }
            }
            journalType.setLeft(left);
            journalType.setRight(right);
            String taxString = getValue(element, VATTYPE);
            if(taxString!=null) journalType.setVatType(VATTransaction.VATType.valueOf(taxString));

            try {
                journalTypes.addBusinessObject(journalType);
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }
    }

    public static void readJournals(Journals journals, Accounts accounts, JournalTypes journalTypes, VATTransactions vatTransactions, File accountingFolder) {
        File journalsFolder = new File(accountingFolder, "Journals");
        File xmlFile = new File(accountingFolder, "Journals.xml");
        Element rootElement = getRootElement(xmlFile, JOURNALS);
        for (Element element : getChildren(rootElement, JOURNAL)) {

            String name = getValue(element, NAME);
            String abbr = getValue(element, ABBREVIATION);
            Journal journal = new Journal(name, abbr);

            String type = getValue(element, TYPE);
            journal.setType(journalTypes.getBusinessObject(type));

            try {
                journals.addBusinessObject(journal);
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }
        String value = getValue(rootElement, CURRENT);
        if (value != null) {
            journals.setCurrentObject(journals.getBusinessObject(value));
        }

        for(Journal journal:journals.getBusinessObjects()){
            readJournal(journal, accounts, vatTransactions, journalsFolder);
        }
    }

    public static void readJournal(Journal journal, Accounts accounts, VATTransactions vatTransactions, File journalsFolder) {
        String name = journal.getName();
        File xmlFile = new File(journalsFolder, name+XML);
        Element rootElement = getRootElement(xmlFile, JOURNAL);
        for (Element element: getChildren(rootElement, TRANSACTION)){

            String dateString = getValue(element, DATE);
            Calendar date = toCalendar(dateString);
            String description = getValue(element, DESCRIPTION);

            Transaction transaction = new Transaction(date,description);

            for(Element bookingsElement:getChildren(element, BOOKING)){
                String idString = getValue(bookingsElement, ID);
                String debitString = getValue(bookingsElement, DEBIT);
                String creditString = getValue(bookingsElement, CREDIT);
                String accountString = getValue(bookingsElement, ACCOUNT);

                Account account = accounts.getBusinessObject(accountString);

                boolean debit= true;
                BigDecimal amount = BigDecimal.ZERO;
                if(debitString!=null){
                    debit = true;
                    amount = new BigDecimal(debitString);
                    if(creditString!=null){
                        System.err.println("Movement cannot contain both 'debit' and 'credit' !!!");
                    }
                } else if(creditString!=null){
                    debit = false;
                    amount = new BigDecimal(creditString);
                } else {
                    System.err.println("No 'debit' or 'credit' tag found in Movement !!!");
                }
                Booking booking = new Booking(account, amount, debit, parseInt(idString));

                transaction.addBusinessObject(booking);
            }

            journal.addBusinessObject(transaction);

            String vatIdString = getValue(element, VAT_ID);
            if(vatIdString!=null){
                VATTransaction vatTransaction = vatTransactions.getBusinessObject(Utils.parseInt(vatIdString));
                transaction.addVatTransaction(vatTransaction);
                vatTransaction.setTransaction(transaction);
            }
        }
    }

    public static void writeJournalTypes(JournalTypes journalTypes, File accountingFolder){
        File journalTypesFile = new File(accountingFolder, JOURNAL_TYPES +XML);
        try {
            Writer writer = new FileWriter(journalTypesFile);
            writer.write(getXmlHeader(JOURNAL_TYPES, 2));
            for (JournalType journalType : journalTypes.getBusinessObjects()) {

                AccountsList left = journalType.getLeft();
                ArrayList<AccountType> leftAccountTypes = left.getAccountTypes();
                String leftStream = leftAccountTypes.stream().map(AccountType::getName).collect(Collectors.joining(","));

                AccountsList right = journalType.getRight();
                ArrayList<AccountType> rightAccountTypes = right.getAccountTypes();
                String rightStream = rightAccountTypes.stream().map(AccountType::getName).collect(Collectors.joining(","));

                writer.write(
                        "  <"+JOURNAL_TYPE+">\n" +
                        "    <"+NAME+">"+journalType.getName()+"</"+NAME+">\n" +
                        "    <"+VATTYPE+">"+(journalType.getVatType()==null?"null":journalType.getVatType().toString())+"</"+VATTYPE+">\n" +
                        "    <"+DEBIT_TYPES+">"+leftStream+"</"+DEBIT_TYPES+">\n" +
                        "    <"+CREDIT_TYPES+">"+rightStream+"</"+CREDIT_TYPES+">\n" +
                        "  </"+JOURNAL_TYPE+">\n"
                );
            }
            writer.write("</"+JOURNAL_TYPES+">\n");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(JournalTypes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void writeJournals(Journals journals, File accountingFolder){
        File journalsFile = new File(accountingFolder, JOURNALS +XML);
        File journalsFolder = new File(accountingFolder, JOURNALS);
        try {
            Writer writer = new FileWriter(journalsFile);
            writer.write(getXmlHeader(JOURNALS, 2));
            for (Journal journal : journals.getBusinessObjects()) {
                writer.write(
                        "  <"+JOURNAL+">\n" +
                        "    <"+NAME+">"+journal.getName()+"</"+NAME+">\n" +
                        "    <"+ABBREVIATION+">"+journal.getAbbreviation()+"</"+ABBREVIATION+">\n" +
                        "    <"+TYPE+">"+journal.getType()+"</"+TYPE+">\n" +
                        "  </"+JOURNAL+">\n"
                );
            }
            Journal currentObject = journals.getCurrentObject();
            if(currentObject!=null) {
                writer.write("  <" + CURRENT + ">" + currentObject.getName() + "</" + CURRENT + ">\n");
            }
            writer.write("</"+JOURNALS+">\n");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Journals.class.getName()).log(Level.SEVERE, null, ex);
        }
        journalsFolder.mkdirs();
        for (Journal journal:journals.getBusinessObjects()) {
            writeJournal(journal, journalsFolder);
        }

    }
    public static void writeJournal(Journal journal, File journalsFolder){
        File journalFile = new File(journalsFolder, journal.getName() +XML);
        try {
            Writer writer = new FileWriter(journalFile);
            writer.write(getXmlHeader(JOURNAL, 3));
            writer.write(
                        "  <"+NAME+">"+journal.getName()+"</"+NAME+">\n" +
                        "  <"+ABBREVIATION+">"+journal.getAbbreviation()+"</"+ABBREVIATION+">\n" +
                        "  <"+TYPE+">"+journal.getType()+"</"+TYPE+">\n"
            );
            for (Transaction transaction : journal.getBusinessObjects()) {
                writer.write(
                        "  <"+TRANSACTION+">\n" +
                        "    <"+DATE+">"+ Utils.toString(transaction.getDate())+"</"+DATE+">\n" +
                        "    <"+DESCRIPTION+">"+transaction.getDescription()+"</"+DESCRIPTION+">\n" +
                        "    <"+ID+">"+transaction.getId()+"</"+ID+">\n"
                );
                VATTransaction vatTransaction = transaction.getVatTransaction();
                if(vatTransaction!=null){
                    writer.write(
                        "    <"+VAT_ID+">"+vatTransaction.getId()+"</"+VAT_ID+">\n"
                    );
                }
                for(Booking booking:transaction.getBusinessObjects()){
                    writer.write(
                        "    <"+BOOKING+">\n" +
                        "      <"+ID+">"+booking.getId()+"</"+ID+">\n" +
                        "      <"+ACCOUNT+">"+booking.getAccount()+"</"+ACCOUNT+">\n"
                    );
                    Movement movement = booking.getMovement();
                    if(movement.isDebit()){
                        writer.write(
                        "      <"+DEBIT+">"+movement.getAmount()+"</"+DEBIT+">\n"
                        );
                    } else {
                        writer.write(
                        "      <"+CREDIT+">"+movement.getAmount()+"</"+CREDIT+">\n"
                        );
                    }
                    writer.write(
                        "    </"+BOOKING+">\n"
                    );
                }
                writer.write("  </"+TRANSACTION+">\n");
            }
            writer.write("</"+JOURNAL+">\n");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Journal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
