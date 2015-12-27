package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.Journals;
import be.dafke.ComponentModel.RefreshableTable;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneel on 15/02/2015.
 */
public class BalancePopupMenu extends JPopupMenu implements ActionListener {
    private final JMenuItem details;
    private Journals journals;
    private RefreshableTable<Account> gui;

    public BalancePopupMenu(Journals journals, RefreshableTable<Account> gui) {
        this.journals = journals;
        this.gui = gui;
        details = new JMenuItem(getBundle("Accounting").getString("GO_TO_ACCOUNT_DETAILS"));
        details.addActionListener(this);
        add(details);
    }

    public void actionPerformed(ActionEvent e) {
        Account account = gui.getSelectedObject();
        AccountActions.showDetails(account, journals);
        setVisible(false);
    }
}
