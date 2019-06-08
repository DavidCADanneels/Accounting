package be.dafke.BasicAccounting.Meals;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Ingredients;

import javax.swing.*;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

public class IngredientsGUI extends JFrame {
    private final IngredientsPanel ingredientsPanel;

    private static final HashMap<Accounting, IngredientsGUI> articlesGuis = new HashMap<>();

    private IngredientsGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("INGREDIENTS"));
        ingredientsPanel = new IngredientsPanel(accounting);
        setContentPane(ingredientsPanel);
        pack();
    }

    public static IngredientsGUI showIngredients(Accounting accounting) {
        IngredientsGUI gui = articlesGuis.get(accounting);
        if (gui == null) {
            gui = new IngredientsGUI(accounting);
            articlesGuis.put(accounting, gui);
            Main.addFrame(gui);
        }
        return gui;
    }
}