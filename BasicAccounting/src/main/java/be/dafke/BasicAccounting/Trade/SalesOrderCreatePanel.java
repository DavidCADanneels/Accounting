package be.dafke.BasicAccounting.Trade;


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
    private Contact noInvoice;
    private SalesOrder salesOrder;
    private JCheckBox invoice;
    private JCheckBox creditNote;
    private JCheckBox promoOrder;
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
        salesOrder = new SalesOrder();
        salesOrder.setArticles(articles);

        TotalsPanel saleTotalsPanel = new TotalsPanel();
        salesOrderCreateDataTableModel = new SalesOrderCreateDataTableModel(articles, salesOrder, saleTotalsPanel);
        SelectableTable<OrderItem> table = new SelectableTable<>(salesOrderCreateDataTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(1000, 400));

        creditNote = new JCheckBox("CreditNote");
        creditNote.setSelected(false);
        creditNote.addActionListener(e -> {
            salesOrder.setCreditNote(creditNote.isSelected());
        });
        promoOrder = new JCheckBox("Promo Order");
        promoOrder.setSelected(false);
        promoOrder.addActionListener(e -> {
            salesOrder.setPromoOrder(promoOrder.isSelected());
        });
        invoice = new JCheckBox("Invoice");
        invoice.addActionListener(e -> {
            comboBox.setEnabled(invoice.isSelected());
            salesOrder.setInvoice(invoice.isSelected());
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
            if(invoice.isSelected()) {
                contact = (Contact) comboBox.getSelectedItem();
            }
        });
        JPanel north = new JPanel();
        north.add(invoice);
        north.add(comboBox);
        north.add(creditNote);
        north.add(promoOrder);

        filter = Contact::isCustomer;
        fireCustomerAddedOrRemoved();
        comboBox.setSelectedItem(noInvoice);
        comboBox.setEnabled(false);

        JButton orderButton = new JButton("Add Sales Order");
        orderButton.addActionListener(e -> {
            SalesOrders salesOrders = accounting.getSalesOrders();
            if(invoice.isSelected()) {
                salesOrder.setCustomer(contact);
            } else {
                salesOrder.setCustomer(noInvoice);
            }
            try {
                salesOrder.removeEmptyOrderItems();

                salesOrders.addBusinessObject(salesOrder);
                salesOrder = new SalesOrder();
                salesOrder.setArticles(articles);
                salesOrderCreateDataTableModel.setOrder(salesOrder);
                saleTotalsPanel.fireOrderContentChanged(salesOrder);
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