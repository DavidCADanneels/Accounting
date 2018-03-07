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
public class PurchaseOrdersViewGUI extends JFrame {
    private final PurchaseOrdersViewPanel orderPanel;

    private static PurchaseOrdersViewGUI purchaseOrderGui = null;

    private PurchaseOrdersViewGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("VIEW_PO"));
        orderPanel = new PurchaseOrdersViewPanel(accounting);
        setContentPane(orderPanel);
        pack();
    }

    public static PurchaseOrdersViewGUI showPurchaseOrderGUI(Accounting accounting) {
        if (purchaseOrderGui == null) {
            purchaseOrderGui = new PurchaseOrdersViewGUI(accounting);
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
        orderPanel.firePurchaseOrderAddedOrRemoved();
    }
}