package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.GUI.AccountManagement.NewAccountGUI;
import be.dafke.BasicAccounting.Objects.Accounting;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ddanneel on 14/02/2015.
 */
public class NewAccountActionListener implements ActionListener{
    private Accounting accounting;

    public NewAccountActionListener(Accounting accounting) {
        this.accounting=accounting;
    }

    public void actionPerformed(ActionEvent e) {
        new NewAccountGUI(accounting).setVisible(true);
        // or
        /*
        NewAccountGUI gui = new NewAccountGUI(accounting);
        ComponentMap.addRefreshableComponent(gui);
        gui.setVisible(true);
        */
    }
}
