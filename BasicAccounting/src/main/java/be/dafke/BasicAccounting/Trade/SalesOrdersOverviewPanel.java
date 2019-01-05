package be.dafke.BasicAccounting.Trade;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BasicAccounting.MainApplication.PopupForTableActivator;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.OrderItem;
import be.dafke.BusinessModel.SalesOrder;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.awt.*;

public class SalesOrdersOverviewPanel extends JPanel {
    private final SelectableTable<SalesOrder> overviewTable;
    private final SelectableTable<OrderItem> detailsTable;
    private final SalesOrdersOverviewDataTableModel overviewTableModel;
    private final SalesOrderDetailsDataTableModel detailsTableModel;
    private final TotalsPanel totalsPanel;

    private final SalesOrderDetailPanel salesOrderDetailPanel;
    private final SalesOrderDetailsPopupMenu popup;

    public SalesOrdersOverviewPanel(){
        overviewTableModel = new SalesOrdersOverviewDataTableModel();
        overviewTable = new SelectableTable<>(overviewTableModel);
        overviewTable.setPreferredScrollableViewportSize(new Dimension(1000, 400));

        detailsTableModel = new SalesOrderDetailsDataTableModel();
        detailsTable = new SelectableTable<>(detailsTableModel);
        detailsTable.setPreferredScrollableViewportSize(new Dimension(1000, 200));
        //
        popup = new SalesOrderDetailsPopupMenu(detailsTable);
        detailsTable.addMouseListener(PopupForTableActivator.getInstance(popup,detailsTable));

        totalsPanel = new TotalsPanel();
        salesOrderDetailPanel = new SalesOrderDetailPanel();

        fireSalesOrderAddedOrRemoved();

        DefaultListSelectionModel selection = new DefaultListSelectionModel();
        selection.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                SalesOrder salesOrder = overviewTable.getSelectedObject();
                detailsTableModel.setOrder(salesOrder);
                totalsPanel.fireOrderContentChanged(salesOrder);
                salesOrderDetailPanel.setOrder(salesOrder);
            }
        });
        overviewTable.setSelectionModel(selection);

        JScrollPane overviewScroll = new JScrollPane(overviewTable);
        JScrollPane detailScroll = new JScrollPane(detailsTable);
        JSplitPane splitPane = Main.createSplitPane(overviewScroll, detailScroll, JSplitPane.VERTICAL_SPLIT);

        JPanel center = new JPanel(new BorderLayout());
        center.add(splitPane, BorderLayout.CENTER);
        center.add(totalsPanel, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(center, BorderLayout.CENTER);
        add(salesOrderDetailPanel, BorderLayout.EAST);
    }

    public void fireSalesOrderAddedOrRemoved() {
        overviewTableModel.fireTableDataChanged();
    }

    public void setAccounting(Accounting accounting) {
        overviewTableModel.setAccounting(accounting);
        popup.setAccounting(accounting);
        salesOrderDetailPanel.setAccounting(accounting);
    }
}