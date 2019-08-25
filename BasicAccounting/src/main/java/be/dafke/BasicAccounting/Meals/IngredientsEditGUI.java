package be.dafke.BasicAccounting.Meals;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;

import javax.swing.*;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

public class IngredientsEditGUI extends JFrame {
    private final IngredientsEditPanel ingredientsViewPanel;

    private static final HashMap<Accounting, IngredientsEditGUI> articlesGuis = new HashMap<>();

    private IngredientsEditGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("INGREDIENTS"));
        ingredientsViewPanel = new IngredientsEditPanel(accounting);
        setContentPane(ingredientsViewPanel);
        pack();
    }

    public static IngredientsEditGUI showIngredients(Accounting accounting) {
        IngredientsEditGUI gui = articlesGuis.get(accounting);
        if (gui == null) {
            gui = new IngredientsEditGUI(accounting);
            articlesGuis.put(accounting, gui);
            Main.addFrame(gui);
        }
        return gui;
    }
}