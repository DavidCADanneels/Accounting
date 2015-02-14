package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.ComponentModel.ComponentMap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: Dafke
 * Date: 26/02/13
 * Time: 6:36
 */
public class AccountingActionListener implements ActionListener {
    protected final Accountings accountings;
    public static final String MAIN = "MainPanel";

    public AccountingActionListener(Accountings accountings){
        this.accountings = accountings;
    }

    /**
     * This method is only used in extensions
     * @param ae
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        String actionCommand = ae.getActionCommand();
        String key = accountings.getCurrentObject().toString() + actionCommand;
        ComponentMap.getDisposableComponent(key).setVisible(true);
    }
}
