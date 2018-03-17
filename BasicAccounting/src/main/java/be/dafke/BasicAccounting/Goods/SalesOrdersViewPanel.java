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
public class SalesOrdersViewPanel extends JPanel {
    private final JButton placeOrderButton, deliveredButton, payedButton;
    private final SelectableTable<OrderItem> table;
    private final SalesOrders salesOrders;
    private JComboBox<Order> comboBox;
    private JCheckBox payed, delivered, placed;
    private SalesOrder salesOrder;
    private Accounting accounting;
    private final SalesOrdersViewDataTableModel salesOrdersViewDataTableModel;
    private SaleTotalsPanel salesTotalsPanel;

    public SalesOrdersViewPanel(Accounting accounting) {
        this.accounting = accounting;
        this.salesOrders = accounting.getSalesOrders();
        salesOrdersViewDataTableModel = new SalesOrdersViewDataTableModel();
        table = new SelectableTable<>(salesOrdersViewDataTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));
        table.setAutoCreateRowSorter(true);
//        table.setRowSorter(null);

        placeOrderButton = new JButton("Place Order");
        placeOrderButton.addActionListener(e -> {
            salesOrder = salesOrdersViewDataTableModel.getOrder();
            Transaction transaction = createSalesTransaction(salesOrder);
            Journal journal = salesOrders.getJournal();
            if (journal==null){
                journal = setSalesJournal();
            }
            transaction.setJournal(journal);
            // TODO: ask for Date and Description

            journal.addBusinessObject(transaction);
            Main.setJournal(journal);
            Main.selectTransaction(transaction);

            salesOrder.setPlaced(true);
            updateButtonsAndCheckBoxes();
        });

        deliveredButton = new JButton("Order Delivered");
        deliveredButton.addActionListener(e -> {
            Stock stock = accounting.getStock();
            salesOrder = salesOrdersViewDataTableModel.getOrder();
            stock.sellItems(salesOrder);
            StockGUI.fireStockContentChanged(accounting);
            salesOrder.setDelivered(true);
            updateButtonsAndCheckBoxes();
        });

        payedButton = new JButton("Order Payed");
        payedButton.addActionListener(e -> {
            salesOrder = salesOrdersViewDataTableModel.getOrder();
            salesOrder.setPayed(true);
            updateButtonsAndCheckBoxes();
        });

        placed = new JCheckBox("Ordered");
        payed = new JCheckBox("Payed");
        delivered = new JCheckBox("Delived");
        placed.setEnabled(false);
        payed.setEnabled(false);
        delivered.setEnabled(false);

        salesTotalsPanel = new SaleTotalsPanel();

        comboBox = new JComboBox<>();
        comboBox.addActionListener(e -> {
            salesOrder = (SalesOrder) comboBox.getSelectedItem();
            updateButtonsAndCheckBoxes();
        });
        firePurchaseOrderAddedOrRemoved();

        JScrollPane scrollPane = new JScrollPane(table);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        JPanel north = new JPanel();
        north.add(comboBox);

        north.add(placed);
        north.add(delivered);
        north.add(payed);
        add(north, BorderLayout.NORTH);
        JPanel south = new JPanel(new BorderLayout());
        south.add(salesTotalsPanel);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(placeOrderButton);
        buttonPanel.add(deliveredButton);
        buttonPanel.add(payedButton);

        south.add(buttonPanel, BorderLayout.SOUTH);

