package be.dafke.BasicAccounting.Meals;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;

import javax.swing.*;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

public class IngredientsViewGUI extends JFrame {
    private final IngredientsViewPanel ingredientsViewPanel;

    private static final HashMap<Accounting, IngredientsViewGUI> articlesGuis = new HashMap<>();

    private IngredientsViewGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("INGREDIENTS"));
        ingredientsViewPanel = new IngredientsViewPanel(accounting, true);
        setContentPane(ingredientsViewPanel);
        pack();
    }

    public static IngredientsViewGUI showIngredients(Accounting accounting) {
        IngredientsViewGUI gui = articlesGuis.get(accounting);
        if (gui == null) {
            gui = new IngredientsViewGUI(accounting);
            articlesGuis.put(accounting, gui);
            Main.addFrame(gui);
        }
        return gui;
    }
}