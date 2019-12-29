package be.dafke.Accounting.BasicAccounting.Journals.New

import be.dafke.Accounting.BusinessModel.*
import be.dafke.ComponentModel.RefreshableDialog

import static java.util.ResourceBundle.getBundle

class NewJournalDialog extends RefreshableDialog {
    final NewJournalPanel newJournalPanel

    NewJournalDialog(Accounts accounts, Journals journals, JournalTypes journalTypes, AccountTypes accountTypes) {
        super(getBundle("Accounting").getString("NEW_JOURNAL_GUI_TITLE"))
        newJournalPanel = new NewJournalPanel(accounts, journals, journalTypes, accountTypes)
        setContentPane(newJournalPanel)
        pack()
    }

    void setJournal(Journal journal) {
        newJournalPanel.journal = journal
    }
}