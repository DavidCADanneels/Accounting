package be.dafke.BasicAccounting.Goods;


import be.dafke.BusinessModel.Article;
import be.dafke.BusinessModel.Articles;
import be.dafke.BusinessModel.Contact;
import be.dafke.BusinessModel.Contacts;
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
public class OrderPanel extends JPanel {
    private final Articles articles;
    private final JButton order;
    private final SelectableTable<Article> table;
    private JComboBox<Contact> comboBox;
    private Contacts contacts;
    private Contact supplier;
    private final OrderDataTableModel orderDataTableModel;

    public OrderPanel(Articles articles, Contacts contacts) {
        this.articles = articles;
        this.contacts = contacts;
        orderDataTableModel = new OrderDataTableModel(articles, null);
        table = new SelectableTable<>(orderDataTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));
        table.setAutoCreateRowSorter(true);
//        table.setRowSorter(null);

        comboBox = new JComboBox<>();
        comboBox.addActionListener(e -> {
            supplier = (Contact)comboBox.getSelectedItem();
            orderDataTableModel.setSupplier(supplier);
        });
        fireSupplierAddedOrRemoved();

        order = new JButton("Order");
        order.addActionListener(e -> {

        });


        JScrollPane scrollPane = new JScrollPane(table);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(comboBox, BorderLayout.NORTH);
        add(order, BorderLayout.SOUTH);
    }

    public void fireSupplierAddedOrRemoved() {
        comboBox.removeAllItems();
        contacts.getBusinessObjects(Contact::isSupplier).forEach(contact -> comboBox.addItem(contact));
//        orderDataTableModel.fireTableDataChanged();
    }
}