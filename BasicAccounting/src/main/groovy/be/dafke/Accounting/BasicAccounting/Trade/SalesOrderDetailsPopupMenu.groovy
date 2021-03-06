package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.OrderItem
import be.dafke.Accounting.BusinessModel.PurchaseOrder
import be.dafke.ComponentModel.SelectableTable

import javax.swing.JMenuItem
import javax.swing.JPopupMenu

class SalesOrderDetailsPopupMenu extends JPopupMenu {
    final JMenuItem goToPurchaseOrder
    final SelectableTable<OrderItem> table
    Accounting accounting

    SalesOrderDetailsPopupMenu(SelectableTable<OrderItem> table) {
        this.table = table
        goToPurchaseOrder = new JMenuItem("go to PO")
        goToPurchaseOrder.addActionListener({ e -> goToPo() })
        add(goToPurchaseOrder)
    }

    void goToPo() {
        setVisible(false)
        OrderItem orderItem = table.selectedObject
        PurchaseOrder purchaseOrder = orderItem.purchaseOrder
        PurchaseOrdersOverviewGUI purchaseOrdersOverviewGUI = PurchaseOrdersOverviewGUI.showPurchaseOrderGUI(accounting)
        purchaseOrdersOverviewGUI.selectOrder(purchaseOrder)
    }

    void setAccounting(Accounting accounting) {
        this.accounting = accounting
    }
}
