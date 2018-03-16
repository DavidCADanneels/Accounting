package be.dafke.BasicAccounting.Goods;


import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;
import java.awt.*;
import java.util.function.Predicate;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
public class SalesOrderCreatePanel extends JPanel {
    private final JButton orderButton;
    private final SelectableTable<OrderItem> table;
    private SalesOrder order;
    private JComboBox<Contact> comboBox;
    private Contacts contacts;
    private Articles articles;
    private Contact contact;
    Predicate<Contact> filter;
    private final SalesOrderCreateDataTableModel salesOrderCreateDataTableModel;

    public SalesOrderCreatePanel(Accounting accounting) {
        this.contacts = accounting.getContacts();
        this.articles = accounting.getArticles();
        order = new SalesOrder();
        order.setArticles(articles);

        SaleTotalsPanel saleTotalsPanel = new SaleTotalsPanel();
        salesOrderCreateDataTableModel = new SalesOrderCreateDataTableModel(articles, null, order, saleTotalsPanel);
        table = new SelectableTable<>(salesOrderCreateDataTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));
        table.setAutoCreateRowSorter(true);
//        table.setRowSorter(null);

        comboBox = new JComboBox<>();
        comboBox.addActionListener(e -> {
            contact = (Contact) comboBox.getSelectedItem();
            salesOrderCreateDataTableModel.setContact(contact);
        });
        filter = Contact::isCustomer;
        fireCustomerAddedOrRemoved();

        orderButton = new JButton("Book Order");
        orderButton.addActionListener(e -> {
            SalesOrders salesOrders = accounting.getSalesOrders();
            order.setCustomer(contact);
            try {
                order.removeEmptyOrderItems();
                salesOrders.addBusinessObject(order);
                order = new SalesOrder();
                order.setArticles(articles);
                salesOrderCreateDataTableModel.setOrder(order);
//                fireSalesOrderAddedOrRemovedForAll();
            } catch (EmptyNameException e1) {
                e1.printStackTrace();
            } catch (DuplicateNameException e1) {
                e1.printStackTrace();
            }
        });
        JPanel south = new JPanel(new BorderLayout());
        south.add(orderButton, BorderLayout.SOUTH);
        south.add(saleTotalsPanel, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(table);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(comboBox, BorderLayout.NORTH);
        add(south, BorderLayout.SOUTH);
    }

    public void fireCustomerAddedOrRemoved() {
        comboBox.removeAllItems();
        contacts.getBusinessObjects(filter).forEach(contact -> comboBox.addItem(contact));
//        salesOrderDataTableModel.fireTableDataChanged();
    }
}