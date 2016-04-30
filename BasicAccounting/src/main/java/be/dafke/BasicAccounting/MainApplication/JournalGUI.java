package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BasicAccounting.DetailsPopupMenu;
import be.dafke.BasicAccounting.Journals.JournalDetailsDataModel;
import be.dafke.BusinessActions.PopupForTableActivator;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Booking;
import be.dafke.BusinessModel.Journal;
import be.dafke.BusinessModel.Journals;
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

    private Journal journal;

    private final JournalDetailsDataModel journalDetailsDataModel;

    public JournalGUI(Accounting accounting) {
        if(accounting!=null) {
            journal = accounting.getJournals().getCurrentObject();
        }
		setLayout(new BorderLayout());
        journalDetailsDataModel = new JournalDetailsDataModel(journal);

        viewTable = new RefreshableTable<Booking>(journalDetailsDataModel);
		viewTable.setPreferredScrollableViewportSize(new Dimension(800, 200));
        Journals journals;
        if(accounting!=null) {
            journals = accounting.getJournals();
        } else {
            journals = null;
        }
        viewPopup = new DetailsPopupMenu(journals, viewTable, DetailsPopupMenu.Mode.JOURNAL);
        viewTable.addMouseListener(new PopupForTableActivator(viewPopup, viewTable, 0,2,3,4));


		JScrollPane scrollPane1 = new JScrollPane(viewTable);
        JPanel center = new JPanel();

        center.setLayout(new BoxLayout(center,BoxLayout.Y_AXIS));
        center.add(scrollPane1);
		add(center, BorderLayout.CENTER);
        setAccounting(accounting);
        refresh();
	}

    public void setAccounting(Accounting accounting){
        viewPopup.setAccounting(accounting);
        if(accounting==null || accounting.getJournals()==null){
            journal = null;
        } else {
            journal = accounting.getJournals().getCurrentObject();
        }
        journalDetailsDataModel.setJournal(journal);
    }

	public void refresh() {
        if(journal!=null){
            journalDetailsDataModel.setJournal(journal);
            journalDetailsDataModel.fireTableDataChanged();
        }
    }



}