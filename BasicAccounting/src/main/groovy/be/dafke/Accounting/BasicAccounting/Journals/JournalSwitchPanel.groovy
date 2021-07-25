package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BasicAccounting.Journals.Edit.JournalEditPanel
import be.dafke.Accounting.BasicAccounting.Journals.Selector.JournalSelectorPanel
import be.dafke.Accounting.BasicAccounting.Journals.View.JournalSwitchViewPanel
import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.*

import javax.swing.*
import java.awt.*

import static javax.swing.JSplitPane.VERTICAL_SPLIT

class JournalSwitchPanel extends JPanel {

    final JournalSwitchViewPanel journalSwitchViewPanel
    final JournalSwitchInputPanel journalSwitchInputPanel

    JournalSwitchPanel(JournalEditPanel journalEditPanel) {
        setLayout(new BorderLayout())
        journalSwitchInputPanel = new JournalSwitchInputPanel(journalEditPanel)
        journalSwitchViewPanel = new JournalSwitchViewPanel()

        JSplitPane journalViewAndEditSplitPane = Main.createSplitPane(journalSwitchViewPanel, journalEditPanel, VERTICAL_SPLIT)

        JPanel center = new JPanel(new BorderLayout())

        center.add journalViewAndEditSplitPane, BorderLayout.CENTER
        add center, BorderLayout.CENTER
        add journalSwitchInputPanel, BorderLayout.WEST
    }

//    void setAccounting(Accounting accounting) {
////        journalSwitchViewPanel.accounting = accounting
////        journalSwitchInputPanel.accounting = accounting
//    }

    void refresh(){
        journalSwitchViewPanel.refresh()
        journalSwitchInputPanel.refresh()
    }

    void fireShowInputChanged(boolean enabled) {
        journalSwitchInputPanel.fireShowInputChanged(enabled)
    }

    void setJournal(Journal journal) {
        journalSwitchViewPanel.journal = journal
        journalSwitchInputPanel.journal = journal
        if (journal && journal.type.name == "Payments"){
            journalSwitchInputPanel.switchView(JournalSwitchInputPanel.PAYMENTS_VIEW)
        } else {
            journalSwitchInputPanel.switchView(JournalSwitchInputPanel.DEFAULT_VIEW)
        }
    }

    // INPUT PANEL

    void fireJournalTypeChanges(Journal journal, JournalType journalType) {
        journalSwitchInputPanel.fireJournalTypeChanges(journal, journalType)
    }

    void fireAccountDataChanged(){
        journalSwitchInputPanel.fireAccountDataChanged()
    }

    void fireGlobalShowNumbersChanged(boolean enabled){
        journalSwitchInputPanel.fireGlobalShowNumbersChanged(enabled)
    }

    void setAccountsTypesLeft(JournalType journalType, ArrayList<AccountType> accountTypes) {
        journalSwitchInputPanel.setAccountsTypesLeft(journalType, accountTypes)
    }
    void setAccountsTypesRight(JournalType journalType, ArrayList<AccountType> accountTypes) {
        journalSwitchInputPanel.setAccountsTypesRight(journalType, accountTypes)
    }

    // VIEW and SELECTION

    void fireJournalDataChanged(){
        journalSwitchViewPanel.fireJournalDataChanged()
    }

    void selectTransaction(Transaction transaction){
        journalSwitchViewPanel.selectTransaction(transaction)
    }

    // MORTGAGES

//    void fireOrderPayed(){
//        journalSwitchInputPanel.fireOrderPayed()
//    }

    void setMortgages(Mortgages mortgages) {
//        journalSwitchInputPanel.setMortgages(mortgages)
    }

    void enableMortgagePayButton(Mortgage mortgage) {
        journalSwitchInputPanel.enableMortgagePayButton(mortgage)
    }
}
