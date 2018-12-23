package be.dafke.BusinessModelDao;

import be.dafke.BusinessModel.*;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static be.dafke.BusinessModelDao.XMLConstants.*;
import static be.dafke.BusinessModelDao.XMLReader.*;
import static be.dafke.BusinessModelDao.XMLWriter.getXmlHeader;

/**
 * Created by ddanneels on 15/01/2017.
 */
public class VATIO {

    public static void readVATFields(Accounting accounting) {
        VATFields vatFields = accounting.getVatFields();
        File xmlFile = new File(ACCOUNTINGS_XML_FOLDER +accounting.getName()+ "/" +VATFIELDS + XML_EXTENSION);
        if(xmlFile.exists()) {
            Element rootElement = getRootElement(xmlFile, VATFIELDS);
            for (Element element : getChildren(rootElement, VATFIELD)) {

                String name = getValue(element, NAME);
                VATField vatField = new VATField(name);

                try {
                    vatFields.addBusinessObject(vatField);
                } catch (EmptyNameException | DuplicateNameException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void readVATTransactions(Accounting accounting){
        VATTransactions vatTransactions = accounting.getVatTransactions();
        Accounts accounts = accounting.getAccounts();
        File xmlFile = new File( ACCOUNTINGS_XML_FOLDER +accounting.getName()+"/" +VATTRANSACTIONS + XML_EXTENSION);
        if(xmlFile.exists()) {
            Element rootElement = getRootElement(xmlFile, VATTRANSACTIONS);
            String debitAccountString = getValue(rootElement, DEBIT_ACCOUNT);
            String creditAccountString = getValue(rootElement, CREDIT_ACCOUNT);
            String debitCNAccountString = getValue(rootElement, DEBIT_CN_ACCOUNT);
            String creditCNAccountString = getValue(rootElement, CREDIT_CN_ACCOUNT);

            if (debitAccountString != null) {
                vatTransactions.setDebitAccount(accounts.getBusinessObject(debitAccountString));
            }
            if (creditAccountString != null) {
                vatTransactions.setCreditAccount(accounts.getBusinessObject(creditAccountString));
            }
            if (debitCNAccountString != null) {
                vatTransactions.setDebitCNAccount(accounts.getBusinessObject(debitCNAccountString));
            }
            if (creditCNAccountString != null) {
                vatTransactions.setCreditCNAccount(accounts.getBusinessObject(creditCNAccountString));
            }
        }
    }

    public static void readDeliveroo(Accounting accounting){
        VATTransactions vatTransactions = accounting.getVatTransactions();
        Accounts accounts = accounting.getAccounts();
        Journals journals = accounting.getJournals();
        File xmlFile = new File( ACCOUNTINGS_XML_FOLDER +accounting.getName()+"/" +VATTRANSACTIONS + XML_EXTENSION);
        if(xmlFile.exists()) {
            Element rootElement = getRootElement(xmlFile, VATTRANSACTIONS);
            String deliverooServiceJournalString = getValue(rootElement, DELIVEROO_SERVICE_JOURNAL);
            String deliverooSalesJournalString = getValue(rootElement, DELIVEROO_SALES_JOURNAL);
            String deliverooBalanceAccountString = getValue(rootElement, DELIVEROO_BALANCE_ACCOUNT);
            String deliverooServiceAccountString = getValue(rootElement, DELIVEROO_SERVICE_ACCOUNT);
            String deliverooRevenueAccountString = getValue(rootElement, DELIVEROO_REVENUE_ACCOUNT);

            if (deliverooServiceJournalString != null) {
                vatTransactions.setDeliverooServiceJournal(journals.getBusinessObject(deliverooServiceJournalString));
            }
            if (deliverooSalesJournalString != null) {
                vatTransactions.setDeliverooSalesJournal(journals.getBusinessObject(deliverooSalesJournalString));
            }
            if (deliverooBalanceAccountString != null) {
                vatTransactions.setDeliverooBalanceAccount(accounts.getBusinessObject(deliverooBalanceAccountString));
            }
            if (deliverooServiceAccountString != null) {
                vatTransactions.setDeliverooServiceAccount(accounts.getBusinessObject(deliverooServiceAccountString));
            }
            if (deliverooRevenueAccountString != null) {
                vatTransactions.setDeliverooRevenueAccount(accounts.getBusinessObject(deliverooRevenueAccountString));
            }
        }
    }

    public static void writeVATFields(Accounting accounting){
        VATFields vatFields = accounting.getVatFields();
        File file = new File(ACCOUNTINGS_XML_FOLDER + accounting.getName() + "/" + VATFIELDS+ XML_EXTENSION);
        try{
            Writer writer = new FileWriter(file);
            writer.write(getXmlHeader(VATFIELDS, 2));
            for(VATField vatField: vatFields.getBusinessObjects()) {
                writer.write(
                            "  <"+VATFIELD+">\n" +
                            "    <"+NAME+">"+vatField.getName()+"</"+NAME+">\n" +
                            "    <"+AMOUNT+">"+vatField.getSaldo()+"</"+AMOUNT+">\n" +
                            "  </"+VATFIELD+">\n"
                );
            }
            writer.write(   "</"+VATFIELDS+">");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(VATFields.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void writeVATTransactions(Accounting accounting){
        VATTransactions vatTransactions = accounting.getVatTransactions();
        File file = new File(ACCOUNTINGS_XML_FOLDER + accounting.getName() + "/" + VATTRANSACTIONS+ XML_EXTENSION);
        try{
            Writer writer = new FileWriter(file);
            writer.write(getXmlHeader(VATTRANSACTIONS, 2));
            Journal deliverooSalesJournal = vatTransactions.getDeliverooSalesJournal();
            Journal deliverooServiceJournal = vatTransactions.getDeliverooServiceJournal();
            writer.write(
                    "  <"+DEBIT_ACCOUNT+">"+vatTransactions.getDebitAccount()+"</"+DEBIT_ACCOUNT+">\n" +
                    "  <"+CREDIT_ACCOUNT+">"+vatTransactions.getCreditAccount()+"</"+CREDIT_ACCOUNT+">\n" +
                    "  <"+DEBIT_CN_ACCOUNT+">"+vatTransactions.getDebitCNAccount()+"</"+DEBIT_CN_ACCOUNT+">\n" +
                    "  <"+CREDIT_CN_ACCOUNT+">"+vatTransactions.getCreditCNAccount()+"</"+CREDIT_CN_ACCOUNT+">\n" +
                    "  <"+DELIVEROO_SALES_JOURNAL+">"+ (deliverooSalesJournal==null?"null":deliverooSalesJournal.getName())+"</"+DELIVEROO_SALES_JOURNAL+">\n" +
                    "  <"+DELIVEROO_SERVICE_JOURNAL+">"+ (deliverooServiceJournal==null?"null":deliverooServiceJournal.getName())+"</"+DELIVEROO_SERVICE_JOURNAL+">\n" +
                    "  <"+DELIVEROO_SERVICE_ACCOUNT+">"+vatTransactions.getDeliverooServiceAccount()+"</"+DELIVEROO_SERVICE_ACCOUNT+">\n" +
                    "  <"+DELIVEROO_REVENUE_ACCOUNT+">"+vatTransactions.getDeliverooRevenueAccount()+"</"+DELIVEROO_REVENUE_ACCOUNT+">\n" +
                    "  <"+DELIVEROO_BALANCE_ACCOUNT+">"+vatTransactions.getDeliverooBalanceAccount()+"</"+DELIVEROO_BALANCE_ACCOUNT+">\n"
            );
            writer.write("</"+VATTRANSACTIONS+">");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(VATTransactions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
