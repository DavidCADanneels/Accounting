package be.dafke.BasicAccounting.Goods;


import be.dafke.BasicAccounting.Accounts.AccountSelectorDialog;
import be.dafke.BasicAccounting.Contacts.ContactDetailsPanel;
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
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.util.ResourceBundle.getBundle;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
class SalesOrderDetailPanel extends JPanel {
    private JButton placeOrderButton, deliveredButton, payedButton, createInvoiceButton;
    private JButton createSalesOrder;
    private final SalesOrders salesOrders;
    private JTextField invoiceNr;
    private JCheckBox payed, delivered, placed;
    private SalesOrder salesOrder;
    private Accounting accounting;
    private ContactDetailsPanel contactDetailsPanel;

    SalesOrderDetailPanel(Accounting accounting) {
        this.accounting = accounting;
        this.salesOrders = accounting.getSalesOrders();

        createSalesOrder = new JButton(getBundle("Accounting").getString("CREATE_SO"));
        createSalesOrder.addActionListener(e -> {
            SalesOrderCreateGUI salesOrderCreateGUI = SalesOrderCreateGUI.showSalesOrderGUI(accounting);
            salesOrderCreateGUI.setLocation(getLocationOnScreen());
            salesOrderCreateGUI.setVisible(true);
        });

        JPanel orderPanel = createOrderPanel();
        JPanel customerPanel = createCustomerPanel(accounting.getContacts());

        setLayout(new BorderLayout());
        add(orderPanel, BorderLayout.NORTH);
        add(customerPanel,BorderLayout.CENTER);
        add(createSalesOrder, BorderLayout.SOUTH);
    }

    private JPanel createOrderPanel(){
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Order"));

        JPanel statusPanel = createStatusPanel();
        JPanel buttonPanel = createButtonPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(statusPanel);
        panel.add(buttonPanel);
        return panel;
    }

    private JPanel createCustomerPanel(Contacts contacts){
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Customer"));

        panel.setLayout(new BorderLayout());
        contactDetailsPanel = new ContactDetailsPanel(contacts);
        contactDetailsPanel.setEnabled(false);

        panel.add(contactDetailsPanel, BorderLayout.NORTH);

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
        DateAndDescriptionDialog dateAndDescriptionDialog = DateAndDescriptionDialog.getDateAndDescriptionDialog();
        Contact customer = salesOrder.getCustomer();
        dateAndDescriptionDialog.setDescription(customer.getName());
        dateAndDescriptionDialog.enableDescription(false);
        dateAndDescriptionDialog.setVisible(true);

        Calendar date = dateAndDescriptionDialog.getDate();
        String description = dateAndDescriptionDialog.getDescription();

        salesOrder.setDeliveryDate(Utils.toString(date));
        salesOrder.setDescription(description);

        StockTransactions stockTransactions = accounting.getStockTransactions();
        stockTransactions.addOrder(salesOrder);

        StockGUI.fireStockContentChanged();
        StockHistoryGUI.fireStockContentChanged();

        salesOrder.getBusinessObjects().forEach(orderItem -> {
            Article article = orderItem.getArticle();
            int numberOfItems = orderItem.getNumberOfItems();
            article.setSoDelivered(numberOfItems);
        });


        salesOrder.setDelivered(true);


        updateButtonsAndCheckBoxes();
    }

