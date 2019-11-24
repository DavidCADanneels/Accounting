package be.dafke.BasicAccounting.Trade;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.Accounting.BusinessModel.Accounting;

import javax.swing.*;

import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

public class SalesOrdersOverviewGUI extends JFrame {
    private final SalesOrdersOverviewPanel ordersOverViewPanel;

    private static HashMap<Accounting,SalesOrdersOverviewGUI> map = new HashMap<>();

    private SalesOrdersOverviewGUI(){
        super(getBundle("Accounting").getString("SO_OVERVIEW"));
        ordersOverViewPanel = new SalesOrdersOverviewPanel();
        setContentPane(ordersOverViewPanel);
        pack();
    }

    public static SalesOrdersOverviewGUI showSalesOrderGUI(Accounting accounting) {
        SalesOrdersOverviewGUI gui = map.get(accounting);
        if (gui == null) {
            gui = new SalesOrdersOverviewGUI();
            gui.setAccounting(accounting);
            map.put(accounting, gui);
            Main.addFrame(gui);
        }
        return gui;
    }

    public void setAccounting(Accounting accounting) {
        ordersOverViewPanel.setAccounting(accounting);
    }

    public static void fireSalesOrderAddedOrRemovedForAccounting(Accounting accounting){
        SalesOrdersOverviewGUI gui = map.get(accounting);
        if (gui!=null){
            gui.fireSalesOrderAddedOrRemoved();
        }
    }

    private void fireSalesOrderAddedOrRemoved() {
        ordersOverViewPanel.fireSalesOrderAddedOrRemoved();
    }
}