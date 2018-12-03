package be.dafke.BasicAccounting.Deliveroo;

import be.dafke.BasicAccounting.Accounts.AccountSelectorDialog;
import be.dafke.BasicAccounting.Journals.DateAndDescriptionPanel;
import be.dafke.BasicAccounting.Journals.JournalSelectorDialog;
import be.dafke.BusinessModel.*;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.Utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

public class DeliverooOrderCreatePanel extends JPanel {
    public static final int FOOD_SALES_PERCENTAGE = 6;
    public static final int DELIVERY_PROFIT_PERCENTAGE = 27;
    public static final int DELIVERY_SERVICE_PERCENTAGE = 21;
    private JTextField price;
    private JButton book;
    private DeliveryTotalsPanel totalsPanel;
    private DateAndDescriptionPanel dateAndDescriptionPanel;
    private Transaction transaction;
    private Accounting accounting;

    private JTable table;
    private DeliverooOrdersCreateOrderDataTableModel tableModel;
    private MealOrder mealOrder;

    public DeliverooOrderCreatePanel(Accounting accounting) {
        this.accounting = accounting;
        totalsPanel = new DeliveryTotalsPanel();
        transaction = new Transaction(Calendar.getInstance(),"");

        setLayout(new BorderLayout());
        add(totalsPanel, BorderLayout.SOUTH);
        add(createTopPanel(), BorderLayout.NORTH);
        add(createOrderPanel(), BorderLayout.CENTER);
        clear();
    }

    private JScrollPane createOrderPanel() {
        tableModel = new DeliverooOrdersCreateOrderDataTableModel(accounting.getDeliverooMeals(), mealOrder);
        table = new JTable(tableModel);
        return new JScrollPane(table);
    }

    private void clear() {
        totalsPanel.clear();
        price.setText("");
        Calendar date = transaction.getDate();
        transaction = new Transaction(date, "");
        dateAndDescriptionPanel.setTransaction(transaction);
        dateAndDescriptionPanel.fireTransactionDataChanged();

        book.setEnabled(false);

        mealOrder = new MealOrder();
        mealOrder.setMeals(accounting.getDeliverooMeals());
        tableModel.setMealOrder(mealOrder);
    }

    private JPanel createTopPanel(){
        dateAndDescriptionPanel = new DateAndDescriptionPanel();
        dateAndDescriptionPanel.setTransaction(transaction);
        dateAndDescriptionPanel.fireTransactionDataChanged();

        book = new JButton("Book");
        book.addActionListener(e -> {
            Calendar date = dateAndDescriptionPanel.getDate();
            String description = dateAndDescriptionPanel.getDescription();
            mealOrder.setDate(date);
            mealOrder.setDescription(description);
            addHistory();
            book();
            clear();
        });
        price = new JTextField(10);
        price.setEnabled(false);
//        price.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusLost(FocusEvent e) {
//                calculateTotals();
//            }
//        });
        JPanel panel = new JPanel();
        panel.add(dateAndDescriptionPanel);
        JPanel right = new JPanel(new GridLayout(0,2));
        right.add(new JLabel("Sales Price:"));
        right.add(price);
        right.add(new JLabel(""));
        right.add(book);
        panel.add(right);
        return panel;
    }

    private void addHistory() {
        mealOrder.removeEmptyOrderItems();
        MealOrders mealOrders = accounting.getMealOrders();
        try {
            mealOrders.addBusinessObject(mealOrder);
        } catch (EmptyNameException | DuplicateNameException e) {
            e.printStackTrace();
        }
//        clear();
    }

