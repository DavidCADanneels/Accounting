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
public class PurchaseOrderCreateGUI extends JFrame {
    private final PurchaseOrderCreatePanel orderPanel;

    private static PurchaseOrderCreateGUI purchaseOrderCreateGui = null;

    private PurchaseOrderCreateGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("CREATE_SO"));
        orderPanel = new PurchaseOrderCreatePanel(accounting);
        setContentPane(orderPanel);
        pack();
    }

    public static PurchaseOrderCreateGUI showPurchaseOrderGUI(Accounting accounting) {
        if (purchaseOrderCreateGui == null) {
            purchaseOrderCreateGui = new PurchaseOrderCreateGUI(accounting);
            Main.addFrame(purchaseOrderCreateGui);
        }
        return purchaseOrderCreateGui;
    }
}