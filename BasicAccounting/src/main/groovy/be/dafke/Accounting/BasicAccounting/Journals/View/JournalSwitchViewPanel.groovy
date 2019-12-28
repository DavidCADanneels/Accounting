package be.dafke.Accounting.BasicAccounting.Journals.View


import be.dafke.Accounting.BasicAccounting.Journals.View.DualView.TransactionOverviewPanel
import be.dafke.Accounting.BasicAccounting.Journals.View.SingleView.JournalSingleViewPanel
import be.dafke.Accounting.BasicAccounting.MainApplication.Main
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
    JCheckBox mergeTransactions

    static final String VIEW1 = "View1"
    static final String VIEW2 = "View2"

    JournalSwitchViewPanel() {
        cardLayout = new CardLayout()
        setLayout(new BorderLayout())

        center = new JPanel(cardLayout)
        singleView = new JournalSingleViewPanel()
        dualView = new TransactionOverviewPanel()
        center.add(singleView, VIEW1)
        center.add(dualView, VIEW2)

        add(center, BorderLayout.CENTER)
        add(createTopPanel(),BorderLayout.NORTH)
    }

    JPanel createTopPanel(){
        JPanel panel = new JPanel()

        JRadioButton view1 = new JRadioButton(VIEW1)
        JRadioButton view2 = new JRadioButton(VIEW2)
        ButtonGroup group = new ButtonGroup()
        group.add(view1)
        group.add(view2)

        view1.selected = true
        view1.addActionListener({ e ->
            if (view1.selected) {
                Main.switchView(VIEW1)
            } else {
                Main.switchView(VIEW2)
            }
        })
        view2.addActionListener({ e ->
            if (view1.selected) {
                Main.switchView(VIEW1)
            } else {
                Main.switchView(VIEW2)
            }
        })
        panel.add(view1)
        panel.add(view2)

        mergeTransactions = new JCheckBox("Merge Transactions")
        mergeTransactions.addActionListener({ e ->
            Main.fireMultiTransactionChanged(mergeTransactions.selected)
        })
        mergeTransactions.selected = false
        panel.add(mergeTransactions)

        panel
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
