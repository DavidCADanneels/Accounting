package be.dafke.Accounting.BasicAccounting.MainApplication

import be.dafke.Accounting.BasicAccounting.Accounts.AccountsTable.AccountsTablePanel
import be.dafke.Accounting.BasicAccounting.Journals.Edit.JournalEditPanel
import be.dafke.Accounting.BasicAccounting.Journals.Selector.JournalSelectorPanel
import be.dafke.Accounting.BasicAccounting.Journals.View.JournalSwitchViewPanel
import be.dafke.Accounting.BasicAccounting.Mortgages.MortgagesPanel
import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.BusinessModelDao.AccountingSession
import be.dafke.Accounting.BusinessModelDao.JournalSession
import be.dafke.Accounting.BusinessModelDao.Session

import javax.swing.*
import java.awt.*

import static javax.swing.JSplitPane.VERTICAL_SPLIT

class OldLayoutPanel extends JPanel {

    AccountsTablePanel accountGuiLeft
    AccountsTablePanel accountGuiRight
    MortgagesPanel mortgagesPanel
    JSplitPane journalViewAndEditSplitPane


    OldLayoutPanel(JournalEditPanel journalEditPanel, JournalSelectorPanel journalSelectorPanel, JournalSwitchViewPanel journalSwitchViewPanel) {

        accountGuiLeft = new AccountsTablePanel(true)
        accountGuiRight = new AccountsTablePanel(false)
        mortgagesPanel = new MortgagesPanel(journalEditPanel)


        JPanel links = new JPanel()
        links.setLayout(new BorderLayout())
        links.add(accountGuiLeft, BorderLayout.CENTER)
        links.add(mortgagesPanel, BorderLayout.SOUTH)

        setLayout(new BorderLayout())
        journalViewAndEditSplitPane = Main.createSplitPane(journalSwitchViewPanel, journalEditPanel, VERTICAL_SPLIT)

        JPanel centerPanel = new JPanel(new BorderLayout())
        centerPanel.add(journalViewAndEditSplitPane, BorderLayout.CENTER)
        centerPanel.add(journalSelectorPanel, BorderLayout.NORTH)

        add(accountGuiRight, BorderLayout.EAST)
        add(centerPanel, BorderLayout.CENTER)
        add(links, BorderLayout.WEST)
    }

    void setAccounting(Accounting accounting){
        accountGuiLeft.setAccounting(accounting, true)
        accountGuiRight.setAccounting(accounting, false)
        mortgagesPanel.setMortgages(accounting?accounting.mortgages:null)
    }

    void fireShowInputChanged(boolean enabled) {
        accountGuiLeft.visible = enabled
        accountGuiRight.visible = enabled
        mortgagesPanel.visible = enabled
    }

    void setJournal(Journal journal){
        Accounting activeAccounting = Session.activeAccounting
        AccountingSession accountingSession = Session.getAccountingSession(activeAccounting)
        Journal activeJournal = accountingSession?accountingSession.activeJournal:null
        JournalSession journalSession = activeJournal?accountingSession.getJournalSession(activeJournal):null
        accountGuiLeft.setJournalSession(journalSession)
        accountGuiRight.setJournalSession(journalSession)
        accountGuiLeft.setJournal(journal, true)
        accountGuiRight.setJournal(journal, false)
    }

    void fireGlobalShowNumbersChanged(boolean enabled){
        accountGuiLeft.fireGlobalShowNumbersChanged(enabled)
        accountGuiRight.fireGlobalShowNumbersChanged(enabled)
    }

    void fireAccountDataChanged(){
        // fireAccountDataChanged in AccountsListGUI is only needed if accounts have been added
        // in AccountsTableGUI it is also needed if the saldo of 1 or more accounts has changed
        accountGuiLeft.fireAccountDataChanged()
        accountGuiRight.fireAccountDataChanged()
    }

    void setAccountsTypesLeft(JournalType journalType, ArrayList<AccountType> accountTypes) {
        if(journalType == accountGuiLeft.getJournalType())
            accountGuiLeft.setAccountTypesList(accountTypes)
    }
    void setAccountsTypesRight(JournalType journalType, ArrayList<AccountType> accountTypes) {
        if(journalType == accountGuiRight.getJournalType())
            accountGuiRight.setAccountTypesList(accountTypes)
    }

    void fireJournalTypeChanges(Journal journal, JournalType journalType) {
        if(journal==accountGuiLeft.journal) {
            accountGuiLeft.setJournalType(journalType)
            accountGuiLeft.setAccountsList(journalType.getLeft())
        }
        if(journal==accountGuiRight.journal) {
            accountGuiRight.setJournalType(journalType)
            accountGuiRight.setAccountsList(journalType.getRight())
        }
    }

    void setMortgages(Mortgages mortgages) {
        mortgagesPanel.setMortgages(mortgages)
    }

    void enableMortgagePayButton(Mortgage mortgage) {
        mortgagesPanel.enablePayButton(mortgage)
    }
}
