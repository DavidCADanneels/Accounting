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
        File xmlFile = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$JOURNAL_TYPES$XML_EXTENSION")
        Element rootElement = getRootElement(xmlFile, JOURNAL_TYPES)
        for (Element element : getChildren(rootElement, JOURNAL_TYPE)) {

            String name = getValue(element, NAME)
            JournalType journalType = new JournalType(name)
            // Do not add default types here, they are read, right below

            String taxString = getValue(element, VATTYPE)
            if(taxString) journalType.setVatType(VATTransaction.VATType.valueOf(taxString))

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

        if(typesString) {
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

        File xmlFile = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$JOURNALS$XML_EXTENSION")
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

            String baseAccountName = getValue(element, BASE_ACCOUNT)
            if(baseAccountName){
                journal.baseAccount = accounting.accounts.getBusinessObject(baseAccountName)
            }

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
        File xmlFile = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$JOURNALS/$name$XML_EXTENSION")
        Element rootElement = getRootElement(xmlFile, JOURNAL)
        for (Element element: getChildren(rootElement, TRANSACTION)) {
            int id = parseInt(getValue(element, TRANSACTION_ID))

            Transaction transaction = transactions.getBusinessObject(id)
            if (transaction == null){
                System.err.println("id($id)not found")
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
        File xmlFile = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$TRANSACTIONS$XML_EXTENSION")
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

            // Not needed here: links are set while parsing PurchaseOrders and SalesOrders
//            String purchaseOrder = getValue(element, PURCHASE_ORDER)
//            if(purchaseOrder){
//                PurchaseOrder order = accounting.purchaseOrders.getBusinessObject(purchaseOrder)
//                transaction.order = order
//            }
//
//            String salesOrder = getValue(element, SALES_ORDER)
//            if(salesOrder) {
//                SalesOrder order = accounting.salesOrders.getBusinessObject(salesOrder)
//                transaction.order = order
//            }

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
                        if (vatField == null) System.err.println("Field[$vatFieldString] not found")
                        String amountString = getValue(vatBookingsElement, AMOUNT)
                        BigDecimal vatAmount = new BigDecimal(amountString)
                        if(amount.compareTo(vatAmount)!=0){
                            System.err.println("Difference: ${amount.toString()} | ${vatAmount.toString()}")
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
        File journalTypesFile = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$JOURNAL_TYPES$XML_EXTENSION")
        try {
            Writer writer = new FileWriter(journalTypesFile)
            writer.write getXmlHeader(JOURNAL_TYPES, 2)
            for (JournalType journalType : journalTypes.businessObjects) {

                AccountsList left = journalType.getLeft()
                AccountsList right = journalType.getRight()

                ArrayList<AccountType> leftAccountTypes = left.accountTypes
                ArrayList<AccountType> rightAccountTypes = right.accountTypes

                String leftStream = leftAccountTypes.name.stream().sorted().collect(Collectors.joining(","))
                String rightStream = rightAccountTypes.name.stream().sorted().collect(Collectors.joining(","))

                writer.write """\
  <$JOURNAL_TYPE>
    <$NAME>$journalType.name</$NAME>
    <$VATTYPE>${(journalType.getVatType()==null?"null":journalType.getVatType().toString())}</$VATTYPE>
    <$LEFT_LIST>
      <$LEFT_ACTION>$left.leftAction</$LEFT_ACTION>
      <$LEFT_BUTTON>$left.leftButton</$LEFT_BUTTON>
      <$RIGHT_ACTION>$left.rightAction</$RIGHT_ACTION>
      <$RIGHT_BUTTON>$left.rightButton</$RIGHT_BUTTON>
      <$VATTYPE>$journalType.leftVatType</$VATTYPE>
      <$SINGLE_ACCOUNT>$left.singleAccount</$SINGLE_ACCOUNT>
      <$ACCOUNT>$left.account</$ACCOUNT>
      <$TYPES>$leftStream</$TYPES>
    </$LEFT_LIST>
    <$RIGHT_LIST>
      <$LEFT_ACTION>$right.leftAction</$LEFT_ACTION>
      <$LEFT_BUTTON>$right.leftButton</$LEFT_BUTTON>
      <$RIGHT_ACTION>$right.rightAction</$RIGHT_ACTION>
      <$RIGHT_BUTTON>$right.rightButton</$RIGHT_BUTTON>
      <$VATTYPE>$journalType.rightVatType</$VATTYPE>
      <$SINGLE_ACCOUNT>$right.singleAccount</$SINGLE_ACCOUNT>
      <$ACCOUNT>$right.account</$ACCOUNT>
      <$TYPES>$rightStream</$TYPES>
    </$RIGHT_LIST>
  </$JOURNAL_TYPE>
"""
            }
            writer.write("</$JOURNAL_TYPES>\n")
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(JournalTypes.class.name).log(Level.SEVERE, null, ex)
        }
    }

    static String createTmpPath(Accounting accounting) {
        String accountingName = accounting.name
        String tmpFolderPath = "$ACCOUNTINGS_XML_PATH/$accountingName/$TMP"
        File tempFolder = new File(tmpFolderPath)
        tempFolder.mkdirs()
        tmpFolderPath
    }

    static String createTmpFolder(Accounting accounting) {
        String accountingName = accounting.name
        String tmpFolderPath = "$ACCOUNTINGS_XML_PATH/$accountingName/$TMP/"
        File tempFolder = new File(tmpFolderPath)
        tempFolder.mkdirs()
        tmpFolderPath
    }

    static String createPdfPath(Accounting accounting) {
        String accountingName = accounting.name
        String resultPdfPolderPath = "$PDFPATH/$accountingName/$JOURNALS"
        File targetFolder = new File(resultPdfPolderPath)
        targetFolder.mkdirs()
        resultPdfPolderPath
    }

    static void writeJournalPdfFiles(Accounting accounting){
        String xslPath = "$XSLPATH/JournalPdf.xsl"

        String tmpFolderPath = createTmpPath(accounting)
        String resultPdfPolderPath = createPdfPath(accounting)

        Journals journals = accounting.journals
        for (Journal journal:journals.businessObjects) {
            if(!journal.businessObjects.isEmpty()) {
                writeJournal(journal, tmpFolderPath, true)
            }
        }

        journals.businessObjects.forEach({ journal ->
            if (!journal.businessObjects.isEmpty()) {
                try {
                    String fileName = "$journal.name$XML_EXTENSION"
                    String xmlPath = "$tmpFolderPath/$fileName"
                    PDFCreator.convertToPDF(xmlPath, xslPath, "$resultPdfPolderPath/$journal.name$PDF_EXTENSION")
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
        String path = "$ACCOUNTINGS_XML_PATH/$accounting.name/$JOURNALS"
        File journalsXmlFile = new File("$path$XML_EXTENSION")
        try {
            Writer writer = new FileWriter(journalsXmlFile)
            writer.write getXmlHeader(JOURNALS, 2)
            for (Journal journal : journals.businessObjects) writer.write """\
  <$JOURNAL>
    <$NAME>$journal.name</$NAME>
    <$BASE_ACCOUNT>$journal.baseAccount</$BASE_ACCOUNT>
    <$ABBREVIATION>$journal.abbreviation</$ABBREVIATION>
    <$TYPE>$journal.type</$TYPE>
  </$JOURNAL>
"""
            writer.write """\
</$JOURNALS>
"""
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(Journals.class.name).log(Level.SEVERE, null, ex)
        }
        for (Journal journal:journals.businessObjects) {
            writeJournal(journal, path, false)
        }

        if(writeHtml){
            File journalsHtmlFile = new File("$ACCOUNTINGS_HTML_PATH/$accounting.name/$JOURNALS$HTML_EXTENSION")
            File journalsXslFile = new File("$XSLPATH/Journals.xsl")
            XMLtoHTMLWriter.xmlToHtml(journalsXmlFile,journalsXslFile,journalsHtmlFile, null)

            String tmpFolderPath = createTmpPath(accounting)
            File journalsHtmlFolder = new File("$ACCOUNTINGS_HTML_PATH/$accounting.name/$JOURNALS")
            journalsHtmlFolder.mkdirs()

            for (Journal journal:journals.businessObjects) {
                writeJournal(journal, tmpFolderPath, true)
                File journalXmlFile = new File("$tmpFolderPath/$journal.name$XML_EXTENSION")
                File journalHtmlFile = new File(journalsHtmlFolder, "$journal.name$HTML_EXTENSION")
                File journalXslFile = new File("$XSLPATH/Journal.xsl")
                XMLtoHTMLWriter.xmlToHtml(journalXmlFile, journalXslFile, journalHtmlFile, null)
            }
        }
    }

    static void writeJournal(Journal journal, String path, boolean details){
        File journalsFolder = new File(path)
        journalsFolder.mkdirs()
        File journalFile = new File(path, "$journal.name$XML_EXTENSION")
        try {
            Writer writer = new FileWriter(journalFile)
            writer.write getXmlHeader(JOURNAL, 3)
            writer.write """\
  <$NAME>$journal.name</$NAME>
  <$ABBREVIATION>$journal.abbreviation</$ABBREVIATION>
  <$TYPE>$journal.type</$TYPE>"""
            for (Transaction transaction : journal.businessObjects) {
                writer.write """
  <$TRANSACTION>"""

                Journal transactionJournal = transaction.journal
                if (transactionJournal == journal) writer.write """
    <$ID>$transaction.id</$ID>"""
                else writer.write """
    <$ID>${journal.getId(transaction)}</$ID>
    <$ORIGINAL_JOURNAL>$transaction.abbreviation</$ORIGINAL_JOURNAL>
    <$ORIGINAL_ID>$transaction.id</$ORIGINAL_ID>"""

                writer.write """
    <$TRANSACTION_ID>$transaction.transactionId</$TRANSACTION_ID>"""

                if(transaction.balanceTransaction) writer.write"""
    <$BALANCE_TRANSACTION>true</$BALANCE_TRANSACTION>"""
                if(details){
//                    TODO: move all to method
//                    writeDetails()
                    writer.write """
    <$DATE>${Utils.toString(transaction.date)}</$DATE>
    <$DESCRIPTION>$transaction.description</$DESCRIPTION>"""

                    for (Booking booking : transaction.businessObjects) {
                        writer.write """
    <$BOOKING>
      <MovementId>$booking.id</MovementId>
      <$ACCOUNT>$booking.account</$ACCOUNT>"""
                        Movement movement = booking.getMovement()
                        if (movement.debit) writer.write """
      <$DEBIT>$movement.amount</$DEBIT>"""
                        else writer.write"""
      <$CREDIT>$movement.amount</$CREDIT>"""

                        ArrayList<VATBooking> vatBookings = booking.vatBookings
                        for (VATBooking vatBooking : vatBookings) {
                            VATField vatField = vatBooking.vatField
                            VATMovement vatMovement = vatBooking.vatMovement
                            writer.write"""
      <$VATFIELD>$vatField.name</$VATFIELD>"""
                        }
                        writer.write"""
    </$BOOKING>"""
                    }
                }
                writer.write"""
  </$TRANSACTION>"""
            }
            writer.write"""
</$JOURNAL>
"""
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(Journal.class.name).log(Level.SEVERE, null, ex)
        }
    }

    static void writeTransactions(Accounting accounting){
        Transactions transactions = accounting.transactions
        File transactionsFile = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$TRANSACTIONS$XML_EXTENSION")
        try {
            Writer writer = new FileWriter(transactionsFile)
            writer.write getXmlHeader(TRANSACTIONS, 2)
            for (Transaction transaction : transactions.businessObjects) {
                writer.write"""\
  <$TRANSACTION>
    <$TRANSACTION_ID>$transaction.transactionId</$TRANSACTION_ID>
    <$DATE>${Utils.toString(transaction.date)}</$DATE>
    <$DESCRIPTION>$transaction.description</$DESCRIPTION>
    <$JOURNAL>$transaction.abbreviation</$JOURNAL>
    <$JOURNAL_ID>$transaction.id</$JOURNAL_ID>"""
                if(transaction.order){
                    if(transaction.order instanceof PurchaseOrder)
                    writer.write """
    <$PURCHASE_ORDER>$transaction.order</$PURCHASE_ORDER>"""
                    if(transaction.order instanceof SalesOrder)
                    writer.write """
    <$SALES_ORDER>$transaction.order</$SALES_ORDER>"""
                }
                for(Journal journal : transaction.duplicateJournals) writer.write """
    <$DUPLICATE_JOURNAL>$journal.abbreviation</$DUPLICATE_JOURNAL>
        <$DUPLICATE_JOURNAL_ID>${journal.getId(transaction)}</$DUPLICATE_JOURNAL_ID>"""

//                TODO: reuse method writeDetails()
                if(transaction.balanceTransaction) writer.write"""
    <$BALANCE_TRANSACTION>true</$BALANCE_TRANSACTION>"""

                writer.write"""
    <$REGISTERED>$transaction.registered</$REGISTERED>"""
                for (Booking booking : transaction.businessObjects) {
                    writer.write """
    <$BOOKING>
      <$ID>$booking.id</$ID>
      <$ACCOUNT>$booking.account</$ACCOUNT>"""

                    Movement movement = booking.getMovement()
                    if (movement.debit) writer.write"""
      <$DEBIT>$movement.amount</$DEBIT>"""
                    else writer.write"""
      <$CREDIT>$movement.amount</$CREDIT>"""
                    ArrayList<VATBooking> vatBookings = booking.vatBookings
                    for (VATBooking vatBooking : vatBookings) {
                        VATField vatField = vatBooking.vatField
                        VATMovement vatMovement = vatBooking.vatMovement
                        writer.write"""
      <$VATBOOKING>
        <$VATFIELD>$vatField.name</$VATFIELD>
        <$AMOUNT>$vatMovement.amount</$AMOUNT>
      </$VATBOOKING>"""
                    }
                    writer.write"""
    </$BOOKING>"""
                }
//                TODO: end of method writeDetails(transaction)
                writer.write"""
  </$TRANSACTION>
"""
            }
            writer.write"""\
</$TRANSACTIONS>
"""
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(Journal.class.name).log(Level.SEVERE, null, ex)
        }
    }
}
