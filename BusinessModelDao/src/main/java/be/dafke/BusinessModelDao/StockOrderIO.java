package be.dafke.BusinessModelDao;

import be.dafke.Accounting.BusinessModel.*;
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException;
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

public class StockOrderIO {
    public static void readStockOrders(Accounting accounting){
        StockOrders stockOrders = accounting.getStockOrders();
        Articles articles = accounting.getArticles();
        File xmlFile = new File(ACCOUNTINGS_XML_FOLDER +accounting.getName()+"/"+STOCK_ORDERS + XML_EXTENSION);
        Element rootElement = getRootElement(xmlFile, STOCK_ORDERS);

        for (Element orderElement : getChildren(rootElement, STOCK_ORDER)) {
            StockOrder order = new StockOrder();

            String idString = getValue(orderElement, ID);
            order.setId(parseInt(idString));

//            String orderName = getValue(orderElement, NAME);
//            order.setName(orderName);

            for (Element element : getChildren(orderElement, ARTICLE)) {
                String name = getValue(element, NAME);
                Article article = articles.getBusinessObject(name);

                String numberOfItemsString = getValue(element, NR_OF_ITEMS);
                int numberOfItems = parseInt(numberOfItemsString);

                String purchaseVatRateString = getValue(element, PURCHASE_VAT_RATE);
                int purchaseVatRate = parseInt(purchaseVatRateString);

                String purchasePriceString = getValue(element, PURCHASE_PRICE);
                BigDecimal purchasePrice = parseBigDecimal(purchasePriceString);

                OrderItem orderItem = new OrderItem(numberOfItems, article, order);
                orderItem.setPurchaseVatRate(purchaseVatRate);
                orderItem.setPurchasePriceForUnit(purchasePrice);
                orderItem.setName(name);
                order.addBusinessObject(orderItem);
            }

            Transactions transactions = accounting.getTransactions();
            int balanceTransactionId = parseInt(getValue(orderElement, BALANCE_TRANSACTION));
            Transaction balanceTransaction = transactions.getBusinessObject(balanceTransactionId);
            order.setBalanceTransaction(balanceTransaction);

            int paymentTransactionId = parseInt(getValue(orderElement, PAYMENT_TRANSACTION));
            Transaction paymentTransaction = transactions.getBusinessObject(paymentTransactionId);
            order.setPaymentTransaction(paymentTransaction);

            try {
                stockOrders.addBusinessObject(order);
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }
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
                                "    <" + ID + ">" + order.getId() + "</" + ID + ">\n" +
                                "    <" + NAME + ">" + order.getName() + "</" + NAME + ">\n"
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
