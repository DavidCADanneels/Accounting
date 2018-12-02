package be.dafke.BasicAccounting.Goods;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Articles;
import be.dafke.BusinessModel.Contacts;
import be.dafke.BusinessModel.Stock;

import javax.swing.*;

import java.awt.event.KeyEvent;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 27/12/2015.
 */
public class GoodsMenu extends JMenu {
    private JMenuItem articlesTable, stockTable, purchaseOrders, salesOrders;

    private Articles articles;
    private Contacts contacts;
    private Accounting accounting;

    public GoodsMenu() {
        super(getBundle("Accounting").getString("GOODS"));
        setMnemonic(KeyEvent.VK_G);
        articlesTable = new JMenuItem(getBundle("Accounting").getString("ARTICLES"));
        articlesTable.setMnemonic(KeyEvent.VK_A);
        articlesTable.addActionListener(e -> {
            ArticlesGUI articlesGUI = ArticlesGUI.showArticles(articles, contacts);
            articlesGUI.setLocation(getLocationOnScreen());
            articlesGUI.setVisible(true);
        });
        articlesTable.setEnabled(false);

        stockTable = new JMenuItem(getBundle("Accounting").getString("STOCK"));
        stockTable.setMnemonic(KeyEvent.VK_T);
        stockTable.addActionListener(e -> {
            StockGUI stockGUI = StockGUI.showStock(accounting);
            stockGUI.setLocation(getLocationOnScreen());
            stockGUI.setVisible(true);
        });
        stockTable.setEnabled(false);

        purchaseOrders = new JMenuItem(getBundle("Accounting").getString("PO"));
        purchaseOrders.setMnemonic(KeyEvent.VK_P);
        purchaseOrders.addActionListener(e -> {
            PurchaseOrdersGUI purchaseOrdersGUI = PurchaseOrdersGUI.showPurchaseOrderGUI(accounting);
            purchaseOrdersGUI.setLocation(getLocationOnScreen());
            purchaseOrdersGUI.setVisible(true);
        });
        purchaseOrders.setEnabled(false);

        salesOrders = new JMenuItem(getBundle("Accounting").getString("SO"));
        salesOrders.setMnemonic(KeyEvent.VK_S);
        salesOrders.addActionListener(e -> {
//            SalesOrdersGUI salesOrdersGUI = SalesOrdersGUI.showPurchaseOrderGUI(accounting);
//            salesOrdersGUI.setLocation(getLocationOnScreen());
//            salesOrdersGUI.setVisible(true);
        });
        salesOrders.setEnabled(false);

        add(articlesTable);
        add(stockTable);
        add(purchaseOrders);
        add(salesOrders);
    }

    public void setAccounting(Accounting accounting) {
        this.accounting = accounting;
        setContacts(accounting==null?null:accounting.getContacts());
        setArticles(accounting==null?null:accounting.getArticles());
        stockTable.setEnabled(accounting!=null);
        purchaseOrders.setEnabled(accounting!=null);
        salesOrders.setEnabled(accounting!=null);
    }

    public void setContacts(Contacts contacts) {
        this.contacts = contacts;
    }

    public void setArticles(Articles articles){
        this.articles = articles;
        articlesTable.setEnabled(articles!=null);
    }
}
