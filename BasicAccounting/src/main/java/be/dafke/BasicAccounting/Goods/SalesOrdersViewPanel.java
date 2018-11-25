package be.dafke.BasicAccounting.Goods;


import be.dafke.BasicAccounting.Accounts.AccountSelectorDialog;
import be.dafke.BasicAccounting.Contacts.ContactSelectorDialog;
import be.dafke.BasicAccounting.Contacts.ContactsPanel;
import be.dafke.BasicAccounting.Journals.DateAndDescriptionDialog;
import be.dafke.BasicAccounting.Journals.JournalSelectorDialog;
import be.dafke.BasicAccounting.MainApplication.InvoicePDF;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;
import be.dafke.BusinessModelDao.SalesOrderIO;
import be.dafke.ComponentModel.SelectableTable;
import be.dafke.Utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
class SalesOrdersViewPanel extends JPanel {
    private final JButton placeOrderButton, deliveredButton, payedButton, createInvoiceButton;
    private final SelectableTable<OrderItem> table;
    private final SalesOrders salesOrders;
    private final JTextField customerName;
    private JComboBox<Order> comboBox;
    private JCheckBox payed, delivered, placed;
    private SalesOrder salesOrder;
    private Accounting accounting;
    private final SalesOrdersViewDataTableModel salesOrdersViewDataTableModel;
    private SaleTotalsPanel salesTotalsPanel;

    SalesOrdersViewPanel(Accounting accounting) {
        this.accounting = accounting;
        this.salesOrders = accounting.getSalesOrders();
        salesOrdersViewDataTableModel = new SalesOrdersViewDataTableModel();
        table = new SelectableTable<>(salesOrdersViewDataTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));
        table.setAutoCreateRowSorter(true);
//        table.setRowSorter(null);

        createInvoiceButton = new JButton("Create Invoice");
        createInvoiceButton.setVisible(false);
        createInvoiceButton.addActionListener(e -> {
            salesOrder = salesOrdersViewDataTableModel.getOrder();
            if (salesOrder.getCustomer() == null) {
                // should not occur
            }

            if (salesOrder.getSupplier() == null){
                Contact companyContact = accounting.getCompanyContact();
                if (companyContact == null) {
                    ContactsPanel.setCompanyContact(accounting);
                }
                salesOrder.setSupplier(companyContact);
            }
            String invoiceNumber = salesOrder.getInvoiceNumber();
            if (invoiceNumber==null || "".equals(invoiceNumber)){
                invoiceNumber = JOptionPane.showInputDialog(this, "Enter Invoice Number");
                salesOrder.setInvoiceNumber(invoiceNumber);
            }

            DateAndDescriptionDialog dateAndDescriptionDialog = DateAndDescriptionDialog.getDateAndDescriptionDialog();
            dateAndDescriptionDialog.setVisible(true);

            Calendar date = dateAndDescriptionDialog.getDate();
            String description = dateAndDescriptionDialog.getDescription();

            salesOrder.setDate(Utils.toString(date));
            salesOrder.setDescription(description);

            String xmlPath = SalesOrderIO.writeInvoiceXmlInputFile(accounting, salesOrder);
            String pdfPath = SalesOrderIO.calculatePdfPath(accounting, salesOrder);
            InvoicePDF.createInvoice(xmlPath, pdfPath);
        });

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

            DateAndDescriptionDialog dateAndDescriptionDialog = DateAndDescriptionDialog.getDateAndDescriptionDialog();
            Contact customer = salesOrder.getCustomer();
            dateAndDescriptionDialog.setDescription(customer.getName());
            dateAndDescriptionDialog.enableDescription(false);
            dateAndDescriptionDialog.setVisible(true);

            Calendar date = dateAndDescriptionDialog.getDate();
            String description = dateAndDescriptionDialog.getDescription();

            salesOrder.setDate(Utils.toString(date));
            salesOrder.setDescription(description);

