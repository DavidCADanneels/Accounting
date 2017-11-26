package be.dafke.BasicAccounting.Accounts;

import be.dafke.BusinessModel.*;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import java.util.ArrayList;

import static be.dafke.BasicAccounting.Accounts.AccountManagementGUI.showAccountManager;

/**
 * Created by ddanneels on 1/05/2017.
 */
public abstract class AccountsGUI extends JPanel {
    private JPopupMenu popup;
    protected Accounts accounts;
    protected AccountsList accountsList;
    private JournalType journalType;

    public void setPopup(JPopupMenu popup) {
        this.popup = popup;
    }

    public void setAccounting(Accounting accounting){
        setAccounts(accounting==null?null:accounting.getAccounts());
    }

    public void setAccounts(Accounts accounts) {
        this.accounts = accounts;
    }

    public void setJournalType(JournalType journalType) {
        this.journalType = journalType;
    }

    public void setAccountsList(AccountsList accountsList) {
        this.accountsList = accountsList;
    }

    public void manageAccounts(){
        popup.setVisible(false);
        ArrayList<AccountType> accountTypes = accountsList.getAccountTypes();
        showAccountManager(accounts, accountTypes).setVisible(true);
    }

    public void addAccount(){
        popup.setVisible(false);
        ArrayList<AccountType> accountTypes = accountsList.getAccountTypes();
        new NewAccountGUI(accounts, accountTypes).setVisible(true);
    }

    public abstract void showDetails();
    public abstract void book(boolean debit);
    public abstract void fireAccountDataChanged();
    public abstract void setVatType(VATTransaction.VATType vatType);

    public JournalType getJournalType() {
        return journalType;
    }
}
