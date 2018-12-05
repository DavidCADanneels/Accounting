package be.dafke.BasicAccounting.Goods;


import be.dafke.BasicAccounting.Contacts.ContactSelectorDialog;
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
class SalesOrderCreatePanel extends JPanel {
    private Contact noInvoice = null;
    private SalesOrder order;
    private JCheckBox invoice;
    private JComboBox<Contact> comboBox;
    private Contacts contacts;
    private Articles articles;
    private Contact contact;
    private Predicate<Contact> filter;
    private final SalesOrderCreateDataTableModel salesOrderCreateDataTableModel;

    SalesOrderCreatePanel(Accounting accounting) {
        this.contacts = accounting.getContacts();
        this.articles = accounting.getArticles();
        noInvoice=accounting.getContactNoInvoice();
        order = new SalesOrder();
        order.setArticles(articles);

        SaleTotalsPanel saleTotalsPanel = new SaleTotalsPanel();
        salesOrderCreateDataTableModel = new SalesOrderCreateDataTableModel(articles, null, order, saleTotalsPanel);
        SelectableTable<OrderItem> table = new SelectableTable<>(salesOrderCreateDataTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(1000, 400));

        invoice = new JCheckBox("Invoice");
        invoice.addActionListener(e -> {
            comboBox.setEnabled(invoice.isSelected());
            order.setInvoice(invoice.isSelected());
            ComboBoxModel<Contact> model = comboBox.getModel();
            if(invoice.isSelected()) {
                model.setSelectedItem(contact);
            } else {
                if(noInvoice == null){
                    ContactSelectorDialog contactSelectorDialog = ContactSelectorDialog.getContactSelector(contacts, Contact.ContactType.CUSTOMERS);
                    contactSelectorDialog.setLocation(getLocationOnScreen());
                    contactSelectorDialog.setVisible(true);
                    noInvoice = contactSelectorDialog.getSelection();
                    accounting.setContactNoInvoice(noInvoice);
                }
                model.setSelectedItem(noInvoice);
            }
        });
        //
        comboBox = new JComboBox<>();
        comboBox.addActionListener(e -> {
            contact = (Contact) comboBox.getSelectedItem();
            salesOrderCreateDataTableModel.setContact(contact);
        });
        comboBox.setEnabled(false);
        JPanel north = new JPanel();
        north.add(invoice);
        north.add(comboBox);

        filter = Contact::isCustomer;
        fireCustomerAddedOrRemoved();

        JButton orderButton = new JButton("Book Order");
        orderButton.addActionListener(e -> {
            SalesOrders salesOrders = accounting.getSalesOrders();
            order.setCustomer(contact);
            try {
                order.removeEmptyOrderItems();
                salesOrders.addBusinessObject(order);
                order = new SalesOrder();
                order.setArticles(articles);
                salesOrderCreateDataTableModel.setOrder(order);
                saleTotalsPanel.fireOrderContentChanged(order);
                SalesOrdersOverviewGUI.fireSalesOrderAddedOrRemovedForAll();
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
        add(north, BorderLayout.NORTH);
        add(south, BorderLayout.SOUTH);
    }

    void fireCustomerAddedOrRemoved() {
        comboBox.removeAllItems();
        contacts.getBusinessObjects(filter).forEach(contact -> comboBox.addItem(contact));
//        salesOrderDataTableModel.fireTableDataChanged();
    }
}