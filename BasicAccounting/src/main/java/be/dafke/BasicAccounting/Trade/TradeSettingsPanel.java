package be.dafke.BasicAccounting.Trade;

import be.dafke.BusinessModel.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by ddanneels on 3/03/2017.
 */
public class TradeSettingsPanel extends JPanel {
    private Accounting accounting;
    private final JComboBox<Account> stockAccountSelection, gainAccountSelection, salesAccountSelection, salesGainAccountSelection, promoAccountSelection;
    private final JComboBox<Journal> purchaseJournalSelection, salesJournalSelection, salesNoInvoiceSelection, gainJournalSelection;
    private final DefaultComboBoxModel<Account> stockAccountModel, gainAccountModel, salesAccountModel, salesGainAccountModel, promoAccountModel;
    private final DefaultComboBoxModel<Journal> purchaseJournalModel, salesJournalModel, salesNoInvoiceModel, gainJournalModel;

    public TradeSettingsPanel(Accounting accounting) {
        this.accounting = accounting;
        stockAccountModel = new DefaultComboBoxModel<>();
        gainAccountModel = new DefaultComboBoxModel<>();
        salesAccountModel = new DefaultComboBoxModel<>();
        salesGainAccountModel = new DefaultComboBoxModel<>();
        promoAccountModel = new DefaultComboBoxModel<>();

        purchaseJournalModel = new DefaultComboBoxModel<>();
        salesJournalModel = new DefaultComboBoxModel<>();
        salesNoInvoiceModel = new DefaultComboBoxModel<>();
        gainJournalModel = new DefaultComboBoxModel<>();

        // TODO: add only relevant account(type)s, e.g. COST, REVENUE, ...
        accounting.getAccounts().getBusinessObjects().forEach(account -> {
            stockAccountModel.addElement(account);
            gainAccountModel.addElement(account);
            salesAccountModel.addElement(account);
            salesGainAccountModel.addElement(account);
            promoAccountModel.addElement(account);
        });

        // TODO: add only relevant journal(type)s, e.g. SALES, PURCHASE, PAYMENT, GAIN
        accounting.getJournals().getBusinessObjects().forEach(journal -> {
            purchaseJournalModel.addElement(journal);
            salesJournalModel.addElement(journal);
            salesNoInvoiceModel.addElement(journal);
            gainJournalModel.addElement(journal);
        });

        stockAccountSelection = new JComboBox<>(stockAccountModel);
        gainAccountSelection = new JComboBox<>(gainAccountModel);
        salesAccountSelection = new JComboBox<>(salesAccountModel);
        salesGainAccountSelection = new JComboBox<>(salesGainAccountModel);
        promoAccountSelection = new JComboBox<>(promoAccountModel);

        purchaseJournalSelection = new JComboBox<>(purchaseJournalModel);
        salesJournalSelection = new JComboBox<>(salesJournalModel);
        salesNoInvoiceSelection = new JComboBox<>(salesNoInvoiceModel);
        gainJournalSelection = new JComboBox<>(gainJournalModel);

        StockTransactions stockTransactions = accounting.getStockTransactions();

        Account stockAccount = stockTransactions.getStockAccount();
        Account gainAccount = stockTransactions.getGainAccount();
        Account salesAccount = stockTransactions.getSalesAccount();
        Account salesGainAccount = stockTransactions.getSalesGainAccount();
        Account promoAccount = stockTransactions.getPromoAccount();

        Journal purchaseJournal = stockTransactions.getPurchaseJournal();
        Journal salesJournal = stockTransactions.getSalesJournal();
        Journal salesNoInvoiceJournal = stockTransactions.getSalesNoInvoiceJournal();
        Journal gainJournal = stockTransactions.getGainJournal();

        stockAccountSelection.setSelectedItem(stockAccount);
        stockAccountSelection.addActionListener(e -> updateSelectedStockAccount());
        stockAccountSelection.setEnabled(accounting.isTradeAccounting());

        gainAccountSelection.setSelectedItem(gainAccount);
        gainAccountSelection.addActionListener(e -> updateSelectedSalesGainAccount());
        gainAccountSelection.setEnabled(accounting.isTradeAccounting());

        salesAccountSelection.setSelectedItem(salesAccount);
        salesAccountSelection.addActionListener(e -> updateSelectedSalesAccount());
        salesAccountSelection.setEnabled(accounting.isTradeAccounting());

        salesGainAccountSelection.setSelectedItem(salesGainAccount);
        salesGainAccountSelection.addActionListener(e -> updateSelectedGainAccount());
        salesGainAccountSelection.setEnabled(accounting.isTradeAccounting());

        promoAccountSelection.setSelectedItem(promoAccount);
        promoAccountSelection.addActionListener(e -> updateSelectedPromoAccount());
        promoAccountSelection.setEnabled(accounting.isTradeAccounting());

        purchaseJournalSelection.setSelectedItem(purchaseJournal);
        purchaseJournalSelection.addActionListener(e -> updateSelectedPurchaseJournal());
        purchaseJournalSelection.setEnabled(accounting.isTradeAccounting());

        salesJournalSelection.setSelectedItem(salesJournal);
        salesJournalSelection.addActionListener(e -> updateSelectedSalesJournal());
        salesJournalSelection.setEnabled(accounting.isTradeAccounting());

        salesNoInvoiceSelection.setSelectedItem(salesNoInvoiceJournal);
        salesNoInvoiceSelection.addActionListener(e -> updateSelectedSalesNoInvoiceJournal());
        salesNoInvoiceSelection.setEnabled(accounting.isTradeAccounting());

        gainJournalSelection.setSelectedItem(gainJournal);
        gainJournalSelection.addActionListener(e -> updateSelectedGainJournal());
        gainJournalSelection.setEnabled(accounting.isTradeAccounting());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2));

        panel.add(new JLabel("Stock Account"));
        panel.add(stockAccountSelection);
        panel.add(new JLabel("Gain Account"));
        panel.add(gainAccountSelection);
        panel.add(new JLabel("Sales Account"));
        panel.add(salesAccountSelection);
        panel.add(new JLabel("Sales Gain Account"));
        panel.add(salesGainAccountSelection);
        panel.add(new JLabel("Sales Promo Account"));
        panel.add(promoAccountSelection);

        panel.add(new JLabel("Purchase Journal"));
        panel.add(purchaseJournalSelection);
        panel.add(new JLabel("Sales Journal"));
        panel.add(salesJournalSelection);
        panel.add(new JLabel("Sales (no invoice) Journal"));
        panel.add(salesNoInvoiceSelection);
        panel.add(new JLabel("Gain Journal"));
        panel.add(gainJournalSelection);

        add(panel);
    }

    public void updateSelectedStockAccount() {
        StockTransactions stockTransactions = accounting.getStockTransactions();
        Account account = (Account) stockAccountSelection.getSelectedItem();
        stockTransactions.setStockAccount(account);
    }

    public void updateSelectedGainAccount() {
        StockTransactions stockTransactions = accounting.getStockTransactions();
        Account account = (Account) gainAccountSelection.getSelectedItem();
        stockTransactions.setGainAccount(account);
    }

    public void updateSelectedSalesAccount() {
        StockTransactions stockTransactions = accounting.getStockTransactions();
        Account account = (Account) salesAccountSelection.getSelectedItem();
        stockTransactions.setSalesAccount(account);
    }

    public void updateSelectedSalesGainAccount() {
        StockTransactions stockTransactions = accounting.getStockTransactions();
        Account account = (Account) salesGainAccountSelection.getSelectedItem();
        stockTransactions.setSalesGainAccount(account);
    }

    public void updateSelectedPromoAccount() {
        StockTransactions stockTransactions = accounting.getStockTransactions();
        Account account = (Account) promoAccountSelection.getSelectedItem();
        stockTransactions.setPromoAccount(account);
    }

    public void updateSelectedPurchaseJournal() {
        StockTransactions stockTransactions = accounting.getStockTransactions();
        Journal journal = (Journal) purchaseJournalSelection.getSelectedItem();
        stockTransactions.setPurchaseJournal(journal);
    }

    public void updateSelectedSalesJournal() {
        StockTransactions stockTransactions = accounting.getStockTransactions();
        Journal journal = (Journal) salesJournalSelection.getSelectedItem();
        stockTransactions.setSalesJournal(journal);
    }

    public void updateSelectedSalesNoInvoiceJournal() {
        StockTransactions stockTransactions = accounting.getStockTransactions();
        Journal journal = (Journal) salesNoInvoiceSelection.getSelectedItem();
        stockTransactions.setSalesNoInvoiceJournal(journal);
    }

    public void updateSelectedGainJournal() {
        StockTransactions stockTransactions = accounting.getStockTransactions();
        Journal journal = (Journal) gainJournalSelection.getSelectedItem();
        stockTransactions.setGainJournal(journal);
    }


    @Override
    public void setEnabled(boolean enabled){
        super.setEnabled(enabled);
        stockAccountSelection.setEnabled(enabled);
        gainAccountSelection.setEnabled(enabled);
        salesAccountSelection.setEnabled(enabled);
        salesGainAccountSelection.setEnabled(enabled);
        promoAccountSelection.setEnabled(enabled);

        purchaseJournalSelection.setEnabled(enabled);
        salesJournalSelection.setEnabled(enabled);
        salesNoInvoiceSelection.setEnabled(enabled);
        gainJournalSelection.setEnabled(enabled);
        if(!enabled){
            stockAccountSelection.setSelectedItem(null);
            gainAccountSelection.setSelectedItem(null);
            salesAccountSelection.setSelectedItem(null);
            salesGainAccountSelection.setSelectedItem(null);
            promoAccountSelection.setSelectedItem(null);

            purchaseJournalSelection.setSelectedItem(null);
            salesJournalSelection.setSelectedItem(null);
            salesNoInvoiceSelection.setSelectedItem(null);
            gainJournalSelection.setSelectedItem(null);

            updateSelectedStockAccount();
            updateSelectedGainAccount();
            updateSelectedSalesAccount();
            updateSelectedSalesGainAccount();

            updateSelectedPurchaseJournal();
            updateSelectedSalesJournal();
            updateSelectedSalesNoInvoiceJournal();
            updateSelectedGainJournal();
        }
    }

    public void copyTradeSettings(Accounting copyFrom) {
        stockAccountModel.removeAllElements();
        gainAccountModel.removeAllElements();
        salesAccountModel.removeAllElements();
        salesGainAccountModel.removeAllElements();
        promoAccountModel.removeAllElements();

        purchaseJournalModel.removeAllElements();
        salesJournalModel.removeAllElements();
        salesNoInvoiceModel.removeAllElements();
        gainJournalModel.removeAllElements();

        if (copyFrom != null) {
            StockTransactions stockTransactions = copyFrom.getStockTransactions();

            Account stockAccount = stockTransactions.getStockAccount();
            Account gainAccount = stockTransactions.getGainAccount();
            Account salesAccount = stockTransactions.getSalesAccount();
            Account salesGainAccount = stockTransactions.getSalesGainAccount();
            Account promoAccount = stockTransactions.getPromoAccount();

            Journal salesJournal = stockTransactions.getSalesJournal();
            Journal purchaseJournal = stockTransactions.getPurchaseJournal();
            Journal salesNoInvoiceJournal = stockTransactions.getSalesNoInvoiceJournal();
            Journal gainJournal = stockTransactions.getGainJournal();

            Accounts accounts = accounting.getAccounts();
            Journals journals = accounting.getJournals();


            if (accounts != null) {
                accounts.getBusinessObjects().forEach(account -> {
                    stockAccountModel.addElement(account);
                    gainAccountModel.addElement(account);
                    salesAccountModel.addElement(account);
                    salesGainAccountModel.addElement(account);
                    promoAccountModel.addElement(account);
                });
            }
            if (journals != null) {
                journals.getBusinessObjects().forEach(journal -> {
                    purchaseJournalModel.addElement(journal);
                    salesJournalModel.addElement(journal);
                    salesNoInvoiceModel.addElement(journal);
                    gainJournalModel.addElement(journal);
                });
            }

            if (stockAccount != null) {
                Account account = accounts.getBusinessObject(stockAccount.getName());
                stockTransactions.setStockAccount(account);
                stockAccountSelection.setSelectedItem(account);
            } else {
                stockTransactions.setStockAccount(null);
                stockAccountSelection.setSelectedItem(null);
            }

            if (gainAccount != null) {
                Account account = accounts.getBusinessObject(gainAccount.getName());
                stockTransactions.setGainAccount(account);
                gainAccountSelection.setSelectedItem(account);
            } else {
                stockTransactions.setGainAccount(null);
                gainAccountSelection.setSelectedItem(null);
            }

            if (salesAccount != null) {
                Account account = accounts.getBusinessObject(salesAccount.getName());
                stockTransactions.setSalesAccount(account);
                salesAccountSelection.setSelectedItem(account);
            } else {
                stockTransactions.setSalesAccount(null);
                salesAccountSelection.setSelectedItem(null);
            }

            if (salesGainAccount != null) {
                Account account = accounts.getBusinessObject(salesGainAccount.getName());
                stockTransactions.setSalesGainAccount(account);
                salesGainAccountSelection.setSelectedItem(account);
            } else {
                stockTransactions.setSalesGainAccount(null);
                salesGainAccountSelection.setSelectedItem(null);
            }

            if (promoAccount != null) {
                Account account = accounts.getBusinessObject(promoAccount.getName());
                stockTransactions.setPromoAccount(account);
                promoAccountSelection.setSelectedItem(account);
            } else {
                stockTransactions.setPromoAccount(null);
                promoAccountSelection.setSelectedItem(null);
            }

            if (salesJournal != null) {
                Journal journal = journals.getBusinessObject(salesJournal.getName());
                stockTransactions.setSalesJournal(journal);
                purchaseJournalSelection.setSelectedItem(journal);
            } else {
                stockTransactions.setSalesJournal(null);
                purchaseJournalSelection.setSelectedItem(null);
            }

            if (purchaseJournal != null) {
                Journal journal = journals.getBusinessObject(purchaseJournal.getName());
                stockTransactions.setPurchaseJournal(journal);
                salesJournalSelection.setSelectedItem(journal);
            } else {
                stockTransactions.setPurchaseJournal(null);
                salesJournalSelection.setSelectedItem(null);
            }

            if (salesNoInvoiceJournal != null) {
                Journal journal = journals.getBusinessObject(salesNoInvoiceJournal.getName());
                stockTransactions.setSalesNoInvoiceJournal(journal);
                salesNoInvoiceSelection.setSelectedItem(journal);
            } else {
                stockTransactions.setSalesNoInvoiceJournal(null);
                salesNoInvoiceSelection.setSelectedItem(null);
            }

            if (gainJournal != null) {
                Journal journal = journals.getBusinessObject(gainJournal.getName());
                stockTransactions.setGainJournal(journal);
                gainJournalSelection.setSelectedItem(journal);
            } else {
                stockTransactions.setGainJournal(null);
                gainJournalSelection.setSelectedItem(null);
            }
        } else {
            stockAccountSelection.setSelectedItem(null);
            gainAccountSelection.setSelectedItem(null);
            salesAccountSelection.setSelectedItem(null);
            salesGainAccountSelection.setSelectedItem(null);
            promoAccountModel.setSelectedItem(null);

            purchaseJournalSelection.setSelectedItem(null);
            salesJournalSelection.setSelectedItem(null);
            salesNoInvoiceSelection.setSelectedItem(null);
            gainJournalSelection.setSelectedItem(null);
        }
    }
}
