package be.dafke.BasicAccounting.Deliveroo;

import be.dafke.BusinessModel.*;

import javax.swing.*;
import java.awt.*;

/**
 * Created by ddanneels on 3/03/2017.
 */
public class DeliverooSettingsPanel extends JPanel {
    private Accounting accounting;
    private final JComboBox<Account> serviceAccountSelection, revenueAccountSelection, balanceAccountSelection;
    private final JComboBox<Journal> salesJournalSelection, serviceJournalSelection;
    private final DefaultComboBoxModel<Account> serviceAccountModel, salesAccountModel, balanceAccountModel;
    private final DefaultComboBoxModel<Journal> salesJournalModel, serviceJournalModel;

    public DeliverooSettingsPanel(Accounting accounting) {
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

        Account serviceAccount = mealOrders.getDeliverooServiceAccount();
        Account revenueAccount = mealOrders.getDeliverooRevenueAccount();
        Account balanceAccount = mealOrders.getDeliverooBalanceAccount();

        Journal salesJournal = mealOrders.getDeliverooSalesJournal();
        Journal serviceJournal = mealOrders.getDeliverooServiceJournal();

        serviceAccountSelection.setSelectedItem(serviceAccount);
        serviceAccountSelection.addActionListener(e -> updateSelectedServiceAccount());
        serviceAccountSelection.setEnabled(accounting.isDeliverooAccounting());

        revenueAccountSelection.setSelectedItem(revenueAccount);
        revenueAccountSelection.addActionListener(e -> updateSelectedRevenueAccount());
        revenueAccountSelection.setEnabled(accounting.isDeliverooAccounting());

        balanceAccountSelection.setSelectedItem(balanceAccount);
        balanceAccountSelection.addActionListener(e -> updateSelectedBalanceAccount());
        balanceAccountSelection.setEnabled(accounting.isDeliverooAccounting());

        salesJournalSelection.setSelectedItem(salesJournal);
        salesJournalSelection.addActionListener(e -> updateSelectedSalesJournal());
        salesJournalSelection.setEnabled(accounting.isDeliverooAccounting());

        serviceJournalSelection.setSelectedItem(serviceJournal);
        serviceJournalSelection.addActionListener(e -> updateSelectedServiceJournal());
        serviceJournalSelection.setEnabled(accounting.isDeliverooAccounting());

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
        mealOrders.setDeliverooServiceAccount(account);
    }

    public void updateSelectedRevenueAccount() {
        Account account = (Account) revenueAccountSelection.getSelectedItem();
        MealOrders mealOrders = accounting.getMealOrders();
        mealOrders.setDeliverooRevenueAccount(account);
    }

    public void updateSelectedBalanceAccount() {
        Account account = (Account) balanceAccountSelection.getSelectedItem();
        MealOrders mealOrders = accounting.getMealOrders();
        mealOrders.setDeliverooBalanceAccount(account);
    }

    public void updateSelectedSalesJournal() {
        Journal journal = (Journal) salesJournalSelection.getSelectedItem();
        MealOrders mealOrders = accounting.getMealOrders();
        mealOrders.setDeliverooSalesJournal(journal);
    }

    public void updateSelectedServiceJournal() {
        Journal journal = (Journal) serviceJournalSelection.getSelectedItem();
        MealOrders mealOrders = accounting.getMealOrders();
        mealOrders.setDeliverooServiceJournal(journal);
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
}
