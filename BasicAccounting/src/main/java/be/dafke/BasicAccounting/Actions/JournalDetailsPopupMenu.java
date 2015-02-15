package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.GUI.Details.JournalDetails;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Booking;
import be.dafke.BasicAccounting.Objects.Journal;
import be.dafke.BasicAccounting.Objects.Transaction;
import be.dafke.ComponentModel.ComponentMap;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by ddanneel on 15/02/2015.
 */
public class JournalDetailsPopupMenu extends JPopupMenu  implements ActionListener {
    private final JMenuItem move, delete, edit;
    private Accounting accounting;
    private JournalDetails gui;
    private Journal journal;

    public JournalDetailsPopupMenu(Accounting accounting, JournalDetails gui, Journal journal) {
        this.accounting = accounting;
        this.gui = gui;
        this.journal = journal;
        delete = new JMenuItem(getBundle("Accounting").getString("DELETE"));
        move = new JMenuItem(getBundle("Accounting").getString("MOVE"));
        edit = new JMenuItem(getBundle("Accounting").getString("EDIT_TRANSACTION"));
        delete.addActionListener(this);
        move.addActionListener(this);
        edit.addActionListener(this);
        add(delete);
        add(move);
        add(edit);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        menuAction((JMenuItem) e.getSource());
    }

    private void menuAction(JMenuItem source) {
        setVisible(false);
        Booking booking = gui.getSelectedBooking();
        Transaction transaction = booking.getTransaction();
        if (source == move) {
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
        } else if (source == delete) {
            journal.removeBusinessObject(transaction);
            String text = getBundle("Accounting").getString("TRANSACTION_REMOVED");
            Object[] messageArguments = {journal.getName()};
            MessageFormat formatter = new MessageFormat(text);
            String output = formatter.format(messageArguments);
            JOptionPane.showMessageDialog(null, output);
        } else if (source == edit) {
            journal.removeBusinessObject(transaction);
            journal.setCurrentObject(transaction);
            accounting.getJournals().setCurrentObject(journal);
            String text = getBundle("Accounting").getString("TRANSACTION_REMOVED");
            Object[] messageArguments = {journal.getName()};
            MessageFormat formatter = new MessageFormat(text);
            String output = formatter.format(messageArguments);
            JOptionPane.showMessageDialog(null, output);
        }
        ComponentMap.refreshAllFrames();
    }
}
