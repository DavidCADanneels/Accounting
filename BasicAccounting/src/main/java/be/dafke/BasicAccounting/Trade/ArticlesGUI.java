package be.dafke.BasicAccounting.Trade;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Articles;
import be.dafke.BusinessModel.Contacts;

import javax.swing.*;

import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

public class ArticlesGUI extends JFrame {
    private final ArticlesPanel articlesPanel;

    private static final HashMap<Articles, ArticlesGUI> articlesGuis = new HashMap<>();

    private ArticlesGUI(Articles articles, Contacts contacts) {
        super(getBundle("Accounting").getString("ARTICLES"));
        articlesPanel = new ArticlesPanel(articles, contacts);
        setContentPane(articlesPanel);
        pack();
    }

    public static ArticlesGUI showArticles(Articles articles, Contacts contacts) {
        ArticlesGUI gui = articlesGuis.get(articles);
        if (gui == null) {
            gui = new ArticlesGUI(articles, contacts);
            articlesGuis.put(articles, gui);
            Main.addFrame(gui);
        }
        return gui;
    }

    public static void fireSupplierAddedOrRemovedForAll(){
        articlesGuis.values().forEach(ArticlesGUI::fireSupplierAddedOrRemoved);
    }

    public void fireSupplierAddedOrRemoved(){
        articlesPanel.fireSupplierAddedOrRemoved();
    }
}