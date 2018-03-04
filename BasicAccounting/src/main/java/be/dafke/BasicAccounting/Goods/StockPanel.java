package be.dafke.BasicAccounting.Goods;


import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;

import static java.util.ResourceBundle.getBundle;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
public class StockPanel extends JPanel {
    private final JButton createOrder, addOrder;
    private final SelectableTable<StockItem> table;
    private Accounting accounting;
    private final StockDataTableModel stockDataTableModel;

    public StockPanel(Accounting accounting) {
        Stock stock = accounting.getStock();
        Articles articles = accounting.getArticles();
        Contacts contacts = accounting.getContacts();
        stockDataTableModel = new StockDataTableModel(stock);
        table = new SelectableTable<>(stockDataTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));
        table.setAutoCreateRowSorter(true);
//        table.setRowSorter(null);
        JScrollPane scrollPane = new JScrollPane(table);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        createOrder = new JButton("create Order");
        createOrder.addActionListener(e -> {
            OrderGUI.showOrderGUI(articles, contacts).setVisible(true);
        });

        addOrder = new JButton("add Order");
        addOrder.addActionListener(e -> {
            // TODO: implement
        });


        JPanel buttons = new JPanel();
        buttons.add(createOrder);
        buttons.add(addOrder);

        add(buttons, BorderLayout.NORTH);
    }
}