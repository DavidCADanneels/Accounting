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
        Accounts accounts = accounting.getAccounts()
        AccountTypes accountTypes = accounting.getAccountTypes()
        File xmlFile = new File(ACCOUNTINGS_XML_FOLDER +accounting.getName()+"/"+ACCOUNTS+XML_EXTENSION)
        Element rootElement = getRootElement(xmlFile, ACCOUNTS)
        for (Element element : getChildren(rootElement, ACCOUNT)) {

            String name = getValue(element, NAME)
            Account account = new Account(name)

            String type = getValue(element, TYPE)
            AccountType accountType = accountTypes.getBusinessObject(type)
            account.setType(accountType)

            String number = getValue(element, NUMBER)
            if(number!=null)
                account.setNumber(parseBigInteger(number))

            String defaultAmount = getValue(element, DEFAULT_AMOUNT)
            if(defaultAmount!=null)
                account.setDefaultAmount(parseBigDecimal(defaultAmount))

            try {
                accounts.addBusinessObject(account)
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace()
            }
        }
    }

    static String createTmpFolder(Accounting accounting) {
        String accountingName = accounting.getName()
        String tmpFolderPath = ACCOUNTINGS_XML_FOLDER + accountingName + "/" + TMP + "/"
        File tempFolder = new File(tmpFolderPath)
        tempFolder.mkdirs()
        tmpFolderPath
    }

    static String createPdfFolder(Accounting accounting) {
        String accountingName = accounting.getName()
        String resultPdfPolderPath = ACCOUNTINGS_PDF_FOLDER + accountingName + "/" + ACCOUNTS + "/"
        File targetFolder = new File(resultPdfPolderPath)
        targetFolder.mkdirs()
        resultPdfPolderPath
    }

    static void writeAccountPdfFiles(Accounting accounting){
        String xslPath = XSLFOLDER + "AccountPdf.xsl"

        String tmpFolderPath = createTmpFolder(accounting)
        String resultPdfPolderPath = createPdfFolder(accounting)

        Accounts accounts = accounting.getAccounts()
        for (Account account:accounts.getBusinessObjects()) {
            if(!account.getBusinessObjects().isEmpty()) {
                writeAccount(account, tmpFolderPath)
            }
        }

        accounts.getBusinessObjects().forEach({ account ->
            if (!account.getBusinessObjects().isEmpty()) {
                try {
                    String fileName = account.getName() + XML_EXTENSION
                    String xmlPath = tmpFolderPath + fileName
                    PDFCreator.convertToPDF(xmlPath, xslPath, resultPdfPolderPath + account.getName() + PDF_EXTENSION)
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
        Accounts accounts = accounting.getAccounts()
        File accountsXmlFile = new File(ACCOUNTINGS_XML_FOLDER + accounting.getName() + "/" + ACCOUNTS+ XML_EXTENSION)
        try{
            Writer writer = new FileWriter(accountsXmlFile)
            writer.write(getXmlHeader(ACCOUNTS, 2))
            for(Account account: accounts.getBusinessObjects()) {
                writer.write(
                        "  <"+ACCOUNT+">\n" +
                                "    <"+NAME+">"+account.getName()+"</"+NAME+">\n" +
                                "    <"+TYPE+">"+account.getType()+"</"+TYPE+">\n"
                )
                BigDecimal defaultAmount = account.getDefaultAmount()
                if(defaultAmount!=null){
                    writer.write(
                            "    <"+DEFAULT_AMOUNT+">"+defaultAmount+"</"+DEFAULT_AMOUNT+">\n"
                    )
                }
                BigInteger number = account.getNumber()
                if(number!=null) {
                    writer.write(
                            "    <"+NUMBER+">"+number+"</"+NUMBER+">\n"
                    )
                }
                writer.write(
                        "  </"+ACCOUNT+">\n"
                )
            }
            writer.write("</"+ACCOUNTS+">\n")
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(Accounts.class.getName()).log(Level.SEVERE, null, ex)
        }
        if(writeHtml){
            writeAllAccounts(accounting, writeHtml)
            File accountsHtmlFile = new File(ACCOUNTINGS_HTML_FOLDER + accounting.getName() + "/" + ACCOUNTS+ HTML_EXTENSION)
            File accountsXslFile = new File(XSLFOLDER + "Accounts.xsl")
            XMLtoHTMLWriter.xmlToHtml(accountsXmlFile,accountsXslFile,accountsHtmlFile, null)
        }

    }

    static void writeAllAccounts(Accounting accounting, boolean writeHtml){
        Accounts accounts =  accounting.getAccounts()
        String path = ACCOUNTINGS_XML_FOLDER + accounting.getName() + "/" + ACCOUNTS
        File accountsXmlFolder = new File(path)
        accountsXmlFolder.mkdirs()
        for (Account account:accounts.getBusinessObjects()) {
            if (!account.getBusinessObjects().isEmpty()) {
                writeAccount(account, path)
                if (writeHtml) {
                    File accountsHtmlFolder = new File(ACCOUNTINGS_HTML_FOLDER + accounting.getName() + "/" + ACCOUNTS)
                    accountsHtmlFolder.mkdirs()
                    File accountHtmlFile = new File(accountsHtmlFolder, account.getName() + HTML_EXTENSION)
                    File accountXmlFile = new File(accountsXmlFolder, account.getName() + XML_EXTENSION)
                    File accountXslFile = new File(XSLFOLDER + "Account.xsl")
                    XMLtoHTMLWriter.xmlToHtml(accountXmlFile, accountXslFile, accountHtmlFile, null)
                }
            }
        }
    }

    private static void writeAccount(Account account, String accountsPath) {
        File accountsFolder = new File(accountsPath)
//        accountsFolder.mkdirs()
        File accountFile = new File(accountsFolder, account.getName()+ XML_EXTENSION)
        try {
            Writer writer = new FileWriter(accountFile)
            writer.write(getXmlHeader(ACCOUNT, 3))
            for(Movement movement:account.getBusinessObjects()){
                Journal journal = movement.getJournal()
                Transaction transaction = movement.getTransaction()
                writer.write(
                        "  <"+MOVEMENT+">\n" +
                                "    <"+TRANSACTION_ID+">"+transaction.getTransactionId()+"</"+TRANSACTION_ID+">\n" +
                                "    <"+ID+">"+movement.getId()+"</"+ID+">\n" +
                                "    <"+DATE+">"+ Utils.toString(movement.getDate())+"</"+DATE+">\n" +
                                "    <"+DESCRIPTION +">"+movement.getDescription()+"</"+DESCRIPTION+">\n" +
                                "    <"+JOURNAL_ABBR+">"+journal.getAbbreviation()+"</"+JOURNAL_ABBR+">\n" +
                                "    <"+JOURNAL_NAME+">"+journal.getName()+"</"+JOURNAL_NAME+">\n" +
                                "    <"+JOURNAL_ID+">"+transaction.getId()+"</"+JOURNAL_ID+">\n"
                )
                if(movement.isDebit()){
                    writer.write(
                            "    <"+DEBIT+">"+movement.getAmount()+"</"+DEBIT+">\n"
                    )
                } else {
                    writer.write(
                            "    <"+CREDIT+">"+movement.getAmount()+"</"+CREDIT+">\n"
                    )
                }
                writer.write("  </"+MOVEMENT+">\n")
            }
            writer.write(   "</"+ACCOUNT+">\n")
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex)
        }
    }
}
