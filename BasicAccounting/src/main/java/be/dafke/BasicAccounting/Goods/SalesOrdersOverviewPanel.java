package be.dafke.BasicAccounting.Goods;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.OrderItem;
import be.dafke.BusinessModel.SalesOrder;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.awt.*;

import static java.util.ResourceBundle.getBundle;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
public class SalesOrdersOverviewPanel extends JPanel {
    private final SelectableTable<SalesOrder> overviewTable;
    private final SelectableTable<OrderItem> detailsTable;
    private final SalesOrdersOverviewDataTableModel overviewTableModel;
    private final SalesOrdersViewDataTableModel detailsTableModel;
    private final SaleTotalsPanel saleTotalsPanel;

    private final SalesOrdersDetailPanel salesOrdersDetailPanel;

    public SalesOrdersOverviewPanel(Accounting accounting) {
        overviewTableModel = new SalesOrdersOverviewDataTableModel(accounting.getSalesOrders());
        overviewTable = new SelectableTable<>(overviewTableModel);
        overviewTable.setPreferredScrollableViewportSize(new Dimension(1000, 400));

        detailsTableModel = new SalesOrdersViewDataTableModel();
        detailsTable = new SelectableTable<>(detailsTableModel);
        detailsTable.setPreferredScrollableViewportSize(new Dimension(1000, 200));

        saleTotalsPanel = new SaleTotalsPanel();

        salesOrdersDetailPanel = new SalesOrdersDetailPanel(accounting);

        fireSalesOrderAddedOrRemoved();

        DefaultListSelectionModel selection = new DefaultListSelectionModel();
        selection.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                SalesOrder salesOrder = overviewTable.getSelectedObject();
                detailsTableModel.setOrder(salesOrder);
                saleTotalsPanel.fireOrderContentChanged(salesOrder);
                salesOrdersDetailPanel.setOrder(salesOrder);
            }
        });
        overviewTable.setSelectionModel(selection);

        JScrollPane overviewScroll = new JScrollPane(overviewTable);
        JScrollPane detailScroll = new JScrollPane(detailsTable);
        JSplitPane splitPane = Main.createSplitPane(overviewScroll, detailScroll, JSplitPane.VERTICAL_SPLIT);

        JPanel center = new JPanel(new BorderLayout());
        center.add(splitPane, BorderLayout.CENTER);
        center.add(saleTotalsPanel, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(center, BorderLayout.CENTER);
        add(salesOrdersDetailPanel, BorderLayout.EAST);
    }

    public void fireSalesOrderAddedOrRemoved() {
        overviewTableModel.fireTableDataChanged();
    }
}