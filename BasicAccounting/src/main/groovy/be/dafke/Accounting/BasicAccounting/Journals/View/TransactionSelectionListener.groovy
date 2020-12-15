package be.dafke.Accounting.BasicAccounting.Journals.View


import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener

class TransactionSelectionListener implements ListSelectionListener {

    JournalSwitchViewPanel journalSwitchViewPanel

    TransactionSelectionListener(JournalSwitchViewPanel journalSwitchViewPanel) {
        this.journalSwitchViewPanel = journalSwitchViewPanel
    }

    @Override
    void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            journalSwitchViewPanel.updateSelection()
        }
    }
}
