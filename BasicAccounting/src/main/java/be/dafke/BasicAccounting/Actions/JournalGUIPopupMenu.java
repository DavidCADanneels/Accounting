package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.GUI.InputWindows.AccountSelector;
import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.BasicAccounting.Objects.Booking;
import be.dafke.BasicAccounting.Objects.Movement;
import be.dafke.BasicAccounting.Objects.Transaction;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.RefreshableTable;

import javax.swing.*;
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
    private final Accountings accountings;
    private final AccountDetailsLauncher accountDetailsLauncher;

    public JournalGUIPopupMenu(RefreshableTable<Booking> table, Accountings accountings) {
        this.accountings = accountings;
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
        accountDetailsLauncher = new AccountDetailsLauncher();
        add(delete);
        add(edit);
        add(change);
        add(debitCredit);
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
            BigDecimal amount = AddBookingToTransactionLauncher.askAmount(transaction, account, debit);
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
            AccountSelector sel = new AccountSelector(accountings.getCurrentObject());
            ComponentMap.addRefreshableComponent(sel);
            sel.setVisible(true);
            Account account = sel.getSelection();
            if(account!=null){
                booking.setAccount(account);
            }
        } else if (source == details){
            Account account = booking.getAccount();
            accountDetailsLauncher.showDetails(accountings.getCurrentObject(), account);
        }
        ComponentMap.refreshAllFrames();
    }
}
