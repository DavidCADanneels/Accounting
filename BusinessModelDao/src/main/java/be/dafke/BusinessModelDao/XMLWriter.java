package be.dafke.BusinessModelDao;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Accountings;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static be.dafke.BusinessModelDao.AccountsIO.writeAccounts;
import static be.dafke.BusinessModelDao.BalancesIO.writeBalances;
import static be.dafke.BusinessModelDao.ContactsIO.writeContacts;
import static be.dafke.BusinessModelDao.JournalsIO.writeJournalTypes;
import static be.dafke.BusinessModelDao.JournalsIO.writeJournals;
import static be.dafke.BusinessModelDao.MortgageIO.writeMortgages;
import static be.dafke.BusinessModelDao.ProjectsIO.writeProjects;
import static be.dafke.BusinessModelDao.VATIO.writeVATFields;
import static be.dafke.BusinessModelDao.VATIO.writeVATTransactions;
import static be.dafke.BusinessModelDao.XMLConstants.ACCOUNTINGS;

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
                        "  </Accounting>\n"
                );
            }
            writer.write("  <CurrentObject>"+accountings.getCurrentObject()+"</CurrentObject>\n");
            writer.write("</Accountings>");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Accountings.class.getName()).log(Level.SEVERE, null, ex);
        }

        for(Accounting accounting:accountings.getBusinessObjects()){
            writeAccounting(accounting, xmlFolder);
        }
    }

    private static void writeAccounting(Accounting accounting, File xmlFolder) {
        File accountingsFolder = new File(xmlFolder, "Accountings");
        File accountingFolder = new File(accountingsFolder, accounting.getName());
        accountingFolder.mkdirs();
        writeAccounts(accounting.getAccounts(), accountingFolder);
        writeJournals(accounting.getJournals(), accountingFolder);
        writeJournalTypes(accounting.getJournalTypes(), accountingFolder);
        writeProjects(accounting.getProjects(), accountingFolder);
        writeBalances(accounting.getBalances(), accountingFolder);
        writeVATFields(accounting.getVatFields(), accountingFolder);
        writeVATTransactions(accounting.getVatTransactions(), accountingFolder);
        writeContacts(accounting.getContacts(), accountingFolder);
        writeMortgages(accounting.getMortgages(), accountingFolder);
    }

}
