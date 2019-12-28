package be.dafke.Accounting.BasicAccounting.Journals.View

import be.dafke.Accounting.BasicAccounting.Journals.Selector.JournalSelectorPanel
import be.dafke.Accounting.BasicAccounting.Journals.View.DualView.TransactionOverviewPanel
import be.dafke.Accounting.BasicAccounting.Journals.View.SingleView.JournalSingleViewPanel
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Journal
import be.dafke.Accounting.BusinessModel.Transaction

import javax.swing.*
import java.awt.*

class JournalSwitchViewPanel extends JPanel{
//    List<Transaction> selectedTransactions = new ArrayList<>()
    JournalSingleViewPanel singleView
    TransactionOverviewPanel dualView
    CardLayout cardLayout
    JPanel center

    JournalSwitchViewPanel() {
        cardLayout = new CardLayout()
        setLayout(new BorderLayout())

        center = new JPanel(cardLayout)
        singleView = new JournalSingleViewPanel()
        dualView = new TransactionOverviewPanel()
        center.add(singleView, JournalSelectorPanel.VIEW1)
        center.add(dualView, JournalSelectorPanel.VIEW2)

        add(center, BorderLayout.CENTER)
    }

    void switchView(String view){
        cardLayout.show(center, view)
        fireJournalDataChanged()
    }

    void setAccounting(Accounting accounting){
        singleView.accounting = accounting
        dualView.accounting = accounting
    }

    void setJournal(Journal journal){
        singleView.journal = journal
        dualView.journal = journal
    }

    void fireJournalDataChanged(){
        singleView.fireJournalDataChanged()
        dualView.fireJournalDataChanged()
    }

    void selectTransaction(Transaction transaction){
        singleView.selectTransaction(transaction)
        dualView.selectTransaction(transaction)
    }

    void setMultiSelection(boolean enabled){
//        singleView.setMultiSelection(enabled)
        dualView.setMultiSelection(enabled)
    }
}
