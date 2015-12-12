package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.GUI.JournalManagement.JournalManagementGUI;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.DisposableComponent;

/**
 * Created by ddanneel on 14/02/2015.
 */
public class JournalManagementLauncher {
    public static final String JOURNAL_MANAGEMENT = "JournalManagement";

    public void showJournalManager(Accounting accounting) {
        String key = JOURNAL_MANAGEMENT;
        DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
        if(gui == null){
            gui = new JournalManagementGUI(accounting);
            ComponentMap.addDisposableComponent(key, gui); // DETAILS
        }
        gui.setVisible(true);
    }
}
