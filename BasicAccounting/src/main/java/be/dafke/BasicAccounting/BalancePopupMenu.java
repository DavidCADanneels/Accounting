package be.dafke.BasicAccounting;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.Journals;
import be.dafke.ComponentModel.RefreshableTable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
        ArrayList<Account> accounts = gui.getSelectedObjects();
        for(Account account: accounts) {
            Main.getAccountDetails(account, journals);
        }
        setVisible(false);
    }
}
