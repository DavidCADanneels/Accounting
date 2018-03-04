package be.dafke.BasicAccounting.Goods;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Articles;
import be.dafke.BusinessModel.Contacts;

import javax.swing.*;

import java.awt.event.KeyEvent;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 27/12/2015.
 */
public class GoodsMenu extends JMenu {
    private JMenuItem articlesTable;

    private Articles articles;
    private Contacts contacts;

    public GoodsMenu() {
        super(getBundle("Accounting").getString("GOODS"));
        setMnemonic(KeyEvent.VK_G);
        articlesTable = new JMenuItem(getBundle("Accounting").getString("ARTICLES"));
        articlesTable.addActionListener(e -> {
            ArticlesGUI.showArticles(articles, contacts).setVisible(true);
        });
        articlesTable.setEnabled(false);
        add(articlesTable);
    }

    public void setAccounting(Accounting accounting) {
        setContacts(accounting==null?null:accounting.getContacts());
        setArticles(accounting==null?null:accounting.getArticles());
    }

    public void setContacts(Contacts contacts) {
        this.contacts = contacts;
    }

    public void setArticles(Articles articles){
        this.articles = articles;
        articlesTable.setEnabled(articles!=null);
    }
}
