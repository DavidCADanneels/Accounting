package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Accounts
import be.dafke.Accounting.BusinessModel.Journal
import be.dafke.Accounting.BusinessModel.Journals
import be.dafke.Accounting.BusinessModel.StockTransactions

import javax.swing.DefaultComboBoxModel
import javax.swing.JComboBox
import javax.swing.JLabel
import javax.swing.JPanel
import java.awt.GridLayout

class TradeSettingsPanel extends JPanel {
    Accounting accounting
    final JComboBox<Account> stockAccountSelection, gainAccountSelection, salesAccountSelection, salesGainAccountSelection, promoAccountSelection
    final JComboBox<Journal> purchaseJournalSelection, salesJournalSelection, salesNoInvoiceSelection, gainJournalSelection
    final DefaultComboBoxModel<Account> stockAccountModel, gainAccountModel, salesAccountModel, salesGainAccountModel, promoAccountModel
    final DefaultComboBoxModel<Journal> purchaseJournalModel, salesJournalModel, salesNoInvoiceModel, gainJournalModel

    TradeSettingsPanel(Accounting accounting) {
        this.accounting = accounting
        stockAccountModel = new DefaultComboBoxModel<>()
        gainAccountModel = new DefaultComboBoxModel<>()
        salesAccountModel = new DefaultComboBoxModel<>()
        salesGainAccountModel = new DefaultComboBoxModel<>()
        promoAccountModel = new DefaultComboBoxModel<>()

        purchaseJournalModel = new DefaultComboBoxModel<>()
        salesJournalModel = new DefaultComboBoxModel<>()
        salesNoInvoiceModel = new DefaultComboBoxModel<>()
        gainJournalModel = new DefaultComboBoxModel<>()

        // TODO: add only relevant account(type)s, e.g. COST, REVENUE, ...
        accounting.accounts.businessObjects.forEach({ account ->
            stockAccountModel.addElement(account)
            gainAccountModel.addElement(account)
            salesAccountModel.addElement(account)
            salesGainAccountModel.addElement(account)
            promoAccountModel.addElement(account)
        })

        // TODO: add only relevant journal(type)s, e.g. SALES, PURCHASE, PAYMENT, GAIN
        accounting.journals.businessObjects.forEach({ journal ->
            purchaseJournalModel.addElement(journal)
            salesJournalModel.addElement(journal)
            salesNoInvoiceModel.addElement(journal)
            gainJournalModel.addElement(journal)
        })

        stockAccountSelection = new JComboBox<>(stockAccountModel)
        gainAccountSelection = new JComboBox<>(gainAccountModel)
        salesAccountSelection = new JComboBox<>(salesAccountModel)
        salesGainAccountSelection = new JComboBox<>(salesGainAccountModel)
        promoAccountSelection = new JComboBox<>(promoAccountModel)

        purchaseJournalSelection = new JComboBox<>(purchaseJournalModel)
        salesJournalSelection = new JComboBox<>(salesJournalModel)
        salesNoInvoiceSelection = new JComboBox<>(salesNoInvoiceModel)
        gainJournalSelection = new JComboBox<>(gainJournalModel)

        StockTransactions stockTransactions = accounting.stockTransactions

        Account stockAccount = stockTransactions.stockAccount
        Account gainAccount = stockTransactions.gainAccount
        Account salesAccount = stockTransactions.salesAccount
        Account salesGainAccount = stockTransactions.salesGainAccount
        Account promoAccount = stockTransactions.promoAccount

        Journal purchaseJournal = stockTransactions.purchaseJournal
        Journal salesJournal = stockTransactions.salesJournal
        Journal salesNoInvoiceJournal = stockTransactions.salesNoInvoiceJournal
        Journal gainJournal = stockTransactions.gainJournal

        stockAccountSelection.setSelectedItem(stockAccount)
        stockAccountSelection.addActionListener({ e -> updateSelectedStockAccount() })
        stockAccountSelection.enabled = accounting.tradeAccounting

        gainAccountSelection.setSelectedItem(gainAccount)
        gainAccountSelection.addActionListener({ e -> updateSelectedSalesGainAccount() })
        gainAccountSelection.enabled = accounting.tradeAccounting

        salesAccountSelection.setSelectedItem(salesAccount)
        salesAccountSelection.addActionListener({ e -> updateSelectedSalesAccount() })
        salesAccountSelection.enabled = accounting.tradeAccounting

        salesGainAccountSelection.setSelectedItem(salesGainAccount)
        salesGainAccountSelection.addActionListener({ e -> updateSelectedGainAccount() })
        salesGainAccountSelection.enabled = accounting.tradeAccounting

        promoAccountSelection.setSelectedItem(promoAccount)
        promoAccountSelection.addActionListener({ e -> updateSelectedPromoAccount() })
        promoAccountSelection.enabled = accounting.tradeAccounting

        purchaseJournalSelection.setSelectedItem(purchaseJournal)
        purchaseJournalSelection.addActionListener({ e -> updateSelectedPurchaseJournal() })
        purchaseJournalSelection.enabled = accounting.tradeAccounting

        salesJournalSelection.setSelectedItem(salesJournal)
        salesJournalSelection.addActionListener({ e -> updateSelectedSalesJournal() })
        salesJournalSelection.enabled = accounting.tradeAccounting

        salesNoInvoiceSelection.setSelectedItem(salesNoInvoiceJournal)
        salesNoInvoiceSelection.addActionListener({ e -> updateSelectedSalesNoInvoiceJournal() })
        salesNoInvoiceSelection.enabled = accounting.tradeAccounting

        gainJournalSelection.setSelectedItem(gainJournal)
        gainJournalSelection.addActionListener({ e -> updateSelectedGainJournal() })
        gainJournalSelection.enabled = accounting.tradeAccounting

        JPanel panel = new JPanel()
        panel.setLayout(new GridLayout(0, 2))

        panel.add(new JLabel("Stock Account"))
        panel.add(stockAccountSelection)
        panel.add(new JLabel("Gain Account"))
        panel.add(gainAccountSelection)
        panel.add(new JLabel("Sales Account"))
        panel.add(salesAccountSelection)
        panel.add(new JLabel("Sales Gain Account"))
        panel.add(salesGainAccountSelection)
        panel.add(new JLabel("Sales Promo Account"))
        panel.add(promoAccountSelection)

        panel.add(new JLabel("Purchase Journal"))
        panel.add(purchaseJournalSelection)
        panel.add(new JLabel("Sales Journal"))
        panel.add(salesJournalSelection)
        panel.add(new JLabel("Sales (no invoice) Journal"))
        panel.add(salesNoInvoiceSelection)
        panel.add(new JLabel("Gain Journal"))
        panel.add(gainJournalSelection)

        add(panel)
    }

