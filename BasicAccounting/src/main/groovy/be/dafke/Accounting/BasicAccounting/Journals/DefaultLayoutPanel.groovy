package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BasicAccounting.Accounts.AccountsTable.AccountsTablePanel
import be.dafke.Accounting.BasicAccounting.Journals.Edit.JournalEditPanel
import be.dafke.Accounting.BasicAccounting.Mortgages.MortgagesPanel
import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.BusinessModelDao.AccountingSession
import be.dafke.Accounting.BusinessModelDao.JournalSession
import be.dafke.Accounting.BusinessModelDao.Session

import javax.swing.*
import java.awt.*

class DefaultLayoutPanel extends JPanel {

    AccountsTablePanel accountGuiLeft
    AccountsTablePanel accountGuiRight
    MortgagesPanel mortgagesPanel

    DefaultLayoutPanel(JournalEditPanel journalEditPanel) {

        accountGuiLeft = new AccountsTablePanel(true)
        accountGuiRight = new AccountsTablePanel(false)
        mortgagesPanel = new MortgagesPanel(journalEditPanel)

        JPanel links = new JPanel()
        links.setLayout(new BorderLayout())
        links.add(accountGuiLeft, BorderLayout.CENTER)
        links.add(mortgagesPanel, BorderLayout.SOUTH)

        setLayout(new BorderLayout())

        add(accountGuiRight, BorderLayout.EAST)
        add(links, BorderLayout.WEST)
    }

    void fireShowInputChanged(boolean enabled) {
        accountGuiLeft.visible = enabled
        accountGuiRight.visible = enabled
        mortgagesPanel.visible = enabled
    }

    void setJournal(Journal journal) {
        Accounting activeAccounting = Session.activeAccounting
        AccountingSession accountingSession = Session.getAccountingSession(activeAccounting)
        Journal activeJournal = accountingSession ? accountingSession.activeJournal : null
        JournalSession journalSession = activeJournal ? accountingSession.getJournalSession(activeJournal) : null
        accountGuiLeft.setJournalSession(journalSession)
        accountGuiRight.setJournalSession(journalSession)
        accountGuiLeft.setJournal(journal, true)
        accountGuiRight.setJournal(journal, false)
    }

    void fireGlobalShowNumbersChanged(boolean enabled) {
        accountGuiLeft.fireGlobalShowNumbersChanged(enabled)
        accountGuiRight.fireGlobalShowNumbersChanged(enabled)
    }

    void fireAccountDataChanged() {
        // fireAccountDataChanged in AccountsListGUI is only needed if accounts have been added
        // in AccountsTableGUI it is also needed if the saldo of 1 or more accounts has changed
        accountGuiLeft.fireAccountDataChanged()
        accountGuiRight.fireAccountDataChanged()
    }

    void setAccountsTypesLeft(JournalType journalType, ArrayList<AccountType> accountTypes) {
        if (journalType == accountGuiLeft.getJournalType())
            accountGuiLeft.setAccountTypesList(accountTypes)
    }

    void setAccountsTypesRight(JournalType journalType, ArrayList<AccountType> accountTypes) {
        if (journalType == accountGuiRight.getJournalType())
            accountGuiRight.setAccountTypesList(accountTypes)
    }

    void fireJournalTypeChanges(Journal journal, JournalType journalType) {
        if (journal == accountGuiLeft.journal) {
            accountGuiLeft.setJournalType(journalType)
            accountGuiLeft.setAccountsList(journalType.getLeft())
        }
        if (journal == accountGuiRight.journal) {
            accountGuiRight.setJournalType(journalType)
            accountGuiRight.setAccountsList(journalType.getRight())
        }
    }

    void refresh() {
        mortgagesPanel.refresh()
        accountGuiLeft.refresh(true)
        accountGuiRight.refresh(false)
        mortgagesPanel.refresh()
    }

    void enableMortgagePayButton(Mortgage mortgage) {
        mortgagesPanel.enablePayButton(mortgage)
    }
}
    