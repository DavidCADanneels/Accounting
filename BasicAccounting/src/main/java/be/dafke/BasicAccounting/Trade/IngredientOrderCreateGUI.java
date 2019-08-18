package be.dafke.BasicAccounting.Trade;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.IngredientOrder;

import javax.swing.*;

import static java.util.ResourceBundle.getBundle;

public class IngredientOrderCreateGUI extends JFrame {

    private static IngredientOrderCreateGUI gui = null;
    private final IngredientOrderCreatePanel ingredientOrderCreatePanel;

    private IngredientOrderCreateGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("BUY_INGREDIENTS"));
        ingredientOrderCreatePanel = new IngredientOrderCreatePanel(accounting);
//        ingredientOrderCreatePanel = new IngredientOrderDetailsPanel(accounting);
//        buyIngredientsPanel.setAccounting(accounting);


//        ingredientOrder = new IngredientOrder();
//        ingredientOrder.setIngredients(ingredients);


        setContentPane(ingredientOrderCreatePanel);
        pack();
    }

    public static IngredientOrderCreateGUI showIngredientsOrderCreateGUI(Accounting accounting) {
        if (gui == null) {
            gui = new IngredientOrderCreateGUI(accounting);
            Main.addFrame(gui);
        }
        return gui;
    }
}