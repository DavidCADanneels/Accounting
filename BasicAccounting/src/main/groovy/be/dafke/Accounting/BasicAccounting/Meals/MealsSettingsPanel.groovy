package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Accounts
import be.dafke.Accounting.BusinessModel.Journal
import be.dafke.Accounting.BusinessModel.Journals
import be.dafke.Accounting.BusinessModel.MealOrders

import javax.swing.DefaultComboBoxModel
import javax.swing.JComboBox
import javax.swing.JLabel
import javax.swing.JPanel
import java.awt.GridLayout

class MealsSettingsPanel extends JPanel {
    Accounting accounting
    final JComboBox<Account> serviceAccountSelection, revenueAccountSelection, balanceAccountSelection
    final JComboBox<Journal> salesJournalSelection, serviceJournalSelection
    final DefaultComboBoxModel<Account> serviceAccountModel, salesAccountModel, balanceAccountModel
    final DefaultComboBoxModel<Journal> salesJournalModel, serviceJournalModel

    MealsSettingsPanel(Accounting accounting) {
        this.accounting = accounting
        serviceAccountModel = new DefaultComboBoxModel<>()
        salesAccountModel = new DefaultComboBoxModel<>()
        balanceAccountModel = new DefaultComboBoxModel<>()

        salesJournalModel = new DefaultComboBoxModel<>()
        serviceJournalModel = new DefaultComboBoxModel<>()

        accounting.accounts.businessObjects.forEach({ account ->
            serviceAccountModel.addElement(account)
            salesAccountModel.addElement(account)
            balanceAccountModel.addElement(account)
        })

        accounting.journals.businessObjects.forEach({ journal ->
            salesJournalModel.addElement(journal)
            serviceJournalModel.addElement(journal)
        })

        serviceAccountSelection = new JComboBox<>(serviceAccountModel)
        revenueAccountSelection = new JComboBox<>(salesAccountModel)
        balanceAccountSelection = new JComboBox<>(balanceAccountModel)

        salesJournalSelection = new JComboBox<>(salesJournalModel)
        serviceJournalSelection = new JComboBox<>(serviceJournalModel)

        MealOrders mealOrders = accounting.mealOrders

        Account serviceAccount = mealOrders.getMealOrderServiceAccount()
        Account revenueAccount = mealOrders.getMealOrderRevenueAccount()
        Account balanceAccount = mealOrders.getMealOrderBalanceAccount()

        Journal salesJournal = mealOrders.getMealOrderSalesJournal()
        Journal serviceJournal = mealOrders.getMealOrderServiceJournal()

        serviceAccountSelection.setSelectedItem(serviceAccount)
        serviceAccountSelection.addActionListener({ e -> updateSelectedServiceAccount() })
        serviceAccountSelection.enabled = accounting.mealsAccounting

        revenueAccountSelection.setSelectedItem(revenueAccount)
        revenueAccountSelection.addActionListener({ e -> updateSelectedRevenueAccount() })
        revenueAccountSelection.enabled = accounting.mealsAccounting

        balanceAccountSelection.setSelectedItem(balanceAccount)
        balanceAccountSelection.addActionListener({ e -> updateSelectedBalanceAccount() })
        balanceAccountSelection.enabled = accounting.mealsAccounting

        salesJournalSelection.setSelectedItem(salesJournal)
        salesJournalSelection.addActionListener({ e -> updateSelectedSalesJournal() })
        salesJournalSelection.enabled = accounting.mealsAccounting

        serviceJournalSelection.setSelectedItem(serviceJournal)
        serviceJournalSelection.addActionListener({ e -> updateSelectedServiceJournal() })
        serviceJournalSelection.enabled = accounting.mealsAccounting

        JPanel panel = new JPanel()
        panel.setLayout(new GridLayout(0, 2))

        panel.add(new JLabel("Service Account"))
        panel.add(serviceAccountSelection)
        panel.add(new JLabel("Revenue Account"))
        panel.add(revenueAccountSelection)
        panel.add(new JLabel("Balance Account"))
        panel.add(balanceAccountSelection)
        panel.add(new JLabel("Sales Journal"))
        panel.add(salesJournalSelection)
        panel.add(new JLabel("Service Journal"))
        panel.add(serviceJournalSelection)

        add(panel)
    }

    void updateSelectedServiceAccount() {
        Account account = (Account) serviceAccountSelection.selectedItem
        MealOrders mealOrders = accounting.mealOrders
        mealOrders.setMealOrderServiceAccount(account)
    }

    void updateSelectedRevenueAccount() {
        Account account = (Account) revenueAccountSelection.selectedItem
        MealOrders mealOrders = accounting.mealOrders
        mealOrders.setMealOrderRevenueAccount(account)
    }

    void updateSelectedBalanceAccount() {
        Account account = (Account) balanceAccountSelection.selectedItem
        MealOrders mealOrders = accounting.mealOrders
        mealOrders.setMealOrderBalanceAccount(account)
    }

