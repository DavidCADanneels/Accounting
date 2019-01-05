package be.dafke.BasicAccounting.Trade;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.PurchaseOrder;

import javax.swing.*;

import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

public class PurchaseOrdersOverviewGUI extends JFrame {
    private final PurchaseOrdersOverviewPanel ordersOverViewPanel;

    private static HashMap<Accounting,PurchaseOrdersOverviewGUI> map = null;

    private PurchaseOrdersOverviewGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("PO_OVERVIEW"));
        ordersOverViewPanel = new PurchaseOrdersOverviewPanel(accounting);
        setContentPane(ordersOverViewPanel);
        pack();
    }

    public static PurchaseOrdersOverviewGUI showPurchaseOrderGUI(Accounting accounting) {
        PurchaseOrdersOverviewGUI gui = map.get(accounting);
        if (gui == null) {
            gui = new PurchaseOrdersOverviewGUI(accounting);
            map.put(accounting, gui);
            Main.addFrame(gui);
        }
        return gui;
    }

    public static void firePurchaseOrderAddedOrRemovedForAll(){
        for (PurchaseOrdersOverviewGUI gui : map.values()){
            gui.firePurchaseOrderAddedOrRemoved();
        }
    }

    public void firePurchaseOrderAddedOrRemoved(){
        ordersOverViewPanel.firePurchaseOrderAddedOrRemoved();
    }

    public void selectOrder(PurchaseOrder purchaseOrder) {
        ordersOverViewPanel.selectOrder(purchaseOrder);
    }
}