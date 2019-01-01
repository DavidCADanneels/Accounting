package be.dafke.BusinessModelDao;

import be.dafke.BusinessModel.*;
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
public class StockIO {
    public static void readStockSettings(Accounting accounting) {
        StockTransactions stockTransactions = accounting.getStockTransactions();
        File xmlFile = new File(ACCOUNTINGS_XML_FOLDER + accounting.getName() + "/" + STOCK_TRANSACTIONS + XML_EXTENSION);
        Element transactionsElement = getRootElement(xmlFile, STOCK_TRANSACTIONS);

        Accounts accounts = accounting.getAccounts();
        Journals journals = accounting.getJournals();

        String stockAccountString = getValue(transactionsElement, STOCK_ACCOUNT);
        if (stockAccountString != null) {
            stockTransactions.setStockAccount(accounts.getBusinessObject(stockAccountString));
        }

        String purchaseJournalString = getValue(transactionsElement, PURCHASE_JOURNAL);
        if (purchaseJournalString != null) {
            Journal journal = journals.getBusinessObject(purchaseJournalString);
            if(journal!=null) {
                stockTransactions.setPurchaseJournal(journal);
            }
        }

        String journalNameSales = getValue(transactionsElement, SALES_JOURNAL);
        if (journalNameSales != null) {
            Journal journal = journals.getBusinessObject(journalNameSales);
            if (journal != null) {
                stockTransactions.setSalesJournal(journal);
            }
        }
        String journalNoInvoiceNameSales = getValue(transactionsElement, SALES_NO_INVOICE_JOURNAL);
        if (journalNoInvoiceNameSales != null) {
            Journal journal = journals.getBusinessObject(journalNoInvoiceNameSales);
            if (journal != null) {
                stockTransactions.setSalesNoInvoiceJournal(journal);
            }
        }
        String journalNameGain = getValue(transactionsElement, GAIN_JOURNAL);
        if (journalNameGain != null) {
            Journal journal = journals.getBusinessObject(journalNameGain);
            if (journal != null) {
                stockTransactions.setGainJournal(journal);
            }
        }
        String gainAccount = getValue(transactionsElement, GAIN_ACCOUNT);
        if (gainAccount != null) {
            Account account = accounts.getBusinessObject(gainAccount);
            if (account != null) {
                stockTransactions.setGainAccount(account);
            }
        }
        String salesAccount = getValue(transactionsElement, SALES_ACCOUNT);
        if (salesAccount != null) {
            Account account = accounts.getBusinessObject(salesAccount);
            if (account != null) {
                stockTransactions.setSalesAccount(account);
            }
        }
        String salesGainAccount = getValue(transactionsElement, SALES_GAIN_ACCOUNT);
        if (salesGainAccount != null) {
            Account account = accounts.getBusinessObject(salesGainAccount);
            if (account != null) {
                stockTransactions.setSalesGainAccount(account);
            }
        }
        String promoAccount = getValue(transactionsElement, PROMO_ACCOUNT);
        if (promoAccount != null) {
            Account account = accounts.getBusinessObject(promoAccount);
            if (account != null) {
                stockTransactions.setPromoAccount(account);
            }
        }

    }

    public static void readStockTransactions(Accounting accounting){
        StockTransactions stockTransactions = accounting.getStockTransactions();

        File xmlFile = new File(ACCOUNTINGS_XML_FOLDER + accounting.getName() + "/" + STOCK_TRANSACTIONS + XML_EXTENSION);
        Element transactionsElement = getRootElement(xmlFile, STOCK_TRANSACTIONS);

        for (Element element : getChildren(transactionsElement, STOCK_TRANSACTION)) {
            String name = getValue(element, NAME);
            String type = getValue(element, TYPE);
            String date = getValue(element, DATE);
            String description = getValue(element, DESCRIPTION);

            if(type!=null&&type.equals(PURCHASE_ORDER)){
                PurchaseOrders purchaseOrders = accounting.getPurchaseOrders();
                PurchaseOrder purchaseOrder = purchaseOrders.getBusinessObject(name);
                stockTransactions.addOrder(purchaseOrder);
                purchaseOrder.setDescription(description);
                purchaseOrder.setDeliveryDate(date);
            } else if(type!=null&&type.equals(SALES_ORDER)){
                SalesOrders salesOrders = accounting.getSalesOrders();
                SalesOrder salesOrder = salesOrders.getBusinessObject(name);
                stockTransactions.addOrder(salesOrder);
                salesOrder.setDescription(description);
                salesOrder.setDeliveryDate(date);
            } else if(type!=null&&type.equals(STOCK_ORDER)){
                StockOrders stockOrders = accounting.getStockOrders();
                StockOrder stockOrder = stockOrders.getBusinessObject(name);
                stockTransactions.addOrder(stockOrder);
                stockOrder.setDescription(description);
            } else if(type!=null&&type.equals(PROMO_ORDER)){

            }
        }
    }