    void updateSelectedSalesJournal() {
        Journal journal = (Journal) salesJournalSelection.selectedItem
        MealOrders mealOrders = accounting.mealOrders
        mealOrders.setMealOrderSalesJournal(journal)
    }

    void updateSelectedServiceJournal() {
        Journal journal = (Journal) serviceJournalSelection.selectedItem
        MealOrders mealOrders = accounting.mealOrders
        mealOrders.setMealOrderServiceJournal(journal)
    }

    @Override
    void setEnabled(boolean enabled){
        super.setEnabled(enabled)
        serviceAccountSelection.enabled = enabled
        revenueAccountSelection.enabled = enabled
        balanceAccountSelection.enabled = enabled
        salesJournalSelection.enabled = enabled
        serviceJournalSelection.enabled = enabled
        if(!enabled){
            serviceAccountSelection.setSelectedItem(null)
            revenueAccountSelection.setSelectedItem(null)
            balanceAccountSelection.setSelectedItem(null)
            salesJournalSelection.setSelectedItem(null)
            serviceJournalSelection.setSelectedItem(null)
            updateSelectedServiceAccount()
            updateSelectedRevenueAccount()
            updateSelectedBalanceAccount()
            updateSelectedSalesJournal()
            updateSelectedServiceJournal()
        }
    }

    void copyMealOrderSettings(Accounting copyFrom) {
        serviceAccountModel.removeAllElements()
        salesAccountModel.removeAllElements()
        balanceAccountModel.removeAllElements()

        salesJournalModel.removeAllElements()
        serviceJournalModel.removeAllElements()

        if (copyFrom != null) {
            MealOrders mealOrders = copyFrom.mealOrders

            Account mealOrderServiceAccount = mealOrders.getMealOrderServiceAccount()
            Account mealOrderRevenueAccount = mealOrders.getMealOrderRevenueAccount()
            Account mealOrderBalanceAccount = mealOrders.getMealOrderBalanceAccount()

            Journal mealOrderSalesJournal = mealOrders.getMealOrderSalesJournal()
            Journal mealOrderServiceJournal = mealOrders.getMealOrderServiceJournal()

            Accounts accounts = accounting.accounts
            Journals journals = accounting.journals

            if (accounts != null) {
                accounts.businessObjects.forEach({ account ->
                    serviceAccountModel.addElement(account)
                    salesAccountModel.addElement(account)
                    balanceAccountModel.addElement(account)
                })
            }
            if (journals != null) {
                journals.businessObjects.forEach({ journal ->
                    salesJournalModel.addElement(journal)
                    serviceJournalModel.addElement(journal)
                })
            }

            if (mealOrderServiceAccount != null) {
                Account account = accounts.getBusinessObject(mealOrderServiceAccount.name)
                mealOrders.setMealOrderServiceAccount(account)
                serviceAccountSelection.setSelectedItem(account)
            } else {
                mealOrders.setMealOrderServiceAccount(null)
                serviceAccountSelection.setSelectedItem(null)
            }

            if (mealOrderRevenueAccount != null) {
                Account account = accounts.getBusinessObject(mealOrderRevenueAccount.name)
                mealOrders.setMealOrderRevenueAccount(account)
                revenueAccountSelection.setSelectedItem(account)
            } else {
                mealOrders.setMealOrderRevenueAccount(null)
                revenueAccountSelection.setSelectedItem(null)
            }

            if (mealOrderBalanceAccount != null) {
                Account account = accounts.getBusinessObject(mealOrderBalanceAccount.name)
                mealOrders.setMealOrderBalanceAccount(account)
                balanceAccountSelection.setSelectedItem(account)
            } else {
                mealOrders.setMealOrderBalanceAccount(null)
                balanceAccountSelection.setSelectedItem(null)
            }

            if (mealOrderSalesJournal != null) {
                Journal journal = journals.getBusinessObject(mealOrderSalesJournal.name)
                mealOrders.setMealOrderSalesJournal(journal)
                salesJournalSelection.setSelectedItem(journal)
            } else {
                mealOrders.setMealOrderSalesJournal(null)
                salesJournalSelection.setSelectedItem(null)
            }

            if (mealOrderServiceJournal != null) {
                Journal journal = journals.getBusinessObject(mealOrderServiceJournal.name)
                mealOrders.setMealOrderServiceJournal(journal)
                serviceJournalSelection.setSelectedItem(journal)
            } else {
                mealOrders.setMealOrderServiceJournal(null)
                serviceJournalSelection.setSelectedItem(null)
            }
        } else {
            serviceAccountModel.setSelectedItem(null)
            salesAccountModel.setSelectedItem(null)
            balanceAccountModel.setSelectedItem(null)

            salesJournalModel.setSelectedItem(null)
            serviceJournalSelection.setSelectedItem(null)
        }
    }
}
