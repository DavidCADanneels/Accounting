package be.dafke.BusinessModelDao;

import be.dafke.BusinessModel.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static be.dafke.BusinessModelDao.AccountsIO.writeAccounts;
import static be.dafke.BusinessModelDao.ArticlesIO.writeArticles;
import static be.dafke.BusinessModelDao.BalancesIO.writeBalances;
import static be.dafke.BusinessModelDao.ContactsIO.writeContacts;
import static be.dafke.BusinessModelDao.IngredientsIO.writeIngredientes;
import static be.dafke.BusinessModelDao.JournalsIO.writeJournalTypes;
import static be.dafke.BusinessModelDao.JournalsIO.writeJournals;
import static be.dafke.BusinessModelDao.MealOrderIO.writeMealOrders;
import static be.dafke.BusinessModelDao.MealsIO.writeMeals;
import static be.dafke.BusinessModelDao.MortgageIO.writeMortgages;
import static be.dafke.BusinessModelDao.ProjectsIO.writeProjects;
import static be.dafke.BusinessModelDao.JournalsIO.writeTransactions;
import static be.dafke.BusinessModelDao.PromoOrderIO.writePromoOrders;
import static be.dafke.BusinessModelDao.PurchaseOrderIO.writePurchasesOrders;
import static be.dafke.BusinessModelDao.SalesOrderIO.writeSalesOrders;
import static be.dafke.BusinessModelDao.StockIO.writeStock;
import static be.dafke.BusinessModelDao.StockIO.writeStockTransactions;
import static be.dafke.BusinessModelDao.StockOrderIO.writeStockOrders;
import static be.dafke.BusinessModelDao.VATIO.writeVATFields;
import static be.dafke.BusinessModelDao.VATIO.writeVATTransactions;
import static be.dafke.BusinessModelDao.XMLConstants.*;
import static be.dafke.BusinessModelDao.XMLReader.*;

public class XMLWriter {
    public static String getXmlHeader(String className, int depth) {
        StringBuilder builder = new StringBuilder("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");

//        builder.append("<!DOCTYPE ").append(className).append(" SYSTEM \"");

        builder.append("<"+className+" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "xsi:noNamespaceSchemaLocation=\"");
        for(int i=0 ; i<depth; i++){
            builder.append("../");
        }
        builder.append("../xsd/").append(className).append(".xsd\">\n");
        return builder.toString();
    }

