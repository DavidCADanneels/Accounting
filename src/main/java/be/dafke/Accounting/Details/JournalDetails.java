package be.dafke.Accounting.Details;

import be.dafke.Accounting.Objects.Accounting;
import be.dafke.Accounting.Objects.Accountings;
import be.dafke.Accounting.Objects.Journal;
import be.dafke.Accounting.Objects.Transaction;
import be.dafke.RefreshableTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

/**
 * @author David Danneels
 */

public class JournalDetails extends RefreshableTable implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPopupMenu popup;
	private Object selected;
	private final JMenuItem move, delete;
	private final Journal journal;
	private final Accountings accountings;

//	private final AccountingGUIFrame parent;

	public JournalDetails(Journal journal, Accountings accountings) {
		super(java.util.ResourceBundle.getBundle("Accounting").getString("DAGBOEK_DETAILS")
				+ journal.toString(), new JournalDetailsDataModel(journal));
		// this.parent = parent;
		this.accountings = accountings;
		this.journal = journal;
		tabel.setAutoCreateRowSorter(true);
		popup = new JPopupMenu();
		delete = new JMenuItem(java.util.ResourceBundle.getBundle("Accounting").getString("VERWIJDER"));
		move = new JMenuItem(java.util.ResourceBundle.getBundle("Accounting").getString("VERPLAATS"));
		delete.addActionListener(this);
		move.addActionListener(this);
		popup.add(delete);
		popup.add(move);
		tabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				Point cell = me.getPoint();//
				Point location = me.getLocationOnScreen();
				int col = tabel.columnAtPoint(cell);
				if (col == 0 && me.getClickCount() == 2) {
					int row = tabel.rowAtPoint(cell);
					selected = tabel.getValueAt(row, col);
					popup.show(null, location.x, location.y);
				} else popup.setVisible(false);
			}
		});
	}

	@Override
	public void windowClosing(WindowEvent we) {
		super.windowClosing(we);
		popup.setVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() instanceof JMenuItem) {
			menuAction((JMenuItem) ae.getSource());
		}
	}

	private void menuAction(JMenuItem source) {
		popup.setVisible(false);
		String s;
		if (selected == null) s = "";
		else s = selected.toString();
		ArrayList<Transaction> transacties = journal.getTransactions();
		int nr = Integer.parseInt(s.replaceFirst(journal.getAbbreviation(), ""));
		Transaction transactie = transacties.get(nr - 1);
		if (source == move) {
			Accounting accounting = accountings.getCurrentAccounting();
			ArrayList<Journal> dagboeken = accounting.getJournals().getAllJournalsExcept(journal);
			Object[] lijst = dagboeken.toArray();
			int keuze = JOptionPane.showOptionDialog(null,
					java.util.ResourceBundle.getBundle("Accounting").getString("KIES_DAGBOEK"),
					java.util.ResourceBundle.getBundle("Accounting").getString("DAGBOEK_KEUZE"),
					JOptionPane.YES_OPTION, JOptionPane.INFORMATION_MESSAGE, null, lijst, lijst[0]);
			Journal newJournal = (Journal) lijst[keuze];
			transactie.moveTransaction(journal, newJournal);

			JOptionPane.showMessageDialog(
					null,
					java.util.ResourceBundle.getBundle("Accounting").getString(
							"TRANSACTIE_VERPLAATST_VAN")
							+ journal
							+ java.util.ResourceBundle.getBundle("Accounting").getString("NAAR")
							+ newJournal);
		} else if (source == delete) {
			transactie.deleteTransaction(journal);
			JOptionPane.showMessageDialog(
					null,
					java.util.ResourceBundle.getBundle("Accounting").getString(
							"TRANSACTIE_VERWIJDERD_UIT")
							+ journal);
		}
		super.refresh();
	}
}