package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.DetailsPopupMenu;
import be.dafke.BusinessActions.PopupForTableActivator;
import be.dafke.BusinessModel.Booking;
import be.dafke.BusinessModel.Journal;
import be.dafke.BusinessModel.Journals;
import be.dafke.ComponentModel.RefreshableFrame;
import be.dafke.ComponentModel.RefreshableTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class JournalDetails extends RefreshableFrame implements WindowListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final DetailsPopupMenu popup;
	private RefreshableTable<Booking> tabel;
	private JournalDetailsDataModel dataModel;

	public JournalDetails(Journal journal, Journals journals) {
		super(getBundle("Accounting").getString("JOURNAL_DETAILS") + " " + journal.toString());
		dataModel = new JournalDetailsDataModel();
		dataModel.setJournal(journal);

		tabel = new RefreshableTable<>(dataModel);
		tabel.setPreferredScrollableViewportSize(new Dimension(500, 200));
		//tabel.setAutoCreateRowSorter(true);
		tabel.setRowSorter(null);
		JScrollPane scrollPane = new JScrollPane(tabel);
		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.add(scrollPane, BorderLayout.CENTER);
//		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setContentPane(contentPanel);
		pack();

		popup = new DetailsPopupMenu(journals, tabel, DetailsPopupMenu.Mode.JOURNAL);
		tabel.addMouseListener(new PopupForTableActivator(popup,tabel, 0,2,3,4));
	}

	public void selectObject(Booking object){
		if(tabel!=null) tabel.selectObject(object);
	}

	public void refresh() {
//		tabel.refresh();
		dataModel.fireTableDataChanged();
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
}
