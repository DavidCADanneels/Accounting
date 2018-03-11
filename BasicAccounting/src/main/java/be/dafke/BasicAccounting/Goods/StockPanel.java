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
    private final JButton createPurchaseOrder, createSalesOrder, viewPurchaseOrder ,viewSalesOrder;
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

        createPurchaseOrder = new JButton(getBundle("Accounting").getString("CREATE_PO"));
        createPurchaseOrder.addActionListener(e -> {
            PurchaseOrderCreateGUI.showPurchaseOrderGUI(accounting).setVisible(true);
        });

        createSalesOrder = new JButton(getBundle("Accounting").getString("CREATE_SO"));
        createSalesOrder.addActionListener(e -> {
            SalesOrderCreateGUI.showSalesOrderGUI(accounting).setVisible(true);
        });


        viewPurchaseOrder = new JButton(getBundle("Accounting").getString("VIEW_PO"));
        viewPurchaseOrder.addActionListener(e -> {
            PurchaseOrdersViewGUI.showPurchaseOrderGUI(accounting).setVisible(true);
        });

        viewSalesOrder = new JButton(getBundle("Accounting").getString("VIEW_SO"));
        viewSalesOrder.addActionListener(e -> {
            SalesOrdersViewGUI.showSalesOrderGUI(accounting).setVisible(true);
        });


        JPanel buttons = new JPanel(new GridLayout(0,2));
        buttons.add(createPurchaseOrder);
        buttons.add(createSalesOrder);
        buttons.add(viewPurchaseOrder);
        buttons.add(viewSalesOrder);

        add(buttons, BorderLayout.NORTH);
    }

    public void fireStockContentChanged(){
        stockDataTableModel.fireTableDataChanged();
    }
}