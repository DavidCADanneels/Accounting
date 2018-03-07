package be.dafke.BasicAccounting.Goods;


import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.awt.*;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
public class PurchaseOrdersViewPanel extends JPanel {
    private final JButton deliveredButton, payedButton;
    private final SelectableTable<StockItem> table;
    private final PurchaseOrders purchaseOrders;
    private JComboBox<Order> comboBox;
    private JCheckBox payed, delivered;
    private Order order;
    private final PurchaseOrdersViewDataTableModel purchaseOrdersViewDataTableModel;

    public PurchaseOrdersViewPanel(Accounting accounting) {
        this.purchaseOrders = accounting.getPurchaseOrders();
        purchaseOrdersViewDataTableModel = new PurchaseOrdersViewDataTableModel();
        table = new SelectableTable<>(purchaseOrdersViewDataTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));
        table.setAutoCreateRowSorter(true);
//        table.setRowSorter(null);

        deliveredButton = new JButton("Order Delivered");
        deliveredButton.addActionListener(e -> {
            Stock stock = accounting.getStock();
            Order order = purchaseOrdersViewDataTableModel.getOrder();
            stock.addLoad(order);
            order.setDelivered(true);
        });

        payedButton = new JButton("Order Payed");
        payedButton.addActionListener(e -> {
            Order order = purchaseOrdersViewDataTableModel.getOrder();
            order.setPayed(true);
        });

        payed = new JCheckBox("Payed");
        delivered = new JCheckBox("Delived");
        payed.setEnabled(false);
        delivered.setEnabled(false);

        comboBox = new JComboBox<>();
        comboBox.addActionListener(e -> {
            order = (Order) comboBox.getSelectedItem();
            payed.setSelected(order!=null&&order.isPayed());
            delivered.setSelected(order!=null&&order.isDelivered());
            deliveredButton.setEnabled(order!=null&&!order.isDelivered());
            payedButton.setEnabled(order!=null&&!order.isPayed());
            purchaseOrdersViewDataTableModel.setOrder(order);
        });
        firePurchaseOrderAddedOrRemoved();

        JScrollPane scrollPane = new JScrollPane(table);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        JPanel north = new JPanel();
        north.add(comboBox);

        north.add(payed);
        north.add(delivered);
        add(north, BorderLayout.NORTH);
        JPanel south = new JPanel();
        south.add(deliveredButton);
        south.add(payedButton);
        add(south, BorderLayout.SOUTH);
    }

    public void firePurchaseOrderAddedOrRemoved() {
        comboBox.removeAllItems();
        purchaseOrders.getBusinessObjects().forEach(order -> comboBox.addItem(order));
//        purchaseOrderDataTableModel.fireTableDataChanged();
    }
}