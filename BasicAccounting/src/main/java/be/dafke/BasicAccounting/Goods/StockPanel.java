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
    private final JButton createPurchaseOrder, createSalesOrder;
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
            OrderGUI.showPurchaseOrderGUI(accounting).setVisible(true);
        });

        createSalesOrder = new JButton("create Sales Order");
        createSalesOrder.addActionListener(e -> {
            OrderGUI.showSalesOrderGUI(accounting).setVisible(true);
        });


        JPanel buttons = new JPanel();
        buttons.add(createPurchaseOrder);
        buttons.add(createSalesOrder);

        add(buttons, BorderLayout.NORTH);
    }
}