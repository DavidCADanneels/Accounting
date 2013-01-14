package be.dafke.Accounting.GUI.MainWindow;

import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Booking;
import be.dafke.Accounting.Objects.Accounting.Transaction;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;

/**
 * @author David Danneels
 */

public class JournalDataModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String[] columnNames = { java.util.ResourceBundle.getBundle("Accounting").getString("DEBET"),
			java.util.ResourceBundle.getBundle("Accounting").getString("CREDIT"),
			java.util.ResourceBundle.getBundle("Accounting").getString("D"),
			java.util.ResourceBundle.getBundle("Accounting").getString("C") };
	Class[] columnClasses = { Account.class, Account.class, BigDecimal.class, BigDecimal.class };

// DE GET METHODEN
// ===============
	@Override
	public Object getValueAt(int row, int col) {
		Booking booking = Transaction.getInstance().getBookings().get(row);
		if (booking.isDebet()) {
			if (col == 0) {
				return booking.getAccount();
			}
			if (col == 2) {
				return booking.getAmount();
			}
			return null;
		}// else credit
		if (col == 1) {
			return booking.getAccount();
		}
		if (col == 3) {
			return booking.getAmount();
		}
		return null;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return Transaction.getInstance().getBookings().size();
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public Class getColumnClass(int col) {
		return columnClasses[col];
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}

// DE SET METHODEN
// ===============
	@Override
	public void setValueAt(Object value, int row, int col) {
//        data[row][col] = value;
	}
}