            stock.sellOrder(salesOrder);
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
            createInvoiceButton.setVisible(salesOrder!=null&&salesOrder.isInvoice());
            updateButtonsAndCheckBoxes();
        });
        firePurchaseOrderAddedOrRemoved();

        setLayout(new BorderLayout());

        JPanel tablePanel = createTablePanel();

        JPanel orderPanel = new JPanel(new BorderLayout());

        JPanel customerPanel = new JPanel();
        customerPanel.setLayout(new BoxLayout(customerPanel, BoxLayout.Y_AXIS));
        //
        JPanel statusPanel = new JPanel();
        statusPanel.add(placed);
        statusPanel.add(delivered);
        statusPanel.add(payed);
        //
        customerPanel.add(customerName);
        customerPanel.add(statusPanel);

        orderPanel.add(customerPanel, BorderLayout.NORTH);
        orderPanel.add(tablePanel,BorderLayout.CENTER);

        JPanel south = new JPanel();
        south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createInvoiceButton);
        buttonPanel.add(placeOrderButton);
        buttonPanel.add(deliveredButton);
        buttonPanel.add(payedButton);

        south.add(buttonPanel);
        south.add(comboBox);

        add(orderPanel,BorderLayout.CENTER);

        add(south, BorderLayout.SOUTH);
    }

    private JPanel createTablePanel(){
        JPanel panel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane,BorderLayout.CENTER);
        panel.add(salesTotalsPanel,BorderLayout.SOUTH);
        return panel;
    }

    private void updateButtonsAndCheckBoxes() {
        payed.setSelected(salesOrder !=null&& salesOrder.isPayed());
        placed.setSelected(salesOrder !=null&& salesOrder.isPlaced());
        delivered.setSelected(salesOrder !=null&& salesOrder.isDelivered());
        deliveredButton.setEnabled(salesOrder !=null&&!salesOrder.isDelivered());
        placeOrderButton.setEnabled(salesOrder !=null&&!salesOrder.isPlaced());
        createInvoiceButton.setEnabled(salesOrder !=null);
        payedButton.setEnabled(salesOrder !=null&&!salesOrder.isPayed());
        salesOrdersViewDataTableModel.setOrder(salesOrder);
        salesTotalsPanel.fireOrderContentChanged(salesOrder);
        customerName.setText(salesOrder!=null&&salesOrder.getCustomer()!=null?salesOrder.getCustomer().getName():"");
    }

    private Journal setSalesJournal(){
        JournalSelectorDialog journalSelectorDialog = new JournalSelectorDialog(accounting.getJournals());
        journalSelectorDialog.setTitle("Select Sales Journal");
        journalSelectorDialog.setVisible(true);
        Journal journal = journalSelectorDialog.getSelection();
        salesOrders.setSalesJournal(journal);
        return journal;
    }

    private Journal setGainJournal(){
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

        Contact customer = salesOrder.getCustomer();
        if(customer == null){
            Contacts contacts = accounting.getContacts();
            ContactSelectorDialog contactSelectorDialog = ContactSelectorDialog.getContactSelector(contacts, Contact.ContactType.CUSTOMERS);
            contactSelectorDialog.setVisible(true);
            customer = contactSelectorDialog.getSelection();
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
        VATTransactions vatTransactions = accounting.getVatTransactions();

        DateAndDescriptionDialog dateAndDescriptionDialog = DateAndDescriptionDialog.getDateAndDescriptionDialog();
        dateAndDescriptionDialog.setVisible(true);

        Calendar date = dateAndDescriptionDialog.getDate();
        String description = dateAndDescriptionDialog.getDescription();

        // For Sales
        Transaction salesTransaction = new Transaction(date, description);
        salesTransaction.setContact(customer);

        BigDecimal customerAmount = salesOrder.getTotalSalesPriceInclVat();
        Booking customerBooking = new Booking(customerAccount, customerAmount, true);
        salesTransaction.addBusinessObject(customerBooking);

        // Calculate Net Amounts per VAT Rate -> Fields 0, 1, 2, 3
        List<Integer> vatRates = new ArrayList<>();
        vatRates.add(6);
        vatRates.add(21);

        VATTransaction vatTransaction = new VATTransaction();
        for(int i:vatRates){
            BigDecimal netAmount = salesOrder.getTotalSalesPriceExclVat(OrderItem.withSalesVatRate(i));
            if(netAmount.compareTo(BigDecimal.ZERO) != 0){
                Booking salesBooking = new Booking(salesAccount, netAmount, false);
                salesTransaction.addBusinessObject(salesBooking);

                salesTransaction.increaseTurnOverAmount(netAmount);

                VATBooking revenueBooking = vatTransactions.getRevenueBooking(salesBooking, i);
                vatTransaction.addBusinessObject(revenueBooking);

                salesBooking.addVatBooking(revenueBooking);
            }
        }
        // Calculate Total VAT Amount -> Field 59
        BigDecimal vatAmount = salesOrder.calculateTotalSalesVat(); // ensure no cent different

        Booking vatBooking = new Booking(vatAccount, vatAmount, false);
        salesTransaction.addBusinessObject(vatBooking);

        salesTransaction.setVATAmount(vatAmount);

        VATBooking vatSalesBooking = vatTransactions.getVatSalesBooking(vatBooking);
        vatTransaction.addBusinessObject(vatSalesBooking);

        vatBooking.addVatBooking(vatSalesBooking);

        // ---

        salesTransaction.addVatTransaction(vatTransaction);
        vatTransaction.setTransaction(salesTransaction);

        Transactions transactions = accounting.getTransactions();
/*
        // For Div
        BigDecimal stockAmount = salesOrder.getTotalPurchasePriceExclVat();
        BigDecimal totalSalesPriceExclVat = salesOrder.getTotalSalesPriceExclVat();
        BigDecimal gainAmount = totalSalesPriceExclVat.subtract(stockAmount);

        Booking stockBooking = new Booking(stockAccount, stockAmount, false);
        Booking gainBooking = new Booking(gainAccount, gainAmount, false);
        Booking salesDivBooking = new Booking(salesAccount, totalSalesPriceExclVat, true);

        Transaction gainTransaction = new Transaction(date, description);

        gainTransaction.addBusinessObject(gainBooking);
        gainTransaction.addBusinessObject(stockBooking);
        gainTransaction.addBusinessObject(salesDivBooking);

        // ---

        Journal gainJournal = salesOrders.getGainJournal();
        if (gainJournal==null){
            gainJournal = setGainJournal();
        }
        gainTransaction.setJournal(gainJournal);
        // TODO: ask for Date and Description

        transactions.setId(gainTransaction);
        transactions.addBusinessObject(gainTransaction);
        gainJournal.addBusinessObject(gainTransaction);
        Main.fireJournalDataChanged(gainJournal);
*/
        Journal salesJournal;
        if(salesOrder.isInvoice()) {
            salesJournal = salesOrders.getSalesJournal();
            if (salesJournal == null) {
                salesJournal = setSalesJournal();
            }
        } else {
            salesJournal = salesOrders.getSalesNoInvoiceJournal();
            if (salesJournal == null) {
                salesJournal = setSalesNoInvoiceJournal();
            }
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

    private Journal setSalesNoInvoiceJournal() {
        JournalSelectorDialog journalSelectorDialog = new JournalSelectorDialog(accounting.getJournals());
        journalSelectorDialog.setTitle("Select Sales (No Invoice) Journal");
        journalSelectorDialog.setVisible(true);
        Journal journal = journalSelectorDialog.getSelection();
        salesOrders.setSalesNoInvoiceJournal(journal);
        return journal;
    }

    void firePurchaseOrderAddedOrRemoved() {
        comboBox.removeAllItems();
        salesOrders.getBusinessObjects().forEach(order -> comboBox.insertItemAt(order,0));
        comboBox.setSelectedIndex(0);
//        salesOrdersViewDataTableModel.fireTableDataChanged();
    }
}