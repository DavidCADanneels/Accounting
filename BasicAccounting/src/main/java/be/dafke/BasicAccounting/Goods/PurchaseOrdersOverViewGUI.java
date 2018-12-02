package be.dafke.BasicAccounting.Goods;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;

import javax.swing.*;

import static java.util.ResourceBundle.getBundle;
import static javax.swing.JSplitPane.VERTICAL_SPLIT;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
public class PurchaseOrdersOverViewGUI extends JFrame {
    private final PurchaseOrdersViewPanel orderViewPanel;
    private final PurchaseOrderCreatePanel orderCreatePanel;
    private final PurchaseOrdersOverViewPanel ordersOverViewPanel;

    private static PurchaseOrdersOverViewGUI purchaseOrderGui = null;

    private PurchaseOrdersOverViewGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("PO_OVERVIEW"));
        ordersOverViewPanel = new PurchaseOrdersOverViewPanel(accounting);
        orderViewPanel = new PurchaseOrdersViewPanel(accounting);
        orderCreatePanel = new PurchaseOrderCreatePanel(accounting);

//        JSplitPane splitPane = Main.createSplitPane(orderViewPanel, orderCreatePanel, VERTICAL_SPLIT);
//        JSplitPane splitPane = Main.createSplitPane(ordersOverViewPanel, orderViewPanel, VERTICAL_SPLIT);

        setContentPane(ordersOverViewPanel);
        pack();
    }

    public static PurchaseOrdersOverViewGUI showPurchaseOrderGUI(Accounting accounting) {
        if (purchaseOrderGui == null) {
            purchaseOrderGui = new PurchaseOrdersOverViewGUI(accounting);
            Main.addFrame(purchaseOrderGui);
        }
        return purchaseOrderGui;
    }

    public static void firePurchaseOrderAddedOrRemovedForAll(){
        if (purchaseOrderGui!=null){
            purchaseOrderGui.firePurchaseOrderAddedOrRemoved();
        }
    }

    public static void fireSupplierAddedOrRemovedForAll() {
        if (purchaseOrderGui != null) {
            purchaseOrderGui.fireSupplierAddedOrRemoved();
        }
    }

    public void fireSupplierAddedOrRemoved() {
        orderCreatePanel.fireSupplierAddedOrRemoved();
//        ordersOverViewPanel.
    }


    public void firePurchaseOrderAddedOrRemoved(){
        orderViewPanel.firePurchaseOrderAddedOrRemoved();
        ordersOverViewPanel.firePurchaseOrderAddedOrRemoved();
    }
}