package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.GUI.Details.JournalDetails;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Accountings;
import be.dafke.BasicAccounting.Objects.Journal;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.DisposableComponent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ddanneel on 14/02/2015.
 */
public class JournalDetailsActionListener implements ActionListener{
    private Accountings accountings;
    private static final String JOURNAL_DETAILS = "JournalDetails";
    public JournalDetailsActionListener(Accountings accountings) {
        this.accountings=accountings;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Accounting accounting = accountings.getCurrentObject();
        Journal journal = accounting.getJournals().getCurrentObject();
        String key = JOURNAL_DETAILS + accounting.toString() + journal.toString();
        DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
        if(gui == null){
            gui = new JournalDetails(journal, accounting);
            ComponentMap.addDisposableComponent(key, gui); // DETAILS
        }
        gui.setVisible(true);
    }
}
