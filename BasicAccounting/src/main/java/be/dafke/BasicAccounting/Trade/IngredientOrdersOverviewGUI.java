package be.dafke.BasicAccounting.Trade;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.Accounting.BusinessModel.Accounting;

import javax.swing.*;

import static java.util.ResourceBundle.getBundle;

public class IngredientOrdersOverviewGUI extends JFrame {

    private static IngredientOrdersOverviewGUI gui = null;
    private final IngredientOrdersOverviewPanel overviewPanel;

    private IngredientOrdersOverviewGUI() {
        super(getBundle("Accounting").getString("INGREDIENT_ORDERS"));
        overviewPanel = new IngredientOrdersOverviewPanel();

        setContentPane(overviewPanel);
        pack();
    }

    public static IngredientOrdersOverviewGUI showIngredientOrdersGUI(Accounting accounting) {
        if (gui == null) {
            gui = new IngredientOrdersOverviewGUI();
            gui.setAccounting(accounting);
            Main.addFrame(gui);
        }
        return gui;
    }

    public void setAccounting(Accounting accounting) {
        overviewPanel.setAccounting(accounting);
    }

    public static void fireIngredientAddedOrRemovedForAll(){
        if (gui != null){
            gui.fireIngredientAddedOrRemoved();
        }
    }

    private void fireIngredientAddedOrRemoved() {
        overviewPanel.fireIngredientAddedOrRemoved();
    }
}