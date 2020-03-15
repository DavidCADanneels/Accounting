package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BasicAccounting.Journals.Edit.JournalEditPanel
import be.dafke.Accounting.BasicAccounting.Journals.Selector.JournalSelectorPanel
import be.dafke.Accounting.BasicAccounting.Journals.View.JournalSwitchViewPanel
import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.*

import javax.swing.*
import java.awt.*

import static javax.swing.JSplitPane.VERTICAL_SPLIT

class JournalSwitchPanel extends JPanel {

    final JournalSelectorPanel journalSelectorPanel
    final JournalEditPanel journalEditPanel
    final JournalSwitchViewPanel journalSwitchViewPanel
    final JournalSwitchInputPanel journalSwitchInputPanel

    JournalSwitchPanel() {
        setLayout(new BorderLayout())

        journalSwitchInputPanel = new JournalSwitchInputPanel()
        journalEditPanel = new JournalEditPanel()
        journalSelectorPanel = new JournalSelectorPanel(journalEditPanel)
        journalSwitchViewPanel = new JournalSwitchViewPanel()

        JSplitPane journalViewAndEditSplitPane = Main.createSplitPane(journalSwitchViewPanel, journalEditPanel, VERTICAL_SPLIT)

        JPanel center = new JPanel(new BorderLayout())

        center.add journalViewAndEditSplitPane, BorderLayout.CENTER
        center.add journalSelectorPanel, BorderLayout.NORTH
        add center, BorderLayout.CENTER
        add journalSwitchInputPanel, BorderLayout.WEST
    }

    void setAccounting(Accounting accounting) {
        journalSwitchViewPanel.accounting = accounting
        journalEditPanel.accounting = accounting
        journalSelectorPanel.accounting = accounting
        journalSwitchInputPanel.accounting = accounting
    }

    void fireShowInputChanged(boolean enabled) {
        journalEditPanel.visible = enabled
        journalSwitchInputPanel.fireShowInputChanged(enabled)
    }

    void setJournal(Journal journal) {
        journalSelectorPanel.journal = journal
        journalEditPanel.journal = journal
        journalSwitchViewPanel.journal = journal
        journalSwitchInputPanel.journal = journal
        if (journal && journal.type.name == "Payments"){
            journalSwitchInputPanel.switchView(JournalSwitchInputPanel.PAYMENTS_VIEW)
        } else {
            journalSwitchInputPanel.switchView(JournalSwitchInputPanel.DEFAULT_VIEW)
        }
    }

    // INPUT PANEL

    void fireJournalTypeChanges(Journal journal, JournalType journalType) {
        journalSwitchInputPanel.fireJournalTypeChanges(journal, journalType)
    }

    void fireAccountDataChanged(){
        journalSwitchInputPanel.fireAccountDataChanged()
    }

    void fireGlobalShowNumbersChanged(boolean enabled){
        journalSwitchInputPanel.fireGlobalShowNumbersChanged(enabled)
    }

    void setAccountsTypesLeft(JournalType journalType, ArrayList<AccountType> accountTypes) {
        journalSwitchInputPanel.setAccountsTypesLeft(journalType, accountTypes)
    }
    void setAccountsTypesRight(JournalType journalType, ArrayList<AccountType> accountTypes) {
        journalSwitchInputPanel.setAccountsTypesRight(journalType, accountTypes)
    }

    void fireTransactionInputDataChanged() {
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

    void moveTransactions(Set<Transaction> bookings, Journals journals) {
        journalEditPanel.moveTransaction(bookings, journals)
    }

    Transaction getTransaction(){
        journalEditPanel.transaction
    }

    // TODO: add directly in (shared) JournalInputPanel (not via LayoutPanel)
    void addBooking(Booking booking){
        journalEditPanel.addBooking(booking)
    }

    // VIEW and SELECTION

    void fireJournalDataChanged(){
        journalSwitchViewPanel.fireJournalDataChanged()
    }

    void setJournals(Journals journals) {
        journalSelectorPanel.setJournals(journals)
    }

    void selectTransaction(Transaction transaction){
        journalSwitchViewPanel.selectTransaction(transaction)
    }

    // MORTGAGES

    void setMortgages(Mortgages mortgages) {
        journalSwitchInputPanel.setMortgages(mortgages)
    }

    void enableMortgagePayButton(Mortgage mortgage) {
        journalSwitchInputPanel.enableMortgagePayButton(mortgage)
    }
}
