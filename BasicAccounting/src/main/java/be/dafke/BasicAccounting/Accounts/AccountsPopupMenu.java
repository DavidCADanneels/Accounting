package be.dafke.BasicAccounting.Accounts;

import javax.swing.*;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 10/12/2015.
 */
public class AccountsPopupMenu extends JPopupMenu {
    private final JMenuItem manage,add;
    public final String MANAGE = "manage";
    public final String ADD = "add";

    public AccountsPopupMenu(AccountsListGUI accountsListGUI) {
        manage = new JMenuItem(getBundle("Accounting").getString("MANAGE_ACCOUNT"));
        add = new JMenuItem(getBundle("Accounting").getString("ADD_ACCOUNT"));

        manage.setActionCommand(MANAGE);
        add.setActionCommand(ADD);

        add(manage);
        add(add);

        manage.addActionListener(e -> accountsListGUI.manageAccount());
        add.addActionListener(e -> accountsListGUI.addAccount());
    }
}
