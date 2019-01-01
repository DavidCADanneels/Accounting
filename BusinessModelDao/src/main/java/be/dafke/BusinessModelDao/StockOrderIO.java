package be.dafke.BusinessModelDao;

import be.dafke.BusinessModel.*;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

import static be.dafke.BusinessModelDao.XMLConstants.*;
import static be.dafke.BusinessModelDao.XMLReader.*;
import static be.dafke.BusinessModelDao.XMLWriter.getXmlHeader;
import static be.dafke.Utils.Utils.parseBigDecimal;
import static be.dafke.Utils.Utils.parseInt;

/**
 * Created by ddanneels on 15/01/2017.
 */
public class StockOrderIO {
    public static void readStockOrders(Accounting accounting){
        StockOrders stockOrders = accounting.getStockOrders();
        Contacts contacts = accounting.getContacts();
        Articles articles = accounting.getArticles();
        File xmlFile = new File(ACCOUNTINGS_XML_FOLDER +accounting.getName()+"/"+STOCK_ORDERS + XML_EXTENSION);
        Element rootElement = getRootElement(xmlFile, STOCK_ORDERS);
        int nr = 0;

        for (Element purchaseOrderElement : getChildren(rootElement, STOCK_ORDER)) {
            StockOrder stockOrder = new StockOrder();
            String id = getValue(purchaseOrderElement, ID);
            stockOrder.setName(id);
            nr++;

            for (Element element : getChildren(purchaseOrderElement, ARTICLE)) {
                String name = getValue(element, NAME);
                Article article = articles.getBusinessObject(name);

                String numberOfUnitsString = getValue(element, NR_OF_UNITS);
                int numberOfUnits = parseInt(numberOfUnitsString);

                String numberOfItemsString = getValue(element, NR_OF_ITEMS);
                int numberOfItems = parseInt(numberOfItemsString);

                String purchaseVatRateString = getValue(element, PURCHASE_VAT_RATE);
                int purchaseVatRate = parseInt(purchaseVatRateString);

                String purchasePriceString = getValue(element, PURCHASE_PRICE);
                BigDecimal purchasePrice = parseBigDecimal(purchasePriceString);

                OrderItem orderItem = new OrderItem(numberOfUnits, numberOfItems, article, stockOrder);
                orderItem.setPurchaseVatRate(purchaseVatRate);
                orderItem.setPurchasePriceForUnit(purchasePrice);
                orderItem.setName(name);
                stockOrder.addBusinessObject(orderItem);
            }

            Transactions transactions = accounting.getTransactions();
            int balanceTransactionId = parseInt(getValue(purchaseOrderElement, BALANCE_TRANSACTION));
            Transaction balanceTransaction = transactions.getBusinessObject(balanceTransactionId);
            stockOrder.setBalanceTransaction(balanceTransaction);

            int paymentTransactionId = parseInt(getValue(purchaseOrderElement, PAYMENT_TRANSACTION));
            Transaction paymentTransaction = transactions.getBusinessObject(paymentTransactionId);
            stockOrder.setPaymentTransaction(paymentTransaction);

            try {
                stockOrders.addBusinessObject(stockOrder);
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }
        PurchaseOrders.setId(nr);
    }

    public static void writeStockOrders(Accounting accounting) {
        StockOrders stockOrders = accounting.getStockOrders();
        File file = new File(ACCOUNTINGS_XML_FOLDER + accounting.getName() + "/" + STOCK_ORDERS + XML_EXTENSION);
        try {
            Writer writer = new FileWriter(file);
            writer.write(getXmlHeader(STOCK_ORDERS, 2));
            for (StockOrder order : stockOrders.getBusinessObjects()) {
                writer.write(
                             "  <" + STOCK_ORDER + ">\n" +
                                "    <" + ID + ">" + order.getName() + "</" + ID + ">\n"
                );
                Transaction balanceTransaction = order.getBalanceTransaction();
                if(balanceTransaction!=null) {
                    writer.write("    <" + BALANCE_TRANSACTION + ">" + balanceTransaction.getTransactionId() + "</" + BALANCE_TRANSACTION + ">\n");
                }
                Transaction paymentTransaction = order.getPaymentTransaction();
                if(paymentTransaction!=null) {
                    writer.write("    <" + PAYMENT_TRANSACTION + ">" + paymentTransaction.getTransactionId() + "</" + PAYMENT_TRANSACTION + ">\n");
                }

                for (OrderItem orderItem : order.getBusinessObjects()) {
                    Article article = orderItem.getArticle();
                    writer.write(
                            "    <" + ARTICLE + ">\n" +
                                "      <" + NAME + ">" + article.getName() + "</" + NAME + ">\n" +
                                "      <" + NR_OF_UNITS + ">" + orderItem.getNumberOfUnits() + "</" + NR_OF_UNITS + ">\n" +
                                "      <" + NR_OF_ITEMS + ">" + orderItem.getNumberOfItems() + "</" + NR_OF_ITEMS + ">\n" +
                                "      <" + PURCHASE_VAT_RATE + ">" + orderItem.getPurchaseVatRate() + "</" + PURCHASE_VAT_RATE + ">\n" +
                                "      <" + PURCHASE_PRICE + ">" + orderItem.getPurchasePriceForUnit() + "</" + PURCHASE_PRICE + ">\n" +
                                "    </" + ARTICLE + ">\n"
                    );
                }
                writer.write("  </" + STOCK_ORDER + ">\n");
            }
            writer.write("</" + STOCK_ORDERS + ">\n");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Accounts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}