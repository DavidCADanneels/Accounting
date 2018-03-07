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
public class PurchaseOrderIO {
    public static void readPurchaseOrders(Accounting accounting){
        PurchaseOrders purchaseOrders = accounting.getPurchaseOrders();
        Contacts contacts = accounting.getContacts();
        Articles articles = accounting.getArticles();
        File xmlFile = new File(XML_PATH+accounting.getName()+"/"+PURCHASE_ORDERS + XML_EXTENSION);
        Element rootElement = getRootElement(xmlFile, PURCHASE_ORDERS);
        int nr = 0;
        for (Element purchaseOrderElement : getChildren(rootElement, PURCHASE_ORDER)) {
            Order order = new Order(articles);
            String id = getValue(purchaseOrderElement, ID);
            order.setName(id);
            nr++;
            String supplierString = getValue(purchaseOrderElement, SUPPLIER);
            Contact supplier = contacts.getBusinessObject(supplierString);
            order.setSupplier(supplier);

            for (Element element : getChildren(purchaseOrderElement, ARTICLE)) {
                String name = getValue(element, NAME);
                Article article = articles.getBusinessObject(name);

                String numberString = getValue(element, NUMBER);
                int number = parseInt(numberString);

                StockItem stockItem = new StockItem(number, article);
                order.setItem(stockItem);
            }
            try {
                purchaseOrders.addBusinessObject(order);
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }
        PurchaseOrders.setId(nr);
    }

    public static void writePurchasesOrders(PurchaseOrders purchaseOrders, File accountingFolder) {
        File file = new File(accountingFolder, PURCHASE_ORDERS + XML_EXTENSION);
        try {
            Writer writer = new FileWriter(file);
            writer.write(getXmlHeader(PURCHASE_ORDERS, 2));
            for (Order order : purchaseOrders.getBusinessObjects()) {
                writer.write(
                             "  <" + PURCHASE_ORDER + ">\n" +
                                "    <" + ID + ">" + order.getName() + "</" + ID + ">\n" +
                                "    <" + SUPPLIER + ">" + order.getSupplier() + "</" + SUPPLIER + ">\n"
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
                writer.write("  </" + PURCHASE_ORDER + ">\n");
            }
            writer.write("</" + PURCHASE_ORDERS + ">\n");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Accounts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
