package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.PurchaseOrder

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class PurchaseOrdersOverviewGUI extends JFrame {
    final PurchaseOrdersOverviewPanel ordersOverViewPanel

    static HashMap<Accounting,PurchaseOrdersOverviewGUI> purchaseOrderOverviewGuiMap = new HashMap<>()

    PurchaseOrdersOverviewGUI() {
        super(getBundle("Accounting").getString("PO_OVERVIEW"))
        ordersOverViewPanel = new PurchaseOrdersOverviewPanel()
        setContentPane(ordersOverViewPanel)
        pack()
    }

    static PurchaseOrdersOverviewGUI showPurchaseOrderGUI(Accounting accounting) {
        PurchaseOrdersOverviewGUI gui = purchaseOrderOverviewGuiMap.get(accounting)
        if (gui == null) {
            gui = new PurchaseOrdersOverviewGUI()
            gui.accounting = accounting
            purchaseOrderOverviewGuiMap.put(accounting, gui)
            Main.addFrame(gui)
        }
        gui
    }

    static void firePurchaseOrderAddedOrRemovedForAccounting(Accounting accounting){
        PurchaseOrdersOverviewGUI gui = purchaseOrderOverviewGuiMap.get(accounting)
        if(gui!=null){
            gui.firePurchaseOrderAddedOrRemoved()
        }
    }

    void firePurchaseOrderAddedOrRemoved(){
        ordersOverViewPanel.firePurchaseOrderAddedOrRemoved()
    }

    void selectOrder(PurchaseOrder purchaseOrder) {
        ordersOverViewPanel.selectOrder(purchaseOrder)
    }

    void setAccounting(Accounting accounting) {
        ordersOverViewPanel.accounting = accounting
    }
}
