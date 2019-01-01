package be.dafke.BasicAccounting.Trade;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;

import javax.swing.*;

import static java.util.ResourceBundle.getBundle;

public class PromoOrdersOverviewGUI extends JFrame {
    private final PromoOrdersOverviewPanel ordersOverViewPanel;

    private static PromoOrdersOverviewGUI salesOrderGui = null;

    private PromoOrdersOverviewGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("PR_OVERVIEW"));
        ordersOverViewPanel = new PromoOrdersOverviewPanel(accounting);
        setContentPane(ordersOverViewPanel);
        pack();
    }

    public static PromoOrdersOverviewGUI showPromoOrderGUI(Accounting accounting) {
        if (salesOrderGui == null) {
            salesOrderGui = new PromoOrdersOverviewGUI(accounting);
            Main.addFrame(salesOrderGui);
        }
        return salesOrderGui;
    }

    public static void firePromoOrderAddedOrRemovedForAll(){
        if (salesOrderGui!=null){
            salesOrderGui.firePromoOrderAddedOrRemoved();
        }
    }

    private void firePromoOrderAddedOrRemoved() {
        ordersOverViewPanel.firePromoOrderAddedOrRemoved();
    }
}