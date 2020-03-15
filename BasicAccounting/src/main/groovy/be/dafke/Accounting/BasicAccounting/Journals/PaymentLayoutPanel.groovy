package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BasicAccounting.Accounts.AccountActions
import be.dafke.Accounting.BasicAccounting.Journals.Edit.JournalEditPanel
import be.dafke.Accounting.BasicAccounting.Mortgages.MortgagesPanel
import be.dafke.Accounting.BasicAccounting.Trade.StockUtils
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Journal
import be.dafke.Accounting.BusinessModel.Mortgage
import be.dafke.Accounting.BusinessModel.Mortgages

import javax.swing.*
import java.awt.*

class PaymentLayoutPanel extends JPanel {

    OpenCustomerInvoicesPanel openCustomerInvoices
    OpenSupplierInvoicesPanel openSupplierInvoices
    Accounting accounting
    Journal journal
    JTextField accountNameField
    JPanel accountPanel

    MortgagesPanel mortgagesPanel

    PaymentLayoutPanel(JournalEditPanel journalEditPanel) {

        openCustomerInvoices = new OpenCustomerInvoicesPanel()
        openSupplierInvoices = new OpenSupplierInvoicesPanel()
        mortgagesPanel = new MortgagesPanel(journalEditPanel)

        JPanel boxPanel = new JPanel()
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS))
        boxPanel.add openCustomerInvoices
        boxPanel.add openSupplierInvoices
//        if(mortgagesPanel)boxPanel.add mortgagesPanel

        accountPanel = new JPanel()
        accountPanel.add new JLabel('Account to book against: ')
        accountNameField = new JTextField(20)
        accountNameField.editable = false
        accountPanel.add accountNameField
        JButton accountPay = new JButton("Pay")
        JButton accountReceive = new JButton("Receive")
        accountPanel.add accountPay
        accountPanel.add accountReceive
        accountPay.addActionListener({ e ->
            def account = StockUtils.getJournalBaseAccount(journal)
            AccountActions.book account, false, null, accounting, this
        })
        accountReceive.addActionListener({ e ->
            def account = StockUtils.getJournalBaseAccount(journal)
            AccountActions.book account, true, null, accounting, this
        })

        setLayout(new BorderLayout())

        add(mortgagesPanel, BorderLayout.SOUTH)
        add(boxPanel, BorderLayout.CENTER)
        add(accountPanel, BorderLayout.NORTH)
    }

    void setAccounting(Accounting accounting){
        this.accounting = accounting
        mortgagesPanel.setMortgages(accounting?.mortgages)
        openCustomerInvoices.accounting = accounting
        openSupplierInvoices.accounting = accounting
    }

    void setJournal(Journal journal){
        this.journal = journal
        accountNameField.setText(journal?.baseAccount?.name)
    }

    void fireShowInputChanged(boolean enabled) {
        mortgagesPanel.visible = enabled
        openCustomerInvoices.visible = enabled
        openSupplierInvoices.visible = enabled
        accountPanel.visible = enabled
    }

    void setMortgages(Mortgages mortgages) {
        mortgagesPanel.setMortgages(mortgages)
    }

    void enableMortgagePayButton(Mortgage mortgage) {
        mortgagesPanel.enablePayButton(mortgage)
    }
}
