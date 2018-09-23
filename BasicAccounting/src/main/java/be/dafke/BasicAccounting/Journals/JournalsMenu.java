package be.dafke.BasicAccounting.Journals;

import be.dafke.BusinessModel.*;
import be.dafke.BusinessModelDao.JournalsIO;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import java.awt.*;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 27/12/2015.
 */
public class JournalsMenu extends JMenu {
    private Accounting accounting;
    private JMenuItem add, manage, types, generatePdf;

    private Journals journals;
    private JournalTypes journalTypes;
    private Accounts accounts;
    private AccountTypes accountTypes;
//    private JournalEditPanel journalEditPanel;
    private Transactions transactions;

    public JournalsMenu() {
        super(getBundle("Accounting").getString("JOURNALS"));
//        this.journalEditPanel = journalEditPanel;
//        setMnemonic(KeyEvent.VK_P);
        add = new JMenuItem(getBundle("Accounting").getString("ADD_JOURNAL"));
        add.addActionListener(e -> {
            Point locationOnScreen = getLocationOnScreen();
            NewJournalGUI newJournalGUI = NewJournalGUI.getInstance(accounts, journals, journalTypes, accountTypes);
            newJournalGUI.setLocation(locationOnScreen);
            newJournalGUI.setVisible(true);
        });
        add.setEnabled(false);

        manage = new JMenuItem(getBundle("Accounting").getString("MANAGE_JOURNALS"));
        manage.addActionListener(e -> {
            Point locationOnScreen = getLocationOnScreen();
            JournalManagementGUI journalManagementGUI = JournalManagementGUI.getInstance(accounts, journals, journalTypes, accountTypes);
            journalManagementGUI.setLocation(locationOnScreen);
            journalManagementGUI.setVisible(true);
        });
        manage.setEnabled(false);

        types = new JMenuItem(getBundle("Accounting").getString("MANAGE_JOURNAL_TYPES"));
        types.addActionListener(e -> {
            Point locationOnScreen = getLocationOnScreen();
            JournalTypeManagementGUI journalTypeManagementGUI = JournalTypeManagementGUI.getInstance(accounts, journalTypes, accountTypes);
            journalTypeManagementGUI.setLocation(locationOnScreen);
            journalTypeManagementGUI.setVisible(true);
        });
        types.setEnabled(false);

        generatePdf = new JMenuItem(getBundle("BusinessModel").getString("GENERATE_PDF"));
        generatePdf.addActionListener(e -> JournalsIO.writeJournalPdfFiles(accounting));
        generatePdf.setEnabled(false);

        add(add);
        add(manage);
        add(types);
        add(generatePdf);
    }

    public void setAccounting(Accounting accounting) {
        this.accounting = accounting;
        setJournals(accounting==null?null:accounting.getJournals());
        transactions = accounting==null?null:accounting.getTransactions();
        journalTypes = accounting==null?null:accounting.getJournalTypes();
        accountTypes = accounting==null?null:accounting.getAccountTypes();
        accounts = accounting==null?null:accounting.getAccounts();
        add.setEnabled(journals!=null);
        manage.setEnabled(journals!=null);
        types.setEnabled(journals!=null);
        generatePdf.setEnabled(journals!=null);
        fireJournalDataChanged();
    }

    public void setJournals(Journals journals) {
        this.journals = journals;
        fireJournalDataChanged();
    }

    public void fireJournalDataChanged(){
        removeAll();
        add(add);
        add(manage);
        add(types);
        add(generatePdf);
        if(journals!=null){
            addSeparator();
            journals.getBusinessObjects().stream()
                    .forEach(journal -> {
                        JMenuItem details = new JMenuItem(journal.getName());
                        details.addActionListener(e -> {
                            Point locationOnScreen = getLocationOnScreen();
                            JournalDetailsGUI.getJournalDetails(locationOnScreen,journal,journals);
                        });
                        add(details);
                    });
            addSeparator();
            JMenuItem master = new JMenuItem("Master");
            // FIXME: create other viewer for Master Transactions (no colors, what about ID's? use "MA1 (DIV1)" or just "DIV1" (or "MA1")
            // (or use BusinessCollection<Transaction> iso Journal in JournalDetailsGUI)
            master.addActionListener(e -> {
                Point locationOnScreen = getLocationOnScreen();
                JournalDetailsGUI.getJournalDetails(locationOnScreen, transactions,journals);
            });
            add(master);
        }
    }
}
