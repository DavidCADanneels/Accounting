package be.dafke.Accounting.BasicAccounting.Journals


import be.dafke.Accounting.BusinessModel.*

import javax.swing.*
import java.awt.*

class JournalSwitchPanel extends JPanel {

    static final String DEFAULT_VIEW = "default"
    static final String PAYMENTS_VIEW = "Payments"

    String currentView = DEFAULT_VIEW
    CardLayout cardLayout
    JPanel center
    OldLayoutPanel oldLayoutPanel
    NewLayoutPanel newLayoutPanel

    JournalSwitchPanel() {
        cardLayout = new CardLayout()
        setLayout(new BorderLayout())

        center = new JPanel(cardLayout)

        oldLayoutPanel = new OldLayoutPanel()
        newLayoutPanel = new NewLayoutPanel()
        
        center.add(oldLayoutPanel, DEFAULT_VIEW)
        center.add(newLayoutPanel, PAYMENTS_VIEW)

        add(center, BorderLayout.CENTER)
    }

    void setAccounting(Accounting accounting) {
        oldLayoutPanel.accounting = accounting
        newLayoutPanel.accounting = accounting
    }

    void fireShowInputChanged(boolean enabled) {
        oldLayoutPanel.fireShowInputChanged(enabled)
        newLayoutPanel.fireShowInputChanged(enabled)
    }

    void setJournal(Journal journal) {
        if (journal && journal.type.name == "Payments"){
            switchView(JournalSwitchPanel.PAYMENTS_VIEW)
        } else {
            switchView(JournalSwitchPanel.DEFAULT_VIEW)
        }
        oldLayoutPanel.setJournal(journal)
        newLayoutPanel.setJournal(journal)
    }

    void fireTransactionInputDataChanged(){
        oldLayoutPanel.fireTransactionInputDataChanged()
        newLayoutPanel.fireTransactionInputDataChanged()
    }

    void editTransaction(Transaction transaction){
        oldLayoutPanel.editTransaction(transaction)
        newLayoutPanel.editTransaction(transaction)
    }

    void deleteBookings(ArrayList<Booking> bookings){
        oldLayoutPanel.deleteBookings(bookings)
        newLayoutPanel.deleteBookings(bookings)
    }

    void deleteTransactions(Set<Transaction> transactions){
        oldLayoutPanel.deleteTransactions(transactions)
        newLayoutPanel.deleteTransactions(transactions)
    }

    void moveBookings(ArrayList<Booking> bookings, Journals journals){
        oldLayoutPanel.moveBookings(bookings, journals)
        newLayoutPanel.moveBookings(bookings, journals)
    }

    void moveTransactions(Set<Transaction> bookings, Journals journals){
        oldLayoutPanel.moveTransactions(bookings, journals)
        newLayoutPanel.moveTransactions(bookings, journals)
    }

    Transaction getTransaction(){
        if(currentView == PAYMENTS_VIEW) {
            newLayoutPanel.transaction
        } else {
            oldLayoutPanel.transaction
        }
    }

    void addBooking(Booking booking){
        if(currentView == PAYMENTS_VIEW) {
            newLayoutPanel.addBooking(booking)
        } else {
            oldLayoutPanel.addBooking(booking)
        }
    }

    void fireJournalDataChanged(){
        oldLayoutPanel.fireJournalDataChanged()
        newLayoutPanel.fireJournalDataChanged()
    }

    void setJournals(Journals journals) {
        oldLayoutPanel.setJournals(journals)
        newLayoutPanel.setJournals(journals)
    }

    void fireAccountDataChanged(){
        oldLayoutPanel.fireAccountDataChanged()
    }

    void setAccountsTypesLeft(JournalType journalType, ArrayList<AccountType> accountTypes) {
        oldLayoutPanel.setAccountsTypesLeft(journalType, accountTypes)
    }
    void setAccountsTypesRight(JournalType journalType, ArrayList<AccountType> accountTypes) {
        oldLayoutPanel.setAccountsTypesRight(journalType, accountTypes)
    }
//    void setAccountsListLeft(JournalType journalType, AccountsList accountsList) {
//        if(journalType == accountGuiLeft.getJournalType())
//            accountGuiLeft.setAccountsList(accountsList)
//    }
//
//    void setAccountsListRight(JournalType journalType, AccountsList accountsList) {
//        if(journalType == accountGuiRight.getJournalType())
//            accountGuiRight.setAccountsList(accountsList)
//    }

    void selectTransaction(Transaction transaction){
        oldLayoutPanel.selectTransaction(transaction)
        newLayoutPanel.selectTransaction(transaction)
    }

    void fireJournalTypeChanges(Journal journal, JournalType journalType) {
        oldLayoutPanel.fireJournalTypeChanges(journal, journalType)
    }

    void setMortgages(Mortgages mortgages) {
        oldLayoutPanel.setMortgages(mortgages)
        newLayoutPanel.setMortgages(mortgages)
    }

    void enableMortgagePayButton(Mortgage mortgage) {
        oldLayoutPanel.enableMortgagePayButton(mortgage)
        newLayoutPanel.enableMortgagePayButton(mortgage)
    }

    void fireGlobalShowNumbersChanged(boolean enabled){
        oldLayoutPanel.fireGlobalShowNumbersChanged(enabled)
    }

    void switchView(String view) {
        cardLayout.show(center, view)
    }
}
