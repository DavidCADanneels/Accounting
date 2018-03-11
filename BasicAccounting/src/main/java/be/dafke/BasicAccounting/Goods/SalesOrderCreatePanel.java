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
public class SalesOrderCreatePanel extends JPanel {
    private final JButton orderButton;
    private final SelectableTable<OrderItem> table;
    private JComboBox<Contact> comboBox;
    private Contacts contacts;
    private Articles articles;
    private Contact contact;
    Predicate<Contact> filter;
    private final SalesOrderCreateDataTableModel salesOrderCreateDataTableModel;
    private JTextField textField1, textField2, textField3;

    public SalesOrderCreatePanel(Accounting accounting) {
        this.contacts = accounting.getContacts();
        this.articles = accounting.getArticles();
        salesOrderCreateDataTableModel = new SalesOrderCreateDataTableModel(articles, null);
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
            Order order = salesOrderCreateDataTableModel.getOrder();
            SalesOrders salesOrders = accounting.getSalesOrders();
            order.setCustomer(contact);
            try {
                salesOrders.addBusinessObject(order);
                salesOrderCreateDataTableModel.newOrder();
            } catch (EmptyNameException e1) {
                e1.printStackTrace();
            } catch (DuplicateNameException e1) {
                e1.printStackTrace();
            }
        });
        JPanel south = new JPanel(new BorderLayout());
        JPanel totals = createTotalPanel();
        south.add(orderButton, BorderLayout.SOUTH);
        south.add(totals, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(table);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(comboBox, BorderLayout.NORTH);
        add(south, BorderLayout.SOUTH);
    }

    private JPanel createTotalPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0,2));

        textField1 = new JTextField(10);
        textField2 = new JTextField(10);
        textField3 = new JTextField(10);
        textField1.setEditable(false);
        textField2.setEditable(false);
        textField3.setEditable(false);

        panel.add(new JLabel("Total (excl. VAT):"));
        panel.add(textField1);
        panel.add(new JLabel("Total VAT:"));
        panel.add(textField2);
        panel.add(new JLabel("Total (incl. VAT):"));
        panel.add(textField3);

        return panel;
    }

    public void fireCustomerAddedOrRemoved() {
        comboBox.removeAllItems();
        contacts.getBusinessObjects(filter).forEach(contact -> comboBox.addItem(contact));
//        salesOrderDataTableModel.fireTableDataChanged();
    }
}