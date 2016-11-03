package be.dafke.BasicAccounting;

import be.dafke.BasicAccounting.Accounts.NewAccountGUI;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Accounts;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 10/12/2015.
 */
public class AccountsPopupMenu extends JPopupMenu implements ActionListener{
    private final JMenuItem manage,add;
    public final String MANAGE = "manage";
    public final String ADD = "add";
    private Accounts accounts;
    private AccountTypes accountTypes;

    public AccountsPopupMenu(Accounts accounts, AccountTypes accountTypes) {
        this.accounts = accounts;
        this.accountTypes = accountTypes;
        manage = new JMenuItem(getBundle("Accounting").getString("MANAGE_ACCOUNT"));
        add = new JMenuItem(getBundle("Accounting").getString("ADD_ACCOUNT"));
        manage.setActionCommand(MANAGE);
        add.setActionCommand(ADD);
        add(manage);
        add(add);
        manage.addActionListener(this);
        add.addActionListener(this);
    }

    public void actionPerformed(ActionEvent ae) {
        String actionCommand = ae.getActionCommand();
        if(MANAGE.equals(actionCommand)) {
            GUIActions.showAccountManager(accounts, accountTypes);
        } else if(ADD.equals(actionCommand)){
            new NewAccountGUI(accounts, accountTypes).setVisible(true);
        }
        setVisible(false);
    }
}
