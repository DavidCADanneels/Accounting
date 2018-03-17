package be.dafke.BusinessModelDao;

import be.dafke.BusinessModel.*;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static be.dafke.BusinessModelDao.XMLConstants.*;
import static be.dafke.BusinessModelDao.XMLReader.*;
import static be.dafke.BusinessModelDao.XMLWriter.getXmlHeader;
import static be.dafke.Utils.Utils.parseInt;

/**
 * Created by ddanneels on 15/01/2017.
 */
public class StockIO {
    public static void readStock(Accounting accounting){
        Stock stock = accounting.getStock();
        PurchaseOrders purchaseOrders = accounting.getPurchaseOrders();
        SalesOrders salesOrders = accounting.getSalesOrders();
        Articles articles = accounting.getArticles();
        File xmlFile = new File(XML_PATH+accounting.getName()+"/"+STOCK + XML_EXTENSION);
//        Element rootElement = getRootElement(xmlFile, STOCK);
        Element articlesElement = getRootElement(xmlFile, ARTICLES);
        for (Element element : getChildren(articlesElement, ARTICLE)) {

            String name = getValue(element, NAME);
            Article article = articles.getBusinessObject(name);

            String numberOfUnitsString = getValue(element, NR_OF_UNITS);
            int numberOfUnits = parseInt(numberOfUnitsString);

            String numberOfItemsString = getValue(element, NR_OF_ITEMS);
            int numberOfItems = parseInt(numberOfItemsString);

            OrderItem orderItem = new OrderItem(numberOfUnits, numberOfItems, article);
            orderItem.setName(name);
            stock.addBusinessObject(orderItem);
        }

        Element transactionsElement = getRootElement(xmlFile, STOCK_TRANSACTIONS);
        for (Element element : getChildren(transactionsElement, STOCK_TRANSACTION)) {
            String name = getValue(element, NAME);
            String type = getValue(element, TYPE);
//            String date = getValue(element, DATE);
            Order order = null;
            if(type!=null&&type.equals(PURCHASE_ORDER)){
                order = purchaseOrders.getBusinessObject(name);
            }
            if(type!=null&&type.equals(SALES_ORDER)){
                order = salesOrders.getBusinessObject(name);
            }
            if(order!=null){
                Transaction transaction = order.getTransaction();
                stock.addTransaction(transaction, order);
            }
        }
    }

    public static void writeStock(Accounting accounting) {
        Stock stock = accounting.getStock();
        File file = new File(XML_PATH + accounting.getName() + "/" + STOCK + XML_EXTENSION);
        try {
            Writer writer = new FileWriter(file);
            writer.write(getXmlHeader(STOCK, 2));

            writer.write("  <" + ARTICLES + ">\n");
            for (OrderItem orderItem : stock.getBusinessObjects()) {
                Article article = orderItem.getArticle();
                writer.write(
                         "    <" + ARTICLE + ">\n" +
                             "      <" + NAME + ">" + article.getName() + "</" + NAME + ">\n" +
                             "      <" + NR_OF_UNITS + ">" + orderItem.getNumberOfUnits() + "</" + NR_OF_UNITS + ">\n" +
                             "      <" + NR_OF_ITEMS + ">" + orderItem.getNumberOfItems() + "</" + NR_OF_ITEMS + ">\n" +
                             "    </" + ARTICLE + ">\n"
                );
            }
            writer.write("  </" + ARTICLES + ">\n");

            writer.write("  <" + STOCK_TRANSACTIONS + ">\n");
            for (Order order : stock.getTransactions()) {
                writer.write("    <" + STOCK_TRANSACTION + ">\n");
                writer.write("    <" + NAME + ">" + order.getName() + "</" + NAME + ">\n");
//                writer.write("    <" + DATE + ">" + order.getTransaction() + "</" + DATE + ">\n");
                if (order instanceof PurchaseOrder){
                    writer.write("    <" + TYPE + ">" + PURCHASE_ORDER + "</" + TYPE + ">\n");
                }
                if (order instanceof SalesOrder){
                    writer.write("    <" + TYPE + ">" + SALES_ORDER + "</" + TYPE + ">\n");
                }
                writer.write("    </" + STOCK_TRANSACTION + ">\n");
            }
            writer.write("  </" + STOCK_TRANSACTIONS + ">\n");
            writer.write("</" + STOCK + ">\n");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Accounts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
