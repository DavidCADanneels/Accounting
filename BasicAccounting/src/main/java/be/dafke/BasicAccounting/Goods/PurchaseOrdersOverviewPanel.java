package be.dafke.BasicAccounting.Goods;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
public class PurchaseOrdersOverviewPanel extends JPanel {
    private final SelectableTable<PurchaseOrder> table;
    private final PurchaseOrdersOverViewDataTableModel purchaseOrdersOverViewDataTableModel;
    private final PurchaseOrderDetailTable purchaseOrderDetailTable;
    private final PurchaseTotalsPanel purchaseTotalsPanel;

    public PurchaseOrdersOverviewPanel(Accounting accounting) {
        purchaseOrdersOverViewDataTableModel = new PurchaseOrdersOverViewDataTableModel(accounting.getPurchaseOrders());
        table = new SelectableTable<>(purchaseOrdersOverViewDataTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(1000, 400));
        table.setAutoCreateRowSorter(true);
//        table.setRowSorter(null);

        purchaseTotalsPanel = new PurchaseTotalsPanel();

        purchaseOrderDetailTable = new PurchaseOrderDetailTable(purchaseTotalsPanel);

        firePurchaseOrderAddedOrRemoved();

        DefaultListSelectionModel selection = new DefaultListSelectionModel();
        selection.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                PurchaseOrder purchaseOrder = table.getSelectedObject();
                purchaseOrderDetailTable.setOrder(purchaseOrder);
            }
        });
        table.setSelectionModel(selection);

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel details = new JPanel(new BorderLayout());
        details.add(purchaseOrderDetailTable, BorderLayout.CENTER);
        details.add(purchaseTotalsPanel, BorderLayout.SOUTH);

        add(Main.createSplitPane(scrollPane, details, JSplitPane.VERTICAL_SPLIT));
    }

    public void firePurchaseOrderAddedOrRemoved() {
        purchaseOrdersOverViewDataTableModel.fireTableDataChanged();
    }
}