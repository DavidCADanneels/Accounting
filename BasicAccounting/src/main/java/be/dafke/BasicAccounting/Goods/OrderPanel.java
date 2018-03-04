package be.dafke.BasicAccounting.Goods;


import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;
import java.awt.*;
import java.util.function.Predicate;

import static java.util.ResourceBundle.getBundle;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
public class OrderPanel extends JPanel {
    private final JButton order;
    private final SelectableTable<StockItem> table;
    private JComboBox<Contact> comboBox;
    private Contacts contacts;
    private Articles articles;
    private Contact contact;
    Predicate<Contact> filter;
    private final OrderDataTableModel orderDataTableModel;

    public OrderPanel(Accounting accounting, Order.OrderType orderType) {
        this.contacts = accounting.getContacts();
        this.articles = accounting.getArticles();
        orderDataTableModel = new OrderDataTableModel(articles, null, orderType);
        table = new SelectableTable<>(orderDataTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));
        table.setAutoCreateRowSorter(true);
//        table.setRowSorter(null);

        comboBox = new JComboBox<>();
        comboBox.addActionListener(e -> {
            contact = (Contact) comboBox.getSelectedItem();
            orderDataTableModel.setContact(contact);
        });
        filter = orderType==Order.OrderType.PURCHASE?Contact::isSupplier:Contact::isCustomer;
        fireSupplierAddedOrRemoved();

        order = new JButton("Book Order");
        order.addActionListener(e -> {
            Order order = orderDataTableModel.getOrder();
            if(orderType==Order.OrderType.PURCHASE) {
                PurchaseOrders purchaseOrders = accounting.getPurchaseOrders();
                order.setName(purchaseOrders.getId());
                try {
                    purchaseOrders.addBusinessObject(order);
                } catch (EmptyNameException e1) {
                    e1.printStackTrace();
                } catch (DuplicateNameException e1) {
                    e1.printStackTrace();
                }
            } else if(orderType==Order.OrderType.SALE) {
                SalesOrders salesOrders = accounting.getSalesOrders();
                order.setName(salesOrders.getId());
                try {
                    salesOrders.addBusinessObject(order);
                } catch (EmptyNameException e1) {
                    e1.printStackTrace();
                } catch (DuplicateNameException e1) {
                    e1.printStackTrace();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(comboBox, BorderLayout.NORTH);
        add(order, BorderLayout.SOUTH);
    }

    public void fireSupplierAddedOrRemoved() {
        comboBox.removeAllItems();
        contacts.getBusinessObjects(filter).forEach(contact -> comboBox.addItem(contact));
//        orderDataTableModel.fireTableDataChanged();
    }
}