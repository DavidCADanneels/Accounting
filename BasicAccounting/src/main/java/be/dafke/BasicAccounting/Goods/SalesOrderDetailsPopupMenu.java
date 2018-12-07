package be.dafke.BasicAccounting.Goods;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.OrderItem;
import be.dafke.BusinessModel.PurchaseOrder;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;

public class SalesOrderDetailsPopupMenu extends JPopupMenu {
    private final JMenuItem goToPurchaseOrder;
    private final SelectableTable<OrderItem> table;
    private final Accounting accounting;

    public SalesOrderDetailsPopupMenu(SelectableTable<OrderItem> table, Accounting accounting) {
        this.table = table;
        this.accounting = accounting;
        goToPurchaseOrder = new JMenuItem("go to PO");
        goToPurchaseOrder.addActionListener(e -> goToPo());
        add(goToPurchaseOrder);
    }

    private void goToPo() {
        setVisible(false);
        OrderItem orderItem = table.getSelectedObject();
        PurchaseOrder purchaseOrder = orderItem.getPurchaseOrder();
        PurchaseOrdersOverviewGUI purchaseOrdersOverviewGUI = PurchaseOrdersOverviewGUI.showPurchaseOrderGUI(accounting);
        purchaseOrdersOverviewGUI.selectOrder(purchaseOrder);
    }
}
