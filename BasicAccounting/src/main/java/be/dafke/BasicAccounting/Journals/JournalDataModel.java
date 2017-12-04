package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.Booking;
import be.dafke.BusinessModel.Transaction;
import be.dafke.ComponentModel.SelectableTableModel;

import java.math.BigDecimal;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class JournalDataModel extends SelectableTableModel<Booking> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int DEBIT_ACCOUNT = 0;
	public static final int DEBIT_AMOUNT = 2;
	public static final int CREDIT_ACCOUNT = 1;
	public static final int CREDIT_AMOUNT = 3;
	String[] columnNames = { getBundle("Accounting").getString("DEBIT"),
			getBundle("Accounting").getString("CREDIT"),
			getBundle("Accounting").getString("D"),
			getBundle("Accounting").getString("C") };
	Class[] columnClasses = { Account.class, Account.class, BigDecimal.class, BigDecimal.class };

    private Transaction transaction;

// DE GET METHODEN
// ===============
	public Object getValueAt(int row, int col) {
		Booking booking = getBooking(row);
		if (booking.isDebit()) {
			if (col == DEBIT_ACCOUNT) {
				return booking.getAccount();
			}
			if (col == DEBIT_AMOUNT) {
				return booking.getAmount();
			}
			return null;
		}// else credit
		if (col == CREDIT_ACCOUNT) {
			return booking.getAccount();
		}
		if (col == CREDIT_AMOUNT) {
			return booking.getAmount();
		}
		return null;
	}

	public Booking getBooking(int row){
		return transaction.getBusinessObjects().get(row);
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
        return transaction == null?0:transaction.getBusinessObjects().size();
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
		return getValueAt(row,col)!=null;
	}

// DE SET METHODEN
// ===============
	@Override
	public void setValueAt(Object value, int row, int col) {
		Booking booking = getBooking(row);
		if(col == DEBIT_ACCOUNT || col == CREDIT_ACCOUNT){
			Account newAccount = (Account)value;
			// FIXME: use only for unbooked Bookings
			// if already booked, old account must be unbooked
			// and newAccount booked
			// What to do when editing (booked/unbooked) vatTransactions ???
			if(newAccount!=null) {
				booking.setAccount(newAccount);
			}
			fireTableDataChanged();
		} else if(col == DEBIT_AMOUNT || col == CREDIT_AMOUNT){
			BigDecimal newAmount = (BigDecimal) value;
			if(newAmount!=null){
				Transaction transaction = booking.getTransaction();
				transaction.removeBusinessObject(booking);
				booking.setAmount(newAmount);
				transaction.addBusinessObject(booking);
			}
			Main.fireTransactionInputDataChanged();
		}
	}

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

	public Transaction getTransaction() {
		return transaction;
	}

	@Override
	public Booking getObject(int row, int col) {

		return transaction.getBusinessObjects().get(row);
	}

}