package be.dafke.BasicAccounting.Journals;

import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.Booking;
import be.dafke.BusinessModel.Journal;
import be.dafke.BusinessModel.Transaction;
import be.dafke.ComponentModel.SelectableTableModel;
import be.dafke.Utils.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */
public class TransactionDataModel extends SelectableTableModel<Booking> {
	public static final int DEBIT_ACCOUNT = 0;
	public static final int CREDIT_ACCOUNT = 1;
	public static final int DEBIT_AMOUNT = 2;
	public static final int CREDIT_AMOUNT = 3;
	public static final int VATINFO = 4;
	public static final int NR_OF_COLS = 5;

	private HashMap<Integer, String> columnNames = new HashMap<>();
	private HashMap<Integer, Class> columnClasses = new HashMap<>();

	private Transaction transaction;

	public TransactionDataModel() {
		createColumnNames();
		createColumnClasses();
	}

	private void createColumnNames() {
		columnNames.put(DEBIT_ACCOUNT, getBundle("Accounting").getString("ACCOUNT"));
		columnNames.put(CREDIT_ACCOUNT, getBundle("Accounting").getString("ACCOUNT"));
		columnNames.put(DEBIT_AMOUNT, getBundle("Accounting").getString("DEBIT"));
		columnNames.put(CREDIT_AMOUNT, getBundle("Accounting").getString("CREDIT"));
		columnNames.put(VATINFO, getBundle("Accounting").getString("VATINFO"));
	}


	private void createColumnClasses() {
		columnClasses.put(DEBIT_ACCOUNT, Account.class);
		columnClasses.put(CREDIT_ACCOUNT, Account.class);
		columnClasses.put(DEBIT_AMOUNT, BigDecimal.class);
		columnClasses.put(CREDIT_AMOUNT, BigDecimal.class);
		columnClasses.put(VATINFO, String.class);
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

// DE GET METHODEN
// ===============

	public int getRowCount() {
		if(transaction==null) return 0;
		return transaction.getBusinessObjects().size();
	}

	public int getColumnCount() {
		return NR_OF_COLS;
	}

	@Override
	public String getColumnName(int col) {
		return columnNames.get(col);
	}

	public Booking getValueAt(int row) {
		if(transaction==null) return null;
		ArrayList<Booking> bookings = transaction.getBusinessObjects();
		return bookings.get(row);
	}

	public Object getValueAt(int row, int col) {
		Booking booking = getValueAt(row);
        if (col == DEBIT_ACCOUNT) {
			if (booking.isDebit())
				return booking.getAccount();
			else return null;
		} else if (col == CREDIT_ACCOUNT) {
			if(!booking.isDebit())
				return booking.getAccount();
			else return null;
        } else if (col == DEBIT_AMOUNT) {
            if (booking.isDebit()) return booking.getAmount();
            return "";
        } else if (col == CREDIT_AMOUNT) {
            if (!booking.isDebit()) return booking.getAmount();
            return "";
        } else if (col == VATINFO){
            return booking.getVATBookingsString();
		} else return null;
    }

	@Override
	public Class getColumnClass(int col) {
		return columnClasses.get(col);
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}

// DE SET METHODEN
// ===============

	@Override
	public Booking getObject(int row, int col) {
		if(transaction==null) return null;
		ArrayList<Booking> bookings = transaction.getBusinessObjects();
		return bookings.get(row);
	}

	private int getRowInList(ArrayList<Booking> list, Booking booking){
		int row = 0;
		for(Booking search:list){
			if(search!=booking){
				row++;
			} else{
				return row;
			}
		}
		// TODO: return -1 and catch effects
		return 0;
	}

	public int getRow(Booking booking) {
		if(transaction==null) return -1;
		ArrayList<Booking> bookings = transaction.getBusinessObjects();
		return getRowInList(bookings,booking);
	}
}
