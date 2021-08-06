package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BasicAccounting.Journals.Edit.JournalEditPanel
import be.dafke.Accounting.BusinessModel.*

import javax.swing.*
import javax.swing.border.LineBorder
import javax.swing.border.TitledBorder
import java.awt.*

class JournalSwitchInputPanel extends JPanel{

    static final String DEFAULT_VIEW = "default"
    static final String PAYMENTS_VIEW = "Payments"

    String currentView = DEFAULT_VIEW
    CardLayout cardLayout

    DefaultLayoutPanel defaultLayoutPanel
    PaymentLayoutPanel paymentLayoutPanel

    JPanel center

    JournalSwitchInputPanel(JournalEditPanel journalEditPanel) {
        setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Input"))
        cardLayout = new CardLayout()
        center = new JPanel(cardLayout)

        defaultLayoutPanel = new DefaultLayoutPanel(journalEditPanel)
        paymentLayoutPanel = new PaymentLayoutPanel(journalEditPanel)

        center.add(defaultLayoutPanel, DEFAULT_VIEW)
        center.add(paymentLayoutPanel, PAYMENTS_VIEW)

        setLayout(new BorderLayout())
        add(center, BorderLayout.CENTER)
    }

    void switchView(String view) {
        if (currentView != view) {
            cardLayout.show(center, view)
            currentView = view
        }
    }

    void setJournal(Journal journal){
        if (journal && journal.type.name == "Payments"){
            switchView(JournalSwitchInputPanel.PAYMENTS_VIEW)
            paymentLayoutPanel.setJournal(journal)
        } else {
            switchView(JournalSwitchInputPanel.DEFAULT_VIEW)
            defaultLayoutPanel.setJournal(journal)
        }
    }

    void fireAccountDataChanged() {
        defaultLayoutPanel.fireAccountDataChanged()
    }

    void setAccountsTypesLeft(JournalType journalType, ArrayList<AccountType> accountTypes) {
        defaultLayoutPanel.setAccountsTypesLeft(journalType, accountTypes)
    }
    void setAccountsTypesRight(JournalType journalType, ArrayList<AccountType> accountTypes) {
        defaultLayoutPanel.setAccountsTypesRight(journalType, accountTypes)
    }

    void fireJournalTypeChanges(Journal journal, JournalType journalType) {
        defaultLayoutPanel.fireJournalTypeChanges(journal, journalType)
    }

    void fireGlobalShowNumbersChanged(boolean enabled){
        defaultLayoutPanel.fireGlobalShowNumbersChanged(enabled)
    }

//    void setAccounting(Accounting accounting){
//        defaultLayoutPanel.accounting = accounting
//        paymentLayoutPanel.accounting = accounting
////        refresh()
////        setMortgages(accounting?.mortgages)
//    }

    void refresh() {
        defaultLayoutPanel.refresh()
        paymentLayoutPanel.refresh()
    }

    void enableMortgagePayButton(Mortgage mortgage) {
        defaultLayoutPanel.enableMortgagePayButton(mortgage)
        paymentLayoutPanel.enableMortgagePayButton(mortgage)
    }

    void fireShowInputChanged(boolean enabled) {
        defaultLayoutPanel.fireShowInputChanged(enabled)
        paymentLayoutPanel.fireShowInputChanged(enabled)
    }
}
