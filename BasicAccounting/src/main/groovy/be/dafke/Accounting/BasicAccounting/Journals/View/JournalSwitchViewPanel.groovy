package be.dafke.Accounting.BasicAccounting.Journals.View

import be.dafke.Accounting.BasicAccounting.Journals.View.DualView.TransactionOverviewPanel
import be.dafke.Accounting.BasicAccounting.Journals.View.SingleView.JournalSingleViewPanel
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Booking
import be.dafke.Accounting.BusinessModel.Journal
import be.dafke.Accounting.BusinessModel.Transaction

import javax.swing.*
import java.awt.*

class JournalSwitchViewPanel extends JPanel {
    TransactionSelectionModel transactionSelectionModel
    JournalSingleViewPanel singleView
    TransactionOverviewPanel dualView
    CardLayout cardLayout
    JPanel center
    JCheckBox mergeTransactions
    TransactionSelectionListener transactionSelectionListener

    JournalSwitchViewPanel() {
        cardLayout = new CardLayout()
        center = new JPanel(cardLayout)

        transactionSelectionModel = new TransactionSelectionModel()

        singleView = new JournalSingleViewPanel(transactionSelectionModel)
        dualView = new TransactionOverviewPanel(transactionSelectionModel)

        transactionSelectionListener = new TransactionSelectionListener(singleView, dualView)
        transactionSelectionModel.addListSelectionListener(transactionSelectionListener)

        center.add(singleView, TransactionSelectionModel.VIEW1)
        center.add(dualView, TransactionSelectionModel.VIEW2)

        setLayout(new BorderLayout())
        add(center, BorderLayout.CENTER)
        add(createTopPanel(),BorderLayout.NORTH)
    }

    JPanel createTopPanel(){
        JPanel panel = new JPanel()

        JRadioButton view1 = new JRadioButton(TransactionSelectionModel.VIEW1)
        JRadioButton view2 = new JRadioButton(TransactionSelectionModel.VIEW2)
        ButtonGroup group = new ButtonGroup()
        group.add(view1)
        group.add(view2)

        view1.selected = true
        view1.addActionListener({ e ->
            if (view1.selected) {
                switchView(TransactionSelectionModel.VIEW1)
            } else {
                switchView(TransactionSelectionModel.VIEW2)
            }
            transactionSelectionModel.printData()
        })
        view2.addActionListener({ e ->
            if (view1.selected) {
                switchView(TransactionSelectionModel.VIEW1)
            } else {
                switchView(TransactionSelectionModel.VIEW2)
            }
            transactionSelectionModel.printData()
        })
        panel.add(new JLabel("View:"))
        panel.add(view1)
        panel.add(view2)

//        mergeTransactions = new JCheckBox("Merge Transactions")
//        mergeTransactions.addActionListener({ e ->
//            dualView.setMultiSelection(mergeTransactions.selected)
//        })
//        mergeTransactions.selected = false
//        mergeTransactions.visible = true//false
//        panel.add(mergeTransactions)

        panel
    }

    void switchView(String view) {
        cardLayout.show(center, view)
        transactionSelectionListener.view = view
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
        transactionSelectionModel.selectTransaction(transaction)
        // TODO: just reload 'singleView' and 'dualView' (model is already updated)
        singleView.selectTransaction(transaction)
        dualView.selectTransaction(transaction)
    }

    void selectBooking(Booking booking){
        singleView.selectBooking(booking)
        dualView.selectBooking(booking)
        transactionSelectionModel.selectBooking(booking)
    }

    void closePopups(){
        singleView.closePopups()
        dualView.closePopups()
    }
}
