package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.GUI.JournalManagement.JournalManagementGUI;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.DisposableComponent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ddanneel on 14/02/2015.
 */
public class JournalManagementActionListener implements ActionListener{
    private Accountings accountings;
    public static final String JOURNAL_MANAGEMENT = "JournalManagement";

    public JournalManagementActionListener(Accountings accountings) {
        this.accountings=accountings;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        Accounting accounting = accountings.getCurrentObject();
        String key = accounting.toString() + JOURNAL_MANAGEMENT;
        DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
        if(gui == null){
            gui = new JournalManagementGUI(accounting);
            ComponentMap.addDisposableComponent(key, gui); // DETAILS
        }
        gui.setVisible(true);
    }
}
