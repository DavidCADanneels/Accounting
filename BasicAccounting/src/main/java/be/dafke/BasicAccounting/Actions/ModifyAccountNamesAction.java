package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneel on 15/02/2015.
 */
public class ModifyAccountNamesAction extends ModifyAccountAction {
    private Accounting accounting;

    public ModifyAccountNamesAction(Accounting accounting) {
        this.accounting = accounting;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (!accountList.isEmpty()) {
            for (Account account : accountList) {
                String oldName = account.getName();
                boolean retry = true;
                while (retry) {
                    String newName = JOptionPane.showInputDialog(getBundle("Accounting").getString("NEW_NAME"), oldName.trim());
                    try {
                        if (newName != null && !oldName.trim().equals(newName.trim())) {
                            accounting.getAccounts().modifyAccountName(oldName, newName);
                            ComponentMap.refreshAllFrames();
                        }
                        retry = false;
                    } catch (DuplicateNameException e) {
                        JOptionPane.showMessageDialog(null, getBundle("Accounting").getString("ACCOUNT_DUPLICATE_NAME") +
                                " \"" + newName.trim() + "\".\r\n" +
                                getBundle("Accounting").getString("PROVIDE_NEW_NAME"));
                    } catch (EmptyNameException e) {
                        JOptionPane.showMessageDialog(null, "Account name cannot be empty" + "\r\n" +
                                getBundle("Accounting").getString("PROVIDE_NEW_NAME"));
                    }
                }
            }
        }
        ComponentMap.refreshAllFrames();
    }
}
