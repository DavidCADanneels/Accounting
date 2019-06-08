package be.dafke.BasicAccounting.Trade;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Ingredients;

import javax.swing.*;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

public class IngredientsGUI extends JFrame {
    private final IngredientsPanel ingredientsPanel;

    private static final HashMap<Ingredients, IngredientsGUI> articlesGuis = new HashMap<>();

    private IngredientsGUI(Ingredients ingredients) {
        super(getBundle("Accounting").getString("INGREDIENTS"));
        ingredientsPanel = new IngredientsPanel(ingredients);
        setContentPane(ingredientsPanel);
        pack();
    }

    public static IngredientsGUI showIngredients(Ingredients ingredients) {
        IngredientsGUI gui = articlesGuis.get(ingredients);
        if (gui == null) {
            gui = new IngredientsGUI(ingredients);
            articlesGuis.put(ingredients, gui);
            Main.addFrame(gui);
        }
        return gui;
    }
}