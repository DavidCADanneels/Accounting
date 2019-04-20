package be.dafke.BasicAccounting.Trade;

import be.dafke.BasicAccounting.Accounts.AccountSelectorDialog;
import be.dafke.BasicAccounting.Journals.JournalSelectorDialog;
import be.dafke.BusinessModel.*;

import java.util.ArrayList;

public class StockUtils {
    public static Account getStockAccount(Accounting accounting){
        StockTransactions stockTransactions = accounting.getStockTransactions();
        Account stockAccount = stockTransactions.getStockAccount();
        if (stockAccount == null){
            AccountType accountType = accounting.getAccountTypes().getBusinessObject(AccountTypes.ASSET);
            ArrayList<AccountType> list = new ArrayList<>();
            list.add(accountType);
            AccountSelectorDialog dialog = new AccountSelectorDialog(accounting.getAccounts(), list, "Select Stock Account");
            dialog.setVisible(true);
            stockAccount = dialog.getSelection();
            stockTransactions.setStockAccount(stockAccount);
        }
        return stockAccount;
    }

    public static Account getGainAccount(Accounting accounting){
        StockTransactions stockTransactions = accounting.getStockTransactions();
        Account account = stockTransactions.getGainAccount();
        if (account == null){
            AccountType accountType = accounting.getAccountTypes().getBusinessObject(AccountTypes.REVENUE);
            ArrayList<AccountType> list = new ArrayList<>();
            list.add(accountType);
            AccountSelectorDialog dialog = new AccountSelectorDialog(accounting.getAccounts(), list, "Select Gain Account");
            dialog.setVisible(true);
            account = dialog.getSelection();
            stockTransactions.setGainAccount(account);
        }
        return account;
    }

    public static Account getSalesAccount(Accounting accounting){
        StockTransactions stockTransactions = accounting.getStockTransactions();
        Account account = stockTransactions.getSalesAccount();
        if (account == null){
            AccountType accountType = accounting.getAccountTypes().getBusinessObject(AccountTypes.REVENUE);
            ArrayList<AccountType> list = new ArrayList<>();
            list.add(accountType);
            AccountSelectorDialog dialog = new AccountSelectorDialog(accounting.getAccounts(), list, "Select Sales Account");
            dialog.setVisible(true);
            account = dialog.getSelection();
            stockTransactions.setSalesAccount(account);
        }
        return account;
    }

    public static Account getSalesGainAccount(Accounting accounting){
        StockTransactions stockTransactions = accounting.getStockTransactions();
        Account account = stockTransactions.getSalesGainAccount();
        if (account == null){
            AccountType accountType = accounting.getAccountTypes().getBusinessObject(AccountTypes.ASSET);
            ArrayList<AccountType> list = new ArrayList<>();
            list.add(accountType);
            AccountSelectorDialog dialog = new AccountSelectorDialog(accounting.getAccounts(), list, "Select Sales Gain Account");
            dialog.setVisible(true);
            account = dialog.getSelection();
            stockTransactions.setSalesGainAccount(account);
        }
        return account;
    }

    public static Account getCustomerAccount(Contact customer, Accounting accounting){
        Account customerAccount = customer.getCustomerAccount();
        if (customerAccount == null){
            AccountType accountType = accounting.getAccountTypes().getBusinessObject(AccountTypes.CREDIT);
            ArrayList<AccountType> list = new ArrayList<>();
            list.add(accountType);
            AccountSelectorDialog dialog = new AccountSelectorDialog(accounting.getAccounts(), list, "Select Customer Account");
            dialog.setVisible(true);
            customerAccount = dialog.getSelection();
            customer.setCustomerAccount(customerAccount);
        }
        return customerAccount;
    }

    public static Account getSupplierAccount(Contact supplier, Accounting accounting){
        Account supplierAccount = supplier.getSupplierAccount();
        if (supplierAccount == null){
            AccountType accountType = accounting.getAccountTypes().getBusinessObject(AccountTypes.DEBIT);
            ArrayList<AccountType> list = new ArrayList<>();
            list.add(accountType);
            AccountSelectorDialog dialog = new AccountSelectorDialog(accounting.getAccounts(), list, "Select Supplier Account");
            dialog.setVisible(true);
            supplierAccount = dialog.getSelection();
            supplier.setSupplierAccount(supplierAccount);
        }
        return supplierAccount;
    }

