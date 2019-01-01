package be.dafke.BasicAccounting.Trade;


import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.awt.*;

import static java.util.ResourceBundle.getBundle;

public class StockPanel extends JPanel {
    private final JButton viewPurchaseOrder ,viewSalesOrder;
    private final SelectableTable<Article> table;
    private final StockDataTableModel stockDataTableModel;

    public StockPanel(Accounting accounting) {
        stockDataTableModel = new StockDataTableModel(accounting.getArticles());
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
        add(createFilterPanel(), BorderLayout.NORTH);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createFilterPanel(){
        JPanel panel = new JPanel();
        JRadioButton withOrders = new JRadioButton("with Orders");
        JRadioButton inStock = new JRadioButton("in Stock");
        withOrders.addActionListener(e -> stockDataTableModel.setFilter(Article.withOrders()));
        inStock.addActionListener(e -> stockDataTableModel.setFilter(Article.inStock()));
        ButtonGroup group = new ButtonGroup();
        group.add(withOrders);
        group.add(inStock);
        withOrders.setSelected(true);
        stockDataTableModel.setFilter(Article.withOrders());
        panel.add(withOrders);
        panel.add(inStock);
        return panel;
    }

    private JPanel createButtonPanel(){
        JPanel panel = new JPanel(new GridLayout(0,2));
        panel.add(viewPurchaseOrder);
        panel.add(viewSalesOrder);
        return panel;
    }

    public void fireStockContentChanged(){
        stockDataTableModel.fireTableDataChanged();
    }
}