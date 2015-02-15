package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.ComponentModel.ComponentMap;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneel on 15/02/2015.
 */
public class ModifyAccountDefaultAmountsAction extends ModifyAccountAction {
    @Override
    public void actionPerformed(ActionEvent e) {
        if(!accountList.isEmpty()) {
            for(Account account : accountList){
                BigDecimal defaultAmount = account.getDefaultAmount();
                boolean retry = true;
                while(retry){
                    String amount = JOptionPane.showInputDialog(account.getName() + ": " + getBundle("Accounting").getString("DEFAULT_AMOUNT"), defaultAmount);
                    try{
                        defaultAmount = new BigDecimal(amount);
                        defaultAmount = defaultAmount.setScale(2);
                        account.setDefaultAmount(defaultAmount);
                        retry = false;
                    } catch (NumberFormatException nfe) {
                    }
                }
            }
        }
        ComponentMap.refreshAllFrames();
    }
}
