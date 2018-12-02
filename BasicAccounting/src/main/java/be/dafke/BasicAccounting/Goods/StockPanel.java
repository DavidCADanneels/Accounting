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
    private final JButton viewPurchaseOrder ,viewSalesOrder;
    private final SelectableTable<OrderItem> table;
    private final StockDataTableModel stockDataTableModel;

    public StockPanel(Accounting accounting) {
        Stock stock = accounting.getStock();
        stockDataTableModel = new StockDataTableModel(stock);
        table = new SelectableTable<>(stockDataTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(1000, 400));
        JScrollPane scrollPane = new JScrollPane(table);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        viewPurchaseOrder = new JButton(getBundle("Accounting").getString("VIEW_PO"));
        viewPurchaseOrder.addActionListener(e -> {
            PurchaseOrdersOverviewGUI purchaseOrdersViewGUI = PurchaseOrdersOverviewGUI.showPurchaseOrderGUI(accounting);
            purchaseOrdersViewGUI.setLocation(getLocationOnScreen());
            purchaseOrdersViewGUI.setVisible(true);
        });

        viewSalesOrder = new JButton(getBundle("Accounting").getString("VIEW_SO"));
        viewSalesOrder.addActionListener(e -> {
            SalesOrdersOverviewGUI salesOrdersViewGUI = SalesOrdersOverviewGUI.showSalesOrderGUI(accounting);
            salesOrdersViewGUI.setLocation(getLocationOnScreen());
            salesOrdersViewGUI.setVisible(true);
        });


        JPanel buttons = new JPanel(new GridLayout(0,2));
        buttons.add(viewPurchaseOrder);
        buttons.add(viewSalesOrder);

        add(buttons, BorderLayout.NORTH);
    }

    public void fireStockContentChanged(){
        stockDataTableModel.fireTableDataChanged();
    }
}