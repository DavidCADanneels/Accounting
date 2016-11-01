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
    private final JMenuItem manage, add, debit, credit,details;
    public final String DEBIT = "debit";
    public final String CREDIT = "credit";
    public final String ADD = "add";
    public final String MANAGE = "manage";
    public final String DETAILS = "details";
    private final RefreshableTable<Account> table;

    private Accounting accounting;

    public AccountsTablePopupMenu(RefreshableTable<Account> table) {
        this.table = table;

        manage = new JMenuItem(getBundle("Accounting").getString("MANAGE_ACCOUNT"));
        add = new JMenuItem(getBundle("Accounting").getString("ADD_ACCOUNT"));
        debit = new JMenuItem(getBundle("Accounting").getString("DEBIT"));
        credit = new JMenuItem(getBundle("Accounting").getString("CREDIT"));
        details = new JMenuItem(getBundle("Accounting").getString("VIEW_ACCOUNT"));

        manage.setActionCommand(MANAGE);
        add.setActionCommand(ADD);
        debit.setActionCommand(DEBIT);
        credit.setActionCommand(CREDIT);
        details.setActionCommand(DETAILS);

        add(debit);
        add(credit);
        add(details);
        addSeparator();
        add(add);
        add(manage);

        manage.addActionListener(this);
        add.addActionListener(this);
        debit.addActionListener(this);
        credit.addActionListener(this);
        details.addActionListener(this);
    }

    public void setAccounting(Accounting accounting) {
        this.accounting = accounting;
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
