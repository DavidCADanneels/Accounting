package be.dafke.BasicAccounting.Accounts;

import javax.swing.*;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 10/12/2015.
 */
public class AccountsTablePopupMenu extends JPopupMenu {
    private final JMenuItem manage, add, debit, credit, details;

    public AccountsTablePopupMenu(AccountsTableGUI table) {

        manage = new JMenuItem(getBundle("Accounting").getString("MANAGE_ACCOUNT"));
        add = new JMenuItem(getBundle("Accounting").getString("ADD_ACCOUNT"));
        debit = new JMenuItem(getBundle("Accounting").getString("DEBIT"));
        credit = new JMenuItem(getBundle("Accounting").getString("CREDIT"));
        details = new JMenuItem(getBundle("Accounting").getString("VIEW_ACCOUNT"));

        add(debit);
        add(credit);
        add(details);
        addSeparator();
        add(add);
        add(manage);

        debit.addActionListener(e -> table.book(true));
        credit.addActionListener(e -> table.book(false));
        manage.addActionListener(e -> table.manageAccounts());
        add.addActionListener(e -> table.addAccount());
        details.addActionListener(e -> table.showDetails());
    }
}