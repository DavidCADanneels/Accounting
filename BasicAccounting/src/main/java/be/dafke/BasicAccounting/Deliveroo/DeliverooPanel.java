package be.dafke.BasicAccounting.Deliveroo;

import be.dafke.BasicAccounting.Accounts.AccountSelectorDialog;
import be.dafke.BasicAccounting.Journals.DateAndDescriptionPanel;
import be.dafke.BasicAccounting.Journals.JournalSelectorDialog;
import be.dafke.BusinessModel.*;
import be.dafke.Utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

public class DeliverooPanel extends JPanel {
    public static final int FOOD_SALES_PERCENTAGE = 6;
    public static final int DELIVERY_PROFIT_PERCENTAGE = 27;
    public static final int DELIVERY_SERVICE_PERCENTAGE = 21;
    private JTextField price;
    private JTextField receivedInclVat, receivedVat, receivedExclVat;
    private JTextField serviceInclVat, serviceVat, serviceExclVat;
    private JButton book;
    private DateAndDescriptionPanel dateAndDescriptionPanel;
    private Transaction transaction;
    private Accounting accounting;

    private BigDecimal salesAmountInclVat = BigDecimal.ZERO.setScale(2,BigDecimal.ROUND_HALF_DOWN);
    private BigDecimal salesAmountExclVat = BigDecimal.ZERO.setScale(2,BigDecimal.ROUND_HALF_DOWN);
    private BigDecimal salesAmountVat = BigDecimal.ZERO.setScale(2,BigDecimal.ROUND_HALF_DOWN);

    private BigDecimal serviceAmountExclVat = BigDecimal.ZERO.setScale(2,BigDecimal.ROUND_HALF_DOWN);
    private BigDecimal serviceAmountVat = BigDecimal.ZERO.setScale(2,BigDecimal.ROUND_HALF_DOWN);
    private BigDecimal serviceAmountInclVat = BigDecimal.ZERO.setScale(2,BigDecimal.ROUND_HALF_DOWN);

    public DeliverooPanel(Accounting accounting) {
        this.accounting = accounting;
        transaction = new Transaction(Calendar.getInstance(),"");
        dateAndDescriptionPanel = new DateAndDescriptionPanel();
        dateAndDescriptionPanel.setTransaction(transaction);
        dateAndDescriptionPanel.fireTransactionDataChanged();

        receivedExclVat = new JTextField(10);
        receivedInclVat = new JTextField(10);
        receivedVat = new JTextField(10);
        serviceExclVat = new JTextField(10);
        serviceVat = new JTextField(10);
        serviceInclVat = new JTextField(10);

        receivedExclVat.setEnabled(false);
        receivedInclVat.setEnabled(false);
        receivedVat.setEnabled(false);
        serviceExclVat.setEnabled(false);
        serviceVat.setEnabled(false);
        serviceInclVat.setEnabled(false);

        setLayout(new BorderLayout());
        add(createTopPanel(), BorderLayout.NORTH);
        add(createOverviewPanel(), BorderLayout.CENTER);
    }

    private JPanel createTopPanel(){
        book = new JButton("Book");
        book.addActionListener(e -> book());
        price = new JTextField(10);
        price.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                calculateTotals();
            }
        });
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

    private JPanel createOverviewPanel(){
        JPanel panel = new JPanel();
        JPanel leftPanel = new JPanel(new GridLayout(0,2));
        JPanel rightPanel = new JPanel(new GridLayout(0,2));

        leftPanel.add(new JLabel("Ontvangsten"));
        leftPanel.add(receivedInclVat);

        leftPanel.add(new JLabel("Excl BTW"));
        leftPanel.add(receivedExclVat);

        leftPanel.add(new JLabel("BTW"));
        leftPanel.add(receivedVat);

        rightPanel.add(new JLabel("Service"));
        rightPanel.add(serviceExclVat);
        rightPanel.add(new JLabel("BTW"));
        rightPanel.add(serviceVat);
        rightPanel.add(new JLabel("Te betalen"));
        rightPanel.add(serviceInclVat);

        panel.add(leftPanel);
        panel.add(rightPanel);
        return panel;
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

        Booking salesBooking = new Booking(deliverooBalanceAccount, salesAmountInclVat, true);
        Booking salesVatBooking = new Booking(vatSalesAccount, salesAmountVat, false);
        Booking salesRevenueBooking = new Booking(deliverooRevenueAccount, salesAmountExclVat, false);
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
        Booking serviceBooking = new Booking(deliverooServiceAccount, serviceAmountExclVat, true);
        Booking serviceVatBooking = new Booking(vatCostsAccount, serviceAmountVat, true);
        Booking debtsBooking = new Booking(deliverooBalanceAccount, serviceAmountInclVat, false);
        serviceTransaction.addBusinessObject(serviceBooking);
        serviceTransaction.addBusinessObject(serviceVatBooking);
        serviceTransaction.addBusinessObject(debtsBooking);
        transactions.setId(serviceTransaction);
        transactions.addBusinessObject(serviceTransaction);
        serviceJournal.addBusinessObject(serviceTransaction);
    }

    private void calculateTotals() {
        String text = price.getText();
        salesAmountInclVat = Utils.parseBigDecimal(text);
        Calendar date = dateAndDescriptionPanel.getDate();
        if(salesAmountInclVat!=null && date!=null){
            salesAmountInclVat = salesAmountInclVat.setScale(2,BigDecimal.ROUND_HALF_DOWN);
            price.setText(salesAmountInclVat.toString());

            salesAmountExclVat = salesAmountInclVat.divide(Utils.getFactor(FOOD_SALES_PERCENTAGE),BigDecimal.ROUND_HALF_DOWN).setScale(2,BigDecimal.ROUND_HALF_DOWN);
            salesAmountVat = salesAmountExclVat.multiply(Utils.getPercentage(FOOD_SALES_PERCENTAGE)).setScale(2,BigDecimal.ROUND_HALF_DOWN);

            serviceAmountExclVat = salesAmountInclVat.multiply(Utils.getPercentage(DELIVERY_PROFIT_PERCENTAGE)).setScale(2,BigDecimal.ROUND_HALF_DOWN);
            serviceAmountVat = serviceAmountExclVat.multiply(Utils.getPercentage(DELIVERY_SERVICE_PERCENTAGE)).setScale(2,BigDecimal.ROUND_HALF_DOWN);
            serviceAmountInclVat = serviceAmountExclVat.add(serviceAmountVat).setScale(2,BigDecimal.ROUND_HALF_DOWN);

            receivedInclVat.setText(salesAmountInclVat.toString());
            receivedExclVat.setText(salesAmountExclVat.toString());
            receivedVat.setText(salesAmountVat.toString());
            serviceInclVat.setText(serviceAmountInclVat.toString());
            serviceExclVat.setText(serviceAmountExclVat.toString());
            serviceVat.setText(serviceAmountVat.toString());
        }
    }
}
