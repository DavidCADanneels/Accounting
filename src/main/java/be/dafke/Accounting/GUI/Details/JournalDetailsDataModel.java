package be.dafke.Accounting.GUI.Details;

import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Booking;
import be.dafke.Accounting.Objects.Accounting.Journal;
import be.dafke.Accounting.Objects.Accounting.Transaction;
import be.dafke.Utils;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;
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
	private final Journal journal;
	private final String[] columnNames = {
			java.util.ResourceBundle.getBundle("Accounting").getString("NR"),
			java.util.ResourceBundle.getBundle("Accounting").getString("DATUM"),
			java.util.ResourceBundle.getBundle("Accounting").getString("REKENING"),
			java.util.ResourceBundle.getBundle("Accounting").getString("DEBET"),
			java.util.ResourceBundle.getBundle("Accounting").getString("CREDIT"),
			java.util.ResourceBundle.getBundle("Accounting").getString("OMSCHRIJVING") };
	private final Class[] columnClasses = { String.class, String.class, Account.class, BigDecimal.class,
			BigDecimal.class, String.class };

	public JournalDetailsDataModel(Journal journal) {
		this.journal = journal;
	}

// DE GET METHODEN
// ===============

	@Override
	public int getRowCount() {
		int size = 0;
		Iterator<Transaction> it = journal.getTransactions().iterator();
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
		Booking boeking = journal.getBooking(row);
        boolean first = boeking.isFirstBooking();
        if (col == 0) {
            if(first){
                return boeking.getAbbreviation() + boeking.getId();
            } else return "";
        } else if (col == 1) {
            if(first){
                return Utils.toString(boeking.getDate());
            } else return "";
        } else if (col == 2) {
            return boeking.getAccount();
        } else if (col == 3) {
            if (boeking.isDebit()) return boeking.getAmount();
            return "";
        } else if (col == 4) {
            if (!boeking.isDebit()) return boeking.getAmount();
            return "";
        } else{
            if(first){
                return boeking.getDescription();
            } else return "";
        }
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
        Booking boeking = journal.getBooking(row);
		if (col == 1) {
			Calendar oudeDatum = boeking.getDate();
			Calendar nieuweDatum = Utils.toCalendar((String) value);
			if (nieuweDatum != null) boeking.getTransaction().setDate(nieuweDatum);
			else setValueAt(Utils.toString(oudeDatum), row, col);
		} else if (col == 5) {
			boeking.getTransaction().setDescription((String) value);
		}
		// ouder.repaint();
		// parent.repaintAllFrames();
//		super.refresh();
	}
}
