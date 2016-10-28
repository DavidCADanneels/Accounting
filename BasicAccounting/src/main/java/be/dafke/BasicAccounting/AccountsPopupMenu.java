package be.dafke.BasicAccounting;

import be.dafke.BusinessModel.Accounting;

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
    public final String MANAGE = "manage";
    private Accounting accounting;

    public AccountsPopupMenu(Accounting accounting) {
        this.accounting = accounting;
        manage = new JMenuItem(getBundle("Accounting").getString("MANAGE_ACCOUNT"));
        manage.setActionCommand(MANAGE);
        add(manage);
        manage.addActionListener(this);
    }

    public void actionPerformed(ActionEvent ae) {
        GUIActions.showAccountManager(accounting);
        setVisible(false);
    }
}
