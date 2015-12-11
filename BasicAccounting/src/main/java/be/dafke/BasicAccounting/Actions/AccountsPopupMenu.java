package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.Objects.Accountings;

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
    final AccountManagementActionListener accountManagementActionListener = new AccountManagementActionListener();
    public final String MANAGE = "manage";
    private Accountings accountings;

    public AccountsPopupMenu(final Accountings accountings) {
        this.accountings = accountings;
        manage = new JMenuItem(getBundle("Accounting").getString("MANAGE_ACCOUNT"));
        manage.setActionCommand(MANAGE);
        add(manage);
        manage.addActionListener(this);
    }

    public void actionPerformed(ActionEvent ae) {
        accountManagementActionListener.showAccountManager(accountings.getCurrentObject());
        setVisible(false);
    }
}
