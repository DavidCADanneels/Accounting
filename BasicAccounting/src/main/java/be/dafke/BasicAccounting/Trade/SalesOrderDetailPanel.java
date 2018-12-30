package be.dafke.BasicAccounting.Trade;


import be.dafke.BasicAccounting.Accounts.AccountActions;
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

import static be.dafke.Utils.Utils.parseInt;
import static java.util.ResourceBundle.getBundle;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
class SalesOrderDetailPanel extends JPanel {
    private JButton placeOrderButton, deliveredButton, payedButton, createInvoiceButton;
    private JButton salesTransactionButton, gainTransactionButton, paymentTransactionButton;
    private JButton createSalesOrder;
    private JTextField invoiceNr;
    private JCheckBox payed, delivered, placed, creditNote, promoOrder;
    private SalesOrder salesOrder;
    private Accounting accounting;
    private ContactDetailsPanel contactDetailsPanel;

    SalesOrderDetailPanel(Accounting accounting) {
        this.accounting = accounting;

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
        creditNote = new JCheckBox("CN");
        promoOrder = new JCheckBox("Promo");
        placed.setEnabled(false);
        payed.setEnabled(false);
        delivered.setEnabled(false);
        creditNote.setEnabled(false);
        promoOrder.setEnabled(false);

        JPanel panel = new JPanel();
        panel.add(placed);
        panel.add(delivered);
        panel.add(payed);
        panel.add(creditNote);
        panel.add(promoOrder);
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

        salesTransactionButton = new JButton("SalesTransaction");
        salesTransactionButton.addActionListener(e -> selectSalesTransaction());

        gainTransactionButton = new JButton("GainTransaction");
        gainTransactionButton.addActionListener(e -> selectGainTransaction());

        paymentTransactionButton = new JButton("PaymentTransaction");
        paymentTransactionButton.addActionListener(e -> selectPaymentTransaction());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JPanel line1 = new JPanel();
        JPanel line2 = new JPanel();
        JPanel line3 = new JPanel();

        invoiceNr = new JTextField(10);
        invoiceNr.setEnabled(false);
        line1.add(new JLabel("Invoice:"));
        line1.add(invoiceNr);
        line1.add(createInvoiceButton);

        line2.add(placeOrderButton);
        line2.add(deliveredButton);
        line2.add(payedButton);

        line3.add(salesTransactionButton);
        line3.add(gainTransactionButton);
        line3.add(paymentTransactionButton);

        panel.add(line1);
        panel.add(line2);
        panel.add(line3);
        return panel;
    }

    private void selectPaymentTransaction() {
        Contact customer = salesOrder.getCustomer();
        if("Daginkomsten".equals(customer.getName())) {
            salesOrder.setPaymentTransaction(askId("CASH"));
        } else {
            salesOrder.setPaymentTransaction(askId("BE"));
        }
        updateButtonsAndCheckBoxes();
    }

    private void selectSalesTransaction() {
        Contact customer = salesOrder.getCustomer();
        if("Daginkomsten".equals(customer.getName())) {
            salesOrder.setSalesTransaction(askId("DAG"));
        } else {
            salesOrder.setSalesTransaction(askId("VB"));
        }
        updateButtonsAndCheckBoxes();
    }

    private void selectGainTransaction() {
        salesOrder.setGainTransaction(askId("GAIN"));
        updateButtonsAndCheckBoxes();
    }

    private Transaction askId(String abbr) {
        Journals journals = accounting.getJournals();
        Journal journal = journals.getJournal(abbr);

        JournalSelectorDialog journalSelectorDialog = new JournalSelectorDialog(accounting.getJournals());
        journalSelectorDialog.setSelectedJournal(journal);
        journalSelectorDialog.setVisible(true);
        journal = journalSelectorDialog.getSelection();

        String idString = JOptionPane.showInputDialog(this, "Enter id for " + journal.getName() + ":");
        int id = parseInt(idString);
        ArrayList<Transaction> businessObjects = journal.getBusinessObjects();
        Transaction transaction = businessObjects.get(id - 1);
        return transaction;
    }

    private void payOrder() {
        updateButtonsAndCheckBoxes();
    }

