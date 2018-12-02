package be.dafke.BasicAccounting.Goods;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;

import static java.util.ResourceBundle.getBundle;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
public class PurchaseOrdersOverviewPanel extends JPanel {
    private final SelectableTable<PurchaseOrder> table;
    private final PurchaseOrdersOverviewDataTableModel tableModel;
    private final PurchaseOrderDetailTable purchaseOrderDetailTable;
    private final PurchaseTotalsPanel purchaseTotalsPanel;
    private final JButton createPurchaseOrder;
    private final PurchaseOrdersDetailPanel purchaseOrdersDetailPanel;

    public PurchaseOrdersOverviewPanel(Accounting accounting) {
        tableModel = new PurchaseOrdersOverviewDataTableModel(accounting.getPurchaseOrders());
        table = new SelectableTable<>(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(1000, 400));

        purchaseTotalsPanel = new PurchaseTotalsPanel();

        purchaseOrderDetailTable = new PurchaseOrderDetailTable(purchaseTotalsPanel);
        purchaseOrdersDetailPanel = new PurchaseOrdersDetailPanel(accounting);

        firePurchaseOrderAddedOrRemoved();

        DefaultListSelectionModel selection = new DefaultListSelectionModel();
        selection.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                RowSorter<? extends TableModel> rowSorter = table.getRowSorter();
                int rowInModel = rowSorter.convertRowIndexToModel(selectedRow);

                PurchaseOrder purchaseOrder = tableModel.getObject(rowInModel, 0);

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
        tableModel.fireTableDataChanged();
    }
}