package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.AccountType;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.ComponentModel.ComponentMap;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneel on 15/02/2015.
 */
public class ModifyAccountTypesAction extends ModifyAccountAction {
    private Accounting accounting;

    public ModifyAccountTypesAction(Accounting accounting) {
        this.accounting = accounting;
    }

    public void actionPerformed(ActionEvent e) {
        if(!accountList.isEmpty()) {
            boolean singleMove;
            if (accountList.size() == 1) {
                singleMove = true;
            } else {
                int option = JOptionPane.showConfirmDialog(null, getBundle("Accounting").getString("APPLY_SAME_TYPE_FOR_ALL_ACCOUNTS"),
                        getBundle("Accounting").getString("ALL"),
                        JOptionPane.YES_NO_OPTION);
                singleMove = (option == JOptionPane.YES_OPTION);
            }
            if (singleMove) {
                Object[] types = accounting.getAccountTypes().getBusinessObjects().toArray();
                int nr = JOptionPane.showOptionDialog(null, getBundle("Accounting").getString("CHOOSE_NEW_TYPE"),
                        getBundle("Accounting").getString("CHANGE_TYPE"),
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, types, null);
                if (nr != JOptionPane.CANCEL_OPTION && nr != JOptionPane.CLOSED_OPTION) {
                    for (Account account : accountList) {
                        account.setType((AccountType) types[nr]);
                    }
                }
            } else {
                for (Account account : accountList) {
                    Object[] types = accounting.getAccountTypes().getBusinessObjects().toArray();
                    int nr = JOptionPane.showOptionDialog(null, getBundle("Accounting").getString("CHOOSE_NEW_TYPE_FOR")
                                    + " " + account.getName(),
                            getBundle("Accounting").getString("CHANGE_TYPE"), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, types,
                            account.getType());
                    if (nr != JOptionPane.CANCEL_OPTION && nr != JOptionPane.CLOSED_OPTION) {
                        account.setType((AccountType) types[nr]);
                    }
                }
            }
        }
        ComponentMap.refreshAllFrames();
    }
}
