package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BasicAccounting.Journals.Edit.JournalEditPanel
import be.dafke.Accounting.BusinessModel.*

import javax.swing.*
import javax.swing.border.LineBorder
import javax.swing.border.TitledBorder
import java.awt.*

class JournalSwitchInputPanel extends JPanel{

    String currentView = JournalType.DEFAULT_TYPE
    CardLayout cardLayout

    DefaultLayoutPanel defaultLayoutPanel
    PaymentLayoutPanel paymentLayoutPanel
    PurchaseLayoutPanel purchaseLayoutPanel

    JPanel center

    JournalSwitchInputPanel(JournalEditPanel journalEditPanel) {
        setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Input"))
        cardLayout = new CardLayout()
        center = new JPanel(cardLayout)

        defaultLayoutPanel = new DefaultLayoutPanel(journalEditPanel)
        paymentLayoutPanel = new PaymentLayoutPanel(journalEditPanel)
        purchaseLayoutPanel = new PurchaseLayoutPanel()

        center.add(defaultLayoutPanel, JournalType.DEFAULT_TYPE)
        center.add(paymentLayoutPanel, JournalType.PAYMENT_TYPE)
        center.add(purchaseLayoutPanel, JournalType.PURCHASE_TYPE)

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
        if (journal && journal.type.name == JournalType.PAYMENT_TYPE) {
            paymentLayoutPanel.journal = journal
        } else {
            defaultLayoutPanel.journal = journal
        }
        if (journal && journal.type.name == JournalType.PAYMENT_TYPE) {
            switchView(JournalType.PAYMENT_TYPE)
        } else if (journal && journal.type.name == JournalType.PURCHASE_TYPE){
            switchView(JournalType.PURCHASE_TYPE)
        } else {
            switchView(JournalType.DEFAULT_TYPE)
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

    void setAccounting() {
        defaultLayoutPanel.setAccounting()
        paymentLayoutPanel.refresh()
    }

    void enableMortgagePayButton(Mortgage mortgage) {
        defaultLayoutPanel.enableMortgagePayButton(mortgage)
        paymentLayoutPanel.enableMortgagePayButton(mortgage)
    }

    void fireShowInputChanged(boolean enabled) {
        center.visible = enabled
//        paymentLayoutPanel.visible = enabled
//        defaultLayoutPanel.fireShowInputChanged(enabled)
//        paymentLayoutPanel.fireShowInputChanged(enabled)
    }
}
