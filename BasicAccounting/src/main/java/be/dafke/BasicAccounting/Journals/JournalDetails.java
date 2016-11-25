package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.MainApplication.SaveAllActionListener;
import be.dafke.BusinessActions.JournalDataChangeListener;
import be.dafke.BusinessActions.PopupForTableActivator;
import be.dafke.BusinessModel.Booking;
import be.dafke.BusinessModel.Journal;
import be.dafke.BusinessModel.Journals;
import be.dafke.ComponentModel.RefreshableTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;

import static be.dafke.BasicAccounting.MainApplication.Main.addJournalDataListener;
import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class JournalDetails extends JFrame implements WindowListener, JournalDataChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JournalDetailsPopupMenu popup;
	private RefreshableTable<Booking> tabel;
	private JournalDetailsDataModel journalDetailsDataModel;
	private static HashMap<Journal,JournalDetails> journalDetailsMap = new HashMap<>();

	private JournalDetails(Journal journal, Journals journals, JournalInputGUI journalInputGUI) {
		super(getBundle("Accounting").getString("JOURNAL_DETAILS") + " " + journal.toString());
		journalDetailsDataModel = new JournalDetailsDataModel();
		journalDetailsDataModel.setJournal(journal);

		tabel = new RefreshableTable<>(journalDetailsDataModel);
		tabel.setPreferredScrollableViewportSize(new Dimension(500, 200));
		//tabel.setAutoCreateRowSorter(true);
		tabel.setRowSorter(null);
		JScrollPane scrollPane = new JScrollPane(tabel);
		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.add(scrollPane, BorderLayout.CENTER);
//		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setContentPane(contentPanel);
		pack();

		popup = new JournalDetailsPopupMenu(journals, tabel, journalInputGUI);
		tabel.addMouseListener(new PopupForTableActivator(popup,tabel, 0,2,3,4));
	}

	public static JournalDetails getJournalDetails(Journal journal, Journals journals, JournalInputGUI journalInputGUI){
		JournalDetails journalDetails = journalDetailsMap.get(journal);
		if(journalDetails==null){
			journalDetails = new JournalDetails(journal, journals, journalInputGUI);
			addJournalDataListener(journal,journalDetails);
			journalDetailsMap.put(journal, journalDetails);
			SaveAllActionListener.addFrame(journalDetails);
		}
		journalDetails.setVisible(true);
		return journalDetails;
	}

	public void selectObject(Booking object){
		if(tabel!=null) tabel.selectObject(object);
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

	@Override
	public void fireJournalDataChanged() {
		journalDetailsDataModel.fireTableDataChanged();
	}
}
