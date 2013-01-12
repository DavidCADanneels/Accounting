package be.dafke.Accounting.Details;

import be.dafke.Accounting.Objects.Account;
import be.dafke.Accounting.Objects.Booking;
import be.dafke.Accounting.Objects.Journal;
import be.dafke.Accounting.Objects.Transaction;
import be.dafke.ParentFrame;
import be.dafke.Utils;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

/**
 * @author David Danneels
 */
public class JournalDetailsDataModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Journal dagboek;
	private final String[] columnNames = {
			java.util.ResourceBundle.getBundle("Accounting").getString("NR"),
			java.util.ResourceBundle.getBundle("Accounting").getString("DATUM"),
			java.util.ResourceBundle.getBundle("Accounting").getString("REKENING"),
			java.util.ResourceBundle.getBundle("Accounting").getString("DEBET"),
			java.util.ResourceBundle.getBundle("Accounting").getString("CREDIT"),
			java.util.ResourceBundle.getBundle("Accounting").getString("OMSCHRIJVING") };
	private final Class[] columnClasses = { String.class, String.class, Account.class, BigDecimal.class,
			BigDecimal.class, String.class };
	private final ParentFrame parent;

	public JournalDetailsDataModel(Journal journal, ParentFrame parent) {
		this.parent = parent;
		dagboek = journal;
	}

// DE GET METHODEN
// ===============

	@Override
	public int getRowCount() {
		int size = 0;
		Iterator<Transaction> it = dagboek.getTransactions().iterator();
		while (it.hasNext()) {
			size += it.next().getBookings().size();
		}
		return size;
	}

	@Override
	public int getColumnCount() {
		return 6;
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public Object getValueAt(int row, int col) {
		ArrayList<Booking> boekingen = new ArrayList<Booking>();
		ArrayList<Transaction> transacties = dagboek.getTransactions();
		Iterator<Transaction> it = transacties.iterator();
		while (it.hasNext()) {
			Transaction trans = it.next();
			boekingen.addAll(trans.getBookings());
		}
		Booking boeking = boekingen.get(row);
		if (col == 0) {
			return boeking.getAbbreviation() + boeking.getId();
		} else if (col == 1) {
			return Utils.toString(boeking.getDate());
		} else if (col == 2) {
			return boeking.getAccount();
		} else if (col == 3) {
			if (boeking.isDebet()) return boeking.getAmount();
			return "";
		} else if (col == 4) {
			if (!boeking.isDebet()) return boeking.getAmount();
			return "";
		} else return boeking.getDescription();
	}

	@Override
	public Class getColumnClass(int col) {
		return columnClasses[col];
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return (col == 1 || col == 5);
	}

// DE SET METHODEN
// ===============

	@Override
	public void setValueAt(Object value, int row, int col) {
		ArrayList<Booking> boekingen = new ArrayList<Booking>();
		ArrayList<Transaction> transacties = dagboek.getTransactions();
		Iterator<Transaction> it = transacties.iterator();
		while (it.hasNext()) {
			Transaction trans = it.next();
			boekingen.addAll(trans.getBookings());
		}
		Booking boeking = boekingen.get(row);
		if (col == 1) {
			Calendar oudeDatum = boeking.getDate();
			Calendar nieuweDatum = Utils.toCalendar((String) value);
			if (nieuweDatum != null) boeking.getTransaction().setDate(nieuweDatum);
			else setValueAt(Utils.toString(oudeDatum), row, col);
		} else if (col == 5) {
			boeking.getTransaction().setDescription((String) value);
		}
		// ouder.repaint();
		parent.repaintAllFrames();
	}
}
