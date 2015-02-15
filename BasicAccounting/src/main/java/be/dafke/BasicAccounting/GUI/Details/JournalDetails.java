package be.dafke.BasicAccounting.GUI.Details;

import be.dafke.BasicAccounting.Actions.JournalDetailsPopupMenu;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Booking;
import be.dafke.BasicAccounting.Objects.Journal;
import be.dafke.ComponentModel.RefreshableTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class JournalDetails extends RefreshableTable implements WindowListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPopupMenu popup;
	private int selectedRow;

	public JournalDetails(Journal journal, Accounting accounting) {
		super(getBundle("Accounting").getString("JOURNAL_DETAILS")
                + " " + journal.toString() + " (" + accounting.toString() + ")", new JournalDetailsDataModel(journal));
		tabel.setAutoCreateRowSorter(true);
		popup = new JournalDetailsPopupMenu(accounting, this, journal);
		tabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				Point cell = me.getPoint();//
				Point location = me.getLocationOnScreen();
				int col = tabel.columnAtPoint(cell);
				if (col == 0 && me.getClickCount() == 2) {
					selectedRow = tabel.rowAtPoint(cell);
					popup.show(null, location.x, location.y);
				} else popup.setVisible(false);
			}
		});
	}

    public Booking getSelectedBooking(){
        return ((JournalDetailsDataModel)tabel.getModel()).getValueAt(selectedRow);
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
