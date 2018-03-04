package be.dafke.BasicAccounting.Goods;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Order;

import javax.swing.*;

import static java.util.ResourceBundle.getBundle;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
public class PurchaseOrderGUI extends JFrame {
    private final PurchaseOrderPanel orderPanel;

    private static PurchaseOrderGUI purchaseOrderGui = null;

    private PurchaseOrderGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("CREATE_SO"));
        orderPanel = new PurchaseOrderPanel(accounting);
        setContentPane(orderPanel);
        pack();
    }

    public static PurchaseOrderGUI showPurchaseOrderGUI(Accounting accounting) {
        if (purchaseOrderGui == null) {
            purchaseOrderGui = new PurchaseOrderGUI(accounting);
            Main.addFrame(purchaseOrderGui);
        }
        return purchaseOrderGui;
    }
}