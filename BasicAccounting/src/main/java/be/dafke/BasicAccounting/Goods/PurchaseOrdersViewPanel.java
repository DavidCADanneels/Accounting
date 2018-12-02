package be.dafke.BasicAccounting.Goods;


import be.dafke.BasicAccounting.Accounts.AccountSelectorDialog;
import be.dafke.BasicAccounting.Journals.DateAndDescriptionDialog;
import be.dafke.BasicAccounting.Journals.JournalSelectorDialog;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;
import be.dafke.Utils.Utils;

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
    private final SelectableTable<OrderItem> table;
    private final PurchaseOrders purchaseOrders;
    private final PurchaseTotalsPanel purchaseTotalsPanel;
    private JComboBox<PurchaseOrder> comboBox;
    private JCheckBox payed, delivered, placed;
    private PurchaseOrder purchaseOrder;
    private Accounting accounting;
    private final PurchaseOrdersViewDataTableModel purchaseOrdersViewDataTableModel;

    public PurchaseOrdersViewPanel(Accounting accounting) {
        this.accounting = accounting;
        this.purchaseOrders = accounting.getPurchaseOrders();
        purchaseOrdersViewDataTableModel = new PurchaseOrdersViewDataTableModel();
        table = new SelectableTable<>(purchaseOrdersViewDataTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(1000, 400));
        table.setAutoCreateRowSorter(true);
//        table.setRowSorter(null);

        placeOrderButton = new JButton("Place Order");
        placeOrderButton.addActionListener(e -> {
            purchaseOrder = purchaseOrdersViewDataTableModel.getOrder();
            Transaction transaction = createPurchaseTransaction();
            Journal journal = purchaseOrders.getJournal();
            if (journal==null){
                journal = setPurchaseJournal();
            }
            transaction.setJournal(journal);
            // TODO: ask for Date and Description

            purchaseOrder.setPurchaseTransaction(transaction);

            Transactions transactions = accounting.getTransactions();
            transactions.setId(transaction);
            transactions.addBusinessObject(transaction);
            journal.addBusinessObject(transaction);
            Main.setJournal(journal);
            Main.selectTransaction(transaction);

            purchaseOrder.setPlaced(true);
            updateButtonsAndCheckBoxes();
        });

        deliveredButton = new JButton("Order Delivered");
        deliveredButton.addActionListener(e -> {
            Stock stock = accounting.getStock();
            purchaseOrder = purchaseOrdersViewDataTableModel.getOrder();
            DateAndDescriptionDialog dateAndDescriptionDialog = DateAndDescriptionDialog.getDateAndDescriptionDialog();
            Contact supplier = purchaseOrder.getSupplier();
            dateAndDescriptionDialog.setDescription(supplier.getName());
            dateAndDescriptionDialog.enableDescription(false);
            dateAndDescriptionDialog.setVisible(true);

            Calendar date = dateAndDescriptionDialog.getDate();
            String description = dateAndDescriptionDialog.getDescription();

            purchaseOrder.setDate(Utils.toString(date));
            purchaseOrder.setDescription(description);

            stock.buyOrder(purchaseOrder);
            StockGUI.fireStockContentChanged(accounting);
            purchaseOrder.setDelivered(true);
            updateButtonsAndCheckBoxes();
        });

        payedButton = new JButton("Pay Order");
        payedButton.addActionListener(e -> {
            purchaseOrder = purchaseOrdersViewDataTableModel.getOrder();
            purchaseOrder.setPayed(true);
            updateButtonsAndCheckBoxes();
        });

        placed = new JCheckBox("Ordered");
        payed = new JCheckBox("Payed");
        delivered = new JCheckBox("Delived");
        placed.setEnabled(false);
        payed.setEnabled(false);
        delivered.setEnabled(false);

        purchaseTotalsPanel = new PurchaseTotalsPanel();

        comboBox = new JComboBox<>();
        comboBox.addActionListener(e -> {
            purchaseOrder = (PurchaseOrder) comboBox.getSelectedItem();
            updateButtonsAndCheckBoxes();
        });
        firePurchaseOrderAddedOrRemoved();

        setLayout(new BorderLayout());

        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);

        JPanel statusPanel = new JPanel();
        statusPanel.add(placed);
        statusPanel.add(delivered);
        statusPanel.add(payed);
        add(statusPanel, BorderLayout.NORTH);

        JPanel south = new JPanel(new BorderLayout());
        south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(placeOrderButton);
        buttonPanel.add(deliveredButton);
        buttonPanel.add(payedButton);

        south.add(buttonPanel);
        south.add(comboBox);

        add(south, BorderLayout.SOUTH);
    }

    private JPanel createTablePanel(){
        JPanel panel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane,BorderLayout.CENTER);
        panel.add(purchaseTotalsPanel,BorderLayout.SOUTH);
        return panel;
    }

    private void updateButtonsAndCheckBoxes() {
        payed.setSelected(purchaseOrder !=null&& purchaseOrder.isPayed());
        placed.setSelected(purchaseOrder !=null&& purchaseOrder.isPlaced());
        delivered.setSelected(purchaseOrder !=null&& purchaseOrder.isDelivered());
        deliveredButton.setEnabled(purchaseOrder !=null&&!purchaseOrder.isDelivered());
        placeOrderButton.setEnabled(purchaseOrder !=null&&!purchaseOrder.isPlaced());
        payedButton.setEnabled(purchaseOrder !=null&&!purchaseOrder.isPayed());
        purchaseOrdersViewDataTableModel.setOrder(purchaseOrder);
        purchaseTotalsPanel.fireOrderContentChanged(purchaseOrder);
    }

    public Journal setPurchaseJournal(){
        JournalSelectorDialog journalSelectorDialog = new JournalSelectorDialog(accounting.getJournals());
        journalSelectorDialog.setVisible(true);
        Journal journal = journalSelectorDialog.getSelection();
        purchaseOrders.setJournal(journal);
        return journal;
    }

    private Transaction createPurchaseTransaction() {
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

        purchaseOrder = purchaseOrdersViewDataTableModel.getOrder();
        Contact supplier = purchaseOrder.getSupplier();
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

        BigDecimal stockAmount = purchaseOrder.getTotalPurchasePriceExclVat();
        BigDecimal vatAmount = purchaseOrder.getTotalPurchaseVat();
        BigDecimal supplierAmount = purchaseOrder.getTotalPurchasePriceInclVat();

        Booking stockBooking = new Booking(stockAccount, stockAmount, true);
        Booking supplierBooking = new Booking(supplierAccount, supplierAmount, false);

        transaction.addBusinessObject(stockBooking);
        transaction.addBusinessObject(supplierBooking);

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
        purchaseOrders.getBusinessObjects().forEach(order -> comboBox.insertItemAt(order,0));
        comboBox.setSelectedIndex(0);
//        purchaseOrderDataTableModel.fireTableDataChanged();
    }
}