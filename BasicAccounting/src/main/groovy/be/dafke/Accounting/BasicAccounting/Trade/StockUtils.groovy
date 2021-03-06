package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BasicAccounting.Accounts.AccountSelectorDialog
import be.dafke.Accounting.BasicAccounting.Journals.JournalSelectorDialog
import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.AccountType
import be.dafke.Accounting.BusinessModel.AccountTypes
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Contact
import be.dafke.Accounting.BusinessModel.Journal
import be.dafke.Accounting.BusinessModel.StockTransactions

class StockUtils {
    static Account getStockAccount(Accounting accounting){
        StockTransactions stockTransactions = accounting.stockTransactions
        Account stockAccount = stockTransactions.stockAccount
        if (stockAccount == null){
            AccountType accountType = accounting.accountTypes.getBusinessObject(AccountTypes.ASSET)
            ArrayList<AccountType> list = new ArrayList<>()
            list.add(accountType)
            AccountSelectorDialog dialog = new AccountSelectorDialog(accounting.accounts, list, "Select Stock Account")
            dialog.visible = true
            stockAccount = dialog.getSelection()
            stockTransactions.setStockAccount(stockAccount)
        }
        stockAccount
    }

    static Account getGainAccount(Accounting accounting){
        StockTransactions stockTransactions = accounting.stockTransactions
        Account account = stockTransactions.gainAccount
        if (account == null){
            AccountType accountType = accounting.accountTypes.getBusinessObject(AccountTypes.REVENUE)
            ArrayList<AccountType> list = new ArrayList<>()
            list.add(accountType)
            AccountSelectorDialog dialog = new AccountSelectorDialog(accounting.accounts, list, "Select Gain Account")
            dialog.visible = true
            account = dialog.getSelection()
            stockTransactions.setGainAccount(account)
        }
        account
    }

    static Account getSalesAccount(Accounting accounting){
        StockTransactions stockTransactions = accounting.stockTransactions
        Account account = stockTransactions.salesAccount
        if (account == null){
            AccountType accountType = accounting.accountTypes.getBusinessObject(AccountTypes.REVENUE)
            ArrayList<AccountType> list = new ArrayList<>()
            list.add(accountType)
            AccountSelectorDialog dialog = new AccountSelectorDialog(accounting.accounts, list, "Select Sales Account")
            dialog.visible = true
            account = dialog.getSelection()
            stockTransactions.setSalesAccount(account)
        }
        account
    }

    static Account getSalesGainAccount(Accounting accounting){
        StockTransactions stockTransactions = accounting.stockTransactions
        Account account = stockTransactions.salesGainAccount
        if (account == null){
            AccountType accountType = accounting.accountTypes.getBusinessObject(AccountTypes.ASSET)
            ArrayList<AccountType> list = new ArrayList<>()
            list.add(accountType)
            AccountSelectorDialog dialog = new AccountSelectorDialog(accounting.accounts, list, "Select Sales Gain Account")
            dialog.visible = true
            account = dialog.getSelection()
            stockTransactions.setSalesGainAccount(account)
        }
        account
    }

    static Account getCustomerAccount(Contact customer, Accounting accounting){
        Account customerAccount = customer.customerAccount
        if (customerAccount == null){
            AccountType accountType = accounting.accountTypes.getBusinessObject(AccountTypes.CREDIT)
            ArrayList<AccountType> list = new ArrayList<>()
            list.add(accountType)
            AccountSelectorDialog dialog = new AccountSelectorDialog(accounting.accounts, list, "Select Customer Account")
            dialog.visible = true
            customerAccount = dialog.getSelection()
            customer.setCustomerAccount(customerAccount)
        }
        customerAccount
    }

    static Account getSupplierAccount(Contact supplier, Accounting accounting){
        Account supplierAccount = supplier.supplierAccount
        if (supplierAccount == null){
            AccountType accountType = accounting.accountTypes.getBusinessObject(AccountTypes.DEBIT)
            ArrayList<AccountType> list = new ArrayList<>()
            list.add(accountType)
            AccountSelectorDialog dialog = new AccountSelectorDialog(accounting.accounts, list, "Select Supplier Account")
            dialog.visible = true
            supplierAccount = dialog.getSelection()
            supplier.setSupplierAccount(supplierAccount)
        }
        supplierAccount
    }