    private void book() {
        VATTransactions vatTransactions = accounting.getVatTransactions();
        Journal salesJournal = vatTransactions.getDeliverooSalesJournal();
        Journal serviceJournal = vatTransactions.getDeliverooServiceJournal();
        Account deliverooBalanceAccount = vatTransactions.getDeliverooBalanceAccount();
        Account deliverooServiceAccount = vatTransactions.getDeliverooServiceAccount();
        Account deliverooRevenueAccount = vatTransactions.getDeliverooRevenueAccount();
        Account vatSalesAccount = vatTransactions.getDebitAccount();
        Account vatCostsAccount = vatTransactions.getCreditAccount();
        if(salesJournal==null){
            JournalSelectorDialog journalSelectorDialog = new JournalSelectorDialog(accounting.getJournals());
            journalSelectorDialog.setVisible(true);
            salesJournal = journalSelectorDialog.getSelection();
            vatTransactions.setDeliverooSalesJournal(salesJournal);
        }
        if(serviceJournal==null){
            JournalSelectorDialog journalSelectorDialog = new JournalSelectorDialog(accounting.getJournals());
            journalSelectorDialog.setVisible(true);
            serviceJournal = journalSelectorDialog.getSelection();
            vatTransactions.setDeliverooServiceJournal(serviceJournal);
        }
        if(deliverooBalanceAccount==null){
            Accounts accounts = accounting.getAccounts();
            ArrayList<AccountType> accountTypes = accounting.getAccountTypes().getBusinessObjects();
            AccountSelectorDialog accountSelectorDialog = AccountSelectorDialog.getAccountSelector(accounts, accountTypes, "select Deliveroo Balance Account");
            accountSelectorDialog.setVisible(true);
            deliverooBalanceAccount = accountSelectorDialog.getSelection();
            vatTransactions.setDeliverooBalanceAccount(deliverooBalanceAccount);
        }
        if(deliverooServiceAccount==null){
            Accounts accounts = accounting.getAccounts();
            ArrayList<AccountType> accountTypes = accounting.getAccountTypes().getBusinessObjects();
            AccountSelectorDialog accountSelectorDialog = AccountSelectorDialog.getAccountSelector(accounts, accountTypes, "select Deliveroo Service Account");
            accountSelectorDialog.setVisible(true);
            deliverooServiceAccount = accountSelectorDialog.getSelection();
            vatTransactions.setDeliverooServiceAccount(deliverooServiceAccount);
        }
        if(deliverooRevenueAccount==null){
            Accounts accounts = accounting.getAccounts();
            ArrayList<AccountType> accountTypes = accounting.getAccountTypes().getBusinessObjects();
            AccountSelectorDialog accountSelectorDialog = AccountSelectorDialog.getAccountSelector(accounts, accountTypes, "select Deliveroo Revenue Account");
            accountSelectorDialog.setVisible(true);
            deliverooRevenueAccount = accountSelectorDialog.getSelection();
            vatTransactions.setDeliverooRevenueAccount(deliverooRevenueAccount);
        }
        if(vatSalesAccount==null){
            Accounts accounts = accounting.getAccounts();
            ArrayList<AccountType> accountTypes = accounting.getAccountTypes().getBusinessObjects();
            AccountSelectorDialog accountSelectorDialog = AccountSelectorDialog.getAccountSelector(accounts, accountTypes, "select Sales VAT Account");
            accountSelectorDialog.setVisible(true);
            vatSalesAccount = accountSelectorDialog.getSelection();
            vatTransactions.setDebitAccount(vatSalesAccount);
        }
        if(vatCostsAccount==null){
            Accounts accounts = accounting.getAccounts();
            ArrayList<AccountType> accountTypes = accounting.getAccountTypes().getBusinessObjects();
            AccountSelectorDialog accountSelectorDialog = AccountSelectorDialog.getAccountSelector(accounts, accountTypes, "select Costs VAT Account");
            accountSelectorDialog.setVisible(true);
            vatCostsAccount = accountSelectorDialog.getSelection();
            vatTransactions.setCreditAccount(vatCostsAccount);
        }

        Booking salesBooking = new Booking(deliverooBalanceAccount, totalsPanel.getSalesAmountInclVat(), true);
        Booking salesVatBooking = new Booking(vatSalesAccount, totalsPanel.getSalesAmountVat(), false);
        Booking salesRevenueBooking = new Booking(deliverooRevenueAccount, totalsPanel.getSalesAmountExclVat(), false);
        transaction.setJournal(salesJournal);
        Calendar date = transaction.getDate();
        String description = dateAndDescriptionPanel.getDescription();
        transaction.addBusinessObject(salesBooking);
        transaction.addBusinessObject(salesVatBooking);
        transaction.addBusinessObject(salesRevenueBooking);
        Transactions transactions = accounting.getTransactions();
        transactions.setId(transaction);
        transactions.addBusinessObject(transaction);
        salesJournal.addBusinessObject(transaction);

        Transaction serviceTransaction = new Transaction(date, description);
        serviceTransaction.setJournal(serviceJournal);
        Booking serviceBooking = new Booking(deliverooServiceAccount, totalsPanel.getServiceAmountExclVat(), true);
        Booking serviceVatBooking = new Booking(vatCostsAccount, totalsPanel.getServiceAmountVat(), true);
        Booking debtsBooking = new Booking(deliverooBalanceAccount, totalsPanel.getServiceAmountInclVat(), false);
        serviceTransaction.addBusinessObject(serviceBooking);
        serviceTransaction.addBusinessObject(serviceVatBooking);
        serviceTransaction.addBusinessObject(debtsBooking);
        transactions.setId(serviceTransaction);
        transactions.addBusinessObject(serviceTransaction);
        serviceJournal.addBusinessObject(serviceTransaction);
    }

    public void calculateTotals() {

        BigDecimal totalPrice = mealOrder.getTotalPrice();
        totalsPanel.setSalesAmountInclVat(totalPrice);
        totalsPanel.calculateTotals();
        BigDecimal salesAmountInclVat = totalsPanel.getSalesAmountInclVat();
        String description = dateAndDescriptionPanel.getDescription();
        book.setEnabled(salesAmountInclVat.compareTo(BigDecimal.ZERO)>0 && !description.isEmpty());
        salesAmountInclVat = salesAmountInclVat.setScale(2,BigDecimal.ROUND_HALF_DOWN);
        price.setText(salesAmountInclVat.toString());
    }
}