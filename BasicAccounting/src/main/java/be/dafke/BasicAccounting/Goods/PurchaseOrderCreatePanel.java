package be.dafke.BasicAccounting.Goods;


import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.Predicate;

import static be.dafke.BasicAccounting.Goods.PurchaseOrdersViewGUI.firePurchaseOrderAddedOrRemovedForAll;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
public class PurchaseOrderCreatePanel extends JPanel {
    private final JButton orderButton;
    private final SelectableTable<OrderItem> table;
    private PurchaseOrder order;
    private JComboBox<Contact> comboBox;
    private Contacts contacts;
    private Articles articles;
    private Contact contact;
    Predicate<Contact> filter;
    private final PurchaseOrderCreateDataTableModel purchaseOrderCreateDataTableModel;

    public PurchaseOrderCreatePanel(Accounting accounting) {
        this.contacts = accounting.getContacts();
        this.articles = accounting.getArticles();
        order = new PurchaseOrder(articles);
        PurchaseTotalsPanel purchaseTotalsPanel = new PurchaseTotalsPanel();
        purchaseOrderCreateDataTableModel = new PurchaseOrderCreateDataTableModel(articles, null, order, purchaseTotalsPanel);
        table = new SelectableTable<>(purchaseOrderCreateDataTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));
        table.setAutoCreateRowSorter(true);
//        table.setRowSorter(null);

        comboBox = new JComboBox<>();
        comboBox.addActionListener(e -> {
            contact = (Contact) comboBox.getSelectedItem();
            purchaseOrderCreateDataTableModel.setContact(contact);
        });
        filter = Contact::isSupplier;
        fireSupplierAddedOrRemoved();

        orderButton = new JButton("Book Order");
        orderButton.addActionListener(e -> {
            PurchaseOrders purchaseOrders = accounting.getPurchaseOrders();
            order.setSupplier(contact);
            try {
                order.getBusinessObjects().forEach(orderItem -> {
                    int numberOfUnits = orderItem.getNumberOfUnits();
                    int numberOfItems = orderItem.getNumberOfItems();
                    if (numberOfUnits==0 && numberOfItems==0) {
                        order.removeBusinessObject(orderItem);
                    }
                });
                purchaseOrders.addBusinessObject(order);
                purchaseOrderCreateDataTableModel.setOrder(order);
                firePurchaseOrderAddedOrRemovedForAll();

                order = new PurchaseOrder(articles);
            } catch (EmptyNameException e1) {
                e1.printStackTrace();
            } catch (DuplicateNameException e1) {
                e1.printStackTrace();
            }
        });
        JPanel south = new JPanel(new BorderLayout());
        south.add(orderButton, BorderLayout.SOUTH);
        south.add(purchaseTotalsPanel, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(table);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(comboBox, BorderLayout.NORTH);
        add(south, BorderLayout.SOUTH);
    }

    public void fireSupplierAddedOrRemoved() {
        comboBox.removeAllItems();
        contacts.getBusinessObjects(filter).forEach(contact -> comboBox.addItem(contact));
//        purchaseOrderDataTableModel.fireTableDataChanged();
    }
}