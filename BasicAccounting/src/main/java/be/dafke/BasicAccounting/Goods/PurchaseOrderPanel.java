package be.dafke.BasicAccounting.Goods;


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
public class PurchaseOrderPanel extends JPanel {
    private final JButton orderButton;
    private final SelectableTable<StockItem> table;
    private JComboBox<Contact> comboBox;
    private Contacts contacts;
    private Articles articles;
    private Contact contact;
    Predicate<Contact> filter;
    private final PurchaseOrderDataTableModel purchaseOrderDataTableModel;

    public PurchaseOrderPanel(Accounting accounting) {
        this.contacts = accounting.getContacts();
        this.articles = accounting.getArticles();
        purchaseOrderDataTableModel = new PurchaseOrderDataTableModel(articles, null);
        table = new SelectableTable<>(purchaseOrderDataTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));
        table.setAutoCreateRowSorter(true);
//        table.setRowSorter(null);

        comboBox = new JComboBox<>();
        comboBox.addActionListener(e -> {
            contact = (Contact) comboBox.getSelectedItem();
            purchaseOrderDataTableModel.setContact(contact);
        });
        filter = Contact::isSupplier;
        fireSupplierAddedOrRemoved();

        orderButton = new JButton("Book Order");
        orderButton.addActionListener(e -> {
            Order order = purchaseOrderDataTableModel.getOrder();
            PurchaseOrders purchaseOrders = accounting.getPurchaseOrders();
            order.setName(purchaseOrders.getId());
            order.setSupplier(contact);
            try {
                purchaseOrders.addBusinessObject(order);
            } catch (EmptyNameException e1) {
                e1.printStackTrace();
            } catch (DuplicateNameException e1) {
                e1.printStackTrace();
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(comboBox, BorderLayout.NORTH);
        add(orderButton, BorderLayout.SOUTH);
    }

    public void fireSupplierAddedOrRemoved() {
        comboBox.removeAllItems();
        contacts.getBusinessObjects(filter).forEach(contact -> comboBox.addItem(contact));
//        purchaseOrderDataTableModel.fireTableDataChanged();
    }
}