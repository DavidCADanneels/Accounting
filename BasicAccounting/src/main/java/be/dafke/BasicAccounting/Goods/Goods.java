package be.dafke.BasicAccounting.Goods;


import be.dafke.BusinessModel.Article;
import be.dafke.BusinessModel.Articles;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
public class Goods extends JFrame {
    public static void main(String[] args) {
        Articles articles = new Articles();
//        addDummyData(articles);
        Goods goederen = new Goods("Goederen", articles);
        goederen.setVisible(true);
    }

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


    public Goods(String title, Articles articles) {
        super(title);
        setContentPane(new GoodsPanel(articles));
        pack();
    }
}