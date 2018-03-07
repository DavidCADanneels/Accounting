package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.JournalTypes;
import be.dafke.BusinessModel.Journals;

import javax.swing.*;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

public class JournalManagementGUI extends JFrame {
    private static final HashMap<Journals, JournalManagementGUI> journalManagementGuis = new HashMap<>();
    private final JournalManagementPanel journalManagementPanel;

    private JournalManagementGUI(Accounts accounts, Journals journals, JournalTypes journalTypes, AccountTypes accountTypes) {
		super(getBundle("Accounting").getString("JOURNAL_MANAGEMENT_TITLE"));
        journalManagementPanel = new JournalManagementPanel(accounts, journals, journalTypes, accountTypes);
		setContentPane(journalManagementPanel);
		pack();
	}

    public static JournalManagementGUI showJournalManager(Accounts accounts, Journals journals, JournalTypes journalTypes, AccountTypes accountTypes) {
        JournalManagementGUI gui = journalManagementGuis.get(journals);
        if(gui == null){
            gui = new JournalManagementGUI(accounts, journals, journalTypes, accountTypes);
            journalManagementGuis.put(journals, gui);
            Main.addFrame(gui);
        }
        return gui;
    }

    public static void fireJournalTypeDataChangedForAll(JournalTypes journalTypes){
        for(JournalManagementGUI journalManagementGUI:journalManagementGuis.values()){
            journalManagementGUI.fireJournalTypeDataChanged(journalTypes);
        }
    }

    public static void fireJournalDataChangedForAll() {
        for(JournalManagementGUI journalManagementGUI:journalManagementGuis.values()){
            journalManagementGUI.fireJournalDataChanged();
        }
    }

    public void fireJournalDataChanged() {
        journalManagementPanel.fireJournalDataChanged();
    }

    public void fireJournalTypeDataChanged(JournalTypes journalTypes){
        journalManagementPanel.fireJournalTypeDataChanged(journalTypes);
    }
}
