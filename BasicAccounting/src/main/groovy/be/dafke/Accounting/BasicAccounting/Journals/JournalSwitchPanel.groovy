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
    DefaultLayoutPanel defaultLayoutPanel
    PaymentLayoutPanel paymentLayoutPanel

    JournalSwitchPanel() {
        cardLayout = new CardLayout()
        setLayout(new BorderLayout())

        center = new JPanel(cardLayout)

        defaultLayoutPanel = new DefaultLayoutPanel()
        paymentLayoutPanel = new PaymentLayoutPanel()
        
        center.add(defaultLayoutPanel, DEFAULT_VIEW)
        center.add(paymentLayoutPanel, PAYMENTS_VIEW)

        add(center, BorderLayout.CENTER)
    }

    void setAccounting(Accounting accounting) {
        defaultLayoutPanel.accounting = accounting
        paymentLayoutPanel.accounting = accounting
    }

    void fireShowInputChanged(boolean enabled) {
        defaultLayoutPanel.fireShowInputChanged(enabled)
        paymentLayoutPanel.fireShowInputChanged(enabled)
    }

    void setJournal(Journal journal) {
        if (journal && journal.type.name == "Payments"){
            switchView(JournalSwitchPanel.PAYMENTS_VIEW)
        } else {
            switchView(JournalSwitchPanel.DEFAULT_VIEW)
        }
        defaultLayoutPanel.setJournal(journal)
        paymentLayoutPanel.setJournal(journal)
    }

    void fireTransactionInputDataChanged(){
        defaultLayoutPanel.fireTransactionInputDataChanged()
        paymentLayoutPanel.fireTransactionInputDataChanged()
    }

    void editTransaction(Transaction transaction){
        defaultLayoutPanel.editTransaction(transaction)
        paymentLayoutPanel.editTransaction(transaction)
    }

    void deleteBookings(ArrayList<Booking> bookings){
        defaultLayoutPanel.deleteBookings(bookings)
        paymentLayoutPanel.deleteBookings(bookings)
    }

    void deleteTransactions(Set<Transaction> transactions){
        defaultLayoutPanel.deleteTransactions(transactions)
        paymentLayoutPanel.deleteTransactions(transactions)
    }

    void moveBookings(ArrayList<Booking> bookings, Journals journals){
        defaultLayoutPanel.moveBookings(bookings, journals)
        paymentLayoutPanel.moveBookings(bookings, journals)
    }

    void moveTransactions(Set<Transaction> bookings, Journals journals){
        defaultLayoutPanel.moveTransactions(bookings, journals)
        paymentLayoutPanel.moveTransactions(bookings, journals)
    }

    Transaction getTransaction(){
        if(currentView == PAYMENTS_VIEW) {
            paymentLayoutPanel.transaction
        } else {
            defaultLayoutPanel.transaction
        }
    }

    void addBooking(Booking booking){
        if(currentView == PAYMENTS_VIEW) {
            paymentLayoutPanel.addBooking(booking)
        } else {
            defaultLayoutPanel.addBooking(booking)
        }
    }

    void fireJournalDataChanged(){
        defaultLayoutPanel.fireJournalDataChanged()
        paymentLayoutPanel.fireJournalDataChanged()
    }

    void setJournals(Journals journals) {
        defaultLayoutPanel.setJournals(journals)
        paymentLayoutPanel.setJournals(journals)
    }

    void fireAccountDataChanged(){
        defaultLayoutPanel.fireAccountDataChanged()
    }

    void setAccountsTypesLeft(JournalType journalType, ArrayList<AccountType> accountTypes) {
        defaultLayoutPanel.setAccountsTypesLeft(journalType, accountTypes)
    }
    void setAccountsTypesRight(JournalType journalType, ArrayList<AccountType> accountTypes) {
        defaultLayoutPanel.setAccountsTypesRight(journalType, accountTypes)
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
        defaultLayoutPanel.selectTransaction(transaction)
        paymentLayoutPanel.selectTransaction(transaction)
    }

    void fireJournalTypeChanges(Journal journal, JournalType journalType) {
        defaultLayoutPanel.fireJournalTypeChanges(journal, journalType)
    }

    void setMortgages(Mortgages mortgages) {
        defaultLayoutPanel.setMortgages(mortgages)
        paymentLayoutPanel.setMortgages(mortgages)
    }

    void enableMortgagePayButton(Mortgage mortgage) {
        defaultLayoutPanel.enableMortgagePayButton(mortgage)
        paymentLayoutPanel.enableMortgagePayButton(mortgage)
    }

    void fireGlobalShowNumbersChanged(boolean enabled){
        defaultLayoutPanel.fireGlobalShowNumbersChanged(enabled)
    }

    void switchView(String view) {
        cardLayout.show(center, view)
    }
}
