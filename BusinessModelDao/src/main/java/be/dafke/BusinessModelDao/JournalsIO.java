package be.dafke.BusinessModelDao;

import be.dafke.BusinessModel.*;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.Utils.Utils;
import org.apache.fop.apps.FOPException;
import org.w3c.dom.Element;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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

    public static void readJournalTypes(Accounting accounting){
        JournalTypes journalTypes = accounting.getJournalTypes();
        Accounts accounts = accounting.getAccounts();
        AccountTypes accountTypes = accounting.getAccountTypes();
        File xmlFile = new File(ACCOUNTINGS_FOLDER +accounting.getName()+"/"+JOURNAL_TYPES+XML_EXTENSION);
        Element rootElement = getRootElement(xmlFile, JOURNAL_TYPES);
        for (Element element : getChildren(rootElement, JOURNAL_TYPE)) {

            String name = getValue(element, NAME);
            JournalType journalType = new JournalType(name);
            // Do not add default types here, they are read, right below

            String taxString = getValue(element, VATTYPE);
            if(taxString!=null) journalType.setVatType(VATTransaction.VATType.valueOf(taxString));

            Element leftElement = getChildren(element, LEFT_LIST).get(0);
            Element rightElement = getChildren(element, RIGHT_LIST).get(0);

            AccountsList left = readTypes(leftElement, accounts, accountTypes);
            AccountsList right = readTypes(rightElement, accounts, accountTypes);

            journalType.setLeft(left);
            journalType.setRight(right);

            try {
                journalTypes.addBusinessObject(journalType);
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }
    }

    private static AccountsList readTypes(Element element, Accounts accounts, AccountTypes accountTypes) {
        String typesString = getValue(element, TYPES);
        AccountsList accountsList = new AccountsList();
        // TODO: save state ENABLED in xml and call setTypeAvailable(ENABLED)
        accountsList.addAllTypes(accountTypes, false);
        ArrayList<String> checkedTypes = new ArrayList<>();
        String checkedString = getValue(element, CHECKED);
        if(checkedString!=null){
            String[] checkedList = checkedString.split(",");
            checkedTypes.addAll(Arrays.asList(checkedList));
        }

        if(typesString!=null) {
            String[] typesList = typesString.split(",");
            for (String s : typesList) {
                if (!"".equals(s)) {
                    AccountType accountType = accountTypes.getBusinessObject(s);
                    if (accountType != null) {
                        accountsList.setTypeAvailable(accountType, Boolean.TRUE);
                        accountsList.setTypeChecked(accountType, checkedTypes.isEmpty()||checkedTypes.contains(s));
                    }
                }
            }
        }

        String leftActionString = getValue(element, LEFT_ACTION);
        String rightActionString = getValue(element, RIGHT_ACTION);
        String leftButtonString = getValue(element, LEFT_BUTTON);
        String rightButtonString = getValue(element, RIGHT_BUTTON);
        accountsList.setLeftAction(Boolean.valueOf(leftActionString));
        accountsList.setRightAction(Boolean.valueOf(rightActionString));
        accountsList.setLeftButton(leftButtonString);
        accountsList.setRightButton(rightButtonString);

        String vatString = getValue(element, VATTYPE);
        VATTransaction.VATType vatType = vatString==null?null:VATTransaction.VATType.valueOf(vatString);
        accountsList.setVatType(vatType);

        String singleString = getValue(element, SINGLE_ACCOUNT);
        boolean single = Boolean.valueOf(singleString);
        accountsList.setSingleAccount(single);

        String accountString = getValue(element, ACCOUNT);
        if (accountString != null){
            Account account = accounts.getBusinessObject(accountString);
            accountsList.setAccount(account);
        }

        return accountsList;
    }

    public static void readJournals(Accounting accounting) {
        JournalTypes journalTypes = accounting.getJournalTypes();
        Journals journals = accounting.getJournals();

        File xmlFile = new File(ACCOUNTINGS_FOLDER +accounting.getName()+"/"+JOURNALS+XML_EXTENSION);
        Element rootElement = getRootElement(xmlFile, JOURNALS);
        for (Element element : getChildren(rootElement, JOURNAL)) {

            String name = getValue(element, NAME);
            String abbr = getValue(element, ABBREVIATION);
            Journal journal = new Journal(name, abbr);
//            journal.setAccounting(accounting);
//            setAccounting() is implicitely done in journals.addBusinessObjects
//            this requires journals.setAccounting() is done already !

            String type = getValue(element, TYPE);
            journal.setType(journalTypes.getBusinessObject(type));

            try {
                journals.addBusinessObject(journal);
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }

        for(Journal journal:journals.getBusinessObjects()){
            readJournal(journal, accounting);
        }
    }

    public static void readJournal(Journal journal, Accounting accounting) {
        Transactions transactions = accounting.getTransactions();
        String name = journal.getName();
        File xmlFile = new File(ACCOUNTINGS_FOLDER +accounting.getName()+"/"+JOURNALS+"/"+name+ XML_EXTENSION);
        Element rootElement = getRootElement(xmlFile, JOURNAL);
        for (Element element: getChildren(rootElement, TRANSACTION)) {
            int id = parseInt(getValue(element, TRANSACTION_ID));

            Transaction transaction = transactions.getBusinessObject(id);
            journal.addBusinessObject(transaction);
        }
    }

    public static void readTransactions(Accounting accounting) {
        Transactions transactions = accounting.getTransactions();
        VATTransactions vatTransactions = accounting.getVatTransactions();
        Accounts accounts = accounting.getAccounts();
        File xmlFile = new File(ACCOUNTINGS_FOLDER +accounting.getName()+"/"+TRANSACTIONS+ XML_EXTENSION);
        Element rootElement = getRootElement(xmlFile, TRANSACTIONS);
        for (Element element: getChildren(rootElement, TRANSACTION)) {

            Calendar date = toCalendar(getValue(element, DATE));
            String description = getValue(element, DESCRIPTION);
            int id = parseInt(getValue(element, TRANSACTION_ID));

            Transaction transaction = new Transaction(date, description);
            transaction.setTransactionId(id);

            String balanceTransactionString = getValue(element, BALANCE_TRANSACTION);
            if(Boolean.valueOf(balanceTransactionString)){
                transaction.setBalanceTransaction(true);
            }

            for (Element bookingsElement : getChildren(element, BOOKING)) {
                String idString = getValue(bookingsElement, ID);
                String debitString = getValue(bookingsElement, DEBIT);
                String creditString = getValue(bookingsElement, CREDIT);
                String accountString = getValue(bookingsElement, ACCOUNT);

                Account account = accounts.getBusinessObject(accountString);

                boolean debit = true;
                BigDecimal amount = BigDecimal.ZERO;
                if (debitString != null) {
                    debit = true;
                    amount = new BigDecimal(debitString);
                    if (creditString != null) {
                        System.err.println("Movement cannot contain both 'debit' and 'credit' !!!");
                    }
                } else if (creditString != null) {
                    debit = false;
                    amount = new BigDecimal(creditString);
                } else {
                    System.err.println("No 'debit' or 'credit' tag found in Movement !!!");
                }
                Booking booking = new Booking(account, amount, debit, parseInt(idString));

                VATFields vatFields = accounting.getVatFields();
                // TODO: get vatMovement etc from existing objects-> vatTransactions
                for (Element vatBookingsElement : getChildren(bookingsElement, VATBOOKING)){
                    String amountString = getValue(vatBookingsElement, AMOUNT);
                    String vatFieldString = getValue(vatBookingsElement, VATFIELD);
                    VATField vatField = vatFields.getBusinessObject(vatFieldString);
                    BigDecimal vatAmount = new BigDecimal(amountString);
                    VATMovement vatMovement = new VATMovement(vatAmount);
                    VATBooking vatBooking = new VATBooking(vatField,vatMovement);
                    booking.addVatBooking(vatBooking);
                }

                transaction.addBusinessObject(booking);
            }
            transactions.addBusinessObject(transaction);
            transactions.raiseId();

            String vatIdString = getValue(element, VAT_ID);
            if (vatIdString != null) {
                int vatid = Utils.parseInt(vatIdString);
                VATTransaction vatTransaction = vatTransactions.getBusinessObject(vatid);
                transaction.addVatTransaction(vatTransaction);
                vatTransaction.setTransaction(transaction);
            }
        }
    }

    public static void writeJournalTypes(Accounting accounting){
        JournalTypes journalTypes = accounting.getJournalTypes();
        File journalTypesFile = new File(ACCOUNTINGS_FOLDER + accounting.getName() + "/" + JOURNAL_TYPES + XML_EXTENSION);
        try {
            Writer writer = new FileWriter(journalTypesFile);
            writer.write(getXmlHeader(JOURNAL_TYPES, 2));
            for (JournalType journalType : journalTypes.getBusinessObjects()) {

                AccountsList left = journalType.getLeft();
                AccountsList right = journalType.getRight();

                ArrayList<AccountType> leftAccountTypes = left.getAccountTypes();
                ArrayList<AccountType> rightAccountTypes = right.getAccountTypes();

                String leftStream = leftAccountTypes.stream().sorted().map(AccountType::getName).collect(Collectors.joining(","));
                String rightStream = rightAccountTypes.stream().sorted().map(AccountType::getName).collect(Collectors.joining(","));

                ArrayList<AccountType> leftCheckedTypes = left.getCheckedTypes();
                ArrayList<AccountType> rightCheckedTypes = right.getCheckedTypes();

                String leftCheckedStream = leftCheckedTypes.stream().sorted().map(AccountType::getName).collect(Collectors.joining(","));
                String rightCheckedStream = rightCheckedTypes.stream().sorted().map(AccountType::getName).collect(Collectors.joining(","));

                writer.write(
                        "  <"+JOURNAL_TYPE+">\n" +
                        "    <"+NAME+">"+journalType.getName()+"</"+NAME+">\n" +
                        "    <"+VATTYPE+">"+(journalType.getVatType()==null?"null":journalType.getVatType().toString())+"</"+VATTYPE+">\n" +
                        "    <"+LEFT_LIST+">\n" +
                        "      <"+LEFT_ACTION+">"+left.isLeftAction()+"</"+LEFT_ACTION+">\n" +
                        "      <"+LEFT_BUTTON+">"+left.getLeftButton()+"</"+LEFT_BUTTON+">\n" +
                        "      <"+RIGHT_ACTION+">"+left.isRightAction()+"</"+RIGHT_ACTION+">\n" +
                        "      <"+RIGHT_BUTTON+">"+left.getRightButton()+"</"+RIGHT_BUTTON+">\n" +
                        "      <"+VATTYPE+">"+journalType.getLeftVatType()+"</"+VATTYPE+">\n" +
                        "      <"+SINGLE_ACCOUNT+">"+left.isSingleAccount()+"</"+SINGLE_ACCOUNT+">\n" +
                        "      <"+ACCOUNT+">"+left.getAccount()+"</"+ACCOUNT+">\n" +
                        "      <"+TYPES+">"+leftStream+"</"+TYPES+">\n" +
                        "      <"+CHECKED+">"+leftCheckedStream+"</"+CHECKED+">\n" +
                        "    </"+LEFT_LIST+">\n" +
                        "    <"+RIGHT_LIST+">\n" +
                        "      <"+LEFT_ACTION+">"+right.isLeftAction()+"</"+LEFT_ACTION+">\n" +
                        "      <"+LEFT_BUTTON+">"+right.getLeftButton()+"</"+LEFT_BUTTON+">\n" +
                        "      <"+RIGHT_ACTION+">"+right.isRightAction()+"</"+RIGHT_ACTION+">\n" +
                        "      <"+RIGHT_BUTTON+">"+right.getRightButton()+"</"+RIGHT_BUTTON+">\n" +
                        "      <"+VATTYPE+">"+journalType.getRightVatType()+"</"+VATTYPE+">\n" +
                        "      <"+SINGLE_ACCOUNT+">"+right.isSingleAccount()+"</"+SINGLE_ACCOUNT+">\n" +
                        "      <"+ACCOUNT+">"+right.getAccount()+"</"+ACCOUNT+">\n" +
                        "      <"+TYPES+">"+rightStream+"</"+TYPES+">\n" +
                        "      <"+CHECKED+">"+rightCheckedStream+"</"+CHECKED+">\n" +
                        "    </"+RIGHT_LIST+">\n" +
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

    public static void writeJournalPdfFiles(Accounting accounting){
        writeJournals(accounting);
        Journals journals = accounting.getJournals();
        String accountingName = accounting.getName();
        File subFolder = new File(ACCOUNTINGS_FOLDER + accountingName + "/" + PDF +"/" +JOURNALS);
        subFolder.mkdirs();

        String journalsFolderPath = ACCOUNTINGS_FOLDER + accountingName + "/" + JOURNALS + "/";
        String xslPath = "data/accounting/xsl/JournalPdf.xsl";
        String resultPdfPolderPath = ACCOUNTINGS_FOLDER + accountingName + "/" + PDF +"/" + JOURNALS + "/";
        journals.getBusinessObjects().forEach(journal -> {
            try {
                PDFCreator.convertToPDF(journalsFolderPath + journal.getName() + XML_EXTENSION, xslPath, resultPdfPolderPath + journal.getName() + PDF_EXTENSION);
            } catch (IOException | FOPException | TransformerException e1) {
                e1.printStackTrace();
            }
        });
    }

    public static void writeJournals(Accounting accounting){
        Journals journals = accounting.getJournals();
        File journalsFile = new File(ACCOUNTINGS_FOLDER + accounting.getName() + "/" + JOURNALS + XML_EXTENSION);
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
            writer.write("</"+JOURNALS+">\n");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Journals.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (Journal journal:journals.getBusinessObjects()) {
            writeJournal(journal, accounting);
        }

    }

    public static void writeJournal(Journal journal, Accounting accounting){
        File journalsFolder = new File(ACCOUNTINGS_FOLDER + accounting.getName() + "/" +  JOURNALS);
        journalsFolder.mkdirs();
        File journalFile = new File(journalsFolder, "/" +  journal.getName() + XML_EXTENSION);
        try {
            Writer writer = new FileWriter(journalFile);
            writer.write(getXmlHeader(JOURNAL, 3));
            writer.write(
                    "  <"+NAME+">"+journal.getName()+"</"+NAME+">\n" +
                            "  <"+ABBREVIATION+">"+journal.getAbbreviation()+"</"+ABBREVIATION+">\n" +
                            "  <"+TYPE+">"+journal.getType()+"</"+TYPE+">\n");
            for (Transaction transaction : journal.getBusinessObjects()) {
                writer.write(
                        "  <"+TRANSACTION+">\n" +
                            "    <"+ID+">" + transaction.getId() + "</"+ID+">\n" +
                            "    <"+TRANSACTION_ID+">"+ transaction.getTransactionId()+"</"+TRANSACTION_ID+">\n");
                if(transaction.isBalanceTransaction()){
                    writer.write("    <"+BALANCE_TRANSACTION+">true</"+BALANCE_TRANSACTION+">\n");
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

    public static void writeTransactions(Accounting accounting){
        Transactions transactions = accounting.getTransactions();
        File transactionsFile = new File(ACCOUNTINGS_FOLDER + accounting.getName() + "/" + TRANSACTIONS + XML_EXTENSION);
        try {
            Writer writer = new FileWriter(transactionsFile);
            writer.write(getXmlHeader(TRANSACTIONS, 2));
            for (Transaction transaction : transactions.getBusinessObjects()) {
                writer.write("  <" + TRANSACTION + ">\n" +
                        "    <" + TRANSACTION_ID + ">" + transaction.getTransactionId() + "</" + TRANSACTION_ID + ">\n" +
                        "    <" + DATE + ">" + Utils.toString(transaction.getDate()) + "</" + DATE + ">\n" +
                        "    <" + DESCRIPTION + ">" + transaction.getDescription() + "</" + DESCRIPTION + ">\n" +
                        "    <" + ID + ">" + transaction.getId() + "</" + ID + ">\n");
                if(transaction.isBalanceTransaction()){
                    writer.write("    <"+BALANCE_TRANSACTION+">true</"+BALANCE_TRANSACTION+">\n");
                }
                VATTransaction vatTransaction = transaction.getVatTransaction();
                if (vatTransaction != null) {
                    writer.write("    <" + VAT_ID + ">" + vatTransaction.getId() + "</" + VAT_ID + ">\n");
                }
                for (Booking booking : transaction.getBusinessObjects()) {
                    writer.write("    <" + BOOKING + ">\n" +
                            "      <" + ID + ">" + booking.getId() + "</" + ID + ">\n" +
                            "      <" + ACCOUNT + ">" + booking.getAccount() + "</" + ACCOUNT + ">\n");
                    Movement movement = booking.getMovement();
                    if (movement.isDebit()) {
                        writer.write("      <" + DEBIT + ">" + movement.getAmount() + "</" + DEBIT + ">\n");
                    } else {
                        writer.write("      <" + CREDIT + ">" + movement.getAmount() + "</" + CREDIT + ">\n");
                    }
                    ArrayList<VATBooking> vatBookings = booking.getVatBookings();
                    for (VATBooking vatBooking : vatBookings) {
                        VATField vatField = vatBooking.getVatField();
                        VATMovement vatMovement = vatBooking.getVatMovement();
                        writer.write("      <" + VATBOOKING + ">\n" +
                                "        <" + VATFIELD + ">" + vatField.getName() + "</" + VATFIELD + ">\n" +
                                "        <" + AMOUNT + ">" + vatMovement.getAmount() + "</" + AMOUNT + ">\n" +
                                "      </" + VATBOOKING + ">\n");
                    }
                    writer.write("    </" + BOOKING + ">\n");
                }
                writer.write("  </" + TRANSACTION + ">\n");
            }
            writer.write("</" + TRANSACTIONS + ">\n");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Journal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