    public static void writeAccountings(Accountings accountings, boolean writeHtml){
        File accountingsXmlFile = new File(ACCOUNTINGS_XML_FILE);
        accountingsXmlFile.getParentFile().mkdirs();
        try {
            Writer writer = new FileWriter(accountingsXmlFile);
            writer.write(getXmlHeader(ACCOUNTINGS, 0));
            for(Accounting accounting:accountings.getBusinessObjects()){
                writer.write(
                    "  <" + ACCOUNTING + ">\n" +
                        "    <" + NAME + ">"+accounting.getName()+"</" + NAME + ">\n" +
                        "    <"+VAT_ACCOUNTING+">"+accounting.isVatAccounting()+"</"+VAT_ACCOUNTING+">\n" +
                        "    <"+CONTACTS_ACCOUNTING+">"+accounting.isContactsAccounting()+"</"+CONTACTS_ACCOUNTING+">\n" +
                        "    <"+TRADE_ACCOUNTING+">"+accounting.isTradeAccounting()+"</"+TRADE_ACCOUNTING+">\n" +
                        "    <"+DELIVEROO_ACCOUNTING+">"+accounting.isDeliverooAccounting()+"</"+DELIVEROO_ACCOUNTING+">\n" +
                        "    <"+PROJECTS_ACCOUNTING+">"+accounting.isProjectsAccounting()+"</"+PROJECTS_ACCOUNTING+">\n" +
                        "    <"+MORTGAGES_ACCOUNTING+">"+accounting.isMortgagesAccounting()+"</"+MORTGAGES_ACCOUNTING+">\n" +
                        "  </" + ACCOUNTING + ">\n"
                );
            }

            writer.write("</Accountings>");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Accountings.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(writeHtml){
            File accountingsHtmlFile = new File(ACCOUNTINGS_HTML_FILE);
            File accountingsXslFile = new File(XSLFOLDER + "Accountings.xsl");
            XMLtoHTMLWriter.xmlToHtml(accountingsXmlFile, accountingsXslFile, accountingsHtmlFile, null);
        }

        for(Accounting accounting:accountings.getBusinessObjects()){
            if(accounting.isRead())
                writeAccounting(accounting, writeHtml);
        }

        writeActiveAccountings(accountings);
    }

    private static void writeActiveAccountings(Accountings accountings) {
        File xmlFile = new File(SESSION_FILE);
        try {
            Writer writer = new FileWriter(xmlFile);
            writer.write(getXmlHeader(SESSION, 0));
            Accounting currentObject = Accountings.getActiveAccounting();
            if(currentObject!=null) {
                writer.write("  <" + ACTIVE_ACCOUNTING + ">" + currentObject.getName() + "</" + ACTIVE_ACCOUNTING + ">\n");
            }
            for(Accounting accounting:accountings.getBusinessObjects()) {
                Journal activeJournal = accounting.getActiveJournal();
                writer.write(
                        "  <" + ACCOUNTING + ">\n" +
                            "    <" + NAME + ">"+accounting.getName()+"</" + NAME + ">\n" +
                            "    <"+ACTIVE_JOURNAL+">"+(activeJournal==null?"null":activeJournal.getName())+"</"+ACTIVE_JOURNAL+">\n");
//                Journals journals = accounting.getJournals();
//                if(journals !=null){
//                    writer.write("    <" + JOURNALS + ">\n");
//                    for(Journal journal:journals.getBusinessObjects()) {
//                        writer.write(
//                        "      <" + JOURNAL + ">\n" +
//                            "        <"+ NAME + ">"+journal.getName()+"</"+ NAME + ">\n" +
//                            "        <checkedTypesLeft>"+""+"</checkedTypesLeft>\n" +
//                            "        <checkedTypesRight>"+""+"</checkedTypesRight>\n" +
//                            "      </" + JOURNAL + ">\n");
//                    }
//                    writer.write("    </" + JOURNALS + ">\n");
//                }
                writer.write("  </"+ ACCOUNTING + ">\n");
            }
            writer.write("</"+SESSION+">");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Accountings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public static void writeAccounting(Accounting accounting, boolean writeHtml) {
        File accountingFolder = new File(ACCOUNTINGS_XML_FOLDER + accounting.getName());
        accountingFolder.mkdirs();
        writeAccounts(accounting, writeHtml);
        writeTransactions(accounting);
        writeJournals(accounting, writeHtml);
        writeJournalTypes(accounting);
        writeBalances(accounting);
        if(accounting.isProjectsAccounting()) {
            writeProjects(accounting);
        }
        if(accounting.isVatAccounting()) {
            writeVATFields(accounting);
            writeVATTransactions(accounting);
        }
        if(accounting.isContactsAccounting()) {
            writeContacts(accounting);
        }
        if(accounting.isTradeAccounting()) {
            writeArticles(accounting);
            writeIngredientes(accounting);
            writeStock(accounting);
            writeStockTransactions(accounting);
            writePurchasesOrders(accounting);
            writeSalesOrders(accounting);
            writePromoOrders(accounting);
            writeStockOrders(accounting);
        }
        if(accounting.isMortgagesAccounting()) {
            writeMortgages(accounting);
        }
        if(accounting.isDeliverooAccounting()){
            writeMeals(accounting);
            writeMealOrders(accounting);
        }

//        JournalsIO.writeJournalPdfFiles(accounting);
    }

}
