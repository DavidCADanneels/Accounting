package be.dafke.BasicAccounting.Accounts;

import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Accounts;

import javax.swing.*;

import static be.dafke.BasicAccounting.Accounts.AccountManagementGUI.showAccountManager;
import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 10/12/2015.
 */
public class AccountsPopupMenu extends JPopupMenu {
    private final JMenuItem manage,add;
    public final String MANAGE = "manage";
    public final String ADD = "add";
    private AccountTypes accountTypes;
    private Accounts accounts;

    public AccountsPopupMenu() {
        manage = new JMenuItem(getBundle("Accounting").getString("MANAGE_ACCOUNT"));
        add = new JMenuItem(getBundle("Accounting").getString("ADD_ACCOUNT"));
        manage.setActionCommand(MANAGE);
        add.setActionCommand(ADD);
        add(manage);
        add(add);
        manage.addActionListener(e -> {
            showAccountManager(accounts, accountTypes).setVisible(true);
            setVisible(false);
        });
        add.addActionListener(e -> {
            new NewAccountGUI(accounts, accountTypes).setVisible(true);
            setVisible(false);
        });
    }

    public void setAccounting(Accounting accounting) {
        setAccounts(accounting == null ? null : accounting.getAccounts());
        setAccountTypes(accounting == null ? null : accounting.getAccountTypes());
    }

    public void setAccountTypes(AccountTypes accountTypes) {
        this.accountTypes = accountTypes;
    }

    public void setAccounts(Accounts accounts) {
        this.accounts = accounts;
    }

}
