package be.dafke.Accounting.BusinessModelDao

import be.dafke.Accounting.BusinessModel.*

import java.util.logging.Level
import java.util.logging.Logger

import static AccountsIO.writeAccounts
import static AllergenesIO.writeAllergenes
import static BalancesIO.writeBalances
import static ContactsIO.writeContacts
import static IngredientOrdersIO.writeIngredientOrders
import static IngredientsIO.writeIngredientes
import static MealOrderIO.writeMealOrders
import static MortgageIO.writeMortgages
import static ProjectsIO.writeProjects
import static PromoOrderIO.writePromoOrders
import static PurchaseOrderIO.writePurchasesOrders
import static SalesOrderIO.writeSalesOrders
import static StockIO.writeStock
import static StockIO.writeStockTransactions
import static StockOrderIO.writeStockOrders
import static VATIO.writeVATFields
import static VATIO.writeVATTransactions
import static be.dafke.Accounting.BusinessModelDao.GoodsIO.writeGoods
import static be.dafke.Accounting.BusinessModelDao.JournalsIO.*
import static be.dafke.Accounting.BusinessModelDao.MealsIO.writeMealsWithAllDetails
import static be.dafke.Accounting.BusinessModelDao.ServicesIO.writeServices
import static be.dafke.Accounting.BusinessModelDao.XMLConstants.*
import static be.dafke.Accounting.BusinessModelDao.XMLReader.*

class XMLWriter {
    static String getXmlHeader(String className, int depth) {
        StringBuilder builder = new StringBuilder("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n")
//        builder.append("<!DOCTYPE ").append(className).append(" SYSTEM \"")
        builder.append """\
<$className xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"
xsi:noNamespaceSchemaLocation=\""""
        for(int i=0;  i<depth; i++){
            builder.append("../")
        }
        builder.append("../xsd/").append(className).append(""".xsd\">
""")
        builder.toString()
    }

    static void writeAccountings(Accountings accountings, boolean writeHtml){
        File accountingsXmlFile = new File(ACCOUNTINGS_XML_FILE)
        accountingsXmlFile.getParentFile().mkdirs()
        try {
            Writer writer = new FileWriter(accountingsXmlFile)
            writer.write getXmlHeader(ACCOUNTINGS, 0)
            for(Accounting accounting:accountings.businessObjects) writer.write """\
  <$ACCOUNTING>
    <$NAME>$accounting.name</$NAME>
    <$VAT_ACCOUNTING>$accounting.vatAccounting</$VAT_ACCOUNTING>
    <$CONTACTS_ACCOUNTING>$accounting.contactsAccounting</$CONTACTS_ACCOUNTING>
    <$TRADE_ACCOUNTING>$accounting.tradeAccounting</$TRADE_ACCOUNTING>
    <$MEAL_ORDER_ACCOUNTING>$accounting.mealsAccounting</$MEAL_ORDER_ACCOUNTING>
    <$PROJECTS_ACCOUNTING>$accounting.projectsAccounting</$PROJECTS_ACCOUNTING>
    <$MORTGAGES_ACCOUNTING>$accounting.mortgagesAccounting</$MORTGAGES_ACCOUNTING>
  </$ACCOUNTING>
"""

            writer.write"""\
</$ACCOUNTINGS>"""
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(Accountings.class.name).log(Level.SEVERE, null, ex)
        }
        if(writeHtml){
            File accountingsHtmlFile = new File(ACCOUNTINGS_HTML_FILE)
            File accountingsXslFile = new File("$XSLPATH/Accountings.xsl")
            XMLtoHTMLWriter.xmlToHtml(accountingsXmlFile, accountingsXslFile, accountingsHtmlFile, null)
        }

        for(Accounting accounting:accountings.businessObjects){
            if(accounting.read)
                writeAccounting(accounting, writeHtml)
                if(writeHtml){
                    File htmlFile = new File(ACCOUNTINGS_HTML_PATH + "/${accounting.name}.html")
                    String text = """\
<html>
    <head>
        <META http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>
            ${accounting.name}
        </title>
    </head>
    <body>
        <h1>${accounting.name}</h1>
        <a href="${accounting.name}/Accounts.html">Accounts</a>
        <a href="${accounting.name}/Journals.html">Journals</a>
    </body
<html>
"""
                    htmlFile.write(text)
                }
        }

        writeActiveAccountings()
    }

