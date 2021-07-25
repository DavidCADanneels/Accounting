package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class PurchaseOrderCreateGUI extends JFrame {
    final PurchaseOrderCreatePanel orderPanel

    static PurchaseOrderCreateGUI purchaseOrderCreateGui = null

    PurchaseOrderCreateGUI(Accounting accounting) {
        super(getBundle("Accounting").getString("CREATE_PO"))
        orderPanel = new PurchaseOrderCreatePanel(accounting)
        setContentPane(orderPanel)
        pack()
    }

    static PurchaseOrderCreateGUI showPurchaseOrderGUI(Accounting accounting) {
        if (purchaseOrderCreateGui == null) {
            purchaseOrderCreateGui = new PurchaseOrderCreateGUI(accounting)
            Main.addFrame(purchaseOrderCreateGui)
        }
        purchaseOrderCreateGui
    }

//    static void fireSupplierAddedOrRemovedForAll() {
//        if (purchaseOrderCreateGui != null) {
//            purchaseOrderCreateGui.fireSupplierAddedOrRemoved()
//        }
//    }

//    void fireSupplierAddedOrRemoved() {
//        orderPanel.fireSupplierAddedOrRemoved()
//    }
}