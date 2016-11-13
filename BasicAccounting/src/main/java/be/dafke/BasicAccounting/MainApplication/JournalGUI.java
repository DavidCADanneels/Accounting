package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BasicAccounting.DetailsPopupMenu;
import be.dafke.BasicAccounting.Journals.JournalDetailsDataModel;
import be.dafke.BusinessActions.AccountingListener;
import be.dafke.BusinessActions.JournalDataChangeListener;
import be.dafke.BusinessActions.JournalsListener;
import be.dafke.BusinessActions.PopupForTableActivator;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Booking;
import be.dafke.BusinessModel.Journal;
import be.dafke.BusinessModel.Journals;
import be.dafke.ComponentModel.RefreshableTable;

import javax.swing.*;
import java.awt.*;

public class JournalGUI extends JPanel implements JournalsListener, AccountingListener, JournalDataChangeListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private final RefreshableTable<Booking> table;
    private final DetailsPopupMenu popup;

    private final JournalDetailsDataModel journalDetailsDataModel;

    public JournalGUI(JournalInputGUI journalInputGUI) {
		setLayout(new BorderLayout());
        journalDetailsDataModel = new JournalDetailsDataModel();

        table = new RefreshableTable<>(journalDetailsDataModel);
        table.setPreferredScrollableViewportSize(new Dimension(800, 200));

        popup = new DetailsPopupMenu(table, DetailsPopupMenu.Mode.JOURNAL, journalInputGUI);
        table.addMouseListener(new PopupForTableActivator(popup, table, 0,2,3,4));

        JPanel center = new JPanel();
        JScrollPane scrollPane = new JScrollPane(table);
        center.setLayout(new BoxLayout(center,BoxLayout.Y_AXIS));
        center.add(scrollPane);
		add(center, BorderLayout.CENTER);
	}

    @Override
    public void setAccounting(Accounting accounting){
        setJournals(accounting==null?null:accounting.getJournals());
    }

    public void setJournals(Journals journals){
        popup.setJournals(journals);
        setJournal(journals==null?null:journals.getCurrentObject());
    }

    @Override
    public void setJournal(Journal journal) {
        journalDetailsDataModel.setJournal(journal);
        journalDetailsDataModel.fireTableDataChanged();
    }

    @Override
    public void fireJournalDataChanged() {
        journalDetailsDataModel.fireTableDataChanged();
    }
}