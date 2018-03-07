package be.dafke.BasicAccounting.Balances;

import be.dafke.BasicAccounting.Accounts.AccountDetails.AccountDetailsGUI;
import be.dafke.BasicAccounting.Journals.JournalEditPanel;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.Journals;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneel on 15/02/2015.
 */
public class BalancePopupMenu extends JPopupMenu {
    private final JMenuItem details;
    private Journals journals;
    private SelectableTable<Account> gui;
    private JournalEditPanel journalEditPanel;

    public BalancePopupMenu(Journals journals, SelectableTable<Account> gui, JournalEditPanel journalEditPanel) {
        this.journals = journals;
        this.gui = gui;
        this.journalEditPanel = journalEditPanel;
        details = new JMenuItem(getBundle("Accounting").getString("GO_TO_ACCOUNT_DETAILS"));
        details.addActionListener(e -> showDetails());
        add(details);
    }

    public void showDetails() {
        ArrayList<Account> accounts = gui.getSelectedObjects();
        for(Account account: accounts) {
            AccountDetailsGUI.getAccountDetails(account, journals, journalEditPanel);
        }
        setVisible(false);
    }
}
