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
public class PurchaseOrdersGUI extends JFrame {
    private final PurchaseOrdersViewPanel orderViewPanel;
    private final PurchaseOrderCreatePanel orderCreatePanel;

    private static PurchaseOrdersGUI purchaseOrderGui = null;

    private PurchaseOrdersGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("PO"));
        orderViewPanel = new PurchaseOrdersViewPanel(accounting);
        orderCreatePanel = new PurchaseOrderCreatePanel(accounting);

        JSplitPane splitPane = Main.createSplitPane(orderViewPanel, orderCreatePanel, VERTICAL_SPLIT);

        setContentPane(splitPane);
        pack();
    }

    public static PurchaseOrdersGUI showPurchaseOrderGUI(Accounting accounting) {
        if (purchaseOrderGui == null) {
            purchaseOrderGui = new PurchaseOrdersGUI(accounting);
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
    }


    public void firePurchaseOrderAddedOrRemoved(){
        orderViewPanel.firePurchaseOrderAddedOrRemoved();

    }
}