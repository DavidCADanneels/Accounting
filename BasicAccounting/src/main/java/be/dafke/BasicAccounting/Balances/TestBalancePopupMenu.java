package be.dafke.BasicAccounting.Balances;

import be.dafke.BasicAccounting.Accounts.AccountDetails;
import be.dafke.BasicAccounting.Journals.JournalInputGUI;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.Journals;

import javax.swing.*;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneel on 15/02/2015.
 */
public class TestBalancePopupMenu extends JPopupMenu {
    private final JMenuItem details;
    private Journals journals;
    private Accounts accounts;
    private JTable table;
    private JournalInputGUI journalInputGUI;

    public TestBalancePopupMenu(Accounts accounts, Journals journals, JTable table, JournalInputGUI journalInputGUI) {
        this.journals = journals;
        this.accounts = accounts;
        this.table = table;
        this.journalInputGUI = journalInputGUI;
        details = new JMenuItem(getBundle("Accounting").getString("GO_TO_ACCOUNT_DETAILS"));
        details.addActionListener(e -> showDetails());
        add(details);
    }

    public void showDetails() {
        for(int i:table.getSelectedRows()) {
            Account account = accounts.getBusinessObjects().get(i);
            AccountDetails.getAccountDetails(account, journals, journalInputGUI);
        }
        setVisible(false);
    }
}
