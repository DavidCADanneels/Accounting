package be.dafke.Accounting.BasicAccounting.Journals.View

import be.dafke.Accounting.BasicAccounting.Journals.View.DualView.TransactionOverviewPanel
import be.dafke.Accounting.BasicAccounting.Journals.View.SingleView.JournalSingleViewPanel

import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener

class TransactionSelectionListener implements ListSelectionListener {//}, ActionListener{

    String view = TransactionSelectionModel.VIEW1
    JournalSingleViewPanel journalSingleViewPanel
    TransactionOverviewPanel transactionOverviewPanel

    TransactionSelectionListener(JournalSingleViewPanel journalSingleViewPanel, TransactionOverviewPanel transactionOverviewPanel) {
        this.journalSingleViewPanel = journalSingleViewPanel
        this.transactionOverviewPanel = transactionOverviewPanel
    }

    @Override
    void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            if(view==TransactionSelectionModel.VIEW1){
                journalSingleViewPanel.updateSelection()
            } else {
                transactionOverviewPanel.updateSelection()
            }
        }
    }

    void setView(String view) {
        this.view = view
        if(view==TransactionSelectionModel.VIEW1){
            journalSingleViewPanel.setSelection()
        } else {
            transactionOverviewPanel.setSelection()
        }
    }

//    @Override
//    void actionPerformed(ActionEvent e) {
//
//    }
}