        add(south, BorderLayout.SOUTH);
    }

    private void updateButtonsAndCheckBoxes() {
        payed.setSelected(salesOrder !=null&& salesOrder.isPayed());
        placed.setSelected(salesOrder !=null&& salesOrder.isPlaced());
        delivered.setSelected(salesOrder !=null&& salesOrder.isDelivered());
        deliveredButton.setEnabled(salesOrder !=null&&!salesOrder.isDelivered());
        placeOrderButton.setEnabled(salesOrder !=null&&!salesOrder.isPlaced());
        payedButton.setEnabled(salesOrder !=null&&!salesOrder.isPayed());
        salesOrdersViewDataTableModel.setOrder(salesOrder);
        salesTotalsPanel.fireOrderContentChanged(salesOrder);
    }

    public Journal setSalesJournal(){
        JournalSelectorDialog journalSelectorDialog = new JournalSelectorDialog(accounting.getJournals());
        journalSelectorDialog.setVisible(true);
        Journal journal = journalSelectorDialog.getSelection();
        salesOrders.setJournal(journal);
        return journal;
    }

    private Transaction createSalesTransaction(SalesOrder salesOrder){
        Transaction transaction;
        // TODO: create transaction
        Calendar date = Calendar.getInstance();
        String description = "";
        transaction = new Transaction(date, description);

        Account vatAccount = salesOrders.getVATAccount();
        if (vatAccount == null){
            AccountType accountType = accounting.getAccountTypes().getBusinessObject(AccountTypes.TAXDEBIT);
            ArrayList<AccountType> list = new ArrayList<>();
            list.add(accountType);
            AccountSelectorDialog dialog = new AccountSelectorDialog(accounting.getAccounts(), list, "Select VAT Account for Sales");
            dialog.setVisible(true);
            vatAccount = dialog.getSelection();
            salesOrders.setVATAccount(vatAccount);
        }
        Account stockAccount = salesOrders.getStockAccount();
        if (stockAccount == null){
            AccountType accountType = accounting.getAccountTypes().getBusinessObject(AccountTypes.ASSET);
            ArrayList<AccountType> list = new ArrayList<>();
            list.add(accountType);
            AccountSelectorDialog dialog = new AccountSelectorDialog(accounting.getAccounts(), list, "Select Stock Account");
            dialog.setVisible(true);
            stockAccount = dialog.getSelection();
            salesOrders.setStockAccount(stockAccount);
        }

        Account gainAccount = salesOrders.getGainAccount();
        if (gainAccount == null){
            AccountType accountType = accounting.getAccountTypes().getBusinessObject(AccountTypes.REVENUE);
            ArrayList<AccountType> list = new ArrayList<>();
            list.add(accountType);
            AccountSelectorDialog dialog = new AccountSelectorDialog(accounting.getAccounts(), list, "Select Gain Account");
            dialog.setVisible(true);
            gainAccount = dialog.getSelection();
            salesOrders.setGainAccount(gainAccount);
        }

        Contact customer = this.salesOrder.getCustomer();
        if(customer == null){
            // TODO
        }
        Account customerAccount = customer.getAccount();
        if (customerAccount == null){
            AccountType accountType = accounting.getAccountTypes().getBusinessObject(AccountTypes.CREDIT);
            ArrayList<AccountType> list = new ArrayList<>();
            list.add(accountType);
            AccountSelectorDialog dialog = new AccountSelectorDialog(accounting.getAccounts(), list, "Select Customer Account");
            dialog.setVisible(true);
            customerAccount = dialog.getSelection();
            customer.setAccount(customerAccount);
        }

        BigDecimal stockAmount = this.salesOrder.getTotalPurchasePriceExclVat();
        BigDecimal totalSalesPriceExclVat = this.salesOrder.getTotalSalesPriceExclVat();
        BigDecimal gainAmount = totalSalesPriceExclVat.subtract(stockAmount);
        BigDecimal customerAmount = this.salesOrder.getTotalSalesPriceInclVat();
        BigDecimal vatAmount = this.salesOrder.getTotalSalesVat();

        Booking stockBooking = new Booking(stockAccount, stockAmount, true);
        Booking gainBooking = new Booking(gainAccount, gainAmount, true);
        Booking customerBooking = new Booking(customerAccount, customerAmount, false);

        transaction.addBusinessObject(stockBooking);
        transaction.addBusinessObject(gainBooking);
        transaction.addBusinessObject(customerBooking);

        if(vatAmount.compareTo(BigDecimal.ZERO) != 0) {
            Booking vatBooking = new Booking(vatAccount, vatAmount, true);
            transaction.addBusinessObject(vatBooking);
            VATTransactions vatTransactions = accounting.getVatTransactions();
            VATTransaction vatTransaction = vatTransactions.purchase(stockBooking, vatBooking, VATTransaction.PurchaseType.GOODS);
            transaction.addVatTransaction(vatTransaction);
            vatTransaction.setTransaction(transaction);
        }

        return transaction;
    }

    public void firePurchaseOrderAddedOrRemoved() {
        comboBox.removeAllItems();
        salesOrders.getBusinessObjects().forEach(order -> comboBox.addItem(order));
//        salesOrdersViewDataTableModel.fireTableDataChanged();
    }
}