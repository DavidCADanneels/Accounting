package be.dafke.BasicAccounting.Trade;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;

import javax.swing.*;

import static java.util.ResourceBundle.getBundle;

public class IngredientOrdersGUI extends JFrame {

    private static IngredientOrdersGUI gui = null;
    private final IngredientOrdersPanel ingredientOrdersPanel;

    private IngredientOrdersGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("BUY_INGREDIENTS"));
        ingredientOrdersPanel = new IngredientOrdersPanel();
        ingredientOrdersPanel.setAccounting(accounting);
        setContentPane(ingredientOrdersPanel);
        pack();
    }

    public static IngredientOrdersGUI showIngredientOrdersGUI(Accounting accounting) {
        if (gui == null) {
            gui = new IngredientOrdersGUI(accounting);
            Main.addFrame(gui);
        }
        return gui;
    }

    public static void fireIngredientAddedOrRemoved(){
        if (gui != null){
            gui.fireIngredientAddedOrRemoved();
        }
    }
}