    public static Account getPromoAccount(Accounting accounting){
        StockTransactions stockTransactions = accounting.getStockTransactions();
        Account promoAccount = stockTransactions.getPromoAccount();
        if (promoAccount == null){
            AccountType accountType = accounting.getAccountTypes().getBusinessObject(AccountTypes.COST);
            ArrayList<AccountType> list = new ArrayList<>();
            list.add(accountType);
            AccountSelectorDialog dialog = new AccountSelectorDialog(accounting.getAccounts(), list, "Select Promo Account");
            dialog.setVisible(true);
            promoAccount = dialog.getSelection();
            stockTransactions.setPromoAccount(promoAccount);
        }
        return promoAccount;
    }

    public static Journal getSalesJournal(Accounting accounting){
        StockTransactions stockTransactions = accounting.getStockTransactions();
        Journal salesJournal = stockTransactions.getSalesJournal();
        if (salesJournal == null) {
            salesJournal = setSalesJournal(accounting);
        }
        return salesJournal;
    }

    private static Journal setSalesJournal(Accounting accounting){
        StockTransactions stockTransactions = accounting.getStockTransactions();
        JournalSelectorDialog journalSelectorDialog = new JournalSelectorDialog(accounting.getJournals());
        journalSelectorDialog.setTitle("Select Sales Journal");
        journalSelectorDialog.setVisible(true);
        Journal journal = journalSelectorDialog.getSelection();
        stockTransactions.setSalesJournal(journal);
        return journal;
    }

    public static Journal getSalesNoInvoiceJournal(Accounting accounting){
        StockTransactions stockTransactions = accounting.getStockTransactions();
        Journal salesNoInvoiceJournal = stockTransactions.getSalesNoInvoiceJournal();
        if (salesNoInvoiceJournal == null) {
            salesNoInvoiceJournal = setSalesNoInvoiceJournal(accounting);
        }
        return salesNoInvoiceJournal;
    }

    private static Journal setSalesNoInvoiceJournal(Accounting accounting) {
        StockTransactions stockTransactions = accounting.getStockTransactions();
        JournalSelectorDialog journalSelectorDialog = new JournalSelectorDialog(accounting.getJournals());
        journalSelectorDialog.setTitle("Select Sales (No Invoice) Journal");
        journalSelectorDialog.setVisible(true);
        Journal journal = journalSelectorDialog.getSelection();
        stockTransactions.setSalesNoInvoiceJournal(journal);
        return journal;
    }

    public static Journal getGainJournal(Accounting accounting){
        StockTransactions stockTransactions = accounting.getStockTransactions();
        Journal gainJournal = stockTransactions.getGainJournal();
        if (gainJournal == null) {
            gainJournal = setGainJournal(accounting);
        }
        return gainJournal;
    }

    private static Journal setGainJournal(Accounting accounting){
        StockTransactions stockTransactions = accounting.getStockTransactions();
        JournalSelectorDialog journalSelectorDialog = new JournalSelectorDialog(accounting.getJournals());
        journalSelectorDialog.setTitle("Select Gain Journal");
        journalSelectorDialog.setVisible(true);
        Journal journal = journalSelectorDialog.getSelection();
        stockTransactions.setGainJournal(journal);
        return journal;
    }

    public static Journal getPurchaseJournal(Accounting accounting){
        StockTransactions stockTransactions = accounting.getStockTransactions();
        Journal purchaseJournal = stockTransactions.getPurchaseJournal();
        if (purchaseJournal == null) {
            purchaseJournal = setPurchaseJournal(accounting);
        }
        return purchaseJournal;
    }

    private static Journal setPurchaseJournal(Accounting accounting){
        StockTransactions stockTransactions = accounting.getStockTransactions();
        JournalSelectorDialog journalSelectorDialog = new JournalSelectorDialog(accounting.getJournals());
        journalSelectorDialog.setTitle("Select Purchase Journal");
        journalSelectorDialog.setVisible(true);
        Journal journal = journalSelectorDialog.getSelection();
        stockTransactions.setPurchaseJournal(journal);
        return journal;
    }
}
