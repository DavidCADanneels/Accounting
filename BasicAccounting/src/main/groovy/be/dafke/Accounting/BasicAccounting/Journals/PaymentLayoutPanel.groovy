package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BasicAccounting.Accounts.AccountActions
import be.dafke.Accounting.BasicAccounting.Journals.Edit.JournalEditPanel
import be.dafke.Accounting.BasicAccounting.Mortgages.MortgagesPanel
import be.dafke.Accounting.BasicAccounting.Trade.StockUtils
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Journal
import be.dafke.Accounting.BusinessModel.Mortgage

import javax.swing.*
import javax.swing.border.LineBorder
import javax.swing.border.TitledBorder
import java.awt.*

class PaymentLayoutPanel extends JPanel {

    OpenCustomerInvoicesPanel openCustomerInvoices
    UnpayedSupplierInvoicesPanel openSupplierInvoices
    Journal journal
    JTextField accountNameField
    JTextField saldoField
    JPanel accountPanel

    MortgagesPanel mortgagesPanel

    PaymentLayoutPanel(JournalEditPanel journalEditPanel) {

        openCustomerInvoices = new OpenCustomerInvoicesPanel()
        openSupplierInvoices = new UnpayedSupplierInvoicesPanel()
        mortgagesPanel = new MortgagesPanel(journalEditPanel)

        JPanel boxPanel = new JPanel()
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS))
        boxPanel.add openCustomerInvoices
        boxPanel.add openSupplierInvoices
//        if(mortgagesPanel)boxPanel.add mortgagesPanel

        JPanel line1 = new JPanel()
        line1.add new JLabel('Account to book against: ')
        accountNameField = new JTextField(15)
        accountNameField.editable = false
        saldoField = new JTextField(5)
        saldoField.editable = false
        line1.add accountNameField
        line1.add saldoField

        JPanel line2 = new JPanel()
        JButton accountPay = new JButton("Pay")
        JButton accountReceive = new JButton("Receive")
        line2.add accountPay
        line2.add accountReceive

        accountPanel = new JPanel()
        accountPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Payment Account"))
        accountPanel.setLayout(new BoxLayout(accountPanel, BoxLayout.Y_AXIS))
        accountPanel.add line1
        accountPanel.add line2

        accountPay.addActionListener({ e ->
            def account = StockUtils.getJournalBaseAccount(journal)
            AccountActions.book account, false, null, this
            refresh()
//            saldoField.setText(journal?.baseAccount?.saldo?.toString())
        })
        accountReceive.addActionListener({ e ->
            def account = StockUtils.getJournalBaseAccount(journal)
            AccountActions.book account, true, null, this
            refresh()
//            saldoField.setText(journal?.baseAccount?.saldo?.toString())
        })

        setLayout(new BorderLayout())

        add(mortgagesPanel, BorderLayout.SOUTH)
        add(boxPanel, BorderLayout.CENTER)
        add(accountPanel, BorderLayout.NORTH)
    }

    void setAccounting(Accounting accounting){
//        mortgagesPanel.setMortgages(accounting?.mortgages)
//        openCustomerInvoices.accounting = accounting
//        openSupplierInvoices.accounting = accounting
    }

    void setJournal(Journal journal){
        this.journal = journal
        accountNameField.setText(journal?.baseAccount?.name)
        refresh()
//        saldoField.setText(journal?.baseAccount?.saldo?.toString())
    }

//    void fireOrderPayed() {
//        openCustomerInvoices.fireOrderPayed()
//        openSupplierInvoices.fireOrderPayed()
//    }

//    void fireShowInputChanged(boolean enabled) {
//        mortgagesPanel.visible = enabled
//        openCustomerInvoices.visible = enabled
//        openSupplierInvoices.visible = enabled
//        accountPanel.visible = enabled
//    }

    void refresh() {
        saldoField.setText(journal?.baseAccount?.saldo?.toString())
        openCustomerInvoices.refresh()
        openSupplierInvoices.refresh()

        mortgagesPanel.refresh()
    }

    void enableMortgagePayButton(Mortgage mortgage) {
        mortgagesPanel.enablePayButton(mortgage)
    }
}
