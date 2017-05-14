package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.JournalTypes;
import be.dafke.BusinessModel.Journals;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.KeyEvent;

import static be.dafke.BasicAccounting.Journals.JournalManagementGUI.showJournalManager;
import static be.dafke.BasicAccounting.Journals.JournalTypeManagementGUI.showJournalTypeManager;
import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 27/12/2015.
 */
public class JournalsMenu extends JMenu {
    private static JMenuItem add, manage, types;

    private Journals journals;
    private JournalTypes journalTypes;
    private Accounts accounts;
    private AccountTypes accountTypes;
    private JournalInputGUI journalInputGUI;

    public JournalsMenu(JournalInputGUI journalInputGUI) {
        super(getBundle("Accounting").getString("JOURNALS"));
        this.journalInputGUI = journalInputGUI;
        setMnemonic(KeyEvent.VK_P);
        add = new JMenuItem(getBundle("Accounting").getString("ADD_JOURNAL"));
        add.addActionListener(e -> NewJournalGUI.getInstance(accounts, journals, journalTypes, accountTypes).setVisible(true));
        add.setEnabled(false);

        manage = new JMenuItem(getBundle("Accounting").getString("MANAGE_JOURNALS"));
        manage.addActionListener(e -> showJournalManager(accounts, journals, journalTypes, accountTypes).setVisible(true));
        manage.setEnabled(false);

        types = new JMenuItem(getBundle("Accounting").getString("MANAGE_JOURNAL_TYPES"));
        types.addActionListener(e -> showJournalTypeManager(accounts, journalTypes,accountTypes));
        types.setEnabled(false);

        add(add);
        add(manage);
        add(types);
    }

    public void setAccounting(Accounting accounting) {
        setJournals(accounting==null?null:accounting.getJournals());
        journalTypes = accounting==null?null:accounting.getJournalTypes();
        accountTypes = accounting==null?null:accounting.getAccountTypes();
        accounts = accounting==null?null:accounting.getAccounts();
        add.setEnabled(journals!=null);
        manage.setEnabled(journals!=null);
        types.setEnabled(journals!=null);
        reload();
    }

    public void setJournals(Journals journals) {
        this.journals = journals;
        reload();
    }

    private void reload(){
        removeAll();
        add(add);
        add(manage);
        add(types);
        if(journals!=null){
            addSeparator();
            journals.getBusinessObjects().stream()
                    .forEach(journal -> {
                        JMenuItem details = new JMenuItem(journal.getName()+ " (details)");
                        details.addActionListener(e -> JournalDetails.getJournalDetails(journal,journals,journalInputGUI));
                        add(details);
                    });
            addSeparator();
            journals.getBusinessObjects().stream()
                    .forEach(journal -> {
                        JMenuItem input = new JMenuItem(journal.getName()+ " (input)");
                        input.addActionListener(e -> Main.setJournal(journal));
                        add(input);
                    });
        }
    }
}
