package be.dafke.BasicAccounting.Goods;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.SalesOrder;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.awt.*;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
public class SalesOrdersOverviewPanel extends JPanel {
    private final SelectableTable<SalesOrder> table;
    private final SalesOrdersOverviewDataTableModel purchaseOrdersOverviewDataTableModel;
    private final SalesOrderDetailTable salesOrderDetailTable;
    private final SaleTotalsPanel saleTotalsPanel;

    public SalesOrdersOverviewPanel(Accounting accounting) {
        purchaseOrdersOverviewDataTableModel = new SalesOrdersOverviewDataTableModel(accounting.getSalesOrders());
        table = new SelectableTable<>(purchaseOrdersOverviewDataTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(1000, 400));
        table.setAutoCreateRowSorter(true);
//        table.setRowSorter(null);

        saleTotalsPanel = new SaleTotalsPanel();

        salesOrderDetailTable = new SalesOrderDetailTable(saleTotalsPanel);

        fireSalesOrderAddedOrRemoved();

        DefaultListSelectionModel selection = new DefaultListSelectionModel();
        selection.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                SalesOrder salesOrder = table.getSelectedObject();
                salesOrderDetailTable.setOrder(salesOrder);
            }
        });
        table.setSelectionModel(selection);

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel details = new JPanel(new BorderLayout());
        details.add(salesOrderDetailTable, BorderLayout.CENTER);
        details.add(saleTotalsPanel, BorderLayout.SOUTH);

        add(Main.createSplitPane(scrollPane, details, JSplitPane.VERTICAL_SPLIT));
    }

    public void fireSalesOrderAddedOrRemoved() {
        purchaseOrdersOverviewDataTableModel.fireTableDataChanged();
    }
}