    private void deliverOrder() {
        Calendar date;
        Contact customer = salesOrder.getCustomer();
        String description = customer.getName();
        String deliveryDate = salesOrder.getDeliveryDate(); // FIXME: return Calendar iso String
        if(deliveryDate==null) {
            DateAndDescriptionDialog dateAndDescriptionDialog = DateAndDescriptionDialog.getDateAndDescriptionDialog();
            dateAndDescriptionDialog.setDescription(description);
            dateAndDescriptionDialog.setDate(Calendar.getInstance());
            dateAndDescriptionDialog.setVisible(true);

            date = dateAndDescriptionDialog.getDate();
//            description = dateAndDescriptionDialog.getDescription();

            salesOrder.setDeliveryDate(Utils.toString(date));
            salesOrder.setDescription(description);
        }
        StockTransactions stockTransactions = accounting.getStockTransactions();
        stockTransactions.addOrder(salesOrder);

        StockGUI.fireStockContentChanged();
        StockHistoryGUI.fireStockContentChanged();

        updateButtonsAndCheckBoxes();
    }

    private void placeOrder() {
        createSalesTransaction();
        if(!salesOrder.isPromoOrder()) {
            createGainTransaction();
        }
        StockHistoryGUI.fireStockContentChanged();
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
        dateAndDescriptionDialog.setDescription("");
        dateAndDescriptionDialog.enableDescription(true);
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
        Transaction salesTransaction = salesOrder==null?null:salesOrder.getSalesTransaction();
        Transaction paymentTransaction = salesOrder==null?null:salesOrder.getPaymentTransaction();

        StockTransactions stockTransactions = accounting.getStockTransactions();
        ArrayList<Order> orders = stockTransactions.getOrders();
        boolean orderDelivered = salesOrder!=null && orders.contains(salesOrder);
        boolean toBeDelivered = salesOrder!=null && !orders.contains(salesOrder);

        boolean isCreditNote = salesOrder!=null&& salesOrder.isCreditNote();
        boolean isPromoOrder = salesOrder!=null&& salesOrder.isPromoOrder();

        placed.setSelected(salesTransaction!=null);
        delivered.setSelected(orderDelivered);
        payed.setSelected(paymentTransaction !=null);
        creditNote.setSelected(isCreditNote);
        promoOrder.setSelected(isPromoOrder);

        deliveredButton.setEnabled(toBeDelivered);
        placeOrderButton.setEnabled(salesTransaction==null);
//        createInvoiceButton.setEnabled(salesOrder !=null&&salesOrder.isInvoice());
        payedButton.setEnabled(salesOrder !=null&& paymentTransaction ==null);
        if(salesOrder!=null&&salesOrder.getCustomer()!=null){
            contactDetailsPanel.setContact(salesOrder.getCustomer());
        } else {
            contactDetailsPanel.clearFields();
        }

        salesTransactionButton.setEnabled(salesOrder!=null && salesOrder.getSalesTransaction()==null);
        gainTransactionButton.setEnabled(salesOrder !=null && salesOrder.getGainTransaction()==null);
        paymentTransactionButton.setEnabled(salesOrder !=null && paymentTransaction ==null);
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
        Account salesAccount = StockUtils.getSalesAccount(accounting);

        DateAndDescriptionDialog dateAndDescriptionDialog = DateAndDescriptionDialog.getDateAndDescriptionDialog();
        dateAndDescriptionDialog.setVisible(true);

        Calendar date = dateAndDescriptionDialog.getDate();
        String description = dateAndDescriptionDialog.getDescription();

        // For Sales
        Transaction salesTransaction = new Transaction(date, description);
        salesTransaction.setContact(customer);

        boolean creditNote = salesOrder.isCreditNote();

        BigDecimal customerAmount = salesOrder.getTotalSalesPriceInclVat();
        Booking customerBooking = new Booking(customerAccount, customerAmount, !creditNote);
        salesTransaction.addBusinessObject(customerBooking);

        // Calculate Net Amounts per VAT Rate -> Fields 0, 1, 2, 3
        List<SalesType> salesTypes = new ArrayList<>();
        salesTypes.add(SalesType.VAT_1);
        salesTypes.add(SalesType.VAT_3);

        VATTransaction vatTransaction = new VATTransaction();
        for(SalesType salesType:salesTypes){
            BigDecimal netAmount = salesOrder.getTotalSalesPriceExclVat(OrderItem.withSalesVatRate(salesType.getPct()));
            if(netAmount.compareTo(BigDecimal.ZERO) != 0){
                if(!creditNote) {
                    Booking salesBooking = new Booking(salesAccount, netAmount, false);
                    AccountActions.addSalesVatTransaction(salesBooking, salesType, vatTransaction);
                    salesTransaction.addBusinessObject(salesBooking);
                    salesTransaction.increaseTurnOverAmount(netAmount);
                } else {
                    Booking salesCnBooking = new Booking(salesAccount, netAmount, true);
                    AccountActions.addSalesCnVatTransaction(salesCnBooking, vatTransaction);
                    salesTransaction.addBusinessObject(salesCnBooking);
                    salesTransaction.increaseTurnOverAmount(netAmount.negate());
                }
            }
        }
        salesTransaction.addVatTransaction(vatTransaction);
        vatTransaction.setTransaction(salesTransaction);

        // Calculate Total VAT Amount -> Field 54
        BigDecimal vatAmount = salesOrder.calculateTotalSalesVat(); // ensure no cent different
        if(!creditNote) {
            Booking vatBooking = AccountActions.createSalesVatBooking(accounting, vatAmount, vatTransaction);
            salesTransaction.setVATAmount(vatAmount);
            salesTransaction.addBusinessObject(vatBooking);
        } else {
            Booking vatBooking = AccountActions.createSalesCnVatBooking(accounting, vatAmount, vatTransaction);
            salesTransaction.setVATAmount(vatAmount.negate());
            salesTransaction.addBusinessObject(vatBooking);
        }
        // ---

        Journal salesJournal;
        if(salesOrder.isInvoice()) {
            salesJournal = StockUtils.getSalesJournal(accounting);
        } else {
            salesJournal = StockUtils.getSalesNoInvoiceJournal(accounting);
        }
        salesTransaction.setJournal(salesJournal);
        // TODO: ask for Date and Description

        Transactions transactions = accounting.getTransactions();
        transactions.setId(salesTransaction);
        transactions.addBusinessObject(salesTransaction);
        salesJournal.addBusinessObject(salesTransaction);
        salesOrder.setSalesTransaction(salesTransaction);
        Main.setJournal(salesJournal);
        Main.selectTransaction(salesTransaction);
        Main.fireJournalDataChanged(salesJournal);
        for (Account account : salesTransaction.getAccounts()) {
            Main.fireAccountDataChanged(account);
        }
    }

