package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.GUI.JournalManagement.JournalTypeManagementGUI;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.DisposableComponent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ddanneel on 14/02/2015.
 */
public class JournalTypeManagementActionListener implements ActionListener{
    private Accounting accounting;
    public static final String JOURNAL_TYPE_MANAGEMENT = "JournalTypeManagement";

    public JournalTypeManagementActionListener(Accounting accounting) {
        this.accounting=accounting;
    }

    public void actionPerformed(ActionEvent ae) {
        //Accounting accounting = accountings.getCurrentObject();
        String key = accounting.toString() + JOURNAL_TYPE_MANAGEMENT;
        DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
        if(gui == null){
            gui = new JournalTypeManagementGUI(accounting);
            ComponentMap.addDisposableComponent(key, gui); // DETAILS
        }
        gui.setVisible(true);
    }
}
