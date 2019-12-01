package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.AccountTypes
import be.dafke.Accounting.BusinessModel.Accounts
import be.dafke.Accounting.BusinessModel.JournalTypes
import be.dafke.Accounting.BusinessModel.Journals

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class JournalManagementGUI extends JFrame {
    static final HashMap<Journals, JournalManagementGUI> journalManagementGuis = new HashMap<>()
    final JournalManagementPanel journalManagementPanel

    JournalManagementGUI(Accounts accounts, Journals journals, JournalTypes journalTypes, AccountTypes accountTypes) {
        super(getBundle("Accounting").getString("JOURNAL_MANAGEMENT_TITLE"))
        journalManagementPanel = new JournalManagementPanel(accounts, journals, journalTypes, accountTypes)
        setContentPane(journalManagementPanel)
        pack()
    }

    static JournalManagementGUI getInstance(Accounts accounts, Journals journals, JournalTypes journalTypes, AccountTypes accountTypes) {
        JournalManagementGUI gui = journalManagementGuis.get(journals)
        if(gui == null){
            gui = new JournalManagementGUI(accounts, journals, journalTypes, accountTypes)
            journalManagementGuis.put(journals, gui)
            Main.addFrame(gui)
        }
        gui
    }

    static void fireJournalTypeDataChangedForAll(JournalTypes journalTypes){
        for(JournalManagementGUI journalManagementGUI:journalManagementGuis.values()){
            journalManagementGUI.fireJournalTypeDataChanged(journalTypes)
        }
    }

    static void fireJournalDataChangedForAll() {
        for(JournalManagementGUI journalManagementGUI:journalManagementGuis.values()){
            journalManagementGUI.fireJournalDataChanged()
        }
    }

    void fireJournalDataChanged() {
        journalManagementPanel.fireJournalDataChanged()
    }

    void fireJournalTypeDataChanged(JournalTypes journalTypes){
        journalManagementPanel.fireJournalTypeDataChanged(journalTypes)
    }
}
