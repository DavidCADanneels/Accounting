package be.dafke.BasicAccounting.Deliveroo;


import be.dafke.BasicAccounting.Goods.ArticlesPanel;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Articles;
import be.dafke.BusinessModel.Contacts;
import be.dafke.BusinessModel.DeliverooMeals;

import javax.swing.*;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
public class MealsGUI extends JFrame {
    private final MealsPanel mealsPanel;

    private static final HashMap<DeliverooMeals, MealsGUI> articlesGuis = new HashMap<>();

    private MealsGUI(DeliverooMeals deliverooMeals) {
        super(getBundle("Accounting").getString("MEALS"));
        mealsPanel = new MealsPanel(deliverooMeals);
        setContentPane(mealsPanel);
        pack();
    }

    public static MealsGUI showMeals(DeliverooMeals deliverooMeals) {
        MealsGUI gui = articlesGuis.get(deliverooMeals);
        if (gui == null) {
            gui = new MealsGUI(deliverooMeals);
            articlesGuis.put(deliverooMeals, gui);
            Main.addFrame(gui);
        }
        return gui;
    }
}