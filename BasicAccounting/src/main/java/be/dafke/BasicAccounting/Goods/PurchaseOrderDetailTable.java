package be.dafke.BasicAccounting.Goods;

import be.dafke.BusinessModel.OrderItem;
import be.dafke.BusinessModel.PurchaseOrder;
import be.dafke.BusinessModel.SalesOrder;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.awt.*;

public class PurchaseOrderDetailTable extends JPanel {

    private final SelectableTable<OrderItem> table;
    private final PurchaseOrdersViewDataTableModel purchaseOrdersViewDataTableModel;
    private final PurchaseTotalsPanel purchaseTotalsPanel;

    public PurchaseOrderDetailTable(PurchaseTotalsPanel purchaseTotalsPanel) {
        this.purchaseTotalsPanel = purchaseTotalsPanel;
        purchaseOrdersViewDataTableModel = new PurchaseOrdersViewDataTableModel();
        table = new SelectableTable<>(purchaseOrdersViewDataTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(1000, 200));
        table.setAutoCreateRowSorter(true);
//        table.setRowSorter(null);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);
    }

    public PurchaseOrder getOrder(){
        return purchaseOrdersViewDataTableModel.getOrder();
    }

    public void setOrder(PurchaseOrder purchaseOrder){
        purchaseOrdersViewDataTableModel.setOrder(purchaseOrder);
        purchaseTotalsPanel.fireOrderContentChanged(purchaseOrder);
    }
}
