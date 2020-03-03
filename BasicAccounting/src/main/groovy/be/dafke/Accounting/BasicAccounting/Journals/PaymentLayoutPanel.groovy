package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BasicAccounting.Accounts.AccountActions
import be.dafke.Accounting.BasicAccounting.Journals.Edit.JournalEditPanel
import be.dafke.Accounting.BasicAccounting.Journals.Selector.JournalSelectorPanel
import be.dafke.Accounting.BasicAccounting.Journals.View.JournalSwitchViewPanel
import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BasicAccounting.Mortgages.MortgagesPanel
import be.dafke.Accounting.BusinessModel.*

import javax.swing.*
import java.awt.*

import static javax.swing.JSplitPane.VERTICAL_SPLIT

class PaymentLayoutPanel extends JPanel {

    JournalSelectorPanel journalSelectorPanel
    JournalEditPanel journalEditPanel
    JournalSwitchViewPanel journalSwitchViewPanel

    OpenCustomerInvoicesPanel openCustomerInvoices
    OpenSupplierInvoicesPanel openSupplierInvoices
    MortgagesPanel mortgagesPanel
    JSplitPane journalViewAndEditSplitPane
    Accounting accounting
    Journal journal

    PaymentLayoutPanel() {

        journalEditPanel = new JournalEditPanel()
        journalSelectorPanel = new JournalSelectorPanel(journalEditPanel)
        journalSwitchViewPanel = new JournalSwitchViewPanel()

        openCustomerInvoices = new OpenCustomerInvoicesPanel()
        openSupplierInvoices = new OpenSupplierInvoicesPanel()
        mortgagesPanel = new MortgagesPanel(journalEditPanel)

        JPanel boxPanel = new JPanel()
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS))
        boxPanel.add openCustomerInvoices
        boxPanel.add openSupplierInvoices
//        boxPanel.add mortgagesPanel

        JPanel accountPanel = new JPanel()
        accountPanel.add new JLabel('Account to book against: ')
        JTextField textField = new JTextField(20)
        textField.editable = false
        accountPanel.add textField
        JButton accountPay = new JButton("Pay")
        JButton accountReceive = new JButton("Receive")
        accountPanel.add accountPay
        accountPanel.add accountReceive
        accountPay.addActionListener({e->
            def account = journal.baseAccount
            if(account == null){
                println 'TODO: ask user to set journal.baseAccount'
            }
            AccountActions.book account, false, null, accounting, this
        })
        accountReceive.addActionListener({e->
            def account = journal.baseAccount
            if(account == null){
                println 'TODO: ask user to set journal.baseAccount'
            }
            AccountActions.book account, true, null, accounting, this
        })

//        JPanel south = new JPanel()
//        south.layout = new BoxLayout(south, BoxLayout.Y_AXIS)
//        south.add accountPanel
//        south.add mortgagesPanel

        JPanel links = new JPanel()
        links.setLayout(new BorderLayout())
        links.add(mortgagesPanel, BorderLayout.SOUTH)
        links.add(boxPanel, BorderLayout.CENTER)
        links.add(accountPanel, BorderLayout.NORTH)

        setLayout(new BorderLayout())
        journalViewAndEditSplitPane = Main.createSplitPane(journalSwitchViewPanel, journalEditPanel, VERTICAL_SPLIT)

        JPanel centerPanel = new JPanel(new BorderLayout())
        centerPanel.add(journalViewAndEditSplitPane, BorderLayout.CENTER)
        centerPanel.add(journalSelectorPanel, BorderLayout.NORTH)

//        add(accountGuiRight, BorderLayout.EAST)
        add(centerPanel, BorderLayout.CENTER)
        add(links, BorderLayout.WEST)
    }

    void setAccounting(Accounting accounting){
        this.accounting = accounting
        mortgagesPanel.setMortgages(accounting?accounting.mortgages:null)
        journalSwitchViewPanel.accounting = accounting
        journalEditPanel.accounting = accounting
        journalSelectorPanel.accounting = accounting
        openCustomerInvoices.accounting = accounting
        openSupplierInvoices.accounting = accounting
    }

    void setJournal(Journal journal){
        this.journal = journal
        journalSelectorPanel.journal = journal
        journalEditPanel.journal = journal
        journalSwitchViewPanel.journal = journal
    }

    void fireShowInputChanged(boolean enabled) {
        mortgagesPanel.visible = enabled
        journalEditPanel.visible = enabled
    }

    void setMortgages(Mortgages mortgages) {
        mortgagesPanel.setMortgages(mortgages)
    }

    void enableMortgagePayButton(Mortgage mortgage) {
        mortgagesPanel.enablePayButton(mortgage)
    }

    void setJournals(Journals journals) {
        journalSelectorPanel.setJournals(journals)
    }

    void fireJournalDataChanged(){
        journalSwitchViewPanel.fireJournalDataChanged()
    }
    void selectTransaction(Transaction transaction){
        journalSwitchViewPanel.selectTransaction(transaction)
    }

    // TODO: move to abstract parent:

    void fireTransactionInputDataChanged(){
        journalEditPanel.fireTransactionDataChanged()
    }

    void editTransaction(Transaction transaction){
        journalEditPanel.editTransaction(transaction)
    }

    void deleteBookings(ArrayList<Booking> bookings){
        journalEditPanel.deleteBookings(bookings)
    }

    void deleteTransactions(Set<Transaction> transactions){
        journalEditPanel.deleteTransactions(transactions)
    }

    void moveBookings(ArrayList<Booking> bookings, Journals journals){
        journalEditPanel.moveBookings(bookings, journals)
    }

    void moveTransactions(Set<Transaction> bookings, Journals journals){
        journalEditPanel.moveTransaction(bookings, journals)
    }

    Transaction getTransaction(){
        journalEditPanel.transaction
    }

    void addBooking(Booking booking){
        journalEditPanel.addBooking(booking)
    }
}
