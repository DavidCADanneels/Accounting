package be.dafke.BasicAccounting.Trade;

import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.OrderItem;
import be.dafke.BusinessModel.PurchaseOrder;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;

public class SalesOrderDetailsPopupMenu extends JPopupMenu {
    private final JMenuItem goToPurchaseOrder;
    private final SelectableTable<OrderItem> table;
    private Accounting accounting;

    public SalesOrderDetailsPopupMenu(SelectableTable<OrderItem> table) {
        this.table = table;
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

    public void setAccounting(Accounting accounting) {
        this.accounting = accounting;
    }
}
