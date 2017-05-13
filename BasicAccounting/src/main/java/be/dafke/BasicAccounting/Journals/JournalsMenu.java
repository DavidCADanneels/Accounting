package be.dafke.BasicAccounting.Journals;

import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.JournalTypes;
import be.dafke.BusinessModel.Journals;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.KeyEvent;

import static be.dafke.BasicAccounting.Journals.JournalManagementGUI.showJournalManager;
import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 27/12/2015.
 */
public class JournalsMenu extends JMenu {
    private static JMenuItem add, manage;

    private Journals journals;
    private JournalTypes journalTypes;
    private AccountTypes accountTypes;

    public JournalsMenu(JournalInputGUI journalInputGUI) {
        super(getBundle("Accounting").getString("JOURNALS"));
        setMnemonic(KeyEvent.VK_P);
        add = new JMenuItem(getBundle("Accounting").getString("ADD_JOURNAL"));
        add.addActionListener(e -> NewJournalGUI.getInstance(journals, journalTypes, accountTypes).setVisible(true));
        add.setEnabled(false);

        manage = new JMenuItem(getBundle("Accounting").getString("MANAGE_JOURNALS"));
        manage.addActionListener(e -> showJournalManager(journals, journalTypes, accountTypes).setVisible(true));
        manage.setEnabled(false);

        add(add);
        add(manage);
    }

    public void setAccounting(Accounting accounting) {
        journals = accounting==null?null:accounting.getJournals();
        journalTypes = accounting==null?null:accounting.getJournalTypes();
        accountTypes = accounting==null?null:accounting.getAccountTypes();
        add.setEnabled(journals!=null);
        manage.setEnabled(journals!=null);
    }
}
