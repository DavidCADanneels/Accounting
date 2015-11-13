package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.ComponentModel.RefreshableTable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneel on 15/02/2015.
 */
public class BalancePopupMenu extends JPopupMenu implements ActionListener {
    private final JMenuItem details;
    private final AccountDetailsActionListener accountDetailsActionListener;
    private Accounting accounting;
    private RefreshableTable<Account> gui;

    public BalancePopupMenu(Accountings accountings, Accounting accounting, RefreshableTable<Account> gui) {
        this.accounting = accounting;
        this.gui = gui;
        details = new JMenuItem(getBundle("Accounting").getString("GO_TO_ACCOUNT_DETAILS"));
        details.addActionListener(this);
        accountDetailsActionListener = new AccountDetailsActionListener(accountings);
        add(details);
    }

    public void actionPerformed(ActionEvent e) {
        Account account = gui.getSelectedObject();
        accountDetailsActionListener.showDetails(accounting, account);
        setVisible(false);
    }
}
