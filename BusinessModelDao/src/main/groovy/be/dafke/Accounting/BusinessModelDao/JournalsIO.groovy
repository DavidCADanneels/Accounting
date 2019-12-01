package be.dafke.Accounting.BusinessModelDao

import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.Utils.Utils
import org.apache.fop.apps.FOPException
import org.w3c.dom.Element

import javax.xml.transform.TransformerException
import java.util.logging.Level
import java.util.logging.Logger
import java.util.stream.Collectors

import static be.dafke.Accounting.BusinessModelDao.XMLConstants.*
import static be.dafke.Accounting.BusinessModelDao.XMLReader.*
import static be.dafke.Accounting.BusinessModelDao.XMLWriter.getXmlHeader
import static be.dafke.Utils.Utils.parseInt
import static be.dafke.Utils.Utils.toCalendar 

class JournalsIO {

    static void readJournalTypes(Accounting accounting){
        JournalTypes journalTypes = accounting.journalTypes
        Accounts accounts = accounting.accounts
        AccountTypes accountTypes = accounting.accountTypes
        File xmlFile = new File(ACCOUNTINGS_XML_FOLDER +accounting.name+"/"+JOURNAL_TYPES+XML_EXTENSION)
        Element rootElement = getRootElement(xmlFile, JOURNAL_TYPES)
        for (Element element : getChildren(rootElement, JOURNAL_TYPE)) {

            String name = getValue(element, NAME)
            JournalType journalType = new JournalType(name)
            // Do not add default types here, they are read, right below

            String taxString = getValue(element, VATTYPE)
            if(taxString!=null) journalType.setVatType(VATTransaction.VATType.valueOf(taxString))

            Element leftElement = getChildren(element, LEFT_LIST).get(0)
            Element rightElement = getChildren(element, RIGHT_LIST).get(0)

            AccountsList left = readTypes(leftElement, accounts, accountTypes)
            AccountsList right = readTypes(rightElement, accounts, accountTypes)

            journalType.setLeft(left)
            journalType.setRight(right)

            try {
                journalTypes.addBusinessObject(journalType)
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace()
            }
        }
    }

    static AccountsList readTypes(Element element, Accounts accounts, AccountTypes accountTypes) {
        String typesString = getValue(element, TYPES)
        AccountsList accountsList = new AccountsList()
        // TODO: save state ENABLED in xml and call setTypeAvailable(ENABLED)
        accountsList.addAllTypes(accountTypes, false)

        if(typesString!=null) {
            String[] typesList = typesString.split(",")
            for (String s : typesList) {
                if (!"".equals(s)) {
                    AccountType accountType = accountTypes.getBusinessObject(s)
                    if (accountType != null) {
                        accountsList.setTypeAvailable(accountType, Boolean.TRUE)
                    }
                }
            }
        }

        String leftActionString = getValue(element, LEFT_ACTION)
        String rightActionString = getValue(element, RIGHT_ACTION)
        String leftButtonString = getValue(element, LEFT_BUTTON)
        String rightButtonString = getValue(element, RIGHT_BUTTON)
        accountsList.setLeftAction(Boolean.valueOf(leftActionString))
        accountsList.setRightAction(Boolean.valueOf(rightActionString))
        accountsList.setLeftButton(leftButtonString)
        accountsList.setRightButton(rightButtonString)

        String vatString = getValue(element, VATTYPE)
        VATTransaction.VATType vatType = vatString==null?null:VATTransaction.VATType.valueOf(vatString)
        accountsList.setVatType(vatType)

        String singleString = getValue(element, SINGLE_ACCOUNT)
        boolean single = Boolean.valueOf(singleString)
        accountsList.setSingleAccount(single)

        String accountString = getValue(element, ACCOUNT)
        if (accountString != null){
            Account account = accounts.getBusinessObject(accountString)
            accountsList.setAccount(account)
        }

        accountsList
    }

