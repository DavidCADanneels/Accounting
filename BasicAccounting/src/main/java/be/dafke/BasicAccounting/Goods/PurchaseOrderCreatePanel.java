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
public class PurchaseOrderCreatePanel extends JPanel {
    private final JButton orderButton;
    private final SelectableTable<OrderItem> table;
    private PurchaseOrder purchaseOrder;
    private JComboBox<Contact> comboBox;
    private Contacts contacts;
    private Articles articles;
    private Contact contact;
    Predicate<Contact> filter;
    private final PurchaseOrderCreateDataTableModel purchaseOrderCreateDataTableModel;

    public PurchaseOrderCreatePanel(Accounting accounting) {
        this.contacts = accounting.getContacts();
        this.articles = accounting.getArticles();
        purchaseOrder = new PurchaseOrder();
        purchaseOrder.setArticles(articles);

        PurchaseTotalsPanel purchaseTotalsPanel = new PurchaseTotalsPanel();
        purchaseOrderCreateDataTableModel = new PurchaseOrderCreateDataTableModel(articles, null, purchaseOrder, purchaseTotalsPanel);
        table = new SelectableTable<>(purchaseOrderCreateDataTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(1000, 400));

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
            purchaseOrder.setSupplier(contact);
            try {
                purchaseOrder.removeEmptyOrderItems();
                purchaseOrder.addPurchaseOrderToArticles();
                purchaseOrders.addBusinessObject(purchaseOrder);
                purchaseOrder = new PurchaseOrder();
                purchaseOrder.setArticles(articles);
                purchaseOrderCreateDataTableModel.setOrder(purchaseOrder);
                // TODO: pass view panel and call directly
                PurchaseOrdersOverviewGUI.firePurchaseOrderAddedOrRemovedForAll();
                purchaseTotalsPanel.fireOrderContentChanged(purchaseOrder);
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