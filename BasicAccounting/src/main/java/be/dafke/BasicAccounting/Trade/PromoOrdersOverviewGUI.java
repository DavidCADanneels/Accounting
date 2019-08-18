package be.dafke.BasicAccounting.Trade;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;

import javax.swing.*;

import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

public class PromoOrdersOverviewGUI extends JFrame {
    private final PromoOrdersOverviewPanel ordersOverViewPanel;

    private static HashMap<Accounting,PromoOrdersOverviewGUI> map = new HashMap<>();

    private PromoOrdersOverviewGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("PR_OVERVIEW"));
        ordersOverViewPanel = new PromoOrdersOverviewPanel();
        ordersOverViewPanel.setAccounting(accounting);
        setContentPane(ordersOverViewPanel);
        pack();
    }

    public static PromoOrdersOverviewGUI showPromoOrderGUI(Accounting accounting) {
        PromoOrdersOverviewGUI gui = map.get(accounting);
        if (gui == null) {
            gui = new PromoOrdersOverviewGUI(accounting);
            map.put(accounting, gui);
            Main.addFrame(gui);
        }
        return gui;
    }

    public static void firePromoOrderAddedOrRemovedForAll(){
        for (PromoOrdersOverviewGUI gui : map.values()){
            gui.firePromoOrderAddedOrRemoved();
        }
    }

    private void firePromoOrderAddedOrRemoved() {
        ordersOverViewPanel.firePromoOrderAddedOrRemoved();
    }
}