    static Account getPromoAccount(Accounting accounting){
        StockTransactions stockTransactions = accounting.stockTransactions
        Account promoAccount = stockTransactions.promoAccount
        if (promoAccount == null){
            AccountType accountType = accounting.accountTypes.getBusinessObject(AccountTypes.COST)
            ArrayList<AccountType> list = new ArrayList<>()
            list.add(accountType)
            AccountSelectorDialog dialog = new AccountSelectorDialog(accounting.accounts, list, "Select Promo Account")
            dialog.visible = true
            promoAccount = dialog.getSelection()
            stockTransactions.setPromoAccount(promoAccount)
        }
        promoAccount
    }

    static Journal getSalesJournal(Accounting accounting){
        StockTransactions stockTransactions = accounting.stockTransactions
        Journal salesJournal = stockTransactions.salesJournal
        if (salesJournal == null) {
            salesJournal = setSalesJournal(accounting)
        }
        salesJournal
    }

    static Journal setSalesJournal(Accounting accounting){
        StockTransactions stockTransactions = accounting.stockTransactions
        JournalSelectorDialog journalSelectorDialog = new JournalSelectorDialog(accounting.journals)
        journalSelectorDialog.setTitle("Select Sales Journal")
        journalSelectorDialog.visible = true
        Journal journal = journalSelectorDialog.getSelection()
        stockTransactions.setSalesJournal(journal)
        journal
    }

    static Journal getSalesNoInvoiceJournal(Accounting accounting){
        StockTransactions stockTransactions = accounting.stockTransactions
        Journal salesNoInvoiceJournal = stockTransactions.salesNoInvoiceJournal
        if (salesNoInvoiceJournal == null) {
            salesNoInvoiceJournal = setSalesNoInvoiceJournal(accounting)
        }
        salesNoInvoiceJournal
    }

    static Journal setSalesNoInvoiceJournal(Accounting accounting) {
        StockTransactions stockTransactions = accounting.stockTransactions
        JournalSelectorDialog journalSelectorDialog = new JournalSelectorDialog(accounting.journals)
        journalSelectorDialog.setTitle("Select Sales (No Invoice) Journal")
        journalSelectorDialog.visible = true
        Journal journal = journalSelectorDialog.getSelection()
        stockTransactions.setSalesNoInvoiceJournal(journal)
        journal
    }

    static Journal getGainJournal(Accounting accounting){
        StockTransactions stockTransactions = accounting.stockTransactions
        Journal gainJournal = stockTransactions.gainJournal
        if (gainJournal == null) {
            gainJournal = setGainJournal(accounting)
        }
        gainJournal
    }

    static Journal setGainJournal(Accounting accounting){
        StockTransactions stockTransactions = accounting.stockTransactions
        JournalSelectorDialog journalSelectorDialog = new JournalSelectorDialog(accounting.journals)
        journalSelectorDialog.setTitle("Select Gain Journal")
        journalSelectorDialog.visible = true
        Journal journal = journalSelectorDialog.getSelection()
        stockTransactions.setGainJournal(journal)
        journal
    }

    static Journal getPurchaseJournal(Accounting accounting){
        StockTransactions stockTransactions = accounting.stockTransactions
        Journal purchaseJournal = stockTransactions.purchaseJournal
        if (purchaseJournal == null) {
            purchaseJournal = setPurchaseJournal(accounting)
        }
        purchaseJournal
    }

    static Journal setPurchaseJournal(Accounting accounting){
        StockTransactions stockTransactions = accounting.stockTransactions
        JournalSelectorDialog journalSelectorDialog = new JournalSelectorDialog(accounting.journals)
        journalSelectorDialog.setTitle("Select Purchase Journal")
        journalSelectorDialog.visible = true
        Journal journal = journalSelectorDialog.getSelection()
        stockTransactions.setPurchaseJournal(journal)
        journal
    }
}
