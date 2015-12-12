package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.GUI.AccountManagement.AccountManagementGUI;
import be.dafke.BasicAccounting.GUI.Details.AccountDetails;
import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.AccountType;
import be.dafke.BasicAccounting.Objects.AccountTypes;
import be.dafke.BasicAccounting.Objects.Accounts;
import be.dafke.BasicAccounting.Objects.Booking;
import be.dafke.BasicAccounting.Objects.Journals;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.DisposableComponent;
import be.dafke.ComponentModel.RefreshableTableFrame;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.ObjectModel.Exceptions.NotEmptyException;

import javax.swing.JOptionPane;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneel on 14/02/2015.
 */
public class AccountActions {
    public static void showAccountManager(Accounts accounts, AccountTypes accountTypes) {
        String key = ""+accounts.hashCode();
        DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
        if(gui == null){
            gui = new AccountManagementGUI(accounts, accountTypes);
            ComponentMap.addDisposableComponent(key, gui); // DETAILS
        }
        gui.setVisible(true);
    }

    public static RefreshableTableFrame<Booking> showDetails(Account account, Journals journals){
        String key = "Details" + account.hashCode();
        DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
        if(gui == null){
            gui = new AccountDetails(account, journals);
            ComponentMap.addDisposableComponent(key, gui); // DETAILS
        }
        gui.setVisible(true);
        return (RefreshableTableFrame<Booking>)gui;
    }

    public static void modifyAccountNames(List<Account> accountList, Accounts accounts) {
        if (!accountList.isEmpty()) {
            for (Account account : accountList) {
                String oldName = account.getName();
                boolean retry = true;
                while (retry) {
                    String newName = JOptionPane.showInputDialog(getBundle("Accounting").getString("NEW_NAME"), oldName.trim());
                    try {
                        if (newName != null && !oldName.trim().equals(newName.trim())) {
                            accounts.modifyAccountName(oldName, newName);
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

    public static void modifyAccountTypes(List<Account> accountList, AccountTypes accountTypes){
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
                Object[] types = accountTypes.getBusinessObjects().toArray();
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
                    Object[] types = accountTypes.getBusinessObjects().toArray();
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

    public static void modifyDefaultAmounts(List<Account> accountList, Accounts accounts){
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
