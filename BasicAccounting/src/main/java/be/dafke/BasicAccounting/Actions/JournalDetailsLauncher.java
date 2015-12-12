package be.dafke.BasicAccounting.Actions;

import be.dafke.BasicAccounting.GUI.Details.JournalDetails;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Booking;
import be.dafke.BasicAccounting.Objects.Journal;
import be.dafke.ComponentModel.ComponentMap;
import be.dafke.ComponentModel.DisposableComponent;
import be.dafke.ComponentModel.RefreshableTableFrame;

/**
 * Created by ddanneel on 14/02/2015.
 */
public class JournalDetailsLauncher {
    private static final String JOURNAL_DETAILS = "JournalDetails";

    public RefreshableTableFrame<Booking> showDetails(Accounting accounting, Journal journal){
        String key = JOURNAL_DETAILS + journal.toString();
        DisposableComponent gui = ComponentMap.getDisposableComponent(key); // DETAILS
        if(gui == null){
            gui = new JournalDetails(journal, accounting);
            ComponentMap.addDisposableComponent(key, gui); // DETAILS
        }
        gui.setVisible(true);
        return (RefreshableTableFrame<Booking>)gui;
    }
}
