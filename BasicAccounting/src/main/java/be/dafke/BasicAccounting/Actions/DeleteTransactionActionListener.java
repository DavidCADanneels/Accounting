package be.dafke.BasicAccounting.Actions;

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
public class DeleteTransactionActionListener implements ActionListener {
    private RefreshableTable<Booking> table;

    public DeleteTransactionActionListener(RefreshableTable<Booking> table) {
        this.table = table;
    }

    public void actionPerformed(ActionEvent e) {
        Booking booking = table.getSelectedObject();
        Transaction transaction = booking.getTransaction();
        Journal journal = transaction.getJournal();
        journal.removeBusinessObject(transaction);
        String text = getBundle("Accounting").getString("TRANSACTION_REMOVED");
        Object[] messageArguments = {journal.getName()};
        MessageFormat formatter = new MessageFormat(text);
        String output = formatter.format(messageArguments);
        JOptionPane.showMessageDialog(null, output);
        ComponentMap.refreshAllFrames();
    }
}
