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
    private JMenuItem articlesTable, stockTable;

    private Articles articles;
    private Contacts contacts;
    private Stock stock;
    private Accounting accounting;

    public GoodsMenu() {
        super(getBundle("Accounting").getString("GOODS"));
        setMnemonic(KeyEvent.VK_G);
        articlesTable = new JMenuItem(getBundle("Accounting").getString("ARTICLES"));
        articlesTable.setMnemonic(KeyEvent.VK_A);
        articlesTable.addActionListener(e -> {
            ArticlesGUI.showArticles(articles, contacts).setVisible(true);
        });
        articlesTable.setEnabled(false);

        stockTable = new JMenuItem(getBundle("Accounting").getString("STOCK"));
        stockTable.setMnemonic(KeyEvent.VK_S);
        stockTable.addActionListener(e -> {
            StockGUI.showStock(accounting).setVisible(true);
        });
        stockTable.setEnabled(false);

        add(articlesTable);
        add(stockTable);
    }

    public void setAccounting(Accounting accounting) {
        this.accounting = accounting;
        setContacts(accounting==null?null:accounting.getContacts());
        setArticles(accounting==null?null:accounting.getArticles());
        setStock(accounting==null?null:accounting.getStock());
    }

    public void setStock(Stock stock) {
        this.stock = stock;
        stockTable.setEnabled(stock!=null);
    }

    public void setContacts(Contacts contacts) {
        this.contacts = contacts;
    }

    public void setArticles(Articles articles){
        this.articles = articles;
        articlesTable.setEnabled(articles!=null);
    }
}
