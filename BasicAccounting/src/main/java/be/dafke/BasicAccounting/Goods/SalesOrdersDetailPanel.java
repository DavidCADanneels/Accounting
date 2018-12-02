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
class SalesOrdersDetailPanel extends JPanel {
    private JButton placeOrderButton, deliveredButton, payedButton, createInvoiceButton;
    private final SalesOrders salesOrders;
    private JTextField customerName, invoiceNr;
    private JCheckBox payed, delivered, placed;
    private SalesOrder salesOrder;
    private Accounting accounting;

    SalesOrdersDetailPanel(Accounting accounting) {
        this.accounting = accounting;
        this.salesOrders = accounting.getSalesOrders();

        JPanel customerDetailsPanel = createCustomerDetailsPanel();
        JPanel customerNamePanel = createCustomerNamePanel();

        JPanel south = new JPanel();
        south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));

        JPanel statusPanel = createStatusPanel();
        JPanel buttonPanel = createButtonPanel();
        south.add(statusPanel);
        south.add(buttonPanel);

        setLayout(new BorderLayout());
        add(customerNamePanel,BorderLayout.NORTH);
        add(customerDetailsPanel,BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);
    }

    private JPanel createCustomerDetailsPanel(){
        JPanel panel = new JPanel();
        panel.add(new JLabel("Details"));
        panel.add(new JLabel("Details"));
        panel.add(new JLabel("Details"));
        panel.add(new JLabel("Details"));
        panel.add(new JLabel("Details"));
        panel.add(new JLabel("Details"));
        return panel;
    }

    private JPanel createStatusPanel(){
        placed = new JCheckBox("Ordered");
        payed = new JCheckBox("Payed");
        delivered = new JCheckBox("Delived");
        placed.setEnabled(false);
        payed.setEnabled(false);
        delivered.setEnabled(false);

        JPanel panel = new JPanel();
        panel.add(placed);
        panel.add(delivered);
        panel.add(payed);
        return panel;
    }

    private JPanel createCustomerNamePanel(){
        JPanel panel = new JPanel();

        customerName = new JTextField(20);
        customerName.setEditable(false);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(customerName);
        return panel;
    }

    private JPanel createButtonPanel(){
        createInvoiceButton = new JButton("Create Invoice");
//        createInvoiceButton.setVisible(false);
        createInvoiceButton.setEnabled(false);
        createInvoiceButton.addActionListener(e -> createInvoice());

        placeOrderButton = new JButton("Place Order");
        placeOrderButton.addActionListener(e -> placeOrder());

        deliveredButton = new JButton("Order Delivered");
        deliveredButton.addActionListener(e -> deliverOrder());

        payedButton = new JButton("Order Payed");
        payedButton.addActionListener(e -> payOrder());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JPanel line1 = new JPanel();
        JPanel line2 = new JPanel();

        invoiceNr = new JTextField(10);
        invoiceNr.setEnabled(false);
        line1.add(new JLabel("Invoice:"));
        line1.add(invoiceNr);
        line1.add(createInvoiceButton);

        line2.add(placeOrderButton);
        line2.add(deliveredButton);
        line2.add(payedButton);

        panel.add(line1);
        panel.add(line2);
        return panel;
    }

    private void payOrder() {
        salesOrder.setPayed(true);
        updateButtonsAndCheckBoxes();
    }

    private void deliverOrder() {
        Stock stock = accounting.getStock();

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
    }

    private void placeOrder() {
        createSalesTransaction();
        salesOrder.setPlaced(true);
        updateButtonsAndCheckBoxes();
    }

    private void createInvoice(){
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
    }

    private void updateButtonsAndCheckBoxes() {
        payed.setSelected(salesOrder !=null&& salesOrder.isPayed());
        placed.setSelected(salesOrder !=null&& salesOrder.isPlaced());
        delivered.setSelected(salesOrder !=null&& salesOrder.isDelivered());
        deliveredButton.setEnabled(salesOrder !=null&&!salesOrder.isDelivered());
        placeOrderButton.setEnabled(salesOrder !=null&&!salesOrder.isPlaced());
//        createInvoiceButton.setEnabled(salesOrder !=null&&salesOrder.isInvoice());
        payedButton.setEnabled(salesOrder !=null&&!salesOrder.isPayed());
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

    public void setOrder(SalesOrder salesOrder){
        this.salesOrder = salesOrder;
        if(salesOrder!=null&&salesOrder.isInvoice()){
//            createInvoiceButton.setVisible(true);
            createInvoiceButton.setEnabled(true);
            invoiceNr.setText(salesOrder.getInvoiceNumber());
        } else {
//            createInvoiceButton.setVisible(false);
            createInvoiceButton.setEnabled(false);
            invoiceNr.setText("");
        }
        updateButtonsAndCheckBoxes();
    }
}