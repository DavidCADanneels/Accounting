package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.GUI.MainWindow.JournalGUI;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.BasicAccounting.Objects.Journal;
import be.dafke.BasicAccounting.Objects.Journals;
import be.dafke.BasicAccounting.Objects.Transaction;
import be.dafke.ComponentModel.ComponentMap;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

/**
 * Created by ddanneel on 14/02/2015.
 */
public class BookTransactionActionListener implements ActionListener {
    private Accountings accountings;
    private JournalGUI gui;

    public BookTransactionActionListener(Accountings accountings, JournalGUI gui) {
        this.accountings = accountings;
        this.gui = gui;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Accounting accounting = accountings.getCurrentObject();
        Journals journals = accounting.getJournals();
        Journal journal = journals.getCurrentObject();
        Transaction transaction = journal.getCurrentObject();
        if(transaction!=null){
            Calendar date = transaction.getDate();
            if(date == null){
                JOptionPane.showMessageDialog(null, "Fill in date");
            } else {
                journal.addBusinessObject(transaction);
                gui.clear();
                ComponentMap.refreshAllFrames();
            }
        }
    }
}
