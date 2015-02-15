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
import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneel on 15/02/2015.
 */
public class MoveTransactionActionListener implements ActionListener {
    private Accounting accounting;
    private RefreshableTable<Booking> gui;
    private Journal journal;

    public MoveTransactionActionListener(Accounting accounting, RefreshableTable<Booking> gui, Journal journal) {
        this.accounting = accounting;
        this.gui = gui;
        this.journal = journal;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Booking booking = gui.getSelectedObject();
        Transaction transaction = booking.getTransaction();
        ArrayList<Journal> dagboeken = accounting.getJournals().getAllJournalsExcept(journal);
        Object[] lijst = dagboeken.toArray();
        int keuze = JOptionPane.showOptionDialog(null,
                getBundle("Accounting").getString("CHOOSE_JOURNAL"),
                getBundle("Accounting").getString("JOURNAL_CHOICE"),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, lijst, lijst[0]);
        if(keuze!=JOptionPane.CANCEL_OPTION && keuze!=JOptionPane.CLOSED_OPTION){
            Journal newJournal = (Journal) lijst[keuze];
            journal.removeBusinessObject(transaction);
            newJournal.addBusinessObject(transaction);
            String text = getBundle("Accounting").getString("TRANSACTION_MOVED");
            Object[] messageArguments = {journal.getName(), newJournal.getName()};
            MessageFormat formatter = new MessageFormat(text);
            String output = formatter.format(messageArguments);
            JOptionPane.showMessageDialog(null,output);
        }
        ComponentMap.refreshAllFrames();
    }
}
