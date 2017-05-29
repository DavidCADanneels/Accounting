package be.dafke.BasicAccounting.Accounts;

import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.VATTransaction;

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

    public void setAccounting(Accounting accounting){
        setAccounts(accounting==null?null:accounting.getAccounts());
        setAccountTypes(accounting==null?null:accounting.getAccountTypes());
    }

    public void setAccounts(Accounts accounts) {
        this.accounts = accounts;
    }

    public void setAccountTypes(AccountTypes accountTypes) {
        this.accountTypes = accountTypes;
    }

    public void manageAccounts(){
        popup.setVisible(false);
        showAccountManager(accounts, accountTypes.getBusinessObjects()).setVisible(true);
    }

    public void addAccount(){
        popup.setVisible(false);
        new NewAccountGUI(accounts, accountTypes.getBusinessObjects()).setVisible(true);
    }

    public abstract void showDetails();
    public abstract void book(boolean debit);
    public abstract void fireAccountDataChanged();
    public abstract void setVatType(VATTransaction.VATType vatType);
}
