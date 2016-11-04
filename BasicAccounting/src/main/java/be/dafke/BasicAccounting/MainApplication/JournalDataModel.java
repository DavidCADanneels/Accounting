package be.dafke.BasicAccounting.MainApplication;

import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.Booking;
import be.dafke.BusinessModel.Transaction;
import be.dafke.ComponentModel.RefreshableTableModel;

import java.math.BigDecimal;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class JournalDataModel extends RefreshableTableModel<Booking> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String[] columnNames = { getBundle("Accounting").getString("DEBIT"),
			getBundle("Accounting").getString("CREDIT"),
			getBundle("Accounting").getString("D"),
			getBundle("Accounting").getString("C") };
	Class[] columnClasses = { Account.class, Account.class, BigDecimal.class, BigDecimal.class };

    private Transaction transaction;

// DE GET METHODEN
// ===============
	public Object getValueAt(int row, int col) {
		Booking booking = transaction.getBusinessObjects().get(row);
		if (booking.isDebit()) {
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

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
        if(transaction == null){
            return 0;
        }
		return transaction.getBusinessObjects().size();
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

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

	@Override
	public Booking getObject(int row, int col) {
		return transaction.getBusinessObjects().get(row);
	}

	@Override
	public int getRow(Booking booking) {
		return 0;
	}

//	@Override
//	public Transaction getTransaction() {
//		return transaction;
//	}
}