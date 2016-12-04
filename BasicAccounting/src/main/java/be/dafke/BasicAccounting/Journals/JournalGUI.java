package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.MainApplication.PopupForTableActivator;
import be.dafke.BusinessModel.Booking;
import be.dafke.BusinessModel.Journal;
import be.dafke.BusinessModel.Journals;
import be.dafke.ComponentModel.RefreshableTable;

import javax.swing.*;
import java.awt.*;

public class JournalGUI extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private final RefreshableTable<Booking> table;
    private final JournalDetailsPopupMenu popup;

    private final JournalDetailsDataModel journalDetailsDataModel;

    public JournalGUI(JournalInputGUI journalInputGUI) {
		setLayout(new BorderLayout());
        journalDetailsDataModel = new JournalDetailsDataModel();

        table = new RefreshableTable<>(journalDetailsDataModel);
        table.setPreferredScrollableViewportSize(new Dimension(800, 200));

        popup = new JournalDetailsPopupMenu(table, journalInputGUI);
        table.addMouseListener(new PopupForTableActivator(popup, table, 0,2,3,4));

        JPanel center = new JPanel();
        JScrollPane scrollPane = new JScrollPane(table);
        center.setLayout(new BoxLayout(center,BoxLayout.Y_AXIS));
        center.add(scrollPane);
		add(center, BorderLayout.CENTER);
	}

    public void setJournals(Journals journals){
        popup.setJournals(journals);
        setJournal(journals==null?null:journals.getCurrentObject());
    }

    public void setJournal(Journal journal) {
        journalDetailsDataModel.setJournal(journal);
        journalDetailsDataModel.fireTableDataChanged();
    }

    public void fireJournalDataChanged() {
        journalDetailsDataModel.fireTableDataChanged();
    }
}