    static void readJournals(Accounting accounting) {
        JournalTypes journalTypes = accounting.journalTypes
        Journals journals = accounting.journals

        File xmlFile = new File(ACCOUNTINGS_XML_FOLDER + accounting.name + "/" + JOURNALS + XML_EXTENSION)
        Element rootElement = getRootElement(xmlFile, JOURNALS)
        for (Element element : getChildren(rootElement, JOURNAL)) {

            String name = getValue(element, NAME)
            String abbr = getValue(element, ABBREVIATION)
            Journal journal = new Journal(name, abbr)
//            journal.accounting = accounting
//            setAccounting() is implicitely done in journals.addBusinessObjects
//            this requires journals.setAccounting() is done already !

            String type = getValue(element, TYPE)
            journal.type = journalTypes.getBusinessObject(type)

            try {
                journals.addBusinessObject(journal)
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace()
            }
        }
    }

    static void readJournalsContent(Journals journals, Accounting accounting){
        for(Journal journal:journals.businessObjects){
            readJournal(journal, accounting)
        }
    }

    static void readJournal(Journal journal, Accounting accounting) {
        Transactions transactions = accounting.transactions
        String name = journal.name
        File xmlFile = new File(ACCOUNTINGS_XML_FOLDER +accounting.name+"/"+JOURNALS+"/"+name+ XML_EXTENSION)
        Element rootElement = getRootElement(xmlFile, JOURNAL)
        for (Element element: getChildren(rootElement, TRANSACTION)) {
            int id = parseInt(getValue(element, TRANSACTION_ID))

            Transaction transaction = transactions.getBusinessObject(id)
            if (transaction == null){
                System.err.println("id("+id+")not found")
            } else {
                journal.addBusinessObject(transaction)
            }
            Journal oldJournal = transaction.journal
            if(oldJournal==null){
                System.out.println("ERROR: should be set by Transactions")
            } else if (oldJournal!=journal){
                transaction.addDuplicateJournal(journal)
            }
        }
    }

    static void readTransactions(Accounting accounting) {
        Transactions transactions = accounting.transactions
        VATFields vatFields = accounting.vatFields
        Accounts accounts = accounting.accounts
        File xmlFile = new File(ACCOUNTINGS_XML_FOLDER +accounting.name+"/"+TRANSACTIONS+ XML_EXTENSION)
        Element rootElement = getRootElement(xmlFile, TRANSACTIONS)
        int maxId = 0
        for (Element element: getChildren(rootElement, TRANSACTION)) {

            Calendar date = toCalendar(getValue(element, DATE))
            String description = getValue(element, DESCRIPTION)

            Transaction transaction = new Transaction(date, description)

            int transactionId = parseInt(getValue(element, TRANSACTION_ID))
            if(transactionId>maxId){
                maxId=transactionId
            }
            transaction.transactionId = transactionId

            String journalAbbr = getValue(element, JOURNAL)
            if (!journalAbbr.equals("NULL")) {
                Journals journals = accounting.journals
                List<Journal> journalList = journals.getBusinessObjects(Journal.withAbbr(journalAbbr))
                if (journalList.size() != 1) {
                    System.out.println("Error: there should be exactly 1 match")
                } else {
                    Journal journal = journalList.get(0)
                    transaction.journal = journal
                }

                String balanceTransactionString = getValue(element, BALANCE_TRANSACTION)
                if (Boolean.valueOf(balanceTransactionString)) {
                    transaction.balanceTransaction = true
                }

                String registeredString = getValue(element, REGISTERED)
                if (registeredString != null) {
                    if ("true".equals(registeredString)) {
                        transaction.registered = true
                    }
                }

                for (Element bookingsElement : getChildren(element, BOOKING)) {
                    String idString = getValue(bookingsElement, ID)
                    String debitString = getValue(bookingsElement, DEBIT)
                    String creditString = getValue(bookingsElement, CREDIT)
                    String accountString = getValue(bookingsElement, ACCOUNT)

                    Account account = accounts.getBusinessObject(accountString)

                    boolean debit = true
                    BigDecimal amount = BigDecimal.ZERO
                    if (debitString != null) {
                        debit = true
                        amount = new BigDecimal(debitString)
                        if (creditString != null) {
                            System.err.println("Movement cannot contain both 'debit' and 'credit' !!!")
                        }
                    } else if (creditString != null) {
                        debit = false
                        amount = new BigDecimal(creditString)
                    } else {
                        System.err.println("No 'debit' or 'credit' tag found in Movement !!!")
                    }
                    Booking booking = new Booking(account, amount, debit, parseInt(idString))

                    // TODO: get vatMovement etc from existing objects-> vatTransactions
                    for (Element vatBookingsElement : getChildren(bookingsElement, VATBOOKING)) {
                        String vatFieldString = getValue(vatBookingsElement, VATFIELD)
                        VATField vatField = vatFields.getBusinessObject(vatFieldString)
                        if (vatField == null) System.err.println("Field[" + vatFieldString + "] not found")
                        String amountString = getValue(vatBookingsElement, AMOUNT)
                        BigDecimal vatAmount = new BigDecimal(amountString)
                        if(amount.compareTo(vatAmount)!=0){
                            System.err.println("Difference: "+ amount.toString() + " | " + vatAmount.toString())
                        }
                        boolean positive = vatAmount.compareTo(BigDecimal.ZERO)>=0
                        VATMovement vatMovement = new VATMovement(positive?amount:amount.negate())
                        VATBooking vatBooking = new VATBooking(vatField, vatMovement)
                        booking.addVatBooking(vatBooking)
                    }
                    transaction.addBusinessObject(booking)
                }
                transactions.addBusinessObject(transaction)
//                transactions.raiseId()
            }
        }

        transactions.id = maxId
    }

