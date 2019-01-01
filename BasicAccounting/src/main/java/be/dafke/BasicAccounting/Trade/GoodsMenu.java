package be.dafke.BasicAccounting.Trade;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Articles;
import be.dafke.BusinessModel.Contacts;

import javax.swing.*;

import java.awt.event.KeyEvent;

import static java.util.ResourceBundle.getBundle;

public class GoodsMenu extends JMenu {
    private JMenuItem articlesTable, stockTable, salesOrders, purchaseOrders, promoOrders, stockHistoryTable;

    private Articles articles;
    private Contacts contacts;
    private Accounting accounting;

    public GoodsMenu() {
        super(getBundle("Accounting").getString("TRADE"));
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

        stockHistoryTable = new JMenuItem(getBundle("Accounting").getString("STOCK_HISTORY"));
        stockHistoryTable.setMnemonic(KeyEvent.VK_H);
        stockHistoryTable.addActionListener(e -> {
            StockHistoryGUI stockGUI = StockHistoryGUI.showStockHistory(accounting);
            stockGUI.setLocation(getLocationOnScreen());
            stockGUI.setVisible(true);
        });
        stockHistoryTable.setEnabled(false);

        purchaseOrders = new JMenuItem(getBundle("Accounting").getString("POS"));
        purchaseOrders.setMnemonic(KeyEvent.VK_P);
        purchaseOrders.addActionListener(e -> {
            PurchaseOrdersOverviewGUI purchaseOrdersOverviewGUI = PurchaseOrdersOverviewGUI.showPurchaseOrderGUI(accounting);
            purchaseOrdersOverviewGUI.setLocation(getLocationOnScreen());
            purchaseOrdersOverviewGUI.setVisible(true);
        });
        purchaseOrders.setEnabled(false);

        salesOrders = new JMenuItem(getBundle("Accounting").getString("SOS"));
        salesOrders.setMnemonic(KeyEvent.VK_S);
        salesOrders.addActionListener(e -> {
            SalesOrdersOverviewGUI salesOrdersGUI = SalesOrdersOverviewGUI.showSalesOrderGUI(accounting);
            salesOrdersGUI.setLocation(getLocationOnScreen());
            salesOrdersGUI.setVisible(true);
        });
        salesOrders.setEnabled(false);

        promoOrders = new JMenuItem(getBundle("Accounting").getString("PROMO_ORDERS"));
        promoOrders.setMnemonic(KeyEvent.VK_R);
        promoOrders.addActionListener(e -> {
            PromoOrdersOverviewGUI promoOrdersGUI = PromoOrdersOverviewGUI.showPromoOrderGUI(accounting);
            promoOrdersGUI.setLocation(getLocationOnScreen());
            promoOrdersGUI.setVisible(true);
        });
        promoOrders.setEnabled(false);

        add(articlesTable);
        add(stockTable);
        add(stockHistoryTable);
        add(purchaseOrders);
        add(salesOrders);
        add(promoOrders);
    }

    public void setAccounting(Accounting accounting) {
        this.accounting = accounting;
        setContacts(accounting==null?null:accounting.getContacts());
        setArticles(accounting==null?null:accounting.getArticles());
        stockTable.setEnabled(accounting!=null);
        stockHistoryTable.setEnabled(accounting!=null);
        purchaseOrders.setEnabled(accounting!=null);
        salesOrders.setEnabled(accounting!=null);
        promoOrders.setEnabled(accounting!=null);
    }

    public void setContacts(Contacts contacts) {
        this.contacts = contacts;
    }

    public void setArticles(Articles articles){
        this.articles = articles;
        articlesTable.setEnabled(articles!=null);
    }
}
