package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.GUI.MainWindow.JournalGUI;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.BasicAccounting.Objects.Journal;
import be.dafke.BasicAccounting.Objects.Journals;
import be.dafke.BasicAccounting.Objects.Transaction;
import be.dafke.ComponentModel.ComponentMap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    public void actionPerformed(ActionEvent e) {
        Accounting accounting = accountings.getCurrentObject();
        Journals journals = accounting.getJournals();
        Journal journal = journals.getCurrentObject();
        //Transaction transaction = journal.getCurrentObject();
        Transaction transaction = gui.saveTransaction();
        if(transaction!=null){
            journal.addBusinessObject(transaction);
            gui.clear();
            ComponentMap.refreshAllFrames();
        }
    }
}
