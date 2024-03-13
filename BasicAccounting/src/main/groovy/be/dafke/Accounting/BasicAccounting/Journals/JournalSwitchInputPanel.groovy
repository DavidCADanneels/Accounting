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
    JPanel blankPanel
    JPanel center
    JRadioButton defaultView, paymentView, purchaseView

    JournalSwitchInputPanel(JournalEditPanel journalEditPanel) {
        setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Input"))
        cardLayout = new CardLayout()
        center = new JPanel(cardLayout)

        defaultLayoutPanel = new DefaultLayoutPanel(journalEditPanel)
        paymentLayoutPanel = new PaymentLayoutPanel(journalEditPanel)
        purchaseLayoutPanel = new PurchaseLayoutPanel()
        blankPanel = new JPanel()

        center.add(defaultLayoutPanel, JournalType.DEFAULT_TYPE)
        center.add(paymentLayoutPanel, JournalType.PAYMENT_TYPE)
        center.add(purchaseLayoutPanel, JournalType.PURCHASE_TYPE)
        center.add(blankPanel, 'BLANK')

        defaultView = new JRadioButton('default')
        paymentView = new JRadioButton('payment')
        purchaseView = new JRadioButton('purchase')
        ButtonGroup group = new ButtonGroup()
        group.add(defaultView)
        group.add(paymentView)
        group.add(purchaseView)

        defaultView.addActionListener({e ->
            switchView(JournalType.DEFAULT_TYPE)
        })
        paymentView.addActionListener({e ->
            switchView(JournalType.PAYMENT_TYPE)
        })
        purchaseView.addActionListener({e ->
            switchView(JournalType.PURCHASE_TYPE)
        })
        defaultView.selected = true

        JPanel north = new JPanel()
        north.add(new JLabel("View:"))
        north.add(defaultView)
        north.add(paymentView)
        north.add(purchaseView)

        setLayout(new BorderLayout())
        add(north, BorderLayout.NORTH)
        add(center, BorderLayout.CENTER)
    }

    void switchView(String view) {
        if (currentView != view) {
            cardLayout.show(center, view)
            currentView = view
        }
    }

    void setJournal(Journal journal){
        if(journal == null) {
            switchView('BLANK')
        } else {
            paymentLayoutPanel.journal = journal
            defaultLayoutPanel.journal = journal
            if(journal.type.name == JournalType.PAYMENT_TYPE) {
                paymentView.selected = true
                switchView(JournalType.PAYMENT_TYPE)
            } else if (journal.type.name == JournalType.PURCHASE_TYPE) {
                purchaseView.selected = true
                switchView(JournalType.PURCHASE_TYPE)
//            } else if (journal.type.name == JournalType.DEFAULT_TYPE){
//                defaultView.selected = true
//                switchView(JournalType.DEFAULT_TYPE)
            } else {
                defaultView.selected = true
                switchView(JournalType.DEFAULT_TYPE)
            }
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
