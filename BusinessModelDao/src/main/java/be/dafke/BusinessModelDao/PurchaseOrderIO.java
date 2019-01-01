package be.dafke.BusinessModelDao;

import be.dafke.BusinessModel.*;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.Utils.Utils;
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

public class PurchaseOrderIO {
    public static void readPurchaseOrders(Accounting accounting){
        PurchaseOrders purchaseOrders = accounting.getPurchaseOrders();
        Contacts contacts = accounting.getContacts();
        Articles articles = accounting.getArticles();
        File xmlFile = new File(ACCOUNTINGS_XML_FOLDER +accounting.getName()+"/"+PURCHASE_ORDERS + XML_EXTENSION);
        Element rootElement = getRootElement(xmlFile, PURCHASE_ORDERS);
        int nr = 0;

        for (Element purchaseOrderElement : getChildren(rootElement, PURCHASE_ORDER)) {
            PurchaseOrder purchaseOrder = new PurchaseOrder();
            String id = getValue(purchaseOrderElement, ID);
            purchaseOrder.setName(id);
            nr++;
            String supplierString = getValue(purchaseOrderElement, SUPPLIER);
            Contact supplier = contacts.getBusinessObject(supplierString);
            purchaseOrder.setSupplier(supplier);

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

                OrderItem orderItem = new OrderItem(numberOfUnits, numberOfItems, article, purchaseOrder);
                orderItem.setPurchaseVatRate(purchaseVatRate);
                orderItem.setPurchasePriceForUnit(purchasePrice);
                orderItem.setName(name);
                purchaseOrder.addBusinessObject(orderItem);
            }

            Transactions transactions = accounting.getTransactions();
            int purchaseTransactionId = parseInt(getValue(purchaseOrderElement, PURCHASE_TRANSACTION));
            Transaction purchaseTransaction = transactions.getBusinessObject(purchaseTransactionId);
            purchaseOrder.setPurchaseTransaction(purchaseTransaction);

            int paymentTransactionId = parseInt(getValue(purchaseOrderElement, PAYMENT_TRANSACTION));
            Transaction paymentTransaction = transactions.getBusinessObject(paymentTransactionId);
            purchaseOrder.setPaymentTransaction(paymentTransaction);

            try {
                purchaseOrders.addBusinessObject(purchaseOrder);
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }
        PurchaseOrders.setId(nr);
    }

    public static void writePurchasesOrders(Accounting accounting) {
        PurchaseOrders purchaseOrders = accounting.getPurchaseOrders();
        File file = new File(ACCOUNTINGS_XML_FOLDER + accounting.getName() + "/" + PURCHASE_ORDERS + XML_EXTENSION);
        try {
            Writer writer = new FileWriter(file);
            writer.write(getXmlHeader(PURCHASE_ORDERS, 2));
            for (PurchaseOrder order : purchaseOrders.getBusinessObjects()) {
                writer.write(
                             "  <" + PURCHASE_ORDER + ">\n" +
                                "    <" + ID + ">" + order.getName() + "</" + ID + ">\n" +
                                "    <" + SUPPLIER + ">" + order.getSupplier() + "</" + SUPPLIER + ">\n"
                );
                Transaction purchaseTransaction = order.getPurchaseTransaction();
                if(purchaseTransaction!=null) {
                    writer.write("    <" + PURCHASE_TRANSACTION + ">" + purchaseTransaction.getTransactionId() + "</" + PURCHASE_TRANSACTION + ">\n");
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