    void updateSelectedStockAccount() {
        StockTransactions stockTransactions = accounting.stockTransactions
        Account account = (Account) stockAccountSelection.selectedItem
        stockTransactions.setStockAccount(account)
    }

    void updateSelectedGainAccount() {
        StockTransactions stockTransactions = accounting.stockTransactions
        Account account = (Account) gainAccountSelection.selectedItem
        stockTransactions.setGainAccount(account)
    }

    void updateSelectedSalesAccount() {
        StockTransactions stockTransactions = accounting.stockTransactions
        Account account = (Account) salesAccountSelection.selectedItem
        stockTransactions.setSalesAccount(account)
    }

    void updateSelectedSalesGainAccount() {
        StockTransactions stockTransactions = accounting.stockTransactions
        Account account = (Account) salesGainAccountSelection.selectedItem
        stockTransactions.setSalesGainAccount(account)
    }

    void updateSelectedPromoAccount() {
        StockTransactions stockTransactions = accounting.stockTransactions
        Account account = (Account) promoAccountSelection.selectedItem
        stockTransactions.setPromoAccount(account)
    }

    void updateSelectedPurchaseJournal() {
        StockTransactions stockTransactions = accounting.stockTransactions
        Journal journal = (Journal) purchaseJournalSelection.selectedItem
        stockTransactions.setPurchaseJournal(journal)
    }

