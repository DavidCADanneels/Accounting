package be.dafke.BusinessModelDao;

import be.dafke.BusinessModel.*;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import org.w3c.dom.Element;

import java.io.File;
import java.math.BigDecimal;
import java.util.Calendar;

import static be.dafke.BusinessModelDao.XMLReader.*;
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
            JournalType journalType = new JournalType(name);

            String debitTypes = getValue(element, DEBIT_TYPES);
            String creditTypes = getValue(element, CREDIT_TYPES);
            String[] debits = debitTypes.split(",");
            String[] credits = creditTypes.split(",");
            for(String s:debits) {
                if(!"".equals(s)) {
                    AccountType accountType = accountTypes.getBusinessObject(s);
                    if (accountType != null) {
                        try {
                            journalType.addDebetType(accountType);
                        } catch (EmptyNameException | DuplicateNameException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            for(String s:credits) {
                if(!"".equals(s)) {
                    AccountType accountType = accountTypes.getBusinessObject(s);
                    if(accountType!=null) {
                        try {
                            journalType.addCreditType(accountType);
                        } catch (EmptyNameException | DuplicateNameException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            String taxString = getValue(element, VATTYPE);
            journalType.setVatType(VATTransaction.VATType.valueOf(taxString));

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
            Journal journal = new Journal(name, abbr, vatTransactions);

            String type = getValue(element, TYPE);
            journal.setType(journalTypes.getBusinessObject(type));

            try {
                journals.addBusinessObject(journal);
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }

        for(Journal journal:journals.getBusinessObjects()){
            readJournal(journal, accounts, journalsFolder);
        }
    }

    public static void readJournal(Journal journal, Accounts accounts, File journalsFolder) {
        String name = journal.getName();
        File xmlFile = new File(journalsFolder, name+".xml");
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
        }
    }

}
