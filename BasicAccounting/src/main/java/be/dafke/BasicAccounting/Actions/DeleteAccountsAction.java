package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ObjectModel.Exceptions.NotEmptyException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneel on 15/02/2015.
 */
public class DeleteAccountsAction extends ModifyAccountAction {
    private Accounting accounting;

    public DeleteAccountsAction(Accounting accounting) {
        this.accounting = accounting;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if(!accountList.isEmpty()) {
            ArrayList<String> failed = new ArrayList<String>();
            for(Account account : accountList) {
                try{
                    accounting.getAccounts().removeBusinessObject(account);
                }catch (NotEmptyException e){
                    failed.add(account.getName());
                }
            }
            if (failed.size() > 0) {
                if (failed.size() == 1) {
                    JOptionPane.showMessageDialog(null, failed.get(0) + " " + getBundle("Accounting").getString("ACCOUNT_NOT_EMPTY"));
                } else {
                    StringBuilder builder = new StringBuilder(getBundle("Accounting").getString("MULTIPLE_ACCOUNTS_NOT_EMPTY")+"\r\n");
                    for(String s : failed){
                        builder.append("- ").append(s).append("\r\n");
                    }
                    JOptionPane.showMessageDialog(null, builder.toString());
                }
            }
        }
        ComponentMap.refreshAllFrames();
    }
}
