package be.dafke.BasicAccounting.Trade;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.Accounting.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.awt.*;

public class PurchaseOrdersOverviewPanel extends JPanel {
    private final SelectableTable<PurchaseOrder> overviewTable;
    private final SelectableTable<OrderItem> detailsTable;
    private final PurchaseOrdersOverviewDataTableModel overviewTableModel;
    private final PurchaseOrderViewDataTableModel detailsTableModel;
    private final TotalsPanel totalsPanel;

    private final PurchaseOrdersDetailPanel purchaseOrdersDetailPanel;

    public PurchaseOrdersOverviewPanel() {
        overviewTableModel = new PurchaseOrdersOverviewDataTableModel();
        overviewTable = new SelectableTable<>(overviewTableModel);
        overviewTable.setPreferredScrollableViewportSize(new Dimension(1000, 400));

        detailsTableModel = new PurchaseOrderViewDataTableModel();
        detailsTable = new SelectableTable<>(detailsTableModel);
        detailsTable.setPreferredScrollableViewportSize(new Dimension(1000, 200));

        totalsPanel = new TotalsPanel();

        purchaseOrdersDetailPanel = new PurchaseOrdersDetailPanel();

        firePurchaseOrderAddedOrRemoved();

        DefaultListSelectionModel selection = new DefaultListSelectionModel();
        selection.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                PurchaseOrder purchaseOrder = overviewTable.getSelectedObject();
                detailsTableModel.setOrder(purchaseOrder);
                totalsPanel.fireOrderContentChanged(purchaseOrder);
                purchaseOrdersDetailPanel.setOrder(purchaseOrder);
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
        add(purchaseOrdersDetailPanel, BorderLayout.EAST);
    }

    public void firePurchaseOrderAddedOrRemoved() {
        overviewTableModel.fireTableDataChanged();
    }

    public void selectOrder(PurchaseOrder purchaseOrder) {
        int row = overviewTableModel.getRow(purchaseOrder);
        if(row!=-1){
            overviewTable.setRowSelectionInterval(row,row);
            Rectangle cellRect = overviewTable.getCellRect(row, 0, false);
            overviewTable.scrollRectToVisible(cellRect);
        }
    }

    public void setAccounting(Accounting accounting) {
        overviewTableModel.setAccounting(accounting);
//        popup.setAccounting(accounting);
        purchaseOrdersDetailPanel.setAccounting(accounting);
    }
}