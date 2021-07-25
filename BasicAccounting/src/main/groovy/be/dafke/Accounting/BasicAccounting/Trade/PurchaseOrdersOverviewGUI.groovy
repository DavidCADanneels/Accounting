package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.PurchaseOrder

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class PurchaseOrdersOverviewGUI extends JFrame {
    final PurchaseOrdersOverviewPanel ordersOverViewPanel

    static PurchaseOrdersOverviewGUI gui = null

    PurchaseOrdersOverviewGUI() {
        super(getBundle("Accounting").getString("PO_OVERVIEW"))
        ordersOverViewPanel = new PurchaseOrdersOverviewPanel()
        setContentPane(ordersOverViewPanel)
        pack()
    }

    static PurchaseOrdersOverviewGUI showPurchaseOrderGUI() {
        if (gui == null) {
            gui = new PurchaseOrdersOverviewGUI()
            Main.addFrame(gui)
        }
        gui
    }

    void selectOrder(PurchaseOrder purchaseOrder) {
        ordersOverViewPanel.selectOrder(purchaseOrder)
    }
}
