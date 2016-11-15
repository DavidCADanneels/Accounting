package be.dafke.BusinessActions;

import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.AccountType;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Accounts;

import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.ObjectModel.Exceptions.NotEmptyException;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static be.dafke.BusinessActions.ActionUtils.CHOOSE_NEW_TYPE_FOR;
import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 29/12/2015.
 */
public class AccountActions {
    public static void deleteAccounts(List<Account> accountList, Accounts accounts){
        if(!accountList.isEmpty()) {
            ArrayList<String> failed = new ArrayList<String>();
            for(Account account : accountList) {
                try{
                    accounts.removeBusinessObject(account);
                }catch (NotEmptyException e){
                    failed.add(account.getName());
                }
            }
            if (failed.size() > 0) {
                if (failed.size() == 1) {
                    ActionUtils.showErrorMessage(ActionUtils.ACCOUNT_NOT_EMPTY, failed.get(0));
                } else {
                    StringBuilder builder = new StringBuilder(getBundle("BusinessActions").getString("MULTIPLE_ACCOUNTS_NOT_EMPTY")+"\n");
                    for(String s : failed){
                        builder.append("- ").append(s).append("\n");
                    }
                    JOptionPane.showMessageDialog(null, builder.toString());
                }
            }
        }
        ////ComponentMap.refreshAllFrames();
    }

    public static void modifyDefaultAmounts(List<Account> accountList, Accounts accounts){
        if(!accountList.isEmpty()) {
            for(Account account : accountList){
                BigDecimal defaultAmount = account.getDefaultAmount();
                boolean retry = true;
                while(retry){
                    String amount = JOptionPane.showInputDialog(account.getName() + ": " + getBundle("BusinessActions").getString("DEFAULT_AMOUNT"), defaultAmount);
                    try{
                        if (amount!=null) {
                            defaultAmount = new BigDecimal(amount);
                            defaultAmount = defaultAmount.setScale(2);
                            account.setDefaultAmount(defaultAmount);
                        }
                        retry = false;
                    } catch (NumberFormatException nfe) {
                    }
                }
            }
        }
        //ComponentMap.refreshAllFrames();
    }

    public static void modifyAccountNames(List<Account> accountList, Accounts accounts) {
        if (!accountList.isEmpty()) {
            for (Account account : accountList) {
                String oldName = account.getName();
                boolean retry = true;
                while (retry) {
                    String newName = JOptionPane.showInputDialog(getBundle("BusinessActions").getString("NEW_NAME"), oldName.trim());
                    try {
                        if (newName != null && !oldName.trim().equals(newName.trim())) {
                            accounts.modifyAccountName(oldName, newName);
                            //ComponentMap.refreshAllFrames();
                        }
                        retry = false;
                    } catch (DuplicateNameException e) {
                        ActionUtils.showErrorMessage(ActionUtils.ACCOUNT_DUPLICATE_NAME,newName.trim());
                    } catch (EmptyNameException e) {
                        ActionUtils.showErrorMessage(ActionUtils.ACCOUNT_NAME_EMPTY);
                    }
                }
            }
        }
        //ComponentMap.refreshAllFrames();
    }

    public static void modifyAccountTypes(List<Account> accountList, AccountTypes accountTypes){
        if(!accountList.isEmpty()) {
            boolean singleMove;
            if (accountList.size() == 1) {
                singleMove = true;
            } else {
                int option = JOptionPane.showConfirmDialog(null, getBundle("BusinessActions").getString("APPLY_SAME_TYPE_FOR_ALL_ACCOUNTS"),
                        getBundle("BusinessActions").getString("ALL"),
                        JOptionPane.YES_NO_OPTION);
                singleMove = (option == JOptionPane.YES_OPTION);
            }
            if (singleMove) {
                Object[] types = accountTypes.getBusinessObjects().toArray();
                int nr = JOptionPane.showOptionDialog(null, ActionUtils.getFormattedString(ActionUtils.CHOOSE_NEW_TYPE),
                        ActionUtils.getFormattedString(ActionUtils.CHANGE_TYPE),
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, types, null);
                if (nr != JOptionPane.CANCEL_OPTION && nr != JOptionPane.CLOSED_OPTION) {
                    for (Account account : accountList) {
                        account.setType((AccountType) types[nr]);
                    }
                }
            } else {
                for (Account account : accountList) {
                    Object[] types = accountTypes.getBusinessObjects().toArray();
                    int nr = JOptionPane.showOptionDialog(null, ActionUtils.getFormattedString(CHOOSE_NEW_TYPE_FOR,account.getName()),
                            ActionUtils.getFormattedString(ActionUtils.CHANGE_TYPE), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, types,
                            account.getType());
                    if (nr != JOptionPane.CANCEL_OPTION && nr != JOptionPane.CLOSED_OPTION) {
                        account.setType((AccountType) types[nr]);
                    }
                }
            }
        }
        //ComponentMap.refreshAllFrames();
    }

}
