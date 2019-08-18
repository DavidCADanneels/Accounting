package be.dafke.BasicAccounting.Trade;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;

import javax.swing.*;

import static java.util.ResourceBundle.getBundle;

public class BuyInGredientsGUI extends JFrame {

    private static BuyInGredientsGUI gui = null;
    private final BuyIngredientsPanel buyIngredientsPanel;

    private BuyInGredientsGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("BUY_INGREDIENTS"));
        buyIngredientsPanel = new BuyIngredientsPanel(accounting);
//        buyIngredientsPanel.setAccounting(accounting);
        setContentPane(buyIngredientsPanel);
        pack();
    }

    public static BuyInGredientsGUI showBuyIngredientsGUI(Accounting accounting) {
        if (gui == null) {
            gui = new BuyInGredientsGUI(accounting);
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