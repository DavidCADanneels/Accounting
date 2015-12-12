package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.GUI.JournalManagement.JournalTypeManagementGUI;
import be.dafke.BasicAccounting.Objects.AccountTypes;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.DisposableComponent;

/**
 * Created by ddanneel on 14/02/2015.
 */
public class JournalTypeManagementLauncher {
    public void showJournalTypeManager(AccountTypes accountTypes) {
        String key = "" + accountTypes.hashCode();
        DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
        if(gui == null){
            gui = new JournalTypeManagementGUI(accountTypes);
            ComponentMap.addDisposableComponent(key, gui); // DETAILS
        }
        gui.setVisible(true);
    }
}
