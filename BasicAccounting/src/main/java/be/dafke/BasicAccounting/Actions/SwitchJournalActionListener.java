package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.BasicAccounting.Objects.Journal;
import be.dafke.BasicAccounting.Objects.Journals;
import be.dafke.BasicAccounting.Objects.Transaction;
import be.dafke.ComponentModel.ComponentMap;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ddanneel on 14/02/2015.
 */
public class SwitchJournalActionListener implements ActionListener {
    private Accountings accountings;
    private JComboBox<Journal> combo;

    public SwitchJournalActionListener(Accountings accountings, JComboBox<Journal> combo) {
        this.accountings = accountings;
        this.combo = combo;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Accounting accounting = accountings.getCurrentObject();
        Journals journals = accounting.getJournals();
        Journal oldJournal = journals.getCurrentObject();
        Journal newJournal = (Journal) combo.getSelectedItem();
        if(newJournal!=null && oldJournal!=null){
            checkTransfer(journals, oldJournal, newJournal);
        } else {
            journals.setCurrentObject(newJournal);
        }
        ComponentMap.refreshAllFrames();
    }

    private void checkTransfer(Journals journals, Journal oldJournal, Journal newJournal){
        Transaction oldTransaction = oldJournal.getCurrentObject();
        Transaction newTransaction = newJournal.getCurrentObject();
        if(oldTransaction!=null && !oldTransaction.getBusinessObjects().isEmpty()){
            StringBuilder builder = new StringBuilder("Do you want to transfer the current transaction from ")
                    .append(oldJournal).append(" to ").append(newJournal);
            if(newTransaction!=null && !newTransaction.getBusinessObjects().isEmpty()){
                builder.append("\r\nWARNING: ").append(newJournal).append(" also has an open transactions, which will be lost if you select transfer");
            }
            int answer = JOptionPane.showConfirmDialog(null, builder.toString());
            if(answer == JOptionPane.YES_OPTION){
                newJournal.setCurrentObject(oldTransaction);
                oldJournal.setCurrentObject(new Transaction());
                journals.setCurrentObject(newJournal);
            } else if(answer == JOptionPane.NO_OPTION){
                journals.setCurrentObject(newJournal);
            } else {
                journals.setCurrentObject(oldJournal);
            }
        } else {
            journals.setCurrentObject(newJournal);
        }
    }
}
