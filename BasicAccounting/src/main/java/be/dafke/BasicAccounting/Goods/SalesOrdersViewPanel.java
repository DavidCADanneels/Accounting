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
public class SalesOrdersViewPanel extends JPanel {
    private final JButton deliveredButton, payedButton;
    private final SelectableTable<OrderItem> table;
    private final SalesOrders salesOrders;
    private JComboBox<Order> comboBox;
    private JCheckBox payed, delivered;
    private Order order;
    private final SalesOrdersViewDataTableModel salesOrdersViewDataTableModel;

    public SalesOrdersViewPanel(Accounting accounting) {
        this.salesOrders = accounting.getSalesOrders();
        salesOrdersViewDataTableModel = new SalesOrdersViewDataTableModel();
        table = new SelectableTable<>(salesOrdersViewDataTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));
        table.setAutoCreateRowSorter(true);
//        table.setRowSorter(null);

        deliveredButton = new JButton("Order Delivered");
        deliveredButton.addActionListener(e -> {
            Stock stock = accounting.getStock();
            Order order = salesOrdersViewDataTableModel.getOrder();
            stock.removeLoad(order);
            order.setDelivered(true);
        });

        payedButton = new JButton("Order Payed");
        payedButton.addActionListener(e -> {
            Order order = salesOrdersViewDataTableModel.getOrder();
            order.setPayed(true);
        });

        payed = new JCheckBox("Payed");
        delivered = new JCheckBox("Delived");
        payed.setEnabled(false);
        delivered.setEnabled(false);

        comboBox = new JComboBox<>();
        comboBox.addActionListener(e -> {
            order = (Order) comboBox.getSelectedItem();
            payed.setSelected(order.isPayed());
            delivered.setSelected(order.isDelivered());
            deliveredButton.setEnabled(!order.isDelivered());
            payedButton.setEnabled(!order.isPayed());
            salesOrdersViewDataTableModel.setOrder(order);
        });
        fireCustomerAddedOrRemoved();

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

    public void fireCustomerAddedOrRemoved() {
        comboBox.removeAllItems();
        salesOrders.getBusinessObjects().forEach(order -> comboBox.addItem(order));
//        salesOrdersViewDataTableModel.fireTableDataChanged();
    }
}