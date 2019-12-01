package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.OrderItem
import be.dafke.Accounting.BusinessModel.PurchaseOrder
import be.dafke.ComponentModel.SelectableTable

import javax.swing.JMenuItem
import javax.swing.JPopupMenu

class SalesOrderDetailsPopupMenu extends JPopupMenu {
    private final JMenuItem goToPurchaseOrder
    private final SelectableTable<OrderItem> table
    private Accounting accounting

    SalesOrderDetailsPopupMenu(SelectableTable<OrderItem> table) {
        this.table = table
        goToPurchaseOrder = new JMenuItem("go to PO")
        goToPurchaseOrder.addActionListener({ e -> goToPo() })
        add(goToPurchaseOrder)
    }

    private void goToPo() {
        setVisible(false)
        OrderItem orderItem = table.getSelectedObject()
        PurchaseOrder purchaseOrder = orderItem.getPurchaseOrder()
        PurchaseOrdersOverviewGUI purchaseOrdersOverviewGUI = PurchaseOrdersOverviewGUI.showPurchaseOrderGUI(accounting)
        purchaseOrdersOverviewGUI.selectOrder(purchaseOrder)
    }

    void setAccounting(Accounting accounting) {
        this.accounting = accounting
    }
}
