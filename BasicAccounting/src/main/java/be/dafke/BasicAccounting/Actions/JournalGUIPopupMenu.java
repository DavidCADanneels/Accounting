package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.GUI.InputWindows.AccountSelector;
import be.dafke.BasicAccounting.GUI.MainWindow.JournalGUI;
import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.BasicAccounting.Objects.Booking;
import be.dafke.BasicAccounting.Objects.Transaction;
import be.dafke.ComponentModel.ComponentMap;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneel on 15/02/2015.
 */
public class JournalGUIPopupMenu extends JPopupMenu implements ActionListener{
    private final JMenuItem delete;
    private final JMenuItem edit;
    private final JMenuItem change;
    private final JournalGUI gui;
    private final Accountings accountings;

    public JournalGUIPopupMenu(JournalGUI gui, Accountings accountings) {
        this.accountings = accountings;
        this.gui = gui;
        delete = new JMenuItem(getBundle("Accounting").getString("DELETE"));
        edit = new JMenuItem(getBundle("Accounting").getString("EDIT_AMOUNT"));
        change = new JMenuItem(getBundle("Accounting").getString("CHANGE_ACCOUNT"));
        delete.addActionListener(this);
        edit.addActionListener(this);
        change.addActionListener(this);
        add(delete);
        add(edit);
        add(change);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        menuAction((JMenuItem) e.getSource());
    }

    private void menuAction(JMenuItem source) {
        setVisible(false);
        Booking booking = gui.getSelectedBooking();
        Transaction transaction = booking.getTransaction();
        if (source == delete) {
            transaction.removeBusinessObject(booking);
        } else if (source == edit) {

        } else if (source == change) {
            AccountSelector sel = new AccountSelector(accountings.getCurrentObject());
            ComponentMap.addRefreshableComponent(sel);
            sel.setVisible(true);
            Account account = sel.getSelection();
            if(account!=null){
                booking.setAccount(account);
            }
        }
        ComponentMap.refreshAllFrames();
    }
}
