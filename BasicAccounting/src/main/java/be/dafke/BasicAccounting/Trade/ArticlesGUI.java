package be.dafke.BasicAccounting.Trade;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;

import javax.swing.*;

import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

public class ArticlesGUI extends JFrame {
    private final ArticlesPanel articlesPanel;

    private static final HashMap<Accounting, ArticlesGUI> articlesGuis = new HashMap<>();

    private ArticlesGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("ARTICLES"));
        articlesPanel = new ArticlesPanel(accounting);
        setContentPane(articlesPanel);
        pack();
    }

    public static ArticlesGUI showArticles(Accounting accounting) {
        ArticlesGUI gui = articlesGuis.get(accounting);
        if (gui == null) {
            gui = new ArticlesGUI(accounting);
            articlesGuis.put(accounting, gui);
            Main.addFrame(gui);
        }
        return gui;
    }

    public static void fireTableUpdateForAccounting(Accounting accounting){
        ArticlesGUI gui = articlesGuis.get(accounting);
        if(gui!=null) {
            gui.fireTableUpdate();
        }
    }

    public void fireTableUpdate(){
        articlesPanel.fireTableUpdate();
    }

    public static void fireSupplierAddedOrRemovedForAccounting(Accounting accounting){
        ArticlesGUI gui = articlesGuis.get(accounting);
        if(gui!=null){
            gui.fireSupplierAddedOrRemoved();
        }
    }

    public void fireSupplierAddedOrRemoved(){
        articlesPanel.fireSupplierAddedOrRemoved();
        articlesPanel.fireTableUpdate();
    }

    public static void fireIngredientAddedOrRemovedForAccounting(Accounting accounting){
        ArticlesGUI gui = articlesGuis.get(accounting);
        if(gui!=null){
            gui.fireIngredientAddedOrRemoved();
        }
    }

    public void fireIngredientAddedOrRemoved(){
        articlesPanel.fireIngredientsAddedOrRemoved();
    }
}