    static void writeJournalTypes(Accounting accounting){
        JournalTypes journalTypes = accounting.journalTypes
        File journalTypesFile = new File(ACCOUNTINGS_XML_FOLDER + accounting.name + "/" + JOURNAL_TYPES + XML_EXTENSION)
        try {
            Writer writer = new FileWriter(journalTypesFile)
            writer.write(getXmlHeader(JOURNAL_TYPES, 2))
            for (JournalType journalType : journalTypes.businessObjects) {

                AccountsList left = journalType.getLeft()
                AccountsList right = journalType.getRight()

                ArrayList<AccountType> leftAccountTypes = left.accountTypes
                ArrayList<AccountType> rightAccountTypes = right.accountTypes

                String leftStream = leftAccountTypes.name.stream().sorted().collect(Collectors.joining(","))
                String rightStream = rightAccountTypes.name.stream().sorted().collect(Collectors.joining(","))

                writer.write(
                        "  <"+JOURNAL_TYPE+">\n" +
                                "    <"+NAME+">"+journalType.name+"</"+NAME+">\n" +
                                "    <"+VATTYPE+">"+(journalType.getVatType()==null?"null":journalType.getVatType().toString())+"</"+VATTYPE+">\n" +
                                "    <"+LEFT_LIST+">\n" +
                                "      <"+LEFT_ACTION+">"+left.isLeftAction()+"</"+LEFT_ACTION+">\n" +
                                "      <"+LEFT_BUTTON+">"+left.getLeftButton()+"</"+LEFT_BUTTON+">\n" +
                                "      <"+RIGHT_ACTION+">"+left.isRightAction()+"</"+RIGHT_ACTION+">\n" +
                                "      <"+RIGHT_BUTTON+">"+left.getRightButton()+"</"+RIGHT_BUTTON+">\n" +
                                "      <"+VATTYPE+">"+journalType.getLeftVatType()+"</"+VATTYPE+">\n" +
                                "      <"+SINGLE_ACCOUNT+">"+left.isSingleAccount()+"</"+SINGLE_ACCOUNT+">\n" +
                                "      <"+ACCOUNT+">"+left.account+"</"+ACCOUNT+">\n" +
                                "      <"+TYPES+">"+leftStream+"</"+TYPES+">\n" +
                                "    </"+LEFT_LIST+">\n" +
                                "    <"+RIGHT_LIST+">\n" +
                                "      <"+LEFT_ACTION+">"+right.isLeftAction()+"</"+LEFT_ACTION+">\n" +
                                "      <"+LEFT_BUTTON+">"+right.getLeftButton()+"</"+LEFT_BUTTON+">\n" +
                                "      <"+RIGHT_ACTION+">"+right.isRightAction()+"</"+RIGHT_ACTION+">\n" +
                                "      <"+RIGHT_BUTTON+">"+right.getRightButton()+"</"+RIGHT_BUTTON+">\n" +
                                "      <"+VATTYPE+">"+journalType.getRightVatType()+"</"+VATTYPE+">\n" +
                                "      <"+SINGLE_ACCOUNT+">"+right.isSingleAccount()+"</"+SINGLE_ACCOUNT+">\n" +
                                "      <"+ACCOUNT+">"+right.account+"</"+ACCOUNT+">\n" +
                                "      <"+TYPES+">"+rightStream+"</"+TYPES+">\n" +
                                "    </"+RIGHT_LIST+">\n" +
                                "  </"+JOURNAL_TYPE+">\n"
                )
            }
            writer.write("</"+JOURNAL_TYPES+">\n")
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(JournalTypes.class.name).log(Level.SEVERE, null, ex)
        }
    }

