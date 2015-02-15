package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Booking;
import be.dafke.BasicAccounting.Objects.Journal;
import be.dafke.BasicAccounting.Objects.Transaction;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.RefreshableTable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneel on 15/02/2015.
 */
public class EditTransactionActionListener implements ActionListener {
    private Accounting accounting;
    private RefreshableTable<Booking> gui;
    private Journal journal;

    public EditTransactionActionListener(Accounting accounting, RefreshableTable<Booking> gui, Journal journal) {
        this.accounting = accounting;
        this.gui = gui;
        this.journal = journal;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Booking booking = gui.getSelectedObject();
        Transaction transaction = booking.getTransaction();
        journal.removeBusinessObject(transaction);
        journal.setCurrentObject(transaction);
        accounting.getJournals().setCurrentObject(journal);
        String text = getBundle("Accounting").getString("TRANSACTION_REMOVED");
        Object[] messageArguments = {journal.getName()};
        MessageFormat formatter = new MessageFormat(text);
        String output = formatter.format(messageArguments);
        JOptionPane.showMessageDialog(null, output);
        ComponentMap.refreshAllFrames();
    }
}
