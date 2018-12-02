package be.dafke.BasicAccounting.Goods;


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
public class PurchaseOrdersOverViewPanel extends JPanel implements ListSelectionListener {
    private final SelectableTable<PurchaseOrder> table;
    private final PurchaseOrdersOverViewDataTableModel purchaseOrdersOverViewDataTableModel;
    private final PurchaseOrderDetailTable purchaseOrderDetailTable;

    public PurchaseOrdersOverViewPanel(Accounting accounting) {
        purchaseOrdersOverViewDataTableModel = new PurchaseOrdersOverViewDataTableModel(accounting.getPurchaseOrders());
        table = new SelectableTable<>(purchaseOrdersOverViewDataTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(1000, 400));
        table.setAutoCreateRowSorter(true);
//        table.setRowSorter(null);


        purchaseOrderDetailTable = new PurchaseOrderDetailTable();

//        firePurchaseOrderAddedOrRemoved();

        DefaultListSelectionModel selection = new DefaultListSelectionModel();
        selection.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                PurchaseOrder purchaseOrder = table.getSelectedObject();
                purchaseOrderDetailTable.setOrder(purchaseOrder);
            }
        });
        table.setSelectionModel(selection);

        setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane,BorderLayout.NORTH);

        add(purchaseOrderDetailTable, BorderLayout.SOUTH);
    }

    public void firePurchaseOrderAddedOrRemoved() {
        purchaseOrdersOverViewDataTableModel.fireTableDataChanged();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {

    }
}