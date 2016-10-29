package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BasicAccounting.DetailsPopupMenu;
import be.dafke.BasicAccounting.Journals.JournalDetailsDataModel;
import be.dafke.BusinessActions.PopupForTableActivator;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Booking;
import be.dafke.BusinessModel.Journal;
import be.dafke.ComponentModel.RefreshableTable;

import javax.swing.*;
import java.awt.*;

public class JournalGUI extends AccountingPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private final RefreshableTable<Booking> viewTable;
    private final DetailsPopupMenu viewPopup;

    private final JournalDetailsDataModel journalDetailsDataModel;

    public JournalGUI(Accounting accounting) {
        Journal journal=null;
        if(accounting!=null && accounting.getJournals()!=null) {
            journal = accounting.getJournals().getCurrentObject();
        }
		setLayout(new BorderLayout());
        journalDetailsDataModel = new JournalDetailsDataModel(journal);

        viewTable = new RefreshableTable<>(journalDetailsDataModel);
        viewTable.setPreferredScrollableViewportSize(new Dimension(800, 200));
        viewPopup = new DetailsPopupMenu(viewTable, DetailsPopupMenu.Mode.JOURNAL);
        viewTable.addMouseListener(new PopupForTableActivator(viewPopup, viewTable, 0,2,3,4));

		JScrollPane scrollPane1 = new JScrollPane(viewTable);
        JPanel center = new JPanel();

        center.setLayout(new BoxLayout(center,BoxLayout.Y_AXIS));
        center.add(scrollPane1);
		add(center, BorderLayout.CENTER);
	}

    public void setAccounting(Accounting accounting){
        super.setAccounting(accounting);
        if(accounting!=null){
            viewPopup.setJournals(accounting.getJournals());
            if(accounting.getJournals()!=null) {
                journalDetailsDataModel.setJournal(accounting.getJournals().getCurrentObject());
//            } else {
//                journalDetailsDataModel.setJournal(null);
            }
//        } else{
//            journalDetailsDataModel.setJournal(null);
//            viewPopup.setJournals(null);
        }
    }

	public void refresh() {
        journalDetailsDataModel.fireTableDataChanged();
    }
}