    static void writeActiveAccountings() {
        String homeDir = System.getProperty("user.home")
        File homeFolder = new File(homeDir)
        File subFolder = new File(homeFolder, ".Accounting")
        subFolder.mkdirs()
        File xmlFile = new File(subFolder, "Session.xml")

        try {
            Writer writer = new FileWriter(xmlFile)
            writer.write getXmlHeader(SESSION, 0)
            Accounting activeAccounting = Session.activeAccounting
            if(activeAccounting) writer.write"""\
  <$ACTIVE_ACCOUNTING>$activeAccounting.name</$ACTIVE_ACCOUNTING>
"""
            Accountings accountings = Session.accountings
            for(Accounting accounting:accountings.businessObjects) {
                AccountingSession accountingSession = Session.getAccountingSession(accounting)
                Journal activeJournal = accountingSession?accountingSession.activeJournal:null
                writer.write """
  <$ACCOUNTING>
    <$NAME>$accounting.name</$NAME>
    <$SHOW_NUMBERS>${accountingSession?accountingSession.showNumbers:false}</$SHOW_NUMBERS>
    <$ACTIVE_JOURNAL>${(activeJournal?activeJournal.name:"null")}</$ACTIVE_JOURNAL>"""
                Journals journals = accounting.journals
                if(journals){
                    writer.write"""
    <$JOURNALS>"""
                    for(Journal journal:journals.businessObjects) {
                        JournalSession journalSession = accountingSession?accountingSession.getJournalSession(journal):null
                        ArrayList<AccountType> leftCheckedTypes = journalSession?journalSession.getCheckedTypesLeft():[]
                        ArrayList<AccountType> rightCheckedTypes = journalSession?journalSession.getCheckedTypesRight():[]

                        String leftCheckedStream = leftCheckedTypes.name.collect().join(",")
                        String rightCheckedStream = rightCheckedTypes.name.collect().join(",")

                        writer.write """
      <$JOURNAL>
        <$NAME>$journal.name</$NAME>
        <$SHOW_NUMBERS_LEFT>${journalSession?journalSession.showNumbersLeft:false}</$SHOW_NUMBERS_LEFT>
        <$SHOW_NUMBERS_RIGHT>${journalSession?journalSession.showNumbersRight:false}</$SHOW_NUMBERS_RIGHT>
        <$CHECKED_LEFT>$leftCheckedStream</$CHECKED_LEFT>
        <$CHECKED_RIGHT>$rightCheckedStream</$CHECKED_RIGHT>
      </$JOURNAL>"""
                    }
                    writer.write """
    </$JOURNALS>"""
                }
                writer.write """
  </$ACCOUNTING>"""
            }
            writer.write """
</$SESSION>"""
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(Accountings.class.name).log(Level.SEVERE, null, ex)
        }
    }


    static void writeAccounting(Accounting accounting, boolean writeHtml) {
        File accountingFolder = new File("$ACCOUNTINGS_XML_PATH/$accounting.name")
        accountingFolder.mkdirs()
        writeAccounts accounting, writeHtml
        writeTransactions accounting
        writeJournals accounting, writeHtml
        writeJournalTypes accounting
        writeBalances accounting
        if(accounting.projectsAccounting) {
            writeProjects accounting
        }
        if(accounting.vatAccounting) {
            writeVATFields accounting
            writeVATTransactions accounting
        }
        if(accounting.contactsAccounting) {
            writeContacts accounting
        }
        if(accounting.tradeAccounting) {
//            writeArticles accounting
            writeServices accounting
            writeGoods accounting
            writeAllergenes accounting
            writeIngredientes accounting
            writeIngredientOrders accounting
            writeStock accounting
            writeStockTransactions accounting
            writePurchasesOrders accounting
            writeSalesOrders accounting
            writePromoOrders accounting
            writeStockOrders accounting
        }
        if(accounting.mortgagesAccounting) {
            writeMortgages accounting
        }
        if(accounting.mealsAccounting){
            writeMealsWithAllDetails accounting
            writeMealOrders accounting
        }

//        JournalsIO.writeJournalPdfFiles(accounting)
    }

}