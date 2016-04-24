package be.dafke.BasicAccounting;

import be.dafke.BasicAccounting.Accounts.AccountSelector;
import be.dafke.BusinessActions.TransactionActions;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.AccountTypes;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Accounts;
import be.dafke.BusinessModel.Booking;
import be.dafke.BusinessModel.Journals;
import be.dafke.BusinessModel.Movement;
import be.dafke.BusinessModel.Transaction;
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
            //TODO: or JournalGUI.table should contain Movements iso Bookings
            BigDecimal amount = TransactionActions.askAmount(transaction, account, booking.isDebit());
            if(amount != null){
                // booking must be removed and re-added to Transaction to re-calculate the totals
                transaction.removeBusinessObject(booking);
                booking.setAmount(amount);
                transaction.addBusinessObject(booking);
            }
        } else if (source == debitCredit){
            // booking must be removed and re-added to Transaction to re-calculate the totals
            transaction.removeBusinessObject(booking);
            booking.setDebit(!booking.isDebit());
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
            GUIActions.showDetails(account, journals);
        }
        ComponentMap.refreshAllFrames();
    }
}
