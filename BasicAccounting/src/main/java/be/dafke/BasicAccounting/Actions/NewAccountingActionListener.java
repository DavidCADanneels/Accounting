package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ddanneel on 14/02/2015.
 */
public class NewAccountingActionListener implements ActionListener {
    private Accountings accountings;

    public NewAccountingActionListener(Accountings accountings) {
        this.accountings = accountings;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String name = JOptionPane.showInputDialog(null, "Enter a name");
        try {
            Accounting accounting = new Accounting();
            accounting.setName(name);
            accountings.addBusinessObject(accounting);
            accountings.setCurrentObject(name);
        } catch (DuplicateNameException e) {
            JOptionPane.showMessageDialog(null, "There is already an accounting with the name \""+name+"\".\r\n"+
                    "Please provide a new name.");
        } catch (EmptyNameException e) {
            JOptionPane.showMessageDialog(null, "The name cannot be empty.\r\nPlease provide a new name.");
        }
        ComponentMap.refreshAllFrames();
    }
}
