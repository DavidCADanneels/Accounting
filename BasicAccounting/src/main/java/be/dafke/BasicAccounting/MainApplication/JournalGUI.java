package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BasicAccounting.DetailsPopupMenu;
import be.dafke.BasicAccounting.Journals.JournalDetailsDataModel;
import be.dafke.BusinessActions.PopupForTableActivator;
import be.dafke.BusinessModel.Booking;
import be.dafke.ComponentModel.RefreshableTable;

import javax.swing.*;
import java.awt.*;

public class JournalGUI extends AccountingPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private final RefreshableTable<Booking> table;
    private final DetailsPopupMenu popup;

    private final JournalDetailsDataModel journalDetailsDataModel;

    public JournalGUI() {
		setLayout(new BorderLayout());
        journalDetailsDataModel = new JournalDetailsDataModel(null);

        table = new RefreshableTable<>(journalDetailsDataModel);
        table.setPreferredScrollableViewportSize(new Dimension(800, 200));

        popup = new DetailsPopupMenu(table, DetailsPopupMenu.Mode.JOURNAL);
        table.addMouseListener(new PopupForTableActivator(popup, table, 0,2,3,4));

        JPanel center = new JPanel();
        JScrollPane scrollPane = new JScrollPane(table);
        center.setLayout(new BoxLayout(center,BoxLayout.Y_AXIS));
        center.add(scrollPane);
		add(center, BorderLayout.CENTER);
	}

    public void refresh() {
        if(accounting!=null){
            popup.setJournals(accounting.getJournals());
            if(accounting.getJournals()!=null) {
                journalDetailsDataModel.setJournal(accounting.getJournals().getCurrentObject());
//            } else {
//                journalDetailsDataModel.setJournal(null);
            }
//        } else{
//            journalDetailsDataModel.setJournal(null);
//            popup.setJournals(null);
        }        journalDetailsDataModel.fireTableDataChanged();
    }
}