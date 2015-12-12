package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.GUI.InputWindows.AccountSelector;
import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.AccountTypes;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accounts;
import be.dafke.BasicAccounting.Objects.Booking;
import be.dafke.BasicAccounting.Objects.Journals;
import be.dafke.BasicAccounting.Objects.Movement;
import be.dafke.BasicAccounting.Objects.Transaction;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.RefreshableTable;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneel on 15/02/2015.
 */
public class JournalGUIPopupMenu extends JPopupMenu implements ActionListener{
    private final JMenuItem delete, edit, change, debitCredit, details;
    private final RefreshableTable<Booking> table;
    private Journals journals;
    private Accounts accounts;
    private AccountTypes accountTypes;

    public JournalGUIPopupMenu(RefreshableTable<Booking> table, Journals journals, Accounts accounts, AccountTypes accountTypes) {
        setAccounting(journals, accounts, accountTypes);
        this.table = table;
        delete = new JMenuItem(getBundle("Accounting").getString("DELETE"));
        edit = new JMenuItem(getBundle("Accounting").getString("EDIT_AMOUNT"));
        change = new JMenuItem(getBundle("Accounting").getString("CHANGE_ACCOUNT"));
        debitCredit = new JMenuItem(getBundle("Accounting").getString("D_C"));
        details = new JMenuItem(getBundle("Accounting").getString("GO_TO_ACCOUNT_DETAILS"));
        add(details);
        delete.addActionListener(this);
        edit.addActionListener(this);
        change.addActionListener(this);
        debitCredit.addActionListener(this);
        details.addActionListener(this);
        add(delete);
        add(edit);
        add(change);
        add(debitCredit);
    }

    public void setAccounting(Accounting accounting){
        setAccounting(accounting.getJournals(),accounting.getAccounts(),accounting.getAccountTypes());
    }

    public void setAccounting(Journals journals, Accounts accounts, AccountTypes accountTypes){
        this.journals = journals;
        this.accounts = accounts;
        this.accountTypes = accountTypes;
    }

    public void actionPerformed(ActionEvent e) {
        menuAction((JMenuItem) e.getSource());
    }

    private void menuAction(JMenuItem source) {
        setVisible(false);
        Booking booking = table.getSelectedObject();
        Transaction transaction = booking.getTransaction();
        if (source == delete) {
            transaction.removeBusinessObject(booking);
        } else if (source == edit) {
            Account account = booking.getAccount();
            //TODO: booking contains list of Movement (should be 1 movement?)
            //TODO: or JournalGUI.table should contain Movements iso Bookings
            Movement movement = booking.getBusinessObjects().get(0);
            boolean debit = movement.isDebit();
            BigDecimal amount = TransactionActions.askAmount(transaction, account, debit);
            if(amount != null){
                // booking must be removed and re-added to Transaction to re-calculate the totals
                transaction.removeBusinessObject(booking);
                movement.setAmount(amount);
                transaction.addBusinessObject(booking);
            }
        } else if (source == debitCredit){
            // booking must be removed and re-added to Transaction to re-calculate the totals
            transaction.removeBusinessObject(booking);
            Movement movement = booking.getBusinessObjects().get(0);
            movement.setDebit(!movement.isDebit());
            transaction.addBusinessObject(booking);
        } else if (source == change) {
            AccountSelector sel = new AccountSelector(accounts, accountTypes);
            ComponentMap.addRefreshableComponent(sel);
            sel.setVisible(true);
            Account account = sel.getSelection();
            if(account!=null){
                booking.setAccount(account);
            }
        } else if (source == details){
            Account account = booking.getAccount();
            AccountActions.showDetails(account, journals);
        }
        ComponentMap.refreshAllFrames();
    }
}
