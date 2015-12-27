package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.Objects.AccountTypes;
import be.dafke.BasicAccounting.Objects.Accounts;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 10/12/2015.
 */
public class AccountsPopupMenu extends JPopupMenu implements ActionListener{
    private final JMenuItem manage;
    final AccountActions accountActions = new AccountActions();
    public final String MANAGE = "manage";
    private Accounts accounts;
    private AccountTypes accountTypes;

    public AccountsPopupMenu(Accounts accounts, AccountTypes accountTypes) {
        this.accounts = accounts;
        this.accountTypes = accountTypes;
        manage = new JMenuItem(getBundle("Accounting").getString("MANAGE_ACCOUNT"));
        manage.setActionCommand(MANAGE);
        add(manage);
        manage.addActionListener(this);
    }

    public void actionPerformed(ActionEvent ae) {
        accountActions.showAccountManager(accounts, accountTypes);
        setVisible(false);
    }
}