    private void placeOrder() {
        createSalesTransaction();
        createGainTransaction();
        salesOrder.getBusinessObjects().forEach(orderItem -> {
            Article article = orderItem.getArticle();
            int numberOfItems = orderItem.getNumberOfItems();
            article.setSoOrdered(numberOfItems);
        });

        StockHistoryGUI.fireStockContentChanged();


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

        salesOrder.setDeliveryDate(Utils.toString(date));
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
        if(salesOrder!=null&&salesOrder.getCustomer()!=null){
            contactDetailsPanel.setContact(salesOrder.getCustomer());
        } else {
            contactDetailsPanel.clearFields();
        }
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

    private Account getVatAccount(){
        Account account = salesOrders.getVATAccount();
        if (account == null){
            AccountType accountType = accounting.getAccountTypes().getBusinessObject(AccountTypes.TAXDEBIT);
            ArrayList<AccountType> list = new ArrayList<>();
            list.add(accountType);
            AccountSelectorDialog dialog = new AccountSelectorDialog(accounting.getAccounts(), list, "Select VAT Account for Sales");
            dialog.setVisible(true);
            account = dialog.getSelection();
            salesOrders.setVATAccount(account);
        }
        return account;
    }

    private Account getStockAccount(){
        Account account = salesOrders.getStockAccount();
        if (account == null){
            AccountType accountType = accounting.getAccountTypes().getBusinessObject(AccountTypes.ASSET);
            ArrayList<AccountType> list = new ArrayList<>();
            list.add(accountType);
            AccountSelectorDialog dialog = new AccountSelectorDialog(accounting.getAccounts(), list, "Select Stock Account");
            dialog.setVisible(true);
            account = dialog.getSelection();
            salesOrders.setStockAccount(account);
        }
        return account;
    }

    private Account getGainAccount(){
        Account account = salesOrders.getGainAccount();
        if (account == null){
            AccountType accountType = accounting.getAccountTypes().getBusinessObject(AccountTypes.REVENUE);
            ArrayList<AccountType> list = new ArrayList<>();
            list.add(accountType);
            AccountSelectorDialog dialog = new AccountSelectorDialog(accounting.getAccounts(), list, "Select Gain Account");
            dialog.setVisible(true);
            account = dialog.getSelection();
            salesOrders.setGainAccount(account);
        }
        return account;
    }

    private Account getSalesAccount(){
        Account account = salesOrders.getSalesAccount();
        if (account == null){
            AccountType accountType = accounting.getAccountTypes().getBusinessObject(AccountTypes.REVENUE);
            ArrayList<AccountType> list = new ArrayList<>();
            list.add(accountType);
            AccountSelectorDialog dialog = new AccountSelectorDialog(accounting.getAccounts(), list, "Select Sales Account");
            dialog.setVisible(true);
            account = dialog.getSelection();
            salesOrders.setSalesAccount(account);
        }
        return account;
    }

    private Account getSalesGainAccount(){
        Account account = salesOrders.getSalesGainAccount();
        if (account == null){
            AccountType accountType = accounting.getAccountTypes().getBusinessObject(AccountTypes.ASSET);
            ArrayList<AccountType> list = new ArrayList<>();
            list.add(accountType);
            AccountSelectorDialog dialog = new AccountSelectorDialog(accounting.getAccounts(), list, "Select Sales Gain Account");
            dialog.setVisible(true);
            account = dialog.getSelection();
            salesOrders.setSalesGainAccount(account);
        }
        return account;
    }

    private Contact getCustomer(){
        Contact customer = salesOrder.getCustomer();
        if(customer == null){
            Contacts contacts = accounting.getContacts();
            ContactSelectorDialog contactSelectorDialog = ContactSelectorDialog.getContactSelector(contacts, Contact.ContactType.CUSTOMERS);
            contactSelectorDialog.setVisible(true);
            customer = contactSelectorDialog.getSelection();
        }
        return customer;
    }

    private Account getCustomerAccount(Contact customer){
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
        return customerAccount;
    }

    private void createSalesTransaction(){
        Contact customer = getCustomer();
        Account customerAccount = getCustomerAccount(customer);
        Account vatAccount = getVatAccount();
        Account salesAccount = getSalesAccount();

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
        for(int pct:vatRates){
            BigDecimal netAmount = salesOrder.getTotalSalesPriceExclVat(OrderItem.withSalesVatRate(pct));
            if(netAmount.compareTo(BigDecimal.ZERO) != 0){
                Booking salesBooking = new Booking(salesAccount, netAmount, false);
                salesTransaction.addBusinessObject(salesBooking);

                salesTransaction.increaseTurnOverAmount(netAmount);

                VATBooking revenueBooking = SalesType.getRevenueBookingByPct(netAmount, pct);
                vatTransaction.addBusinessObject(revenueBooking);

                salesBooking.addVatBooking(revenueBooking);
            }
        }
        // Calculate Total VAT Amount -> Field 54
        BigDecimal vatAmount = salesOrder.calculateTotalSalesVat(); // ensure no cent different

        Booking vatBooking = new Booking(vatAccount, vatAmount, false);
        salesTransaction.addBusinessObject(vatBooking);

        salesTransaction.setVATAmount(vatAmount);

        VATBooking vatSalesBooking = SalesType.getVatBooking(vatAmount);
        vatTransaction.addBusinessObject(vatSalesBooking);

        vatBooking.addVatBooking(vatSalesBooking);

        // ---

        salesTransaction.addVatTransaction(vatTransaction);
        vatTransaction.setTransaction(salesTransaction);

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

        Transactions transactions = accounting.getTransactions();
        transactions.setId(salesTransaction);
        transactions.addBusinessObject(salesTransaction);
        salesJournal.addBusinessObject(salesTransaction);

        Main.setJournal(salesJournal);
        Main.selectTransaction(salesTransaction);
        Main.fireJournalDataChanged(salesJournal);
    }

    private void createGainTransaction(){
        Account salesGainAccount = getSalesGainAccount();
        Account stockAccount = getStockAccount();
        Account gainAccount = getGainAccount();

        BigDecimal stockAmount = salesOrder.calculateTotalStockValue();
        BigDecimal totalSalesPriceExclVat = salesOrder.getTotalSalesPriceExclVat();
        BigDecimal gainAmount = totalSalesPriceExclVat.subtract(stockAmount);

        Booking stockBooking = new Booking(stockAccount, stockAmount, false);
        Booking gainBooking = new Booking(gainAccount, gainAmount, false);
        Booking salesDivBooking = new Booking(salesGainAccount, totalSalesPriceExclVat, true);

        DateAndDescriptionDialog dateAndDescriptionDialog = DateAndDescriptionDialog.getDateAndDescriptionDialog();
        dateAndDescriptionDialog.enableDescription(false);
        dateAndDescriptionDialog.setDescription(salesOrder.getName());
        dateAndDescriptionDialog.setDate(Utils.toCalendar(salesOrder.getDeliveryDate()));
        dateAndDescriptionDialog.setVisible(true);

        Calendar date = dateAndDescriptionDialog.getDate();
        String description = dateAndDescriptionDialog.getDescription();

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

        salesOrder.setGainTransaction(gainTransaction);

        Transactions transactions = accounting.getTransactions();
        transactions.setId(gainTransaction);
        transactions.addBusinessObject(gainTransaction);
        gainJournal.addBusinessObject(gainTransaction);
        Main.fireJournalDataChanged(gainJournal);
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