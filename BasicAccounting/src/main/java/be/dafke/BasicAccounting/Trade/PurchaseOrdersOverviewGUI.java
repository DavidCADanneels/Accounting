package be.dafke.BasicAccounting.Trade;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.PurchaseOrder;

import javax.swing.*;

import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

public class PurchaseOrdersOverviewGUI extends JFrame {
    private final PurchaseOrdersOverviewPanel ordersOverViewPanel;

    private static HashMap<Accounting,PurchaseOrdersOverviewGUI> purchaseOrderOverviewGuiMap = new HashMap<>();

    private PurchaseOrdersOverviewGUI() {
        super(getBundle("Accounting").getString("PO_OVERVIEW"));
        ordersOverViewPanel = new PurchaseOrdersOverviewPanel();
        setContentPane(ordersOverViewPanel);
        pack();
    }

    public static PurchaseOrdersOverviewGUI showPurchaseOrderGUI(Accounting accounting) {
        PurchaseOrdersOverviewGUI gui = purchaseOrderOverviewGuiMap.get(accounting);
        if (gui == null) {
            gui = new PurchaseOrdersOverviewGUI();
            gui.setAccounting(accounting);
            purchaseOrderOverviewGuiMap.put(accounting, gui);
            Main.addFrame(gui);
        }
        return gui;
    }

    public static void firePurchaseOrderAddedOrRemovedForAccounting(Accounting accounting){
        PurchaseOrdersOverviewGUI gui = purchaseOrderOverviewGuiMap.get(accounting);
        if(gui!=null){
            gui.firePurchaseOrderAddedOrRemoved();
        }
    }

    public void firePurchaseOrderAddedOrRemoved(){
        ordersOverViewPanel.firePurchaseOrderAddedOrRemoved();
    }

    public void selectOrder(PurchaseOrder purchaseOrder) {
        ordersOverViewPanel.selectOrder(purchaseOrder);
    }

    public void setAccounting(Accounting accounting) {
        ordersOverViewPanel.setAccounting(accounting);
    }
}