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
        Accounts accounts = accounting.getAccounts();
        Journals journals = accounting.getJournals();

        String journalName = getValue(rootElement, JOURNAL);
        if(journalName!=null){
            Journal journal = journals.getBusinessObject(journalName);
            if (journal!=null) {
                purchaseOrders.setJournal(journal);
            }
        }
        String stockAccountString = getValue(rootElement, STOCK_ACCOUNT);
        if(stockAccountString!=null){
            Account account = accounts.getBusinessObject(stockAccountString);
            if (account!=null) {
                purchaseOrders.setStockAccount(account);
            }
        }
        String vatAccount = getValue(rootElement, VAT_ACCOUNT);
        if(vatAccount!=null){
            Account account = accounts.getBusinessObject(vatAccount);
            if (account!=null) {
                purchaseOrders.setVATAccount(account);
            }
        }
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

    public static void writePurchasesOrders(Accounting accounting) {
        PurchaseOrders purchaseOrders = accounting.getPurchaseOrders();
        File file = new File(XML_PATH + accounting.getName() + "/" + PURCHASE_ORDERS + XML_EXTENSION);
        try {
            Writer writer = new FileWriter(file);
            writer.write(getXmlHeader(PURCHASE_ORDERS, 2));
            Journal journal = purchaseOrders.getJournal();
            writer.write("  <" + JOURNAL + ">"+ (journal==null?"null":journal.getName())+"</" + JOURNAL + ">\n");
            writer.write("  <" + STOCK_ACCOUNT + ">"+purchaseOrders.getStockAccount()+"</" + STOCK_ACCOUNT + ">\n");
            writer.write("  <" + VAT_ACCOUNT + ">"+purchaseOrders.getVATAccount()+"</" + VAT_ACCOUNT + ">\n");

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
