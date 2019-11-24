package be.dafke.BasicAccounting.Balances;

import be.dafke.BasicAccounting.Accounts.AccountDetails.AccountDetailsGUI;
import be.dafke.BasicAccounting.Accounts.NewAccountDialog;
import be.dafke.Accounting.BusinessModel.Account;
import be.dafke.Accounting.BusinessModel.AccountTypes;
import be.dafke.Accounting.BusinessModel.Accounting;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.awt.Point;
import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

public class BalancePopupMenu extends JPopupMenu {
    private final JMenuItem details, edit;
    private Accounting accounting;
    private SelectableTable<Account> gui;

    public BalancePopupMenu(Accounting accounting, SelectableTable<Account> gui) {
        this.accounting = accounting;
        this.gui = gui;

        details = new JMenuItem(getBundle("Accounting").getString("GO_TO_ACCOUNT_DETAILS"));
        details.addActionListener(e -> showDetails());
        add(details);

        edit = new JMenuItem(getBundle("Accounting").getString("EDIT_ACCOUNT"));
        edit.addActionListener(e -> editAccount());
        add(edit);
    }

    public void showDetails() {
        ArrayList<Account> accounts = gui.getSelectedObjects();
        for(Account account: accounts) {
            Point locationOnScreen = getLocationOnScreen();
            AccountDetailsGUI.getAccountDetails(locationOnScreen, account, accounting.getJournals());
        }
        setVisible(false);
    }

    public void editAccount(){
        AccountTypes accountTypes = accounting.getAccountTypes();
        ArrayList<Account> accounts = gui.getSelectedObjects();
        for(Account account: accounts) {
            NewAccountDialog newAccountDialog = new NewAccountDialog(accounting.getAccounts(), accountTypes.getBusinessObjects());
            newAccountDialog.setAccount(account);
            newAccountDialog.setVisible(true);
        }
        setVisible(false);
    }
}
