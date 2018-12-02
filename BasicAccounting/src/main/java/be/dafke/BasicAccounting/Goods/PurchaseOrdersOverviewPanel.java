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
    private final SelectableTable<PurchaseOrder> table;
    private final PurchaseOrdersOverviewDataTableModel purchaseOrdersOverviewDataTableModel;
    private final PurchaseOrderDetailTable purchaseOrderDetailTable;
    private final PurchaseTotalsPanel purchaseTotalsPanel;
    private final JButton createPurchaseOrder;
    private final PurchaseOrdersDetailPanel purchaseOrdersDetailPanel;

    public PurchaseOrdersOverviewPanel(Accounting accounting) {
        purchaseOrdersOverviewDataTableModel = new PurchaseOrdersOverviewDataTableModel(accounting.getPurchaseOrders());
        table = new SelectableTable<>(purchaseOrdersOverviewDataTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(1000, 400));
        table.setAutoCreateRowSorter(true);
//        table.setRowSorter(null);

        purchaseTotalsPanel = new PurchaseTotalsPanel();

        purchaseOrderDetailTable = new PurchaseOrderDetailTable(purchaseTotalsPanel);
        purchaseOrdersDetailPanel = new PurchaseOrdersDetailPanel(accounting);

        firePurchaseOrderAddedOrRemoved();

        DefaultListSelectionModel selection = new DefaultListSelectionModel();
        selection.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                PurchaseOrder purchaseOrder = table.getSelectedObject();
                purchaseOrderDetailTable.setOrder(purchaseOrder);
                purchaseOrdersDetailPanel.setOrder(purchaseOrder);
            }
        });
        table.setSelectionModel(selection);

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel details = new JPanel(new BorderLayout());
        details.add(purchaseOrderDetailTable, BorderLayout.CENTER);
        details.add(purchaseTotalsPanel, BorderLayout.SOUTH);

        createPurchaseOrder = new JButton(getBundle("Accounting").getString("CREATE_PO"));
        createPurchaseOrder.addActionListener(e -> {
            PurchaseOrderCreateGUI purchaseOrderCreateGUI = PurchaseOrderCreateGUI.showPurchaseOrderGUI(accounting);
            purchaseOrderCreateGUI.setLocation(getLocationOnScreen());
            purchaseOrderCreateGUI.setVisible(true);
        });

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(purchaseOrdersDetailPanel, BorderLayout.CENTER);
        rightPanel.add(createPurchaseOrder, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(Main.createSplitPane(scrollPane, details, JSplitPane.VERTICAL_SPLIT));
        add(rightPanel, BorderLayout.EAST);
    }

    public void firePurchaseOrderAddedOrRemoved() {
        purchaseOrdersOverviewDataTableModel.fireTableDataChanged();
    }
}