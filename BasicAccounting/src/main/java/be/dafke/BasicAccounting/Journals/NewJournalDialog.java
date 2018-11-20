package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.RefreshableDialog;

import static java.util.ResourceBundle.getBundle;

/**
 * User: Dafke
 * Date: 24/02/13
 * Time: 11:34
 */
public class NewJournalDialog extends RefreshableDialog {
    private final NewJournalPanel newJournalPanel;

    public NewJournalDialog(Accounts accounts, Journals journals, JournalTypes journalTypes, AccountTypes accountTypes) {
        super(getBundle("Accounting").getString("NEW_JOURNAL_GUI_TITLE"));
        newJournalPanel =new NewJournalPanel(accounts, journals, journalTypes, accountTypes);
        setContentPane(newJournalPanel);
        pack();
    }

    public void setJournal(Journal journal) {
        newJournalPanel.setJournal(journal);
    }
}
