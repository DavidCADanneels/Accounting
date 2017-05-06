package be.dafke.BasicAccounting.Accounts;

import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Accounts;

import javax.swing.*;

import static be.dafke.BasicAccounting.Accounts.AccountManagementGUI.showAccountManager;

/**
 * Created by ddanneels on 1/05/2017.
 */
public abstract class AccountsGUI extends JPanel {
    private JPopupMenu popup;
    protected Accounts accounts;
    protected AccountTypes accountTypes;

    public void setPopup(JPopupMenu popup) {
        this.popup = popup;
    }

    public void setAccounts(Accounts accounts) {
        this.accounts = accounts;
    }

    public void setAccountTypes(AccountTypes accountTypes) {
        this.accountTypes = accountTypes;
    }

    public void manageAccounts(){
        popup.setVisible(false);
        showAccountManager(accounts, accountTypes).setVisible(true);
    }

    public void addAccount(){
        popup.setVisible(false);
        new NewAccountGUI(accounts, accountTypes).setVisible(true);
    }

    public abstract void showDetails();
    public abstract void book(boolean debit);
}