    static String createTmpFolder(Accounting accounting) {
        String accountingName = accounting.name
        String tmpFolderPath = ACCOUNTINGS_XML_FOLDER + accountingName + "/" + TMP + "/"
        File tempFolder = new File(tmpFolderPath)
        tempFolder.mkdirs()
        tmpFolderPath
    }

    static String createPdfFolder(Accounting accounting) {
        String accountingName = accounting.name
        String resultPdfPolderPath = ACCOUNTINGS_PDF_FOLDER + accountingName + "/" + JOURNALS + "/"
        File targetFolder = new File(resultPdfPolderPath)
        targetFolder.mkdirs()
        resultPdfPolderPath
    }

    static void writeJournalPdfFiles(Accounting accounting){
        String xslPath = XSLFOLDER + "JournalPdf.xsl"

        String tmpFolderPath = createTmpFolder(accounting)
        String resultPdfPolderPath = createPdfFolder(accounting)

        Journals journals = accounting.journals
        for (Journal journal:journals.businessObjects) {
            if(!journal.businessObjects.empty) {
                writeJournal(journal, tmpFolderPath, true)
            }
        }

        journals.businessObjects.forEach({ journal ->
            if (!journal.businessObjects.empty) {
                try {
                    String fileName = journal.name + XML_EXTENSION
                    String xmlPath = tmpFolderPath + fileName
                    PDFCreator.convertToPDF(xmlPath, xslPath, resultPdfPolderPath + journal.name + PDF_EXTENSION)
                    File file = new File(xmlPath)
                    file.delete()
                } catch (IOException | FOPException | TransformerException e1) {
                    e1.printStackTrace()
                }
            }
        })
        File folder = new File(tmpFolderPath)
        folder.delete()
    }