    public static void writeStock(Accounting accounting) {
        Articles articles = accounting.getArticles();
        File file = new File(ACCOUNTINGS_XML_FOLDER + accounting.getName() + "/" + STOCK + XML_EXTENSION);
        try {
            Writer writer = new FileWriter(file);
            writer.write(getXmlHeader(STOCK, 2));

            writer.write("  <" + ARTICLES + ">\n");
            for (Article article : articles.getBusinessObjects(Article.inStock())) {
                writer.write(
                         "    <" + ARTICLE + ">\n" +
                             "      <" + NAME + ">" + article.getName() + "</" + NAME + ">\n" +
                             "      <" + NR_OF_UNITS + ">" + article.getNrInStock()/article.getItemsPerUnit() + "</" + NR_OF_UNITS + ">\n" +
                             "      <" + NR_OF_ITEMS + ">" + article.getNrInStock() + "</" + NR_OF_ITEMS + ">\n" +
                             "    </" + ARTICLE + ">\n"
                );
            }
            writer.write("  </" + ARTICLES + ">\n");
            writer.write("</" + STOCK + ">\n");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Accounts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void writeStockTransactions(Accounting accounting) {
        StockTransactions stockTransactions = accounting.getStockTransactions();
        File file = new File(ACCOUNTINGS_XML_FOLDER + accounting.getName() + "/" + STOCK_TRANSACTIONS + XML_EXTENSION);
        try {
            Writer writer = new FileWriter(file);
            writer.write(getXmlHeader(STOCK_TRANSACTIONS, 2));

            Account stockAccount = stockTransactions.getStockAccount();
            Journal salesJournal = stockTransactions.getSalesJournal();
            Journal purchaseJournal = stockTransactions.getPurchaseJournal();
            Journal salesNoInvoiceJournal = stockTransactions.getSalesNoInvoiceJournal();
            Journal gainJournal = stockTransactions.getGainJournal();
            Account gainAccount = stockTransactions.getGainAccount();
            Account salesAccount = stockTransactions.getSalesAccount();
            Account salesGainAccount = stockTransactions.getSalesGainAccount();
            Account salesPromoAccount = stockTransactions.getPromoAccount();

            writer.write("  <" + PURCHASE_JOURNAL + ">"+ (purchaseJournal==null?"null":purchaseJournal.getName())+"</" + PURCHASE_JOURNAL + ">\n");
            writer.write("  <" + STOCK_ACCOUNT+">"+ (stockAccount==null?"null":stockAccount.getName()) +"</" + STOCK_ACCOUNT+">\n");
            writer.write("  <" + SALES_JOURNAL + ">"+ (salesJournal==null?"null":salesJournal.getName())+"</" + SALES_JOURNAL + ">\n");
            writer.write("  <" + SALES_NO_INVOICE_JOURNAL + ">"+ (salesNoInvoiceJournal==null?"null":salesNoInvoiceJournal.getName())+"</" + SALES_NO_INVOICE_JOURNAL + ">\n");
            writer.write("  <" + GAIN_JOURNAL + ">"+ (gainJournal==null?"null":gainJournal.getName())+"</" + GAIN_JOURNAL + ">\n");
            writer.write("  <" + GAIN_ACCOUNT + ">"+(gainAccount==null?"null":gainAccount)+"</" + GAIN_ACCOUNT + ">\n");
            writer.write("  <" + SALES_ACCOUNT + ">"+(salesAccount==null?"null":salesAccount)+"</" + SALES_ACCOUNT + ">\n");
            writer.write("  <" + SALES_GAIN_ACCOUNT + ">"+(salesGainAccount==null?"null":salesGainAccount)+"</" + SALES_GAIN_ACCOUNT + ">\n");
            writer.write("  <" + PROMO_ACCOUNT + ">"+(salesPromoAccount==null?"null":salesPromoAccount)+"</" + PROMO_ACCOUNT + ">\n");

            for (Order order : stockTransactions.getOrders()) {
                writer.write("  <" + STOCK_TRANSACTION + ">\n");
                writer.write("    <" + NAME + ">" + order.getName() + "</" + NAME + ">\n");
                writer.write("    <" + DATE + ">" + order.getDeliveryDate() + "</" + DATE + ">\n");
                writer.write("    <" + DESCRIPTION + ">" + order.getDescription() + "</" + DESCRIPTION + ">\n");
                if (order instanceof PurchaseOrder){
                    writer.write("    <" + TYPE + ">" + PURCHASE_ORDER + "</" + TYPE + ">\n");
                }
                if (order instanceof SalesOrder){
                    writer.write("    <" + TYPE + ">" + SALES_ORDER + "</" + TYPE + ">\n");
                }
                if (order instanceof StockOrder){
                    writer.write("    <" + TYPE + ">" + STOCK_ORDER + "</" + TYPE + ">\n");
                }
                writer.write("  </" + STOCK_TRANSACTION + ">\n");
            }
            writer.write("</" + STOCK_TRANSACTIONS + ">\n");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Accounts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
