package be.dafke.BasicAccounting.Goods;


import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.function.Predicate;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
public class PurchaseOrderCreatePanel extends JPanel {
    private final JButton orderButton;
    private final SelectableTable<StockItem> table;
    private Order order;
    private JComboBox<Contact> comboBox;
    private Contacts contacts;
    private Articles articles;
    private Contact contact;
    Predicate<Contact> filter;
    private final PurchaseOrderCreateDataTableModel purchaseOrderCreateDataTableModel;
    private JTextField textField1, textField2, textField3;

    public PurchaseOrderCreatePanel(Accounting accounting) {
        this.contacts = accounting.getContacts();
        this.articles = accounting.getArticles();
        order = new Order(articles);
        purchaseOrderCreateDataTableModel = new PurchaseOrderCreateDataTableModel(articles, null, order, this);
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
                purchaseOrders.addBusinessObject(order);
                order = new Order(articles);
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

        textField1 = new JTextField("0.00",10);
        textField2 = new JTextField("0.00",10);
        textField3 = new JTextField("0.00",10);
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

    public void fireOrderContentChanged(){
        BigDecimal totalPurchaseExcl = BigDecimal.ZERO.setScale(2);
        BigDecimal totalVat = BigDecimal.ZERO.setScale(2);
        BigDecimal totalPurchaseIncl = BigDecimal.ZERO.setScale(2);
        for (StockItem stockItem : order.getBusinessObjects()){
            Article article = stockItem.getArticle();
            int number = stockItem.getNumber();
            totalPurchaseExcl = totalPurchaseExcl.add(article.getPurchasePrice(number)).setScale(2);
//            totalVat = totalVat.add(article.getPurchaseVat(number)).setScale(2);
            totalPurchaseIncl = totalPurchaseIncl.add(article.getPurchasePriceWithVat(number)).setScale(2);
        }
        textField1.setText(totalPurchaseExcl.toString());
        textField2.setText(totalVat.toString());
        textField3.setText(totalPurchaseIncl.toString());
    }

    public void fireSupplierAddedOrRemoved() {
        comboBox.removeAllItems();
        contacts.getBusinessObjects(filter).forEach(contact -> comboBox.addItem(contact));
//        purchaseOrderDataTableModel.fireTableDataChanged();
    }
}