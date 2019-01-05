package be.dafke.BasicAccounting.Trade;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;

import javax.swing.*;

import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

public class PromoOrdersOverviewGUI extends JFrame {
    private final PromoOrdersOverviewPanel gui;

    private static HashMap<Accounting,PromoOrdersOverviewGUI> map = new HashMap<>();

    private PromoOrdersOverviewGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("PR_OVERVIEW"));
        gui = new PromoOrdersOverviewPanel();
        gui.setAccounting(accounting);
        setContentPane(gui);
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
        gui.firePromoOrderAddedOrRemoved();
    }
}