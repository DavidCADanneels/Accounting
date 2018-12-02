package be.dafke.BasicAccounting.Goods;

import be.dafke.BusinessModel.OrderItem;
import be.dafke.BusinessModel.PurchaseOrder;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.awt.*;

public class PurchaseOrderDetailTable extends JPanel {

    private final SelectableTable<OrderItem> table;
    private final PurchaseOrdersViewDataTableModel purchaseOrdersViewDataTableModel;
    private final PurchaseTotalsPanel purchaseTotalsPanel;

    public PurchaseOrderDetailTable() {
        setLayout(new BorderLayout());

        purchaseOrdersViewDataTableModel = new PurchaseOrdersViewDataTableModel();
        table = new SelectableTable<>(purchaseOrdersViewDataTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(1000, 400));
        table.setAutoCreateRowSorter(true);
//        table.setRowSorter(null);

        purchaseTotalsPanel = new PurchaseTotalsPanel();

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane,BorderLayout.CENTER);
        add(purchaseTotalsPanel,BorderLayout.SOUTH);
    }

    public PurchaseOrder getOrder(){
        return purchaseOrdersViewDataTableModel.getOrder();
    }

    public void setOrder(PurchaseOrder purchaseOrder){
        purchaseOrdersViewDataTableModel.setOrder(purchaseOrder);
        purchaseTotalsPanel.fireOrderContentChanged(purchaseOrder);
    }
}
