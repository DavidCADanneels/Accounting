package be.dafke.Accounting.BusinessModelDao

import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.BusinessModelDao.XMLtoHTMLWriter
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.Utils.Utils
import org.apache.fop.apps.FOPException
import org.w3c.dom.Element

import javax.xml.transform.TransformerException
import java.util.logging.Level
import java.util.logging.Logger

import static be.dafke.Accounting.BusinessModelDao.XMLConstants.*
import static be.dafke.Accounting.BusinessModelDao.XMLReader.*
import static be.dafke.Accounting.BusinessModelDao.XMLWriter.getXmlHeader
import static be.dafke.Utils.Utils.parseBigDecimal
import static be.dafke.Utils.Utils.parseBigInteger

class AccountsIO {
    static void readAccounts(Accounting accounting){
        Accounts accounts = accounting.accounts
        AccountTypes accountTypes = accounting.accountTypes
        File xmlFile = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$ACCOUNTS$XML_EXTENSION")
        Element rootElement = getRootElement xmlFile, ACCOUNTS
        for (Element element : getChildren(rootElement, ACCOUNT)) {

            String name = getValue element, NAME
            Account account = new Account(name)

            String type = getValue element, TYPE
            AccountType accountType = accountTypes.getBusinessObject(type)
            account.type = accountType

            String number = getValue(element, NUMBER)
            if(number)
                account.number = parseBigInteger number

            String defaultAmount = getValue element, DEFAULT_AMOUNT
            if(defaultAmount)
                account.defaultAmount = parseBigDecimal(defaultAmount)

            try {
                accounts.addBusinessObject account
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace()
            }
        }
    }

    static String createTmpFolder(Accounting accounting) {
        String accountingName = accounting.name
        String tmpFolderPath = "$ACCOUNTINGS_XML_PATH/$accountingName/$TMP/"
        File tempFolder = new File(tmpFolderPath)
        tempFolder.mkdirs()
        tmpFolderPath
    }

    static String createPdfFolder(Accounting accounting) {
        String accountingName = accounting.name
        String resultPdfPolderPath = "$PDFPATH/$accountingName/$ACCOUNTS/"
        File targetFolder = new File(resultPdfPolderPath)
        targetFolder.mkdirs()
        resultPdfPolderPath
    }

    static void writeAccountPdfFiles(Accounting accounting){
        String xslPath = "$XSLPATH/AccountPdf.xsl"

        String tmpFolderPath = createTmpFolder(accounting)
        String resultPdfPolderPath = createPdfFolder(accounting)

        Accounts accounts = accounting.accounts
        for (Account account:accounts.businessObjects) {
            if(!account.businessObjects.isEmpty()) {
                writeAccount(account, tmpFolderPath)
            }
        }

        accounts.businessObjects.forEach({ account ->
            if (!account.businessObjects.isEmpty()) {
                try {
                    String fileName = "$account.name$XML_EXTENSION"
                    String xmlPath = "$tmpFolderPath$fileName"
                    PDFCreator.convertToPDF xmlPath, xslPath, "$resultPdfPolderPath$account.name$PDF_EXTENSION"
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

    static void writeAccounts(Accounting accounting, boolean writeHtml){
        Accounts accounts = accounting.accounts
        File accountsXmlFile = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$ACCOUNTS$XML_EXTENSION")
        try{
            Writer writer = new FileWriter(accountsXmlFile)
            writer.write getXmlHeader(ACCOUNTS, 2)
            for(Account account: accounts.businessObjects) {
                writer.write """\
  <$ACCOUNT>
    <$NAME>$account.name</$NAME>
    <$TYPE>$account.type</$TYPE>"""

                BigDecimal defaultAmount = account.defaultAmount
                if(defaultAmount) writer.write """
    <$DEFAULT_AMOUNT>$defaultAmount</$DEFAULT_AMOUNT>"""

                BigInteger number = account.number
                if(number) writer.write """
    <$NUMBER>$number</$NUMBER>"""

                writer.write """
  </$ACCOUNT>
"""
            }
            writer.write """\
</$ACCOUNTS>
"""
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(Accounts.class.name).log(Level.SEVERE, null, ex)
        }
        if(writeHtml){
            writeAllAccounts(accounting, writeHtml)
            File accountsHtmlFile = new File("$ACCOUNTINGS_HTML_PATH/$accounting.name/$ACCOUNTS$HTML_EXTENSION")
            File accountsXslFile = new File("$XSLPATH/Accounts.xsl")
            XMLtoHTMLWriter.xmlToHtml(accountsXmlFile,accountsXslFile,accountsHtmlFile, null)
        }

    }

    static void writeAllAccounts(Accounting accounting, boolean writeHtml){
        Accounts accounts =  accounting.accounts
        String path = "$ACCOUNTINGS_XML_PATH/$accounting.name/$ACCOUNTS"
        File accountsXmlFolder = new File(path)
        accountsXmlFolder.mkdirs()
        for (Account account:accounts.businessObjects) {
            if (!account.businessObjects.isEmpty()) {
                writeAccount(account, path)
                if (writeHtml) {
                    File accountsHtmlFolder = new File("$ACCOUNTINGS_HTML_PATH/$accounting.name/$ACCOUNTS")
                    accountsHtmlFolder.mkdirs()
                    File accountHtmlFile = new File(accountsHtmlFolder, "$account.name$HTML_EXTENSION")
                    File accountXmlFile = new File(accountsXmlFolder, "$account.name$XML_EXTENSION")
                    File accountXslFile = new File("$XSLPATH/Account.xsl")
                    XMLtoHTMLWriter.xmlToHtml(accountXmlFile, accountXslFile, accountHtmlFile, null)
                }
            }
        }
    }

    static void writeAccount(Account account, String accountsPath) {
        File accountsFolder = new File(accountsPath)
//        accountsFolder.mkdirs()
        File accountFile = new File(accountsFolder, "$account.name$XML_EXTENSION")
        try {
            Writer writer = new FileWriter(accountFile)
            writer.write getXmlHeader(ACCOUNT, 3)
            for(Movement movement:account.businessObjects){
                Journal journal = movement.journal
                Transaction transaction = movement.transaction
                writer.write """\
  <$MOVEMENT>
    <$TRANSACTION_ID>$transaction.transactionId</$TRANSACTION_ID>
    <$ID>$movement.id</$ID">
    <$DATE>${Utils.toString(movement.date)}</$DATE>
    <$DESCRIPTION>$movement.description</$DESCRIPTION>
    <$JOURNAL_ABBR>$journal.abbreviation</$JOURNAL_ABBR>
    <$JOURNAL_NAME>$journal.name</$JOURNAL_NAME>
    <$JOURNAL_ID>$transaction.id</$JOURNAL_ID>"""

                if(movement.debit){
                    writer.write """
    <$DEBIT>$movement.amount</$DEBIT>"""
                } else {
                    writer.write """
    <$CREDIT>$movement.amount</$CREDIT>"""
                }

                writer.write"""
  </$MOVEMENT>"""
            }
            writer.write   """
</$ACCOUNT>
"""
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(Account.class.name).log(Level.SEVERE, null, ex)
        }
    }
}
