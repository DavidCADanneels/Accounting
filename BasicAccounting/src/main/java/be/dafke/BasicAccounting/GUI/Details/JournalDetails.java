package be.dafke.BasicAccounting.GUI.Details;

import be.dafke.BasicAccounting.GUI.AccountingComponentMap;
import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Booking;
import be.dafke.BasicAccounting.Objects.Journal;
import be.dafke.BasicAccounting.Objects.Transaction;
import be.dafke.ComponentModel.RefreshableTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class JournalDetails extends RefreshableTable implements ActionListener, WindowListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPopupMenu popup;
	private int selectedRow;
	private final JMenuItem move, delete, edit;
	private final Journal journal;
	private final Accounting accounting;

	public JournalDetails(Journal journal, Accounting accounting) {
		super(getBundle("Accounting").getString("JOURNAL_DETAILS")
                + " " + journal.toString() + " (" + accounting.toString() + ")", new JournalDetailsDataModel(journal));
		this.accounting = accounting;
		this.journal = journal;
		tabel.setAutoCreateRowSorter(true);
		popup = new JPopupMenu();
		delete = new JMenuItem(getBundle("Accounting").getString("DELETE"));
		move = new JMenuItem(getBundle("Accounting").getString("MOVE"));
		edit = new JMenuItem(getBundle("Accounting").getString("EDIT"));
		delete.addActionListener(this);
		move.addActionListener(this);
		edit.addActionListener(this);
		popup.add(delete);
		popup.add(move);
		popup.add(edit);
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

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() instanceof JMenuItem) {
            menuAction((JMenuItem) ae.getSource());
        }
    }

    private void menuAction(JMenuItem source) {
        popup.setVisible(false);
        ArrayList<Booking> boekingen = new ArrayList<Booking>();
        for(Transaction transaction : journal.getBusinessObjects()){
            boekingen.addAll(transaction.getBusinessObjects());
        }
        Booking booking = boekingen.get(selectedRow);
        Transaction transaction = booking.getTransaction();
        if (source == move) {
            ArrayList<Journal> dagboeken = accounting.getJournals().getAllJournalsExcept(journal);
            Object[] lijst = dagboeken.toArray();
            int keuze = JOptionPane.showOptionDialog(null,
                    getBundle("Accounting").getString("CHOOSE_JOURNAL"),
                    getBundle("Accounting").getString("JOURNAL_CHOICE"),
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, lijst, lijst[0]);
            if(keuze!=JOptionPane.CANCEL_OPTION && keuze!=JOptionPane.CLOSED_OPTION){
                Journal newJournal = (Journal) lijst[keuze];
                journal.removeBusinessObject(transaction);
                newJournal.addBusinessObject(transaction);
                // TODO: create translationkeys with parameters !!!
                JOptionPane.showMessageDialog(null,
                        getBundle("Accounting").getString("TRANSACTION_MOVED_FROM") + " " + journal + " "+
                                getBundle("Accounting").getString("TO") +" " + newJournal);
            }
        } else if (source == delete) {
            journal.removeBusinessObject(transaction);
            JOptionPane.showMessageDialog(null, getBundle("Accounting").getString("TRANSACTION_REMOVED_FROM")+ " " + journal);
        } else if (source == edit) {
            journal.removeBusinessObject(transaction);
            journal.setCurrentObject(transaction);
            accounting.getJournals().setCurrentObject(journal);
            JOptionPane.showMessageDialog(null, getBundle("Accounting").getString("TRANSACTION_REMOVED_FROM") + " " + journal);
        }
        AccountingComponentMap.refreshAllFrames();
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
