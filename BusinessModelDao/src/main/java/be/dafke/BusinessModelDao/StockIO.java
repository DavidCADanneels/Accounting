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
    public static void readStockTransactions(Accounting accounting){
        StockTransactions stockTransactions = accounting.getStockTransactions();
        PurchaseOrders purchaseOrders = accounting.getPurchaseOrders();
        SalesOrders salesOrders = accounting.getSalesOrders();
        File xmlFile = new File(ACCOUNTINGS_XML_FOLDER +accounting.getName()+"/"+STOCK_TRANSACTIONS + XML_EXTENSION);
        Element transactionsElement = getRootElement(xmlFile, STOCK_TRANSACTIONS);
        for (Element element : getChildren(transactionsElement, STOCK_TRANSACTION)) {
            String name = getValue(element, NAME);
            String type = getValue(element, TYPE);
            String date = getValue(element, DATE);
            String description = getValue(element, DESCRIPTION);

            if(type!=null&&type.equals(PURCHASE_ORDER)){
                PurchaseOrder purchaseOrder = purchaseOrders.getBusinessObject(name);
                stockTransactions.addOrder(purchaseOrder);
                purchaseOrder.setDescription(description);
                purchaseOrder.setDate(date);
            }
            if(type!=null&&type.equals(SALES_ORDER)){
                SalesOrder salesOrder = salesOrders.getBusinessObject(name);
                stockTransactions.addOrder(salesOrder);
                salesOrder.setDescription(description);
                salesOrder.setDate(date);
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
            for (Article article : articles.getBusinessObjects(Article.withOrders())) {
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
            for (Order order : stockTransactions.getOrders()) {
                writer.write("  <" + STOCK_TRANSACTION + ">\n");
                writer.write("    <" + NAME + ">" + order.getName() + "</" + NAME + ">\n");
                writer.write("    <" + DATE + ">" + order.getDate() + "</" + DATE + ">\n");
                writer.write("    <" + DESCRIPTION + ">" + order.getDescription() + "</" + DESCRIPTION + ">\n");
                if (order instanceof PurchaseOrder){
                    writer.write("    <" + TYPE + ">" + PURCHASE_ORDER + "</" + TYPE + ">\n");
                }
                if (order instanceof SalesOrder){
                    writer.write("    <" + TYPE + ">" + SALES_ORDER + "</" + TYPE + ">\n");
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
