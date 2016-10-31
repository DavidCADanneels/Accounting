package be.dafke.BasicAccounting;

import be.dafke.BasicAccounting.Accounts.NewAccountGUI;
import be.dafke.BusinessActions.TransactionActions;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Transaction;
import be.dafke.ComponentModel.RefreshableTable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneels on 10/12/2015.
 */
public class AccountsTablePopupMenu extends JPopupMenu implements ActionListener{
    private final JMenuItem manage;
    public final String DEBIT = "debit";
    public final String CREDIT = "credit";
    public final String ADD = "add";
    public final String MANAGE = "manage";
    public final String DETAILS = "details";
    private final RefreshableTable<Account> table;

    private Accounting accounting;

    public AccountsTablePopupMenu(Accounting accounting, RefreshableTable<Account> table) {
        this.table = table;
        this.accounting = accounting;
        manage = new JMenuItem(getBundle("Accounting").getString("MANAGE_ACCOUNT"));
        manage.setActionCommand(MANAGE);
        add(manage);
        manage.addActionListener(this);
    }

    public void actionPerformed(ActionEvent ae) {
        String actionCommand = ae.getActionCommand();
        if(accounting!=null) {
            Account selectedAccount = table.getSelectedObject();
            if (MANAGE.equals(actionCommand)) {
                GUIActions.showAccountManager(accounting);
            } else if (DETAILS.equals(actionCommand)) {
                GUIActions.showDetails(selectedAccount, accounting.getJournals());
            } else if (ADD.equals(actionCommand)) {
                new NewAccountGUI(accounting).setVisible(true);
            } else {
                Transaction transaction = accounting.getJournals().getCurrentObject().getCurrentObject();
                if (DEBIT.equals(actionCommand)) {
                    TransactionActions.addBookingToTransaction(accounting.getAccounts(), selectedAccount, transaction, true);
                } else if (CREDIT.equals(actionCommand)) {
                    TransactionActions.addBookingToTransaction(accounting.getAccounts(), selectedAccount, transaction, false);
                }
            }
        }
        setVisible(false);
    }
}
