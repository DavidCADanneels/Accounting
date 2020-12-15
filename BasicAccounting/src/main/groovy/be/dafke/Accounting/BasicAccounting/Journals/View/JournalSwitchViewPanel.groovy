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

    static final String VIEW1 = "Single Table"
    static final String VIEW2 = "Dual Table"

    String view = VIEW1

    JournalSwitchViewPanel() {
        cardLayout = new CardLayout()
        center = new JPanel(cardLayout)

        transactionSelectionModel = new TransactionSelectionModel()

        singleView = new JournalSingleViewPanel(transactionSelectionModel)
        dualView = new TransactionOverviewPanel(transactionSelectionModel)

        transactionSelectionModel.addListSelectionListener( { e ->
            if (!e.getValueIsAdjusting()) {
                updateSelection()
            }
        })

        center.add(singleView, VIEW1)
        center.add(dualView, VIEW2)

        setLayout(new BorderLayout())
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
                switchView(VIEW1)
            } else {
                switchView(VIEW2)
            }
        })
        view2.addActionListener({ e ->
            if (view1.selected) {
                switchView(VIEW1)
            } else {
                switchView(VIEW2)
            }
        })
        panel.add(new JLabel("View:"))
        panel.add(view1)
        panel.add(view2)

        panel
    }

    void switchView(String view) {
        println("===========")
        println("Switch VIEW")
        println("===========")
        cardLayout.show(center, view)
        this.view = view
        setSelection()
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

    void updateSelection(){
        if(view==JournalSwitchViewPanel.VIEW1){
            singleView.updateSelection()
        } else {
            dualView.updateSelection()
        }
    }

    void setSelection(){
        if(view==JournalSwitchViewPanel.VIEW1){
            singleView.setSelection()
        } else {
            dualView.setSelection()
        }
    }

    void selectTransaction(Transaction transaction){
        transactionSelectionModel.selectedTransaction = transaction
        transactionSelectionModel.selectedBooking = null

        dualView.setSelection()
        singleView.setSelection()
    }

    void selectBooking(Booking booking){
        transactionSelectionModel.selectedTransaction = null
        transactionSelectionModel.selectedBooking = booking

        dualView.setSelection()
        singleView.setSelection()
    }

    void closePopups(){
        singleView.closePopups()
        dualView.closePopups()
    }
}
