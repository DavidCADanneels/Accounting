package be.dafke.BasicAccounting.Goods;


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
public class StockPanel extends JPanel {
    private final JButton createPurchaseOrder, createSalesOrder;//, viewSalesOrder, viewPurchaseOrder;
    private final SelectableTable<StockItem> table;
    private final StockDataTableModel stockDataTableModel;

    public StockPanel(Accounting accounting) {
        Stock stock = accounting.getStock();
        stockDataTableModel = new StockDataTableModel(stock);
        table = new SelectableTable<>(stockDataTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));
        table.setAutoCreateRowSorter(true);
//        table.setRowSorter(null);
        JScrollPane scrollPane = new JScrollPane(table);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        createPurchaseOrder = new JButton("create Purchase Order");
        createPurchaseOrder.addActionListener(e -> {
            PurchaseOrderGUI.showPurchaseOrderGUI(accounting).setVisible(true);
        });

        createSalesOrder = new JButton("create Sales Order");
        createSalesOrder.addActionListener(e -> {
            SalesOrderGUI.showSalesOrderGUI(accounting).setVisible(true);
        });


//        viewPurchaseOrder = new JButton("create Purchase Order");
//        viewPurchaseOrder.addActionListener(e -> {
//            PurchaseOrderGUI.showPurchaseOrdersGUI(accounting).setVisible(true);
//        });
//
//        viewSalesOrder = new JButton("create Sales Order");
//        viewSalesOrder.addActionListener(e -> {
//            SalesOrderGUI.showSalesOrdersGUI(accounting).setVisible(true);
//        });


        JPanel buttons = new JPanel(new GridLayout(0,2));
        buttons.add(createPurchaseOrder);
        buttons.add(createSalesOrder);

        add(buttons, BorderLayout.NORTH);
    }
}