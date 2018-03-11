package be.dafke.BasicAccounting.Goods;


import be.dafke.BasicAccounting.Accounts.AccountSelectorDialog;
import be.dafke.BasicAccounting.Journals.JournalSelectorDialog;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
public class PurchaseOrdersViewPanel extends JPanel {
    private final JButton placeOrderButton, deliveredButton, payedButton;
    private final SelectableTable<StockItem> table;
    private final PurchaseOrders purchaseOrders;
    private JComboBox<Order> comboBox;
    private JCheckBox payed, delivered, placed;
    private Order order;
    private Accounting accounting;
    private final PurchaseOrdersViewDataTableModel purchaseOrdersViewDataTableModel;

    public PurchaseOrdersViewPanel(Accounting accounting) {
        this.accounting = accounting;
        this.purchaseOrders = accounting.getPurchaseOrders();
        purchaseOrdersViewDataTableModel = new PurchaseOrdersViewDataTableModel();
        table = new SelectableTable<>(purchaseOrdersViewDataTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));
        table.setAutoCreateRowSorter(true);
//        table.setRowSorter(null);

        placeOrderButton = new JButton("Place Order");
        placeOrderButton.addActionListener(e -> {
            Order order = purchaseOrdersViewDataTableModel.getOrder();
            Transaction transaction = createPurchaseTransaction(order);
            Journal journal = purchaseOrders.getJournal();
            if (journal==null){
                journal = setPurchaseJournal();
            }
            journal.setCurrentTransaction(transaction);
            Main.setJournal(journal);
//            Main.fireTransactionInputDataChanged();
            Main.editTransaction(transaction);
//            Main.selectTransaction(transaction);

            // TODO: setPlaced when booked
//            journal.addBusinessObject(transaction);
            order.setPlaced(true);
        });

        deliveredButton = new JButton("Order Delivered");
        deliveredButton.addActionListener(e -> {
            Stock stock = accounting.getStock();
            Order order = purchaseOrdersViewDataTableModel.getOrder();
            stock.addLoad(order);
            order.setDelivered(true);
        });

        payedButton = new JButton("Order Payed");
        payedButton.addActionListener(e -> {
            Order order = purchaseOrdersViewDataTableModel.getOrder();
            order.setPayed(true);
        });

        placed = new JCheckBox("Delived");
        payed = new JCheckBox("Payed");
        delivered = new JCheckBox("Delived");
        placed.setEnabled(false);
        payed.setEnabled(false);
        delivered.setEnabled(false);

        comboBox = new JComboBox<>();
        comboBox.addActionListener(e -> {
            order = (Order) comboBox.getSelectedItem();
            payed.setSelected(order!=null&&order.isPayed());
            placed.setSelected(order!=null&&order.isPayed());
            delivered.setSelected(order!=null&&order.isDelivered());
            deliveredButton.setEnabled(order!=null&&!order.isDelivered());
            placeOrderButton.setEnabled(order!=null&&!order.isPlaced());
            payedButton.setEnabled(order!=null&&!order.isPayed());
            purchaseOrdersViewDataTableModel.setOrder(order);
        });
        firePurchaseOrderAddedOrRemoved();

        JScrollPane scrollPane = new JScrollPane(table);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        JPanel north = new JPanel();
        north.add(comboBox);

        north.add(placed);
        north.add(payed);
        north.add(delivered);
        add(north, BorderLayout.NORTH);
        JPanel south = new JPanel();
        south.add(placeOrderButton);
        south.add(deliveredButton);
        south.add(payedButton);
        add(south, BorderLayout.SOUTH);
    }

    public Journal setPurchaseJournal(){
        JournalSelectorDialog journalSelectorDialog = new JournalSelectorDialog(accounting.getJournals());
        journalSelectorDialog.setVisible(true);
        Journal journal = journalSelectorDialog.getSelection();
        purchaseOrders.setJournal(journal);
        return journal;
    }

    private Transaction createPurchaseTransaction(Order order) {
        Transaction transaction;
        // TODO: create transaction
        Calendar date = Calendar.getInstance();
        String description = "";
        transaction = new Transaction(date, description);

        Account vatAccount = purchaseOrders.getVATAccount();
        if (vatAccount == null){
            AccountType accountType = accounting.getAccountTypes().getBusinessObject(AccountTypes.TAXCREDIT);
            ArrayList<AccountType> list = new ArrayList<>();
            list.add(accountType);
            AccountSelectorDialog dialog = new AccountSelectorDialog(accounting.getAccounts(), list, "Select VAT Account for Purchases");
            dialog.setVisible(true);
            vatAccount = dialog.getSelection();
            purchaseOrders.setVATAccount(vatAccount);
        }
        Account stockAccount = purchaseOrders.getStockAccount();
        if (stockAccount == null){
            AccountType accountType = accounting.getAccountTypes().getBusinessObject(AccountTypes.ASSET);
            ArrayList<AccountType> list = new ArrayList<>();
            list.add(accountType);
            AccountSelectorDialog dialog = new AccountSelectorDialog(accounting.getAccounts(), list, "Select Stock Account");
            dialog.setVisible(true);
            stockAccount = dialog.getSelection();
            purchaseOrders.setStockAccount(stockAccount);
        }

        Contact supplier = order.getSupplier();
        if(supplier == null){
            // TODO
        }
        Account supplierAccount = supplier.getAccount();
        if (supplierAccount == null){
            AccountType accountType = accounting.getAccountTypes().getBusinessObject(AccountTypes.DEBIT);
            ArrayList<AccountType> list = new ArrayList<>();
            list.add(accountType);
            AccountSelectorDialog dialog = new AccountSelectorDialog(accounting.getAccounts(), list, "Select Supplier Account");
            dialog.setVisible(true);
            supplierAccount = dialog.getSelection();
            supplier.setAccount(supplierAccount);
        }

        BigDecimal stockAmount = order.getTotalPurchasePriceExclVat();
        BigDecimal vatAmount = order.getTotalPurchaseVat();
        BigDecimal supplierAmount = order.getTotalPurchasePriceInclVat();

        Booking stockBooking = new Booking(stockAccount, stockAmount, true);
        Booking vatBooking = new Booking(vatAccount, vatAmount, true);
        Booking supplierBooking = new Booking(supplierAccount, supplierAmount, false);

        transaction.addBusinessObject(stockBooking);
        transaction.addBusinessObject(vatBooking);
        transaction.addBusinessObject(supplierBooking);

        return transaction;
    }

    public void firePurchaseOrderAddedOrRemoved() {
        comboBox.removeAllItems();
        purchaseOrders.getBusinessObjects().forEach(order -> comboBox.addItem(order));
//        purchaseOrderDataTableModel.fireTableDataChanged();
    }
}