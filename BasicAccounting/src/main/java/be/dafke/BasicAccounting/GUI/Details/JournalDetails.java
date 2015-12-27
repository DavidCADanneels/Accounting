package be.dafke.BasicAccounting.GUI.Details;

import be.dafke.BasicAccounting.Actions.DetailsPopupMenu;
import be.dafke.BasicAccounting.Actions.PopupForTableActivator;
import be.dafke.BasicAccounting.Objects.Booking;
import be.dafke.BasicAccounting.Objects.Journal;
import be.dafke.BasicAccounting.Objects.Journals;
import be.dafke.ComponentModel.RefreshableTableFrame;

import javax.swing.JPopupMenu;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class JournalDetails extends RefreshableTableFrame<Booking> implements WindowListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPopupMenu popup;


	public JournalDetails(Journal journal, Journals journals) {
		super(getBundle("Accounting").getString("JOURNAL_DETAILS") + " "
                + journal.toString(), new JournalDetailsDataModel(journal));
		//tabel.setAutoCreateRowSorter(true);
		tabel.setRowSorter(null);
		popup = new DetailsPopupMenu(journals, tabel, DetailsPopupMenu.Mode.JOURNAL);
		tabel.addMouseListener(new PopupForTableActivator(popup,tabel, 0,2,3,4));
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
