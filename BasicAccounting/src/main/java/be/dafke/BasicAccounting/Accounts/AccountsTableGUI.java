package be.dafke.BasicAccounting.Accounts;

import be.dafke.BasicAccounting.Journals.JournalInputGUI;
import be.dafke.BasicAccounting.MainApplication.PopupForTableActivator;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Accounts;
import be.dafke.ComponentModel.RefreshableTable;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class AccountsTableGUI extends JPanel {//implements MouseListener {
    private final RefreshableTable<Account> table;
    private final AccountDataModel accountDataModel;

    private AccountsTablePopupMenu popup;

    public AccountsTableGUI(JournalInputGUI journalInputGUI) {
		setLayout(new BorderLayout());
		setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "Accounting").getString("ACCOUNTS")));

        // CENTER
        //
        accountDataModel = new AccountDataModel();
        table = new RefreshableTable<>(accountDataModel);
        table.setPreferredScrollableViewportSize(new Dimension(100, 600));

        popup = new AccountsTablePopupMenu(table,journalInputGUI);
        // TODO: register popup menu as TransactionListener and remove TransactionListener from 'this'.
        table.addMouseListener(new PopupForTableActivator(popup, table));

        JScrollPane scrollPane1 = new JScrollPane(table);
        JPanel center = new JPanel();

        center.setLayout(new BoxLayout(center,BoxLayout.Y_AXIS));
        center.add(scrollPane1);
        add(center, BorderLayout.CENTER);
	}

    public void setAccounting(Accounting accounting) {
        accountDataModel.setAccounts(accounting==null?null:accounting.getAccounts());
        // if setAccounts() is used here, popup.setAccounts() will be called twice
        popup.setAccounting(accounting);
        table.addMouseListener(new PopupForTableActivator(popup, table));  // TODO: Needed?
        fireAccountDataChanged();
    }

    public void setAccounts(Accounts accounts) {
        accountDataModel.setAccounts(accounts);
        popup.setAccounts(accounts);
        fireAccountDataChanged();
    }

    public void fireAccountDataChanged() {
        accountDataModel.fireTableDataChanged();
    }
}