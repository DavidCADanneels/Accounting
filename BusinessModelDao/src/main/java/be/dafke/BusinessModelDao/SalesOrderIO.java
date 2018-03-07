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
        File xmlFile = new File(XML_PATH+accounting.getName()+"/"+SALES_ORDERS + XML_EXTENSION);
        Element rootElement = getRootElement(xmlFile, SALES_ORDERS);
        int nr = 0;
        for (Element salesOrderElement : getChildren(rootElement, SALES_ORDER)) {
            Order order = new Order(articles);
            String id = getValue(salesOrderElement, ID);
            order.setName(id);
            nr++;
            String customerString = getValue(salesOrderElement, CUSTOMER);
            Contact customer = contacts.getBusinessObject(customerString);
            order.setCustomer(customer);

            for (Element element : getChildren(salesOrderElement, ARTICLE)) {
                String name = getValue(element, NAME);
                Article article = articles.getBusinessObject(name);

                String numberString = getValue(element, NUMBER);
                int number = parseInt(numberString);

                StockItem stockItem = new StockItem(number, article);
                order.setItem(stockItem);
            }
            try {
                salesOrders.addBusinessObject(order);
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }
        SalesOrders.setId(nr);
    }

    public static void writeSalesOrders(SalesOrders salesOrders, File accountingFolder) {
        File file = new File(accountingFolder, SALES_ORDERS + XML_EXTENSION);
        try {
            Writer writer = new FileWriter(file);
            writer.write(getXmlHeader(SALES_ORDERS, 2));
            for (Order order : salesOrders.getBusinessObjects()) {
                writer.write(
                             "  <" + SALES_ORDER + ">\n" +
                                "    <" + ID + ">" + order.getName() + "</" + ID + ">\n" +
                                "    <" + CUSTOMER + ">" + order.getCustomer() + "</" + CUSTOMER + ">\n"
                );
                for (StockItem stockItem: order.getBusinessObjects()) {
                    Article article = stockItem.getArticle();
                    writer.write(
                            "    <" + ARTICLE + ">\n" +
                                "      <" + NAME + ">" + article.getName() + "</" + NAME + ">\n" +
                                "      <" + NUMBER + ">" + stockItem.getNumber() + "</" + NUMBER + ">\n" +
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
