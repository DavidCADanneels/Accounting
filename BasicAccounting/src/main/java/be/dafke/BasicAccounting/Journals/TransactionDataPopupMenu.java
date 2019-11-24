package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.Accounts.AccountDetails.AccountDetailsGUI;
import be.dafke.BasicAccounting.Balances.BalanceGUI;
import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.Accounting.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;

import static java.util.ResourceBundle.getBundle;

public class TransactionDataPopupMenu extends JPopupMenu {
    private final JMenuItem details;

    private SelectableTable<Booking> gui;
    private Journals journals;

    public TransactionDataPopupMenu(Journals journals, SelectableTable<Booking> gui) {
        this(gui);
        this.journals=journals;
    }

    public TransactionDataPopupMenu(SelectableTable<Booking> gui) {
        this.gui = gui;
        details = new JMenuItem(getBundle("Accounting").getString("GO_TO_ACCOUNT_DETAILS"));
        details.addActionListener(e -> showDetails());
        add(details);
    }

    public void setJournals(Journals journals){
        this.journals=journals;
    }

    private void showDetails() {
        Point locationOnScreen = getLocationOnScreen();
        setVisible(false);
        ArrayList<Booking> bookings = gui.getSelectedObjects();
        for (Booking booking : bookings) {
            Account account = booking.getAccount();
            AccountDetailsGUI newGui = AccountDetailsGUI.getAccountDetails(locationOnScreen, account, journals);
            newGui.selectObject(booking);
        }
    }
}