    void updateSelectedSalesJournal() {
        StockTransactions stockTransactions = accounting.stockTransactions
        Journal journal = (Journal) salesJournalSelection.selectedItem
        stockTransactions.setSalesJournal(journal)
    }

    void updateSelectedSalesNoInvoiceJournal() {
        StockTransactions stockTransactions = accounting.stockTransactions
        Journal journal = (Journal) salesNoInvoiceSelection.selectedItem
        stockTransactions.setSalesNoInvoiceJournal(journal)
    }

    void updateSelectedGainJournal() {
        StockTransactions stockTransactions = accounting.stockTransactions
        Journal journal = (Journal) gainJournalSelection.selectedItem
        stockTransactions.setGainJournal(journal)
    }


    @Override
    void setEnabled(boolean enabled){
        super.setEnabled(enabled)
        stockAccountSelection.enabled = enabled
        gainAccountSelection.enabled = enabled
        salesAccountSelection.enabled = enabled
        salesGainAccountSelection.enabled = enabled
        promoAccountSelection.enabled = enabled

        purchaseJournalSelection.enabled = enabled
        salesJournalSelection.enabled = enabled
        salesNoInvoiceSelection.enabled = enabled
        gainJournalSelection.enabled = enabled
        if(!enabled){
            stockAccountSelection.setSelectedItem(null)
            gainAccountSelection.setSelectedItem(null)
            salesAccountSelection.setSelectedItem(null)
            salesGainAccountSelection.setSelectedItem(null)
            promoAccountSelection.setSelectedItem(null)

            purchaseJournalSelection.setSelectedItem(null)
            salesJournalSelection.setSelectedItem(null)
            salesNoInvoiceSelection.setSelectedItem(null)
            gainJournalSelection.setSelectedItem(null)

            updateSelectedStockAccount()
            updateSelectedGainAccount()
            updateSelectedSalesAccount()
            updateSelectedSalesGainAccount()

            updateSelectedPurchaseJournal()
            updateSelectedSalesJournal()
            updateSelectedSalesNoInvoiceJournal()
            updateSelectedGainJournal()
        }
    }

