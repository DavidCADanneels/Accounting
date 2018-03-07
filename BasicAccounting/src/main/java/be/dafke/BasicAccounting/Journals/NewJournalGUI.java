package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;
import java.awt.*;

import static be.dafke.BasicAccounting.Journals.JournalTypeManagementGUI.showJournalTypeManager;
import static java.util.ResourceBundle.getBundle;

/**
 * User: Dafke
 * Date: 24/02/13
 * Time: 11:34
 */
public class NewJournalGUI extends JFrame {
    private static NewJournalGUI newJournalGUI = null;
    private final NewJournalPanel newJournalPanel;

    private NewJournalGUI(Accounts accounts, Journals journals, JournalTypes journalTypes, AccountTypes accountTypes) {
        super(getBundle("Accounting").getString("NEW_JOURNAL_GUI_TITLE"));
        newJournalPanel =new NewJournalPanel(accounts, journals, journalTypes, accountTypes);
        setContentPane(newJournalPanel);
        pack();
    }

    public static NewJournalGUI getInstance(Accounts accounts, Journals journals, JournalTypes journalTypes, AccountTypes accountTypes) {
        if(newJournalGUI == null) {
            newJournalGUI = new NewJournalGUI(accounts, journals, journalTypes, accountTypes);
            Main.addFrame(newJournalGUI);
        }
        return newJournalGUI;
    }

    public void setJournal(Journal journal) {
        newJournalPanel.setJournal(journal);
    }
}
