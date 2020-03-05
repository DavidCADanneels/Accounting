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
        // if (currentView == DEFAULT_VIEW) {
            defaultLayoutPanel.accounting = accounting
        // } else if (currentView == PAYMENTS_VIEW) {
            paymentLayoutPanel.accounting = accounting
//        }
    }

    void fireShowInputChanged(boolean enabled) {
        // if (currentView == DEFAULT_VIEW) {
            defaultLayoutPanel.fireShowInputChanged(enabled)
        // } else if (currentView == PAYMENTS_VIEW) {
            paymentLayoutPanel.fireShowInputChanged(enabled)
//        }
    }

    void setJournal(Journal journal) {
        if (journal && journal.type.name == "Payments"){
            switchView(JournalSwitchPanel.PAYMENTS_VIEW)
        } else {
            switchView(JournalSwitchPanel.DEFAULT_VIEW)
        }
        // if (currentView == DEFAULT_VIEW) {
            defaultLayoutPanel.setJournal(journal)
        // } else if (currentView == PAYMENTS_VIEW) {
            paymentLayoutPanel.setJournal(journal)
//        }
    }

    void switchView(String view) {
        cardLayout.show(center, view)
        currentView = view
    }

    void fireTransactionInputDataChanged() {
        // if (currentView == DEFAULT_VIEW) {
            defaultLayoutPanel.fireTransactionInputDataChanged()
        // } else if (currentView == PAYMENTS_VIEW) {
            paymentLayoutPanel.fireTransactionInputDataChanged()
//        }
    }

    void editTransaction(Transaction transaction){
        // if (currentView == DEFAULT_VIEW) {
            defaultLayoutPanel.editTransaction(transaction)
        // } else if (currentView == PAYMENTS_VIEW) {
            paymentLayoutPanel.editTransaction(transaction)
//        }
    }

    void deleteBookings(ArrayList<Booking> bookings){
        // if (currentView == DEFAULT_VIEW) {
            defaultLayoutPanel.deleteBookings(bookings)
        // } else if (currentView == PAYMENTS_VIEW) {
            paymentLayoutPanel.deleteBookings(bookings)
//        }
    }

    void deleteTransactions(Set<Transaction> transactions){
        // if (currentView == DEFAULT_VIEW) {
            defaultLayoutPanel.deleteTransactions(transactions)
        // } else if (currentView == PAYMENTS_VIEW) {
            paymentLayoutPanel.deleteTransactions(transactions)
//        }
    }

    void moveBookings(ArrayList<Booking> bookings, Journals journals){
        // if (currentView == DEFAULT_VIEW) {
            defaultLayoutPanel.moveBookings(bookings, journals)
        // } else if (currentView == PAYMENTS_VIEW) {
            paymentLayoutPanel.moveBookings(bookings, journals)
//        }
    }

    void moveTransactions(Set<Transaction> bookings, Journals journals) {
        // if (currentView == DEFAULT_VIEW) {
            defaultLayoutPanel.moveTransactions(bookings, journals)
        // } else if (currentView == PAYMENTS_VIEW) {
            paymentLayoutPanel.moveTransactions(bookings, journals)
//        }
    }

    Transaction getTransaction(){
        if (currentView == PAYMENTS_VIEW) {
            paymentLayoutPanel.transaction
        } else {
            defaultLayoutPanel.transaction
        }
    }

    // TODO: add directly in (shared) JournalInputPanel (not via LayoutPanel)
    void addBooking(Booking booking){
        if(currentView == PAYMENTS_VIEW) {
            paymentLayoutPanel.addBooking(booking)
        } else {
            defaultLayoutPanel.addBooking(booking)
        }
    }

    void fireJournalDataChanged(){
        // if (currentView == DEFAULT_VIEW) {
            defaultLayoutPanel.fireJournalDataChanged()
        // } else if(currentView == PAYMENTS_VIEW) {
            paymentLayoutPanel.fireJournalDataChanged()
//        }
    }

    void setJournals(Journals journals) {
        // if (currentView == DEFAULT_VIEW) {
            defaultLayoutPanel.setJournals(journals)
        // } else if (currentView == PAYMENTS_VIEW) {
            paymentLayoutPanel.setJournals(journals)
//        }
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
        // if (currentView == DEFAULT_VIEW) {
            defaultLayoutPanel.selectTransaction(transaction)
        // } else if(currentView == PAYMENTS_VIEW) {
            paymentLayoutPanel.selectTransaction(transaction)
//        }
    }

    void fireJournalTypeChanges(Journal journal, JournalType journalType) {
        defaultLayoutPanel.fireJournalTypeChanges(journal, journalType)
    }

    void setMortgages(Mortgages mortgages) {
        // if (currentView == DEFAULT_VIEW) {
            defaultLayoutPanel.setMortgages(mortgages)
        // } else if(currentView == PAYMENTS_VIEW) {
            paymentLayoutPanel.setMortgages(mortgages)
//        }
    }

    void enableMortgagePayButton(Mortgage mortgage) {
        // if (currentView == DEFAULT_VIEW) {
            defaultLayoutPanel.enableMortgagePayButton(mortgage)
        // } else if (currentView == PAYMENTS_VIEW) {
            paymentLayoutPanel.enableMortgagePayButton(mortgage)
//        }
    }

    void fireGlobalShowNumbersChanged(boolean enabled){
        defaultLayoutPanel.fireGlobalShowNumbersChanged(enabled)
    }

}
