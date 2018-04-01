package be.dafke.BasicAccounting.Goods;


import be.dafke.BasicAccounting.Accounts.AccountSelectorDialog;
import be.dafke.BasicAccounting.Journals.DateAndDescriptionDialog;
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
    private final JTextField customerName;
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
            createSalesTransaction();
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

        customerName = new JTextField(20);
        customerName.setEditable(false);

        comboBox = new JComboBox<>();
        comboBox.addActionListener(e -> {
            salesOrder = (SalesOrder) comboBox.getSelectedItem();
            updateButtonsAndCheckBoxes();
        });
        firePurchaseOrderAddedOrRemoved();

        JScrollPane scrollPane = new JScrollPane(table);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        JPanel north1 = new JPanel();
        north1.add(comboBox);
        north1.add(customerName);

        JPanel north2 = new JPanel();
        north2.add(placed);
        north2.add(delivered);
        north2.add(payed);

        JPanel north = new JPanel();
        north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));
        north.add(north1);
        north.add(north2);

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
        customerName.setText(salesOrder!=null&&salesOrder.getCustomer()!=null?salesOrder.getCustomer().getName():"");
    }

    public Journal setSalesJournal(){
        JournalSelectorDialog journalSelectorDialog = new JournalSelectorDialog(accounting.getJournals());
        journalSelectorDialog.setTitle("Select Sales Journal");
        journalSelectorDialog.setVisible(true);
        Journal journal = journalSelectorDialog.getSelection();
        salesOrders.setSalesJournal(journal);
        return journal;
    }

    public Journal setGainJournal(){
        JournalSelectorDialog journalSelectorDialog = new JournalSelectorDialog(accounting.getJournals());
        journalSelectorDialog.setTitle("Select Gain Journal");
        journalSelectorDialog.setVisible(true);
        Journal journal = journalSelectorDialog.getSelection();
        salesOrders.setGainJournal(journal);
        return journal;
    }

    private void createSalesTransaction(){
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

        Account salesAccount = salesOrders.getSalesAccount();
        if (salesAccount == null){
            AccountType accountType = accounting.getAccountTypes().getBusinessObject(AccountTypes.REVENUE);
            ArrayList<AccountType> list = new ArrayList<>();
            list.add(accountType);
            AccountSelectorDialog dialog = new AccountSelectorDialog(accounting.getAccounts(), list, "Select Sales Account");
            dialog.setVisible(true);
            salesAccount = dialog.getSelection();
            salesOrders.setSalesAccount(salesAccount);
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

        // For DIF
        Booking stockBooking = new Booking(stockAccount, stockAmount, false);
        Booking gainBooking = new Booking(gainAccount, gainAmount, false);
        Booking salesDivBooking = new Booking(salesAccount, totalSalesPriceExclVat, true);

        Booking salesBooking = new Booking(salesAccount, totalSalesPriceExclVat, false);
        Booking customerBooking = new Booking(customerAccount, customerAmount, true);


        DateAndDescriptionDialog dateAndDescriptionDialog = DateAndDescriptionDialog.getDateAndDescriptionDialog();
        dateAndDescriptionDialog.setVisible(true);

        Calendar date = dateAndDescriptionDialog.getDate();
        String description = dateAndDescriptionDialog.getDescription();
        Transaction salesTransaction = new Transaction(date, description);
        Transaction gainTransaction = new Transaction(date, description);


        gainTransaction.addBusinessObject(gainBooking);
        gainTransaction.addBusinessObject(stockBooking);
        gainTransaction.addBusinessObject(salesDivBooking);

        salesTransaction.addBusinessObject(salesBooking);
        salesTransaction.addBusinessObject(customerBooking);

        if(vatAmount.compareTo(BigDecimal.ZERO) != 0) {
            Booking vatBooking = new Booking(vatAccount, vatAmount, false);
            salesTransaction.addBusinessObject(vatBooking);
            VATTransactions vatTransactions = accounting.getVatTransactions();
            // TODO: now hardcoded 6%, could be 21% as well, or different per article
            VATTransaction vatTransaction = vatTransactions.sale(salesBooking, vatBooking, 6);
            salesTransaction.addVatTransaction(vatTransaction);
            vatTransaction.setTransaction(salesTransaction);
        }

        Journal gainJournal = salesOrders.getGainJournal();
        if (gainJournal==null){
            gainJournal = setGainJournal();
        }
        gainTransaction.setJournal(gainJournal);
        // TODO: ask for Date and Description

        Transactions transactions = accounting.getTransactions();
        transactions.setId(gainTransaction);
        transactions.addBusinessObject(gainTransaction);
        gainJournal.addBusinessObject(gainTransaction);
        Main.fireJournalDataChanged(gainJournal);

        Journal salesJournal = salesOrders.getSalesJournal();
        if (salesJournal==null){
            salesJournal = setSalesJournal();
        }
        salesTransaction.setJournal(salesJournal);
        // TODO: ask for Date and Description

        transactions.setId(salesTransaction);
        transactions.addBusinessObject(salesTransaction);
        salesJournal.addBusinessObject(salesTransaction);

        Main.setJournal(salesJournal);
        Main.selectTransaction(salesTransaction);
        Main.fireJournalDataChanged(salesJournal);
    }

    public void firePurchaseOrderAddedOrRemoved() {
        comboBox.removeAllItems();
        salesOrders.getBusinessObjects().forEach(order -> comboBox.addItem(order));
//        salesOrdersViewDataTableModel.fireTableDataChanged();
    }
}