    static void writeJournals(Accounting accounting, boolean writeHtml){
        Journals journals = accounting.journals
        String path = ACCOUNTINGS_XML_FOLDER + accounting.name + "/" + JOURNALS
        File journalsXmlFile = new File(path + XML_EXTENSION)
        try {
            Writer writer = new FileWriter(journalsXmlFile)
            writer.write(getXmlHeader(JOURNALS, 2))
            for (Journal journal : journals.businessObjects) {
                writer.write(
                        "  <"+JOURNAL+">\n" +
                                "    <"+NAME+">"+journal.name+"</"+NAME+">\n" +
                                "    <"+ABBREVIATION+">"+journal.abbreviation+"</"+ABBREVIATION+">\n" +
                                "    <"+TYPE+">"+journal.type+"</"+TYPE+">\n" +
                                "  </"+JOURNAL+">\n"
                )
            }
            writer.write("</"+JOURNALS+">\n")
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(Journals.class.name).log(Level.SEVERE, null, ex)
        }
        for (Journal journal:journals.businessObjects) {
            writeJournal(journal, path, false)
        }

        if(writeHtml){
            File journalsHtmlFile = new File(ACCOUNTINGS_HTML_FOLDER + accounting.name + "/" + JOURNALS+ HTML_EXTENSION)
            File journalsXslFile = new File(XSLFOLDER + "Journals.xsl")
            XMLtoHTMLWriter.xmlToHtml(journalsXmlFile,journalsXslFile,journalsHtmlFile, null)

            String tmpFolderPath = createTmpFolder(accounting)
            File journalsHtmlFolder = new File(ACCOUNTINGS_HTML_FOLDER + accounting.name + "/" + JOURNALS)
            journalsHtmlFolder.mkdirs()

            for (Journal journal:journals.businessObjects) {
                writeJournal(journal, tmpFolderPath, true)
                File journalXmlFile = new File(tmpFolderPath + "/" + journal.name + XML_EXTENSION)
                File journalHtmlFile = new File(journalsHtmlFolder, journal.name + HTML_EXTENSION)
                File journalXslFile = new File(XSLFOLDER + "Journal.xsl")
                XMLtoHTMLWriter.xmlToHtml(journalXmlFile, journalXslFile, journalHtmlFile, null)
            }
        }
    }

    static void writeJournal(Journal journal, String path, boolean details){
        File journalsFolder = new File(path)
        journalsFolder.mkdirs()
        File journalFile = new File(path, journal.name + XML_EXTENSION)
        try {
            Writer writer = new FileWriter(journalFile)
            writer.write(getXmlHeader(JOURNAL, 3))
            writer.write(
                    "  <"+NAME+">"+journal.name+"</"+NAME+">\n" +
                            "  <"+ABBREVIATION+">"+journal.abbreviation+"</"+ABBREVIATION+">\n" +
                            "  <"+TYPE+">"+journal.type+"</"+TYPE+">\n")
            for (Transaction transaction : journal.businessObjects) {
                writer.write(
                        "  <" + TRANSACTION + ">\n")
                Journal transactionJournal = transaction.journal
                if (transactionJournal == journal){
                    writer.write("    <" + ID + ">" + transaction.id + "</" + ID + ">\n")
                } else {
                    writer.write("    <" + ID + ">" + journal.getId(transaction) + "</" + ID + ">\n" +
                            "    <" + ORIGINAL_JOURNAL + ">" + transaction.abbreviation + "</" + ORIGINAL_JOURNAL + ">\n" +
                            "    <" + ORIGINAL_ID + ">" + transaction.id + "</" + ORIGINAL_ID + ">\n")
                }
                writer.write("    <" + TRANSACTION_ID + ">" + transaction.transactionId + "</" + TRANSACTION_ID + ">\n")
                if(transaction.balanceTransaction){
                    writer.write("    <"+BALANCE_TRANSACTION+">true</"+BALANCE_TRANSACTION+">\n")
                }
                if(details){
//                    TODO: move all to method
//                    writeDetails()
                    writer.write("    <" + DATE + ">" + Utils.toString(transaction.date) + "</" + DATE + ">\n" +
                            "    <" + DESCRIPTION + ">" + transaction.description + "</" + DESCRIPTION + ">\n")
                    for (Booking booking : transaction.businessObjects) {
                        writer.write("    <" + BOOKING + ">\n" +
                                "      <MovementId>" + booking.id + "</MovementId>\n" +
                                "      <" + ACCOUNT + ">" + booking.account + "</" + ACCOUNT + ">\n")
                        Movement movement = booking.getMovement()
                        if (movement.debit) {
                            writer.write("      <" + DEBIT + ">" + movement.amount + "</" + DEBIT + ">\n")
                        } else {
                            writer.write("      <" + CREDIT + ">" + movement.amount + "</" + CREDIT + ">\n")
                        }
                        ArrayList<VATBooking> vatBookings = booking.vatBookings
                        for (VATBooking vatBooking : vatBookings) {
                            VATField vatField = vatBooking.vatField
                            VATMovement vatMovement = vatBooking.vatMovement
                            writer.write("      <" + VATFIELD + ">" + vatField.name + "</" + VATFIELD + ">\n")
                        }
                        writer.write("    </" + BOOKING + ">\n")
                    }
                }
                writer.write("  </"+TRANSACTION+">\n")
            }
            writer.write("</"+JOURNAL+">\n")
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(Journal.class.name).log(Level.SEVERE, null, ex)
        }
    }

