package be.dafke.Accounting.BusinessModelDao

import be.dafke.Accounting.BusinessModel.*

import java.util.logging.Level
import java.util.logging.Logger
import java.util.stream.Collectors

import static XMLReader.*
import static AccountsIO.writeAccounts
import static AllergenesIO.writeAllergenes
import static ArticlesIO.writeArticles
import static BalancesIO.writeBalances
import static ContactsIO.writeContacts
import static IngredientOrdersIO.writeIngredientOrders
import static IngredientsIO.writeIngredientes
import static JournalsIO.*
import static MealOrderIO.writeMealOrders
import static MealsIO.writeMeals
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
import static XMLConstants.*

class XMLWriter {
    static String getXmlHeader(String className, int depth) {
        StringBuilder builder = new StringBuilder("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n")

//        builder.append("<!DOCTYPE ").append(className).append(" SYSTEM \"")

        builder.append("<"+className+" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "xsi:noNamespaceSchemaLocation=\"")
        for(int i=0;  i<depth; i++){
            builder.append("../")
        }
        builder.append("../xsd/").append(className).append(".xsd\">\n")
        builder.toString()
    }

    static void writeAccountings(Accountings accountings, boolean writeHtml){
        File accountingsXmlFile = new File(ACCOUNTINGS_XML_FILE)
        accountingsXmlFile.getParentFile().mkdirs()
        try {
            Writer writer = new FileWriter(accountingsXmlFile)
            writer.write(getXmlHeader(ACCOUNTINGS, 0))
            for(Accounting accounting:accountings.businessObjects){
                writer.write(
                        "  <" + ACCOUNTING + ">\n" +
                                "    <" + NAME + ">"+accounting.name+"</" + NAME + ">\n" +
                                "    <"+VAT_ACCOUNTING+">"+accounting.vatAccounting+"</"+VAT_ACCOUNTING+">\n" +
                                "    <"+CONTACTS_ACCOUNTING+">"+accounting.contactsAccounting+"</"+CONTACTS_ACCOUNTING+">\n" +
                                "    <"+TRADE_ACCOUNTING+">"+accounting.tradeAccounting+"</"+TRADE_ACCOUNTING+">\n" +
                                "    <"+ MEAL_ORDER_ACCOUNTING +">"+accounting.mealsAccounting+"</"+ MEAL_ORDER_ACCOUNTING +">\n" +
                                "    <"+PROJECTS_ACCOUNTING+">"+accounting.projectsAccounting+"</"+PROJECTS_ACCOUNTING+">\n" +
                                "    <"+MORTGAGES_ACCOUNTING+">"+accounting.mortgagesAccounting+"</"+MORTGAGES_ACCOUNTING+">\n" +
                                "  </" + ACCOUNTING + ">\n"
                )
            }

            writer.write("</Accountings>")
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(Accountings.class.name).log(Level.SEVERE, null, ex)
        }
        if(writeHtml){
            File accountingsHtmlFile = new File(ACCOUNTINGS_HTML_FILE)
            File accountingsXslFile = new File(XSLFOLDER + "Accountings.xsl")
            XMLtoHTMLWriter.xmlToHtml(accountingsXmlFile, accountingsXslFile, accountingsHtmlFile, null)
        }

        for(Accounting accounting:accountings.businessObjects){
            if(accounting.read)
                writeAccounting(accounting, writeHtml)
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
            writer.write(getXmlHeader(SESSION, 0))
            Accounting currentObject = Session.activeAccounting
            if(currentObject!=null) {
                writer.write("  <" + ACTIVE_ACCOUNTING + ">" + currentObject.name + "</" + ACTIVE_ACCOUNTING + ">\n")
            }
            Accountings accountings = Session.accountings
            for(Accounting accounting:accountings.businessObjects) {
                AccountingSession accountingSession = Session.getAccountingSession(accounting)
                Journal activeJournal = accountingSession.activeJournal
                writer.write(
                        "  <" + ACCOUNTING + ">\n" +
                                "    <" + NAME + ">"+accounting.name+"</" + NAME + ">\n" +
                                "    <"+ACTIVE_JOURNAL+">"+(activeJournal==null?"null":activeJournal.name)+"</"+ACTIVE_JOURNAL+">\n")
                Journals journals = accounting.journals
                if(journals !=null){
                    writer.write("    <" + JOURNALS + ">\n")
                    for(Journal journal:journals.businessObjects) {
                        JournalSession journalSession = accountingSession.getJournalSession(journal)
                        ArrayList<AccountType> leftCheckedTypes = journalSession.getCheckedTypesLeft()
                        ArrayList<AccountType> rightCheckedTypes = journalSession.getCheckedTypesRight()

                        String leftCheckedStream = leftCheckedTypes.name.collect().join(",")
                        String rightCheckedStream = rightCheckedTypes.name.collect().join(",")

                        writer.write(
                                "      <" + JOURNAL + ">\n" +
                                        "        <"+ NAME + ">"+journal.name+"</"+ NAME + ">\n" +
                                        "        <"+CHECKED_LEFT+">"+leftCheckedStream+"</"+CHECKED_LEFT+">\n" +
                                        "        <"+CHECKED_RIGHT+">"+rightCheckedStream+"</"+CHECKED_RIGHT+">\n" +
                                        "      </" + JOURNAL + ">\n")
                    }
                    writer.write("    </" + JOURNALS + ">\n")
                }
                writer.write("  </"+ ACCOUNTING + ">\n")
            }
            writer.write("</"+SESSION+">")
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(Accountings.class.name).log(Level.SEVERE, null, ex)
        }
    }


    static void writeAccounting(Accounting accounting, boolean writeHtml) {
        File accountingFolder = new File(ACCOUNTINGS_XML_FOLDER + accounting.name)
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
            writeArticles accounting
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
            writeMeals accounting
            writeMealOrders accounting
        }

//        JournalsIO.writeJournalPdfFiles(accounting)
    }

}