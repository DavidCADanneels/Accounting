package be.dafke.BasicAccounting.Trade;


import be.dafke.BasicAccounting.Accounts.AccountActions;
import be.dafke.BasicAccounting.Contacts.ContactDetailsPanel;
import be.dafke.BasicAccounting.Contacts.ContactSelectorDialog;
import be.dafke.BasicAccounting.Contacts.ContactsPanel;
import be.dafke.BasicAccounting.Journals.DateAndDescriptionDialog;
import be.dafke.BasicAccounting.Journals.JournalSelectorDialog;
import be.dafke.BasicAccounting.MainApplication.InvoicePDF;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.Accounting.BusinessModel.*;
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

public class SalesOrderDetailPanel extends JPanel {
    private JButton placeOrderButton, deliveredButton, createInvoiceButton;
    private JButton salesTransactionButton, gainTransactionButton, paymentTransactionButton;
    private JButton createSalesOrder;
    private JButton editSalesOrder;
    private JTextField invoiceNr;
    private JCheckBox payed, delivered, placed, creditNote, promoOrder;
    private SalesOrder salesOrder;
    private Accounting accounting;
    private ContactDetailsPanel contactDetailsPanel;

    public SalesOrderDetailPanel() {
        createSalesOrder = new JButton(getBundle("Accounting").getString("CREATE_SO"));
        createSalesOrder.addActionListener(e -> {
            SalesOrderCreateGUI salesOrderCreateGUI = SalesOrderCreateGUI.showSalesOrderGUI(accounting);
            salesOrderCreateGUI.setLocation(getLocationOnScreen());
            salesOrderCreateGUI.setVisible(true);
        });

        editSalesOrder = new JButton(getBundle("Accounting").getString("EDIT_ORDER"));
        editSalesOrder.addActionListener(e -> {
            SalesOrderCreateGUI salesOrderCreateGUI = SalesOrderCreateGUI.showSalesOrderEditGUI(accounting);
            salesOrderCreateGUI.setSalesOrder(salesOrder);
            salesOrderCreateGUI.setLocation(getLocationOnScreen());
            salesOrderCreateGUI.setVisible(true);
        });

        JPanel orderPanel = createOrderPanel();
        JPanel customerPanel = createCustomerPanel();

        disableButtons();

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

    private JPanel createCustomerPanel(){
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Customer"));

        panel.setLayout(new BorderLayout());
        contactDetailsPanel = new ContactDetailsPanel();
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

    public void disableButtons(){
        editSalesOrder.setEnabled(false);
        placeOrderButton.setEnabled(false);
        deliveredButton.setEnabled(false);
        createInvoiceButton.setEnabled(false);
        salesTransactionButton.setEnabled(false);
        gainTransactionButton.setEnabled(false);
        paymentTransactionButton.setEnabled(false);
    }

    private JPanel createButtonPanel(){
        createInvoiceButton = new JButton("Create Invoice");
        createInvoiceButton.setEnabled(false);
        createInvoiceButton.addActionListener(e -> createInvoice());

        placeOrderButton = new JButton("Place Order");
        placeOrderButton.addActionListener(e -> placeOrder());

        deliveredButton = new JButton("Order Delivered");
        deliveredButton.addActionListener(e -> deliverOrder());

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

        line2.add(editSalesOrder);
        line2.add(placeOrderButton);
        line2.add(deliveredButton);

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
        if(salesOrder.isPromoOrder()){
            createPromoTransaction();
        } else {
            Transaction salesTransaction = createTransaction(null, salesOrder.getName());
            createSalesTransaction(salesTransaction);
            createGainTransaction(salesTransaction.getDate());
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

    public void updateButtonsAndCheckBoxes() {
        Transaction salesTransaction = salesOrder==null?null:salesOrder.getSalesTransaction();
        Transaction paymentTransaction = salesOrder==null?null:salesOrder.getPaymentTransaction();

        StockTransactions stockTransactions = accounting.getStockTransactions();
        ArrayList<Order> orders = stockTransactions.getOrders();
        boolean orderDelivered = salesOrder!=null && orders.contains(salesOrder);
        boolean toBeDelivered = salesOrder!=null && !orders.contains(salesOrder);

        boolean isCreditNote = salesOrder!=null&& salesOrder.isCreditNote();
        boolean isPromoOrder = salesOrder!=null&& salesOrder.isPromoOrder();
        boolean editable = salesOrder != null && salesOrder.isEditable();

        placed.setSelected(salesTransaction!=null);
        delivered.setSelected(orderDelivered);
        payed.setSelected(paymentTransaction !=null);
        creditNote.setSelected(isCreditNote);
        promoOrder.setSelected(isPromoOrder);

//        editSalesOrder.setEnabled(true);
        editSalesOrder.setEnabled(editable);
        deliveredButton.setEnabled(toBeDelivered);
        placeOrderButton.setEnabled(salesTransaction==null);

        updateInvoiceButtonAndField();

        updateContactDetails(salesOrder);

        salesTransactionButton.setEnabled(salesOrder!=null && salesOrder.getSalesTransaction()==null);
        gainTransactionButton.setEnabled(salesOrder !=null && salesOrder.getGainTransaction()==null);
        paymentTransactionButton.setEnabled(salesOrder !=null && paymentTransaction ==null);
    }

    public void updateInvoiceButtonAndField() {
        if(salesOrder!=null&&salesOrder.isInvoice()){
            createInvoiceButton.setEnabled(true);
            invoiceNr.setText(salesOrder.getInvoiceNumber());
        } else {
            createInvoiceButton.setEnabled(false);
            invoiceNr.setText("");
        }
    }

    public void updateContactDetails(SalesOrder salesOrder){
        if(salesOrder!=null&&salesOrder.getCustomer()!=null){
            contactDetailsPanel.setContact(salesOrder.getCustomer());
        } else {
            contactDetailsPanel.clearFields();
        }
    }

    private Contact getCustomer(){
        Contact customer = salesOrder.getCustomer();
        if(customer == null){
            Contacts contacts = accounting.getContacts();
            ContactSelectorDialog contactSelectorDialog = ContactSelectorDialog.getContactSelector(accounting, Contact.ContactType.CUSTOMERS);
            contactSelectorDialog.setVisible(true);
            customer = contactSelectorDialog.getSelection();
        }
        return customer;
    }

    public Transaction createTransaction(Calendar date, String description){
        if (date == null || description == null) {
            DateAndDescriptionDialog dateAndDescriptionDialog = DateAndDescriptionDialog.getDateAndDescriptionDialog();
            dateAndDescriptionDialog.enableDescription(true);
            if(description!=null){
                dateAndDescriptionDialog.setDescription(description);
            } else {
                dateAndDescriptionDialog.setDescription("");
            }
            dateAndDescriptionDialog.setVisible(true);

            date = dateAndDescriptionDialog.getDate();
            description = dateAndDescriptionDialog.getDescription();
        }
        return new Transaction(date, description);
    }

    private void createPromoTransaction() {
        Transaction transaction = createTransaction(null, salesOrder.getName());

        Account promoCost = StockUtils.getPromoAccount(accounting);
        Account stockAccount = StockUtils.getStockAccount(accounting);
        BigDecimal totalSalesPriceInclVat = salesOrder.getTotalSalesPriceInclVat();

        Booking costBooking = new Booking(promoCost, totalSalesPriceInclVat, true);
        Booking stockBooking = new Booking(stockAccount, totalSalesPriceInclVat, false);
        transaction.addBusinessObject(costBooking);
        transaction.addBusinessObject(stockBooking);

        Journal journal = StockUtils.getGainJournal(accounting);
        transaction.setJournal(journal);

        Transactions transactions = accounting.getTransactions();
        transactions.setId(transaction);
        transactions.addBusinessObject(transaction);
        journal.addBusinessObject(transaction);
        salesOrder.setSalesTransaction(transaction);
        salesOrder.setGainTransaction(transaction);
        Main.setJournal(journal);
        Main.selectTransaction(transaction);
        Main.fireJournalDataChanged(journal);
        for (Account account : transaction.getAccounts()) {
            Main.fireAccountDataChanged(account);
        }
    }

    private void createSalesTransaction(Transaction transaction){

        Contact customer = getCustomer();
        transaction.setContact(customer);

        Account customerAccount = StockUtils.getCustomerAccount(customer, accounting);
        Account salesAccount = StockUtils.getSalesAccount(accounting);

        boolean creditNote = salesOrder.isCreditNote();

        BigDecimal salesPriceInclVat = salesOrder.getTotalSalesPriceInclVat();

        Booking customerBooking = new Booking(customerAccount, salesPriceInclVat, !creditNote);
        transaction.addBusinessObject(customerBooking);

        // Calculate Net Amounts per VAT Rate -> Fields 0, 1, 2, 3
        List<SalesType> salesTypes = new ArrayList<>();
        salesTypes.add(SalesType.VAT_1);
        salesTypes.add(SalesType.VAT_3);

        for(SalesType salesType:salesTypes){
            BigDecimal netAmount = salesOrder.getTotalSalesPriceExclVat(OrderItem.withSalesVatRate(salesType.getPct()));
            if(netAmount.compareTo(BigDecimal.ZERO) != 0){
                if(!creditNote) {
                    Booking salesBooking = new Booking(salesAccount, netAmount, false);
                    AccountActions.addSalesVatTransaction(salesBooking, salesType);
                    transaction.addBusinessObject(salesBooking);
                    transaction.increaseTurnOverAmount(netAmount);
                } else {
                    Booking salesCnBooking = new Booking(salesAccount, netAmount, true);
                    AccountActions.addSalesCnVatTransaction(salesCnBooking);
                    transaction.addBusinessObject(salesCnBooking);
                    transaction.increaseTurnOverAmount(netAmount.negate());
                }
            }
        }

        // Calculate Total VAT Amount -> Field 54
        BigDecimal vatAmount = salesOrder.calculateTotalSalesVat(); // ensure no cent different
        if(!creditNote) {
            Booking vatBooking = AccountActions.createSalesVatBooking(accounting, vatAmount);
            transaction.setVATAmount(vatAmount);
            transaction.addBusinessObject(vatBooking);
        } else {
            Booking vatBooking = AccountActions.createSalesCnVatBooking(accounting, vatAmount);
            transaction.setVATAmount(vatAmount.negate());
            transaction.addBusinessObject(vatBooking);
        }
        // ---

        Journal salesJournal;
        if(salesOrder.isInvoice()) {
            salesJournal = StockUtils.getSalesJournal(accounting);
        } else {
            salesJournal = StockUtils.getSalesNoInvoiceJournal(accounting);
        }
        transaction.setJournal(salesJournal);
        // TODO: ask for Date and Description

        Transactions transactions = accounting.getTransactions();
        transactions.setId(transaction);
        transactions.addBusinessObject(transaction);
        salesJournal.addBusinessObject(transaction);
        salesOrder.setSalesTransaction(transaction);
        Main.setJournal(salesJournal);
        Main.selectTransaction(transaction);
        Main.fireJournalDataChanged(salesJournal);
        for (Account account : transaction.getAccounts()) {
            Main.fireAccountDataChanged(account);
        }
    }

    private void createGainTransaction(Calendar date){
        Account salesGainAccount = StockUtils.getSalesGainAccount(accounting);
        Account stockAccount = StockUtils.getStockAccount(accounting);
        Account gainAccount = StockUtils.getGainAccount(accounting);

        BigDecimal stockAmount = salesOrder.getTotalStockValue();
        BigDecimal totalSalesPriceExclVat = salesOrder.getTotalSalesPriceExclVat();
        BigDecimal gainAmount = totalSalesPriceExclVat.subtract(stockAmount);

        boolean creditNote = salesOrder.isCreditNote();

        Booking stockBooking = new Booking(stockAccount, stockAmount, creditNote);
        Booking gainBooking = new Booking(gainAccount, gainAmount, creditNote);
        Booking salesDivBooking = new Booking(salesGainAccount, totalSalesPriceExclVat, !creditNote);

        Transaction gainTransaction = new Transaction(date, salesOrder.getName());

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
    }

    public void setAccounting(Accounting accounting) {
        this.accounting = accounting;
        contactDetailsPanel.setAccounting(accounting);
    }
}