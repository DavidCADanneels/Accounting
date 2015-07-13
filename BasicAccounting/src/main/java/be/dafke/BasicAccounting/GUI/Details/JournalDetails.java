package be.dafke.BasicAccounting.GUI.Details;

import be.dafke.BasicAccounting.Actions.DetailsPopupMenu;
import be.dafke.BasicAccounting.Actions.PopupActivator;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Booking;
import be.dafke.BasicAccounting.Objects.Journal;
import be.dafke.ComponentModel.RefreshableTableFrame;

import javax.swing.*;
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


	public JournalDetails(Journal journal, Accounting accounting) {
		super(getBundle("Accounting").getString("JOURNAL_DETAILS")
                + " " + journal.toString() + " (" + accounting.toString() + ")", new JournalDetailsDataModel(journal));
		//tabel.setAutoCreateRowSorter(true);
		tabel.setRowSorter(null);
		popup = new DetailsPopupMenu(accounting, tabel, DetailsPopupMenu.Mode.JOURNAL);
		tabel.addMouseListener(new PopupActivator(popup,tabel, 0,2,3,4));
	}

    @Override
    public void windowClosing(WindowEvent we) {
        popup.setVisible(false);
    }

    @Override
    public void windowOpened(WindowEvent e) {}
    @Override
    public void windowClosed(WindowEvent e) {}
    @Override
    public void windowIconified(WindowEvent e) {}
    @Override
    public void windowDeiconified(WindowEvent e) {}
    @Override
    public void windowActivated(WindowEvent e) {}
    @Override
    public void windowDeactivated(WindowEvent e) {}
}
