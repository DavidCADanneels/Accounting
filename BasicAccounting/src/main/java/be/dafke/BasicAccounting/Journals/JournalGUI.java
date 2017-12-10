package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.MainApplication.PopupForTableActivator;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.awt.*;

public class JournalGUI extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private final SelectableTable<Booking> table;
    private final JournalDetailsPopupMenu popup;

    private final JournalDetailsDataModel journalDetailsDataModel;

    public JournalGUI(JournalInputGUI journalInputGUI) {
		setLayout(new BorderLayout());
        journalDetailsDataModel = new JournalDetailsDataModel();

        table = new SelectableTable<>(journalDetailsDataModel);
        table.setPreferredScrollableViewportSize(new Dimension(800, 200));

        popup = new JournalDetailsPopupMenu(table, journalInputGUI);
        table.addMouseListener(PopupForTableActivator.getInstance(popup, table));

        JPanel center = new JPanel();
        JScrollPane scrollPane = new JScrollPane(table);
        center.setLayout(new BoxLayout(center,BoxLayout.Y_AXIS));
        center.add(scrollPane);
		add(center, BorderLayout.CENTER);
	}

	public void setAccounting(Accounting accounting){
        setJournals(accounting==null?null:accounting.getJournals());
        setJournal(accounting.getActiveJournal());
    }

    public void setJournals(Journals journals){
        popup.setJournals(journals);
    }

    public void setJournal(Journal journal) {
        journalDetailsDataModel.setJournal(journal);
        journalDetailsDataModel.fireTableDataChanged();
    }
    
    public void selectBooking(Booking booking){
        int row = journalDetailsDataModel.getRow(booking);

        if(table!=null){
            table.setRowSelectionInterval(row, row);
            Rectangle cellRect = table.getCellRect(row, 0, false);
            table.scrollRectToVisible(cellRect);
        }
    }

    public void fireJournalDataChanged() {
        journalDetailsDataModel.fireTableDataChanged();
    }
}