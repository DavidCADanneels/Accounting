package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BusinessModel.AccountTypes
import be.dafke.Accounting.BusinessModel.Accounts
import be.dafke.Accounting.BusinessModel.Journal
import be.dafke.Accounting.BusinessModel.JournalTypes
import be.dafke.Accounting.BusinessModel.Journals
import be.dafke.ComponentModel.RefreshableDialog

import static java.util.ResourceBundle.getBundle

class NewJournalDialog extends RefreshableDialog {
    private final NewJournalPanel newJournalPanel

    NewJournalDialog(Accounts accounts, Journals journals, JournalTypes journalTypes, AccountTypes accountTypes) {
        super(getBundle("Accounting").getString("NEW_JOURNAL_GUI_TITLE"))
        newJournalPanel = new NewJournalPanel(accounts, journals, journalTypes, accountTypes)
        setContentPane(newJournalPanel)
        pack()
    }

    void setJournal(Journal journal) {
        newJournalPanel.setJournal(journal)
    }
}