    private void createGainTransaction(){
        Account salesGainAccount = StockUtils.getSalesGainAccount(accounting);
        Account stockAccount = StockUtils.getStockAccount(accounting);
        Account gainAccount = StockUtils.getGainAccount(accounting);

        BigDecimal stockAmount = salesOrder.calculateTotalStockValue();
        BigDecimal totalSalesPriceExclVat = salesOrder.getTotalSalesPriceExclVat();
        BigDecimal gainAmount = totalSalesPriceExclVat.subtract(stockAmount);

        boolean creditNote = salesOrder.isCreditNote();

        Booking stockBooking = new Booking(stockAccount, stockAmount, creditNote);
        Booking gainBooking = new Booking(gainAccount, gainAmount, creditNote);
        Booking salesDivBooking = new Booking(salesGainAccount, totalSalesPriceExclVat, !creditNote);

        Calendar date;
        String description = salesOrder.getName();
        String deliveryDate = salesOrder.getDeliveryDate(); // FIXME: return Calendar iso String
        if(deliveryDate==null) {
            DateAndDescriptionDialog dateAndDescriptionDialog = DateAndDescriptionDialog.getDateAndDescriptionDialog();
            dateAndDescriptionDialog.enableDescription(false);
            dateAndDescriptionDialog.setDescription(description);
            dateAndDescriptionDialog.setDate(Calendar.getInstance());
            dateAndDescriptionDialog.setVisible(true);

            date = dateAndDescriptionDialog.getDate();
            description = dateAndDescriptionDialog.getDescription();
        } else {
            date = Utils.toCalendar(deliveryDate);
        }

        Transaction gainTransaction = new Transaction(date, description);

        gainTransaction.addBusinessObject(gainBooking);
        gainTransaction.addBusinessObject(stockBooking);
        gainTransaction.addBusinessObject(salesDivBooking);

        // ---

        Journal gainJournal = StockUtils.getGainJournal(accounting);
        gainTransaction.setJournal(gainJournal);


        Transactions transactions = accounting.getTransactions();
        transactions.setId(gainTransaction);
        transactions.addBusinessObject(gainTransaction);
        gainJournal.addBusinessObject(gainTransaction);
        salesOrder.setGainTransaction(gainTransaction);
        Main.fireJournalDataChanged(gainJournal);
        for (Account account : gainTransaction.getAccounts()) {
            Main.fireAccountDataChanged(account);
        }
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