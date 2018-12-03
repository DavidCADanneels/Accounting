package be.dafke.BasicAccounting.Goods;


import be.dafke.BasicAccounting.Accounts.AccountSelectorDialog;
import be.dafke.BasicAccounting.Contacts.ContactDetailsPanel;
import be.dafke.BasicAccounting.Journals.DateAndDescriptionDialog;
import be.dafke.BasicAccounting.Journals.JournalSelectorDialog;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;
import be.dafke.Utils.Utils;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

import static java.util.ResourceBundle.getBundle;

/**
 * User: david
 * Date: 29-12-13
 * Time: 22:07
 */
public class PurchaseOrdersDetailPanel extends JPanel {
    private JButton placeOrderButton, deliveredButton, payedButton;
    private final JButton createPurchaseOrder;
    private final PurchaseOrders purchaseOrders;
    private JCheckBox payed, delivered, placed;
    private PurchaseOrder purchaseOrder;
    private Accounting accounting;
    private ContactDetailsPanel contactDetailsPanel;


    public PurchaseOrdersDetailPanel(Accounting accounting) {
        this.accounting = accounting;
        this.purchaseOrders = accounting.getPurchaseOrders();

        createPurchaseOrder = new JButton(getBundle("Accounting").getString("CREATE_PO"));
        createPurchaseOrder.addActionListener(e -> {
            PurchaseOrderCreateGUI purchaseOrderCreateGUI = PurchaseOrderCreateGUI.showPurchaseOrderGUI(accounting);
            purchaseOrderCreateGUI.setLocation(getLocationOnScreen());
            purchaseOrderCreateGUI.setVisible(true);
        });

        JPanel orderPanel = createOrderPanel();
        JPanel customerPanel = createCustomerPanel(accounting.getContacts());

        setLayout(new BorderLayout());
        add(orderPanel, BorderLayout.NORTH);
        add(customerPanel,BorderLayout.CENTER);
        add(createPurchaseOrder, BorderLayout.SOUTH);
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
        panel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Supplier"));

        panel.setLayout(new BorderLayout());
        contactDetailsPanel = new ContactDetailsPanel(contacts);
        contactDetailsPanel.setEnabled(false);

        panel.add(contactDetailsPanel, BorderLayout.NORTH);

        return panel;
    }

    private JPanel createStatusPanel(){
        JPanel statusPanel = new JPanel();

        placed = new JCheckBox("Ordered");
        payed = new JCheckBox("Payed");
        delivered = new JCheckBox("Delived");
        placed.setEnabled(false);
        payed.setEnabled(false);
        delivered.setEnabled(false);

        statusPanel.add(placed);
        statusPanel.add(delivered);
        statusPanel.add(payed);

        return statusPanel;
    }

    private JPanel createButtonPanel(){
        JPanel panel = new JPanel();

        placeOrderButton = new JButton("Place Order");
        placeOrderButton.addActionListener(e -> placeOrder());

        deliveredButton = new JButton("Order Delivered");
        deliveredButton.addActionListener(e -> deliverOrder());

        payedButton = new JButton("Pay Order");
        payedButton.addActionListener(e -> payOrder());

        panel.add(placeOrderButton);
        panel.add(deliveredButton);
        panel.add(payedButton);

        return panel;
    }

    public void setOrder(PurchaseOrder purchaseOrder){
        this.purchaseOrder = purchaseOrder;
        updateButtonsAndCheckBoxes();
    }

    private void payOrder(){
        purchaseOrder.setPayed(true);
        updateButtonsAndCheckBoxes();
    }

    private void placeOrder(){
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
    }

    private void deliverOrder(){
        Stock stock = accounting.getStock();
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
    }

    private void updateButtonsAndCheckBoxes() {
        payed.setSelected(purchaseOrder !=null&& purchaseOrder.isPayed());
        placed.setSelected(purchaseOrder !=null&& purchaseOrder.isPlaced());
        delivered.setSelected(purchaseOrder !=null&& purchaseOrder.isDelivered());
        deliveredButton.setEnabled(purchaseOrder !=null&&!purchaseOrder.isDelivered());
        placeOrderButton.setEnabled(purchaseOrder !=null&&!purchaseOrder.isPlaced());
        payedButton.setEnabled(purchaseOrder !=null&&!purchaseOrder.isPayed());
        if(purchaseOrder!=null&&purchaseOrder.getSupplier()!=null){
            contactDetailsPanel.setContact(purchaseOrder.getSupplier());
        } else {
            contactDetailsPanel.clearFields();
        }
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
}