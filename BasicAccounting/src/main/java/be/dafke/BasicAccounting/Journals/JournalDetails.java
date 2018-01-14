package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BasicAccounting.MainApplication.PopupForTableActivator;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.Booking;
import be.dafke.BusinessModel.Journal;
import be.dafke.BusinessModel.Journals;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.math.BigDecimal;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class JournalDetails extends JFrame implements WindowListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JournalDetailsPopupMenu popup;
	private SelectableTable<Booking> table;
	private JournalDetailsDataModel journalDetailsDataModel;
	private static HashMap<Journal,JournalDetails> journalDetailsMap = new HashMap<>();

	private JournalDetails(Journal journal, Journals journals, JournalInputGUI journalInputGUI) {
		super(getBundle("Accounting").getString("JOURNAL_DETAILS") + journal.toString());
		journalDetailsDataModel = new JournalDetailsDataModel();
		journalDetailsDataModel.setJournal(journal);

		table = new SelectableTable<>(journalDetailsDataModel);
		table.setPreferredScrollableViewportSize(new Dimension(500, 200));
		JournalColorRenderer renderer = new JournalColorRenderer();
		table.setDefaultRenderer(String.class, renderer);
		table.setDefaultRenderer(Account.class, renderer);
		table.setDefaultRenderer(BigDecimal.class, renderer);
		//table.setAutoCreateRowSorter(true);
		table.setRowSorter(null);
		JScrollPane scrollPane = new JScrollPane(table);
		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.add(scrollPane, BorderLayout.CENTER);
//		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setContentPane(contentPanel);
		pack();

		popup = new JournalDetailsPopupMenu(journals, table, journalInputGUI);
		table.addMouseListener(PopupForTableActivator.getInstance(popup, table));
	}

	public static JournalDetails getJournalDetails(Journal journal, Journals journals, JournalInputGUI journalInputGUI){
		JournalDetails journalDetails = journalDetailsMap.get(journal);
		if(journalDetails==null){
			journalDetails = new JournalDetails(journal, journals, journalInputGUI);
			journalDetailsMap.put(journal, journalDetails);
			Main.addFrame(journalDetails);
		}
		journalDetails.setVisible(true);
		return journalDetails;
	}

	public void selectObject(Booking object){
		int row = journalDetailsDataModel.getRow(object);
		if(table !=null){
			table.setRowSelectionInterval(row, row);
			Rectangle cellRect = table.getCellRect(row, 0, false);
			table.scrollRectToVisible(cellRect);
		}
	}

	public void windowClosing(WindowEvent we) {
        popup.setVisible(false);
    }
    public void windowOpened(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}

	public static void fireJournalDataChangedForAll(Journal journal) {
		JournalDetails journalDetails = journalDetailsMap.get(journal);
		if(journalDetails!=null) {
			journalDetails.fireJournalDataChanged();
		}
	}

	public void fireJournalDataChanged() {
		journalDetailsDataModel.fireTableDataChanged();
	}
}