    static void writeTransactions(Accounting accounting){
        Transactions transactions = accounting.transactions
        File transactionsFile = new File(ACCOUNTINGS_XML_FOLDER + accounting.name + "/" + TRANSACTIONS + XML_EXTENSION)
        try {
            Writer writer = new FileWriter(transactionsFile)
            writer.write(getXmlHeader(TRANSACTIONS, 2))
            for (Transaction transaction : transactions.businessObjects) {
                writer.write("  <" + TRANSACTION + ">\n" +
                        "    <" + TRANSACTION_ID + ">" + transaction.transactionId + "</" + TRANSACTION_ID + ">\n" +
                        "    <" + DATE + ">" + Utils.toString(transaction.date) + "</" + DATE + ">\n" +
                        "    <" + DESCRIPTION + ">" + transaction.description + "</" + DESCRIPTION + ">\n" +
                        "    <" + JOURNAL + ">" + transaction.abbreviation + "</" + JOURNAL + ">\n" +
                        "    <" + JOURNAL_ID + ">" + transaction.id + "</" + JOURNAL_ID + ">\n")
                for(Journal journal : transaction.duplicateJournals){
                    writer.write(
                            "    <" + DUPLICATE_JOURNAL + ">" + journal.abbreviation + "</" + DUPLICATE_JOURNAL + ">\n" +
                                    "    <" + DUPLICATE_JOURNAL_ID + ">" + journal.getId(transaction) + "</" + DUPLICATE_JOURNAL_ID + ">\n"
                    )
                }
//                TODO: reuse method writeDetails()
                if(transaction.balanceTransaction){
                    writer.write("    <"+BALANCE_TRANSACTION+">true</"+BALANCE_TRANSACTION+">\n")
                }
                writer.write("    <"+REGISTERED+">"+transaction.registered+"</"+REGISTERED+">\n")
                for (Booking booking : transaction.businessObjects) {
                    writer.write("    <" + BOOKING + ">\n" +
                            "      <" + ID + ">" + booking.id + "</" + ID + ">\n" +
                            "      <" + ACCOUNT + ">" + booking.account + "</" + ACCOUNT + ">\n")
                    Movement movement = booking.getMovement()
                    if (movement.debit) {
                        writer.write("      <" + DEBIT + ">" + movement.amount + "</" + DEBIT + ">\n")
                    } else {
                        writer.write("      <" + CREDIT + ">" + movement.amount + "</" + CREDIT + ">\n")
                    }
                    ArrayList<VATBooking> vatBookings = booking.vatBookings
                    for (VATBooking vatBooking : vatBookings) {
                        VATField vatField = vatBooking.vatField
                        VATMovement vatMovement = vatBooking.vatMovement
                        writer.write("      <" + VATBOOKING + ">\n" +
                                "        <" + VATFIELD + ">" + vatField.name + "</" + VATFIELD + ">\n" +
                                "        <" + AMOUNT + ">" + vatMovement.amount + "</" + AMOUNT + ">\n" +
                                "      </" + VATBOOKING + ">\n")
                    }
                    writer.write("    </" + BOOKING + ">\n")
                }
//                TODO: end of method writeDetails(transaction)
                writer.write("  </" + TRANSACTION + ">\n")
            }
            writer.write("</" + TRANSACTIONS + ">\n")
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(Journal.class.name).log(Level.SEVERE, null, ex)
        }
    }
}
