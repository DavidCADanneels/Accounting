package be.dafke.Accounting.BasicAccounting.Journals.Management


import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.AccountTypes
import be.dafke.Accounting.BusinessModel.Accounts
import be.dafke.Accounting.BusinessModel.JournalTypes

import javax.swing.*

import static java.util.ResourceBundle.getBundle

class JournalTypeManagementGUI extends JFrame {
    static final HashMap<JournalTypes, JournalTypeManagementGUI> journalTypeManagementGuis = new HashMap<>()
    final JournalTypeManagementPanel journalTypeManagementPanel

    JournalTypeManagementGUI(Accounts accounts, JournalTypes journalTypes, AccountTypes accountTypes) {
        super(getBundle("Accounting").getString("JOURNAL_TYPE_MANAGEMENT_TITLE"))
        journalTypeManagementPanel = new JournalTypeManagementPanel(accounts, journalTypes, accountTypes)
        setContentPane(journalTypeManagementPanel)
        pack()
    }

    static JournalTypeManagementGUI getInstance(Accounts accounts, JournalTypes journalTypes, AccountTypes accountTypes) {
        JournalTypeManagementGUI gui = journalTypeManagementGuis.get(journalTypes)
        if (gui == null) {
            gui = new JournalTypeManagementGUI(accounts, journalTypes, accountTypes)
            journalTypeManagementGuis.put(journalTypes, gui)
            Main.addFrame(gui)
        }
        return gui
    }
}