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
import static be.dafke.BusinessModelDao.JournalsIO.writeJournalTypes;
import static be.dafke.BusinessModelDao.JournalsIO.writeJournals;
import static be.dafke.BusinessModelDao.MortgageIO.writeMortgages;
import static be.dafke.BusinessModelDao.ProjectsIO.writeProjects;
import static be.dafke.BusinessModelDao.PurchaseOrderIO.writePurchasesOrders;
import static be.dafke.BusinessModelDao.SalesOrderIO.writeSalesOrders;
import static be.dafke.BusinessModelDao.StockIO.writeStock;
import static be.dafke.BusinessModelDao.VATIO.writeVATFields;
import static be.dafke.BusinessModelDao.VATIO.writeVATTransactions;
import static be.dafke.BusinessModelDao.XMLConstants.*;
import static be.dafke.BusinessModelDao.XMLReader.*;

/**
 * Created by ddanneels on 15/01/2017.
 */
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

    public static void writeAccountings(Accountings accountings, File xmlFolder){
        xmlFolder.mkdirs();
        File xmlFile = new File(xmlFolder, "Accountings.xml");
        try {
            Writer writer = new FileWriter(xmlFile);
            writer.write(getXmlHeader(ACCOUNTINGS, 0));
            for(Accounting accounting:accountings.getBusinessObjects()){
                writer.write(
                    "  <Accounting>\n" +
                        "    <name>"+accounting.getName()+"</name>\n" +
                        "    <"+VAT_ACCOUNTING+">"+accounting.isVatAccounting()+"</"+VAT_ACCOUNTING+">\n" +
                        "    <"+CONTACTS_ACCOUNTING+">"+accounting.isContactsAccounting()+"</"+CONTACTS_ACCOUNTING+">\n" +
                        "    <"+PROJECTS_ACCOUNTING+">"+accounting.isProjectsAccounting()+"</"+PROJECTS_ACCOUNTING+">\n" +
                        "    <"+MORTGAGES_ACCOUNTING+">"+accounting.isMortgagesAccounting()+"</"+MORTGAGES_ACCOUNTING+">\n" +
                        "  </Accounting>\n"
                );
            }

            writer.write("</Accountings>");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Accountings.class.getName()).log(Level.SEVERE, null, ex);
        }

        for(Accounting accounting:accountings.getBusinessObjects()){
            writeAccounting(accounting, xmlFolder);
        }

        writeSession(accountings, xmlFolder);
    }

    private static void writeSession(Accountings accountings, File xmlFolder) {
        File xmlFile = new File(xmlFolder, "Session.xml");
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
                        "  <Accounting>\n" +
                            "    <name>"+accounting.getName()+"</name>\n" +
                            "    <"+ACTIVE_JOURNAL+">"+(activeJournal==null?"null":activeJournal.getName())+"</"+ACTIVE_JOURNAL+">\n");
                Journals journals = accounting.getJournals();
                if(journals !=null){
                    writer.write("    <" + JOURNALS + ">\n");
                    for(Journal journal:journals.getBusinessObjects()) {
                        writer.write(
                        "      <" + JOURNAL + ">\n" +
                            "        <name>"+journal.getName()+"</name>\n" +
                            "        <checkedTypesLeft>"+""+"</checkedTypesLeft>\n" +
                            "        <checkedTypesRight>"+""+"</checkedTypesRight>\n" +
                            "      </" + JOURNAL + ">\n");
                    }
                    writer.write("    </" + JOURNALS + ">\n");
                }
                writer.write("  </Accounting>\n");
            }
            writer.write("  <" + NEXT_VAT_ID + ">" + VATTransaction.getCount() + "</" + NEXT_VAT_ID + ">\n");

            writer.write("</"+SESSION+">");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Accountings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    private static void writeAccounting(Accounting accounting, File xmlFolder) {
        File accountingsFolder = new File(xmlFolder, "Accountings");
        File accountingFolder = new File(accountingsFolder, accounting.getName());
        accountingFolder.mkdirs();
        writeAccounts(accounting);
        writeJournals(accounting);
        writeJournalTypes(accounting.getJournalTypes(), accountingFolder);
        writeBalances(accounting.getBalances(), accountingFolder);
        if(accounting.isProjectsAccounting()) {
            writeProjects(accounting.getProjects(), accountingFolder);
        }
        if(accounting.isVatAccounting()) {
            writeVATFields(accounting.getVatFields(), accountingFolder);
            writeVATTransactions(accounting.getVatTransactions(), accountingFolder);
        }
        if(accounting.isContactsAccounting()) {
            writeContacts(accounting.getContacts(), accounting.getCompanyContact(), accountingFolder);
            writeArticles(accounting.getArticles(), accountingFolder);
            writeStock(accounting.getStock(), accountingFolder);
            writePurchasesOrders(accounting.getPurchaseOrders(), accountingFolder);
            writeSalesOrders(accounting.getSalesOrders(), accountingFolder);
        }
        if(accounting.isMortgagesAccounting()) {
            writeMortgages(accounting.getMortgages(), accountingFolder);
        }

//        JournalsIO.writeJournalPdfFiles(accounting.getJournals(), accountingFolder, accounting.getName());
    }

}
