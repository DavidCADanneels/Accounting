package be.dafke.BasicAccounting.Goods;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.awt.*;

import static java.util.ResourceBundle.getBundle;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
public class PurchaseOrdersOverviewPanel extends JPanel {
    private final SelectableTable<PurchaseOrder> overviewTable;
    private final SelectableTable<OrderItem> detailsTable;
    private final PurchaseOrdersOverviewDataTableModel overviewTableModel;
    private final PurchaseOrdersViewDataTableModel detailsTableModel;
    private final PurchaseTotalsPanel purchaseTotalsPanel;

    private final PurchaseOrdersDetailPanel purchaseOrdersDetailPanel;

    public PurchaseOrdersOverviewPanel(Accounting accounting) {
        overviewTableModel = new PurchaseOrdersOverviewDataTableModel(accounting.getPurchaseOrders());
        overviewTable = new SelectableTable<>(overviewTableModel);
        overviewTable.setPreferredScrollableViewportSize(new Dimension(1000, 400));

        detailsTableModel = new PurchaseOrdersViewDataTableModel();
        detailsTable = new SelectableTable<>(detailsTableModel);
        detailsTable.setPreferredScrollableViewportSize(new Dimension(1000, 200));

        purchaseTotalsPanel = new PurchaseTotalsPanel();

        purchaseOrdersDetailPanel = new PurchaseOrdersDetailPanel(accounting);

        firePurchaseOrderAddedOrRemoved();

        DefaultListSelectionModel selection = new DefaultListSelectionModel();
        selection.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                PurchaseOrder purchaseOrder = overviewTable.getSelectedObject();
                detailsTableModel.setOrder(purchaseOrder);
                purchaseTotalsPanel.fireOrderContentChanged(purchaseOrder);
                purchaseOrdersDetailPanel.setOrder(purchaseOrder);
            }
        });
        overviewTable.setSelectionModel(selection);

        JScrollPane overviewScroll = new JScrollPane(overviewTable);
        JScrollPane detailScroll = new JScrollPane(detailsTable);
        JSplitPane splitPane = Main.createSplitPane(overviewScroll, detailScroll, JSplitPane.VERTICAL_SPLIT);

        JPanel center = new JPanel(new BorderLayout());
        center.add(splitPane, BorderLayout.CENTER);
        center.add(purchaseTotalsPanel, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(center, BorderLayout.CENTER);
        add(purchaseOrdersDetailPanel, BorderLayout.EAST);
    }

    public void firePurchaseOrderAddedOrRemoved() {
        overviewTableModel.fireTableDataChanged();
    }
}