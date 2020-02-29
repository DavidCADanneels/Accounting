package be.dafke.Accounting.BasicAccounting.MainApplication


import be.dafke.Accounting.BasicAccounting.Journals.Edit.JournalEditPanel
import be.dafke.Accounting.BasicAccounting.Journals.Selector.JournalSelectorPanel
import be.dafke.Accounting.BasicAccounting.Journals.View.JournalSwitchViewPanel
import be.dafke.Accounting.BasicAccounting.Mortgages.MortgagesPanel
import be.dafke.Accounting.BusinessModel.*

import javax.swing.*
import java.awt.*

import static javax.swing.JSplitPane.VERTICAL_SPLIT

class NewLayoutPanel extends JPanel {

    MortgagesPanel mortgagesPanel
    CardLayout cardLayout
    JournalSwitchViewPanel cardPanel
    JSplitPane journalViewAndEditSplitPane


    NewLayoutPanel(JournalEditPanel journalEditPanel, JournalSelectorPanel journalSelectorPanel) {

        mortgagesPanel = new MortgagesPanel(journalEditPanel)


        JPanel links = new JPanel()
        links.setLayout(new BorderLayout())
//        links.add(accountGuiLeft, BorderLayout.CENTER)
        links.add(mortgagesPanel, BorderLayout.SOUTH)

        setLayout(new BorderLayout())
        cardLayout = new CardLayout()
        cardPanel = new JournalSwitchViewPanel()
        journalViewAndEditSplitPane = Main.createSplitPane(cardPanel, journalEditPanel, VERTICAL_SPLIT)

        JPanel centerPanel = new JPanel(new BorderLayout())
        centerPanel.add(journalViewAndEditSplitPane, BorderLayout.CENTER)
        centerPanel.add(journalSelectorPanel, BorderLayout.NORTH)

//        add(accountGuiRight, BorderLayout.EAST)
        add(centerPanel, BorderLayout.CENTER)
        add(links, BorderLayout.WEST)
    }

    void setAccounting(Accounting accounting){
        cardPanel.accounting = accounting
        mortgagesPanel.setMortgages(accounting?accounting.mortgages:null)
    }

    void fireShowInputChanged(boolean enabled) {
        mortgagesPanel.visible = enabled
    }

    void setJournal(Journal journal){
        cardPanel.journal = journal
    }

//    void fireGlobalShowNumbersChanged(boolean enabled){
//
//    }

    void selectTransaction(Transaction transaction){
        cardPanel.selectTransaction(transaction)
    }

    void fireJournalDataChanged(){
        cardPanel.fireJournalDataChanged()
    }

    void fireAccountDataChanged(){
        // fireAccountDataChanged in AccountsListGUI is only needed if accounts have been added
        // in AccountsTableGUI it is also needed if the saldo of 1 or more accounts has changed
        cardPanel.fireJournalDataChanged()
    }

//    void setAccountsTypesLeft(JournalType journalType, ArrayList<AccountType> accountTypes) {
//
//    }
//    void setAccountsTypesRight(JournalType journalType, ArrayList<AccountType> accountTypes) {
//    }
//
//    void fireJournalTypeChanges(Journal journal, JournalType journalType) {
//    }

    void setMortgages(Mortgages mortgages) {
        mortgagesPanel.setMortgages(mortgages)
    }

    void enableMortgagePayButton(Mortgage mortgage) {
        mortgagesPanel.enablePayButton(mortgage)
    }
}
