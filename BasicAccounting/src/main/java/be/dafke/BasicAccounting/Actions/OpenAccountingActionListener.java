package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.ComponentModel.ComponentMap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ddanneel on 14/02/2015.
 */
public class OpenAccountingActionListener implements ActionListener{
    public static final String OPEN_ACCOUNTING = "OpenAccounting";
    private Accountings accountings;
    private Accounting accounting;

    public OpenAccountingActionListener(Accountings accountings, Accounting accounting) {
        this.accountings = accountings;
        this.accounting = accounting;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        accountings.setCurrentObject(accounting.getName());
        ComponentMap.refreshAllFrames();
    }
}
