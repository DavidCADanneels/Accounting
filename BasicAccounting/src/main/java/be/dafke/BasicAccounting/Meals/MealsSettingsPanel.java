package be.dafke.BasicAccounting.Meals;

import be.dafke.Accounting.BusinessModel.*;

import javax.swing.*;
import java.awt.*;

public class MealsSettingsPanel extends JPanel {
    private Accounting accounting;
    private final JComboBox<Account> serviceAccountSelection, revenueAccountSelection, balanceAccountSelection;
    private final JComboBox<Journal> salesJournalSelection, serviceJournalSelection;
    private final DefaultComboBoxModel<Account> serviceAccountModel, salesAccountModel, balanceAccountModel;
    private final DefaultComboBoxModel<Journal> salesJournalModel, serviceJournalModel;

    public MealsSettingsPanel(Accounting accounting) {
        this.accounting = accounting;
        serviceAccountModel = new DefaultComboBoxModel<>();
        salesAccountModel = new DefaultComboBoxModel<>();
        balanceAccountModel = new DefaultComboBoxModel<>();

        salesJournalModel = new DefaultComboBoxModel<>();
        serviceJournalModel = new DefaultComboBoxModel<>();

        accounting.getAccounts().getBusinessObjects().forEach(account -> {
            serviceAccountModel.addElement(account);
            salesAccountModel.addElement(account);
            balanceAccountModel.addElement(account);
        });

        accounting.getJournals().getBusinessObjects().forEach(journal -> {
            salesJournalModel.addElement(journal);
            serviceJournalModel.addElement(journal);
        });

        serviceAccountSelection = new JComboBox<>(serviceAccountModel);
        revenueAccountSelection = new JComboBox<>(salesAccountModel);
        balanceAccountSelection = new JComboBox<>(balanceAccountModel);

        salesJournalSelection = new JComboBox<>(salesJournalModel);
        serviceJournalSelection = new JComboBox<>(serviceJournalModel);

        MealOrders mealOrders = accounting.getMealOrders();

        Account serviceAccount = mealOrders.getMealOrderServiceAccount();
        Account revenueAccount = mealOrders.getMealOrderRevenueAccount();
        Account balanceAccount = mealOrders.getMealOrderBalanceAccount();

        Journal salesJournal = mealOrders.getMealOrderSalesJournal();
        Journal serviceJournal = mealOrders.getMealOrderServiceJournal();

        serviceAccountSelection.setSelectedItem(serviceAccount);
        serviceAccountSelection.addActionListener(e -> updateSelectedServiceAccount());
        serviceAccountSelection.setEnabled(accounting.isMealsAccounting());

        revenueAccountSelection.setSelectedItem(revenueAccount);
        revenueAccountSelection.addActionListener(e -> updateSelectedRevenueAccount());
        revenueAccountSelection.setEnabled(accounting.isMealsAccounting());

        balanceAccountSelection.setSelectedItem(balanceAccount);
        balanceAccountSelection.addActionListener(e -> updateSelectedBalanceAccount());
        balanceAccountSelection.setEnabled(accounting.isMealsAccounting());

        salesJournalSelection.setSelectedItem(salesJournal);
        salesJournalSelection.addActionListener(e -> updateSelectedSalesJournal());
        salesJournalSelection.setEnabled(accounting.isMealsAccounting());

        serviceJournalSelection.setSelectedItem(serviceJournal);
        serviceJournalSelection.addActionListener(e -> updateSelectedServiceJournal());
        serviceJournalSelection.setEnabled(accounting.isMealsAccounting());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2));

        panel.add(new JLabel("Service Account"));
        panel.add(serviceAccountSelection);
        panel.add(new JLabel("Revenue Account"));
        panel.add(revenueAccountSelection);
        panel.add(new JLabel("Balance Account"));
        panel.add(balanceAccountSelection);
        panel.add(new JLabel("Sales Journal"));
        panel.add(salesJournalSelection);
        panel.add(new JLabel("Service Journal"));
        panel.add(serviceJournalSelection);

        add(panel);
    }

    public void updateSelectedServiceAccount() {
        Account account = (Account) serviceAccountSelection.getSelectedItem();
        MealOrders mealOrders = accounting.getMealOrders();
        mealOrders.setMealOrderServiceAccount(account);
    }

    public void updateSelectedRevenueAccount() {
        Account account = (Account) revenueAccountSelection.getSelectedItem();
        MealOrders mealOrders = accounting.getMealOrders();
        mealOrders.setMealOrderRevenueAccount(account);
    }

    public void updateSelectedBalanceAccount() {
        Account account = (Account) balanceAccountSelection.getSelectedItem();
        MealOrders mealOrders = accounting.getMealOrders();
        mealOrders.setMealOrderBalanceAccount(account);
    }

    public void updateSelectedSalesJournal() {
        Journal journal = (Journal) salesJournalSelection.getSelectedItem();
        MealOrders mealOrders = accounting.getMealOrders();
        mealOrders.setMealOrderSalesJournal(journal);
    }

    public void updateSelectedServiceJournal() {
        Journal journal = (Journal) serviceJournalSelection.getSelectedItem();
        MealOrders mealOrders = accounting.getMealOrders();
        mealOrders.setMealOrderServiceJournal(journal);
    }

    @Override
    public void setEnabled(boolean enabled){
        super.setEnabled(enabled);
        serviceAccountSelection.setEnabled(enabled);
        revenueAccountSelection.setEnabled(enabled);
        balanceAccountSelection.setEnabled(enabled);
        salesJournalSelection.setEnabled(enabled);
        serviceJournalSelection.setEnabled(enabled);
        if(!enabled){
            serviceAccountSelection.setSelectedItem(null);
            revenueAccountSelection.setSelectedItem(null);
            balanceAccountSelection.setSelectedItem(null);
            salesJournalSelection.setSelectedItem(null);
            serviceJournalSelection.setSelectedItem(null);
            updateSelectedServiceAccount();
            updateSelectedRevenueAccount();
            updateSelectedBalanceAccount();
            updateSelectedSalesJournal();
            updateSelectedServiceJournal();
        }
    }

    public void copyMealOrderSettings(Accounting copyFrom) {
        serviceAccountModel.removeAllElements();
        salesAccountModel.removeAllElements();
        balanceAccountModel.removeAllElements();

        salesJournalModel.removeAllElements();
        serviceJournalModel.removeAllElements();

        if (copyFrom != null) {
            MealOrders mealOrders = copyFrom.getMealOrders();

            Account mealOrderServiceAccount = mealOrders.getMealOrderServiceAccount();
            Account mealOrderRevenueAccount = mealOrders.getMealOrderRevenueAccount();
            Account mealOrderBalanceAccount = mealOrders.getMealOrderBalanceAccount();

            Journal mealOrderSalesJournal = mealOrders.getMealOrderSalesJournal();
            Journal mealOrderServiceJournal = mealOrders.getMealOrderServiceJournal();

            Accounts accounts = accounting.getAccounts();
            Journals journals = accounting.getJournals();

            if (accounts != null) {
                accounts.getBusinessObjects().forEach(account -> {
                    serviceAccountModel.addElement(account);
                    salesAccountModel.addElement(account);
                    balanceAccountModel.addElement(account);
                });
            }
            if (journals != null) {
                journals.getBusinessObjects().forEach(journal -> {
                    salesJournalModel.addElement(journal);
                    serviceJournalModel.addElement(journal);
                });
            }

            if (mealOrderServiceAccount != null) {
                Account account = accounts.getBusinessObject(mealOrderServiceAccount.getName());
                mealOrders.setMealOrderServiceAccount(account);
                serviceAccountSelection.setSelectedItem(account);
            } else {
                mealOrders.setMealOrderServiceAccount(null);
                serviceAccountSelection.setSelectedItem(null);
            }

            if (mealOrderRevenueAccount != null) {
                Account account = accounts.getBusinessObject(mealOrderRevenueAccount.getName());
                mealOrders.setMealOrderRevenueAccount(account);
                revenueAccountSelection.setSelectedItem(account);
            } else {
                mealOrders.setMealOrderRevenueAccount(null);
                revenueAccountSelection.setSelectedItem(null);
            }

            if (mealOrderBalanceAccount != null) {
                Account account = accounts.getBusinessObject(mealOrderBalanceAccount.getName());
                mealOrders.setMealOrderBalanceAccount(account);
                balanceAccountSelection.setSelectedItem(account);
            } else {
                mealOrders.setMealOrderBalanceAccount(null);
                balanceAccountSelection.setSelectedItem(null);
            }

            if (mealOrderSalesJournal != null) {
                Journal journal = journals.getBusinessObject(mealOrderSalesJournal.getName());
                mealOrders.setMealOrderSalesJournal(journal);
                salesJournalSelection.setSelectedItem(journal);
            } else {
                mealOrders.setMealOrderSalesJournal(null);
                salesJournalSelection.setSelectedItem(null);
            }

            if (mealOrderServiceJournal != null) {
                Journal journal = journals.getBusinessObject(mealOrderServiceJournal.getName());
                mealOrders.setMealOrderServiceJournal(journal);
                serviceJournalSelection.setSelectedItem(journal);
            } else {
                mealOrders.setMealOrderServiceJournal(null);
                serviceJournalSelection.setSelectedItem(null);
            }
        } else {
            serviceAccountModel.setSelectedItem(null);
            salesAccountModel.setSelectedItem(null);
            balanceAccountModel.setSelectedItem(null);

            salesJournalModel.setSelectedItem(null);
            serviceJournalSelection.setSelectedItem(null);
        }
    }
}
