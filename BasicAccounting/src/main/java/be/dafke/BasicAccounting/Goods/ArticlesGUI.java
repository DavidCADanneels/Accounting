package be.dafke.BasicAccounting.Goods;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Article;
import be.dafke.BusinessModel.Articles;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;

import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
public class ArticlesGUI extends JFrame {
    public static void main(String[] args) {
        Articles articles = new Articles();
//        addDummyData(articles);
        ArticlesGUI goederen = new ArticlesGUI(articles);
        goederen.setVisible(true);
    }

    private static final HashMap<Articles, ArticlesGUI> goodsGuis = new HashMap<>();

    public static void addDummyData(Articles articles){
        Article article1 = new Article("article1");
        Article article2 = new Article("article2");
        Article article3 = new Article("article3");
        Article article4 = new Article("article4");
        try {
            articles.addBusinessObject(article1);
            articles.addBusinessObject(article2);
            articles.addBusinessObject(article3);
            articles.addBusinessObject(article4);
        } catch (EmptyNameException e) {
            e.printStackTrace();
        } catch (DuplicateNameException e) {
            e.printStackTrace();
        }
    }


    private ArticlesGUI(Articles articles) {
        super(getBundle("Accounting").getString("GOODS"));
        setContentPane(new ArticlesPanel(articles));
        pack();
    }

    public static ArticlesGUI showArticles(Articles articles) {
        ArticlesGUI gui = goodsGuis.get(articles);
        if (gui == null) {
            gui = new ArticlesGUI(articles);
            goodsGuis.put(articles, gui);
            Main.addFrame(gui);
        }
        return gui;
    }
}