    void copyTradeSettings(Accounting copyFrom) {
        stockAccountModel.removeAllElements()
        gainAccountModel.removeAllElements()
        salesAccountModel.removeAllElements()
        salesGainAccountModel.removeAllElements()
        promoAccountModel.removeAllElements()

        purchaseJournalModel.removeAllElements()
        salesJournalModel.removeAllElements()
        salesNoInvoiceModel.removeAllElements()
        gainJournalModel.removeAllElements()

        if (copyFrom != null) {
            StockTransactions stockTransactions = copyFrom.stockTransactions

            Account stockAccount = stockTransactions.stockAccount
            Account gainAccount = stockTransactions.gainAccount
            Account salesAccount = stockTransactions.salesAccount
            Account salesGainAccount = stockTransactions.salesGainAccount
            Account promoAccount = stockTransactions.promoAccount

            Journal salesJournal = stockTransactions.salesJournal
            Journal purchaseJournal = stockTransactions.purchaseJournal
            Journal salesNoInvoiceJournal = stockTransactions.salesNoInvoiceJournal
            Journal gainJournal = stockTransactions.gainJournal

            Accounts accounts = accounting.accounts
            Journals journals = accounting.journals


            if (accounts != null) {
                accounts.businessObjects.forEach({ account ->
                    stockAccountModel.addElement(account)
                    gainAccountModel.addElement(account)
                    salesAccountModel.addElement(account)
                    salesGainAccountModel.addElement(account)
                    promoAccountModel.addElement(account)
                })
            }
            if (journals != null) {
                journals.businessObjects.forEach({ journal ->
                    purchaseJournalModel.addElement(journal)
                    salesJournalModel.addElement(journal)
                    salesNoInvoiceModel.addElement(journal)
                    gainJournalModel.addElement(journal)
                })
            }

            if (stockAccount != null) {
                Account account = accounts.getBusinessObject(stockAccount.name)
                stockTransactions.setStockAccount(account)
                stockAccountSelection.setSelectedItem(account)
            } else {
                stockTransactions.setStockAccount(null)
                stockAccountSelection.setSelectedItem(null)
            }

            if (gainAccount != null) {
                Account account = accounts.getBusinessObject(gainAccount.name)
                stockTransactions.setGainAccount(account)
                gainAccountSelection.setSelectedItem(account)
            } else {
                stockTransactions.setGainAccount(null)
                gainAccountSelection.setSelectedItem(null)
            }

            if (salesAccount != null) {
                Account account = accounts.getBusinessObject(salesAccount.name)
                stockTransactions.setSalesAccount(account)
                salesAccountSelection.setSelectedItem(account)
            } else {
                stockTransactions.setSalesAccount(null)
                salesAccountSelection.setSelectedItem(null)
            }

            if (salesGainAccount != null) {
                Account account = accounts.getBusinessObject(salesGainAccount.name)
                stockTransactions.setSalesGainAccount(account)
                salesGainAccountSelection.setSelectedItem(account)
            } else {
                stockTransactions.setSalesGainAccount(null)
                salesGainAccountSelection.setSelectedItem(null)
            }

            if (promoAccount != null) {
                Account account = accounts.getBusinessObject(promoAccount.name)
                stockTransactions.setPromoAccount(account)
                promoAccountSelection.setSelectedItem(account)
            } else {
                stockTransactions.setPromoAccount(null)
                promoAccountSelection.setSelectedItem(null)
            }

            if (salesJournal != null) {
                Journal journal = journals.getBusinessObject(salesJournal.name)
                stockTransactions.setSalesJournal(journal)
                purchaseJournalSelection.setSelectedItem(journal)
            } else {
                stockTransactions.setSalesJournal(null)
                purchaseJournalSelection.setSelectedItem(null)
            }

            if (purchaseJournal != null) {
                Journal journal = journals.getBusinessObject(purchaseJournal.name)
                stockTransactions.setPurchaseJournal(journal)
                salesJournalSelection.setSelectedItem(journal)
            } else {
                stockTransactions.setPurchaseJournal(null)
                salesJournalSelection.setSelectedItem(null)
            }

            if (salesNoInvoiceJournal != null) {
                Journal journal = journals.getBusinessObject(salesNoInvoiceJournal.name)
                stockTransactions.setSalesNoInvoiceJournal(journal)
                salesNoInvoiceSelection.setSelectedItem(journal)
            } else {
                stockTransactions.setSalesNoInvoiceJournal(null)
                salesNoInvoiceSelection.setSelectedItem(null)
            }

            if (gainJournal != null) {
                Journal journal = journals.getBusinessObject(gainJournal.name)
                stockTransactions.setGainJournal(journal)
                gainJournalSelection.setSelectedItem(journal)
            } else {
                stockTransactions.setGainJournal(null)
                gainJournalSelection.setSelectedItem(null)
            }
        } else {
            stockAccountSelection.setSelectedItem(null)
            gainAccountSelection.setSelectedItem(null)
            salesAccountSelection.setSelectedItem(null)
            salesGainAccountSelection.setSelectedItem(null)
            promoAccountModel.setSelectedItem(null)

            purchaseJournalSelection.setSelectedItem(null)
            salesJournalSelection.setSelectedItem(null)
            salesNoInvoiceSelection.setSelectedItem(null)
            gainJournalSelection.setSelectedItem(null)
        }
    }
}
