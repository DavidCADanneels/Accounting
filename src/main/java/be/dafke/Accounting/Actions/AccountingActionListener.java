package be.dafke.Accounting.Actions;

import be.dafke.Accounting.Exceptions.DuplicateNameException;
import be.dafke.Accounting.Exceptions.EmptyNameException;
import be.dafke.Accounting.GUI.ComponentMap;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Accountings;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: Dafke
 * Date: 26/02/13
 * Time: 6:36
 */
public class AccountingActionListener implements ActionListener {

    private Accountings accountings;

    public AccountingActionListener(Accountings accountings){
        this.accountings = accountings;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String actionCommand = ae.getActionCommand();
        if (actionCommand.equals(ComponentMap.NEW_ACCOUNTING)) {
            String name = JOptionPane.showInputDialog(null, "Enter a name");
            try {
                Accounting accounting = accountings.addAccounting(name);
                accountings.setCurrentAccounting(name);
                ComponentMap.addAccountingComponents(accounting);
                JOptionPane.showMessageDialog(null, "Please create a Journal.");
                String key = accounting.toString()+ComponentMap.JOURNAL_MANAGEMENT;
                ComponentMap.getDisposableComponent(key).setVisible(true);
            } catch (DuplicateNameException e) {
                JOptionPane.showMessageDialog(null, "There is already an accounting with the name \""+name+"\".\r\n"+
                        "Please provide a new name.");
            } catch (EmptyNameException e) {
                JOptionPane.showMessageDialog(null, "The name cannot be empty.\r\nPlease provide a new name.");
            }
            ComponentMap.refreshAllFrames();
        } else if(actionCommand.startsWith(ComponentMap.OPEN_ACCOUNTING)){
            String accountingName = actionCommand.replaceAll(ComponentMap.OPEN_ACCOUNTING,"");
            accountings.setCurrentAccounting(accountingName);
        } else {
            String key = accountings.getCurrentAccounting().toString() + actionCommand;
            ComponentMap.getDisposableComponent(key).setVisible(true);
        }
        ComponentMap.refreshAllFrames();
    }
}
