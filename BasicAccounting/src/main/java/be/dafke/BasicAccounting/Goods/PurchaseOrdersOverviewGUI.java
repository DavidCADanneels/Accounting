package be.dafke.BasicAccounting.Goods;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;

import javax.swing.*;

import static java.util.ResourceBundle.getBundle;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
public class PurchaseOrdersOverviewGUI extends JFrame {
    private final PurchaseOrdersOverviewPanel ordersOverViewPanel;

    private static PurchaseOrdersOverviewGUI purchaseOrderGui = null;

    private PurchaseOrdersOverviewGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("PO_OVERVIEW"));
        ordersOverViewPanel = new PurchaseOrdersOverviewPanel(accounting);
        setContentPane(ordersOverViewPanel);
        pack();
    }

    public static PurchaseOrdersOverviewGUI showPurchaseOrderGUI(Accounting accounting) {
        if (purchaseOrderGui == null) {
            purchaseOrderGui = new PurchaseOrdersOverviewGUI(accounting);
            Main.addFrame(purchaseOrderGui);
        }
        return purchaseOrderGui;
    }

    public static void firePurchaseOrderAddedOrRemovedForAll(){
        if (purchaseOrderGui!=null){
            purchaseOrderGui.firePurchaseOrderAddedOrRemoved();
        }
    }

    public void firePurchaseOrderAddedOrRemoved(){
        ordersOverViewPanel.firePurchaseOrderAddedOrRemoved();
    }
}