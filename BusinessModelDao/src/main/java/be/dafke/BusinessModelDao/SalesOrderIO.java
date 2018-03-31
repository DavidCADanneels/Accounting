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
import static be.dafke.Utils.Utils.parseInt;

/**
 * Created by ddanneels on 15/01/2017.
 */
public class SalesOrderIO {
    public static void readSalesOrders(Accounting accounting){
        SalesOrders salesOrders = accounting.getSalesOrders();
        Contacts contacts = accounting.getContacts();
        Articles articles = accounting.getArticles();
        File xmlFile = new File(ACCOUNTINGS_FOLDER +accounting.getName()+"/"+SALES_ORDERS + XML_EXTENSION);
        Element rootElement = getRootElement(xmlFile, SALES_ORDERS);
        Accounts accounts = accounting.getAccounts();
        Journals journals = accounting.getJournals();

        String journalNameSales = getValue(rootElement, SALES_JOURNAL);
        if(journalNameSales!=null){
            Journal journal = journals.getBusinessObject(journalNameSales);
            if (journal!=null) {
                salesOrders.setSalesJournal(journal);
            }
        }
        String journalNameGain = getValue(rootElement, GAIN_JOURNAL);
        if(journalNameGain!=null){
            Journal journal = journals.getBusinessObject(journalNameGain);
            if (journal!=null) {
                salesOrders.setGainJournal(journal);
            }
        }
        String stockAccountString = getValue(rootElement, STOCK_ACCOUNT);
        if(stockAccountString!=null){
            Account account = accounts.getBusinessObject(stockAccountString);
            if (account!=null) {
                salesOrders.setStockAccount(account);
            }
        }
        String vatAccount = getValue(rootElement, VAT_ACCOUNT);
        if(vatAccount!=null){
            Account account = accounts.getBusinessObject(vatAccount);
            if (account!=null) {
                salesOrders.setVATAccount(account);
            }
        }
        String gainAccount = getValue(rootElement, GAIN_ACCOUNT);
        if(gainAccount !=null){
            Account account = accounts.getBusinessObject(gainAccount);
            if (account!=null) {
                salesOrders.setGainAccount(account);
            }
        }
        String salesAccount = getValue(rootElement, SALES_ACCOUNT);
        if(salesAccount !=null){
            Account account = accounts.getBusinessObject(salesAccount);
            if (account!=null) {
                salesOrders.setSalesAccount(account);
            }
        }
        for (Element salesOrderElement : getChildren(rootElement, SALES_ORDER)) {
            SalesOrder order = new SalesOrder();
            String customerString = getValue(salesOrderElement, CUSTOMER);
            Contact customer = contacts.getBusinessObject(customerString);
            order.setCustomer(customer);

            order.setPlaced(getBooleanValue(salesOrderElement, IS_PLACED));
            order.setDelivered(getBooleanValue(salesOrderElement, IS_DELIVERED));
            order.setPayed(getBooleanValue(salesOrderElement, IS_PAYED));

            for (Element element : getChildren(salesOrderElement, ARTICLE)) {
                String name = getValue(element, NAME);
                Article article = articles.getBusinessObject(name);

                String numberOfUnitsString = getValue(element, NR_OF_UNITS);
                int numberOfUnits = parseInt(numberOfUnitsString);

                String numberOfItemsString = getValue(element, NR_OF_ITEMS);
                int numberOfItems = parseInt(numberOfItemsString);

                OrderItem orderItem = new OrderItem(numberOfUnits, numberOfItems, article);
                orderItem.setName(name);
                order.addBusinessObject(orderItem);
            }
            try {
                salesOrders.addBusinessObject(order);
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeSalesOrders(Accounting accounting) {
        SalesOrders salesOrders = accounting.getSalesOrders();
        File file = new File(ACCOUNTINGS_FOLDER + accounting.getName() + "/" + SALES_ORDERS + XML_EXTENSION);
        try {
            Writer writer = new FileWriter(file);
            writer.write(getXmlHeader(SALES_ORDERS, 2));
            Journal salesJournal = salesOrders.getSalesJournal();
            Journal gainJournal = salesOrders.getGainJournal();
            writer.write("  <" + SALES_JOURNAL + ">"+ (salesJournal==null?"null":salesJournal.getName())+"</" + SALES_JOURNAL + ">\n");
            writer.write("  <" + GAIN_JOURNAL + ">"+ (gainJournal==null?"null":gainJournal.getName())+"</" + GAIN_JOURNAL + ">\n");
            writer.write("  <" + STOCK_ACCOUNT + ">"+salesOrders.getStockAccount()+"</" + STOCK_ACCOUNT + ">\n");
            writer.write("  <" + VAT_ACCOUNT + ">"+salesOrders.getVATAccount()+"</" + VAT_ACCOUNT + ">\n");
            writer.write("  <" + GAIN_ACCOUNT + ">"+salesOrders.getGainAccount()+"</" + GAIN_ACCOUNT + ">\n");
            writer.write("  <" + SALES_ACCOUNT + ">"+salesOrders.getSalesAccount()+"</" + SALES_ACCOUNT + ">\n");
            for (SalesOrder order : salesOrders.getBusinessObjects()) {
                writer.write(
                             "  <" + SALES_ORDER + ">\n" +
                                 "    <" + ID + ">" + order.getId() + "</" + ID + ">\n" +
                                 "    <" + CUSTOMER + ">" + order.getCustomer() + "</" + CUSTOMER + ">\n" +
                                 "    <" + IS_PLACED + ">" + order.isPlaced() + "</" + IS_PLACED + ">\n" +
                                 "    <" + IS_DELIVERED + ">" + order.isDelivered() + "</" + IS_DELIVERED + ">\n" +
                                 "    <" + IS_PAYED + ">" + order.isPayed() + "</" + IS_PAYED + ">\n"
                );
                Transaction salesTransaction = order.getSalesTransaction();
                if(salesTransaction!=null) {
                    writer.write("    <" + SALES_TRANSACTION + ">" + salesTransaction.getId() + "</" + SALES_TRANSACTION + ">\n");
                }
                Transaction gainTransaction = order.getGainTransaction();
                if(gainTransaction!=null) {
                    writer.write("    <" + GAIN_TRANSACTION + ">" + gainTransaction.getId() + "</" + GAIN_TRANSACTION + ">\n");
                }
                Transaction paymentTransaction = order.getPaymentTransaction();
                if(paymentTransaction!=null) {
                    writer.write("    <" + PAYMENT_TRANSACTION + ">" + paymentTransaction.getId() + "</" + PAYMENT_TRANSACTION + ">\n");
                }
                for (OrderItem orderItem : order.getBusinessObjects()) {
                    Article article = orderItem.getArticle();
                    writer.write(
                            "    <" + ARTICLE + ">\n" +
                                "      <" + NAME + ">" + article.getName() + "</" + NAME + ">\n" +
                                "      <" + NR_OF_UNITS + ">" + orderItem.getNumberOfUnits() + "</" + NR_OF_UNITS + ">\n" +
                                "      <" + NR_OF_ITEMS + ">" + orderItem.getNumberOfItems() + "</" + NR_OF_ITEMS + ">\n" +
                                "    </" + ARTICLE + ">\n"
                    );
                }
                writer.write("  </" + SALES_ORDER + ">\n");
            }
            writer.write("</" + SALES_ORDERS + ">\n");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Accounts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
