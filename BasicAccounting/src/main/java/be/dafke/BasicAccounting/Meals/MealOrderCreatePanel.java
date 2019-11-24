package be.dafke.BasicAccounting.Meals;

import be.dafke.BasicAccounting.Accounts.AccountActions;
import be.dafke.BasicAccounting.Accounts.AccountSelectorDialog;
import be.dafke.BasicAccounting.Journals.DateAndDescriptionPanel;
import be.dafke.BasicAccounting.Journals.JournalSelectorDialog;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.Accounting.BusinessModel.*;
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.Utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

public class MealOrderCreatePanel extends JPanel {
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
    private MealOrderCreateDataTableModel tableModel;
    private MealOrder mealOrder;

    public MealOrderCreatePanel(Accounting accounting) {
        this.accounting = accounting;
        totalsPanel = new DeliveryTotalsPanel();
        transaction = new Transaction(Calendar.getInstance(),"");

        tableModel = new MealOrderCreateDataTableModel(accounting);//, totalsPanel);
        tableModel.setMealOrder(mealOrder);
        table = new JTable(tableModel);

        JScrollPane orderPanel = new JScrollPane(table);
        setLayout(new BorderLayout());
        add(totalsPanel, BorderLayout.SOUTH);
        add(createTopPanel(), BorderLayout.NORTH);
        add(orderPanel, BorderLayout.CENTER);
        clear();
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
        mealOrder.setMeals(accounting.getMeals());
        tableModel.setMealOrder(mealOrder);
    }

    private JPanel createTopPanel(){
        dateAndDescriptionPanel = new DateAndDescriptionPanel();
        dateAndDescriptionPanel.addDescriptionFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                enableButtonIfPossible();
            }
        });
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
        MealOrdersOverviewGUI.fireOrderAddedForAccounting(accounting, mealOrder);
        Main.fireMealCountUpdated(accounting);
    }

    private void book() {
        //  MealOrder
        //
        MealOrders mealOrders = accounting.getMealOrders();
        Journal salesJournal = mealOrders.getMealOrderSalesJournal();
        Journal serviceJournal = mealOrders.getMealOrderServiceJournal();
        Account mealOrderBalanceAccount = mealOrders.getMealOrderBalanceAccount();
        Account mealOrderServiceAccount = mealOrders.getMealOrderServiceAccount();
        Account mealOrderRevenueAccount = mealOrders.getMealOrderRevenueAccount();
        if(salesJournal==null){
            JournalSelectorDialog journalSelectorDialog = new JournalSelectorDialog(accounting.getJournals());
            journalSelectorDialog.setVisible(true);
            salesJournal = journalSelectorDialog.getSelection();
            mealOrders.setMealOrderSalesJournal(salesJournal);
        }
        if(serviceJournal==null){
            JournalSelectorDialog journalSelectorDialog = new JournalSelectorDialog(accounting.getJournals());
            journalSelectorDialog.setVisible(true);
            serviceJournal = journalSelectorDialog.getSelection();
            mealOrders.setMealOrderServiceJournal(serviceJournal);
        }
        if(mealOrderBalanceAccount==null){
            Accounts accounts = accounting.getAccounts();
            ArrayList<AccountType> accountTypes = accounting.getAccountTypes().getBusinessObjects();
            AccountSelectorDialog accountSelectorDialog = AccountSelectorDialog.getAccountSelector(accounts, accountTypes, "select Meal Order Balance Account");
            accountSelectorDialog.setVisible(true);
            mealOrderBalanceAccount = accountSelectorDialog.getSelection();
            mealOrders.setMealOrderBalanceAccount(mealOrderBalanceAccount);
        }
        if(mealOrderServiceAccount==null){
            Accounts accounts = accounting.getAccounts();
            ArrayList<AccountType> accountTypes = accounting.getAccountTypes().getBusinessObjects();
            AccountSelectorDialog accountSelectorDialog = AccountSelectorDialog.getAccountSelector(accounts, accountTypes, "select Meal Order Service Account");
            accountSelectorDialog.setVisible(true);
            mealOrderServiceAccount = accountSelectorDialog.getSelection();
            mealOrders.setMealOrderServiceAccount(mealOrderServiceAccount);
        }
        if(mealOrderRevenueAccount==null){
            Accounts accounts = accounting.getAccounts();
            ArrayList<AccountType> accountTypes = accounting.getAccountTypes().getBusinessObjects();
            AccountSelectorDialog accountSelectorDialog = AccountSelectorDialog.getAccountSelector(accounts, accountTypes, "select Meal Order Revenue Account");
            accountSelectorDialog.setVisible(true);
            mealOrderRevenueAccount = accountSelectorDialog.getSelection();
            mealOrders.setMealOrderRevenueAccount(mealOrderRevenueAccount);
        }

        // VAT
        //
        Booking customerBooking = new Booking(mealOrderBalanceAccount, totalsPanel.getSalesAmountInclVat(), true);
        Booking salesVatBooking = AccountActions.createSalesVatBooking(accounting, totalsPanel.getSalesAmountVat());
        Booking salesRevenueBooking = new Booking(mealOrderRevenueAccount, totalsPanel.getSalesAmountExclVat(), false);
        AccountActions.addSalesVatTransaction(salesRevenueBooking, SalesType.VAT_1);

        transaction.setJournal(salesJournal);
        Calendar date = transaction.getDate();
        String description = dateAndDescriptionPanel.getDescription();

        transaction.addBusinessObject(customerBooking);
        transaction.addBusinessObject(salesRevenueBooking);
        transaction.addBusinessObject(salesVatBooking);

        Transactions transactions = accounting.getTransactions();
        transactions.setId(transaction);
        transactions.addBusinessObject(transaction);
        salesJournal.addBusinessObject(transaction);


        Booking serviceBooking = new Booking(mealOrderServiceAccount, totalsPanel.getServiceAmountExclVat(), true);
        AccountActions.addPurchaseVatTransaction(serviceBooking, PurchaseType.VAT_82);
        Booking serviceVatBooking = AccountActions.createPurchaseVatBooking(accounting, totalsPanel.getServiceAmountVat());
        Booking debtsBooking = new Booking(mealOrderBalanceAccount, totalsPanel.getServiceAmountInclVat(), false);

        Transaction serviceTransaction = new Transaction(date, description);
        serviceTransaction.setJournal(serviceJournal);

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
        salesAmountInclVat = salesAmountInclVat.setScale(2,BigDecimal.ROUND_HALF_DOWN);
        price.setText(salesAmountInclVat.toString());
        enableButtonIfPossible();
    }

    public void enableButtonIfPossible(){
        String text = price.getText();
        BigDecimal salesAmountInclVat = Utils.parseBigDecimal(text);
        String description = dateAndDescriptionPanel.getDescription();
        book.setEnabled(salesAmountInclVat !=null && salesAmountInclVat.compareTo(BigDecimal.ZERO)>0 && !description.isEmpty());
    }
}
