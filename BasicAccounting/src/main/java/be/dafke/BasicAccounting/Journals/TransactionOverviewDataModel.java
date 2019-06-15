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
public class TransactionOverviewDataModel extends SelectableTableModel<Transaction> {
	public static final int ID = 0;
	public static final int DATE = 1;
	public static final int DESCRIPTION = 2;
	public static final int TOTAL_AMOUNT = 3;
	public static final int NR_OF_COLS = 4;

	private HashMap<Integer, String> columnNames = new HashMap<>();
	private HashMap<Integer, Class> columnClasses = new HashMap<>();

	private Journal journal;

	public TransactionOverviewDataModel() {
		createColumnNames();
		createColumnClasses();
	}

	private void createColumnNames() {
		columnNames.put(ID, getBundle("Accounting").getString("NR"));
		columnNames.put(DATE, getBundle("Accounting").getString("DATE"));
		columnNames.put(DESCRIPTION, getBundle("Accounting").getString("DESCRIPTION"));
		columnNames.put(TOTAL_AMOUNT, getBundle("Accounting").getString("TOTAL_AMOUNT"));
	}


	private void createColumnClasses() {
		columnClasses.put(ID, String.class);
		columnClasses.put(DATE, String.class);
		columnClasses.put(DESCRIPTION, String.class);
		columnClasses.put(TOTAL_AMOUNT, BigDecimal.class);
	}

	public void setJournal(Journal journal) {
		this.journal = journal;
	}

// DE GET METHODEN
// ===============

	public int getRowCount() {
		if(journal==null) return 0;
		return journal.getBusinessObjects().size();
	}

	public int getColumnCount() {
		return NR_OF_COLS;
	}

	@Override
	public String getColumnName(int col) {
		return columnNames.get(col);
	}

	public Transaction getValueAt(int row) {
		ArrayList<Transaction> transactions = journal.getBusinessObjects();
		return transactions.get(row);
	}

	public Object getValueAt(int row, int col) {
		Transaction transaction = getValueAt(row);
		if (col == ID) {
			if(journal==transaction.getJournal()) {
				return journal.getAbbreviation() + journal.getId(transaction);
			} else {
				return journal.getAbbreviation() + journal.getId(transaction) +
						" (" + transaction.getAbbreviation() + transaction.getId() + ")";
			}
        } else if (col == DATE) {
			return Utils.toString(transaction.getDate());
		} else if (col == TOTAL_AMOUNT) {
			return transaction.getCreditTotaal();
        } else if (col == DESCRIPTION){
			return transaction.getDescription();
		} else return null;
    }

	@Override
	public Class getColumnClass(int col) {
		return columnClasses.get(col);
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return (col == DATE || col == DESCRIPTION);
	}

// DE SET METHODEN
// ===============

	@Override
	public void setValueAt(Object value, int row, int col) {
        Transaction transaction = getObject(row,col);
		if (col == DATE) {
			Calendar date = transaction.getDate();
			Calendar newDate = Utils.toCalendar((String) value);
			if (journal != null && newDate != null) {
				transaction.setDate(newDate);
			} else setValueAt(Utils.toString(date), row, col);
		} else if (col == DESCRIPTION) {
			transaction.setDescription((String) value);
		}
		fireTableDataChanged();
	}

	@Override
	public Transaction getObject(int row, int col) {
		if(journal==null) return null;
		ArrayList<Transaction> transactions = journal.getBusinessObjects();
		return transactions.get(row);
	}

	private int getRowInList(Transaction transaction){
		int row = 0;
		ArrayList<Transaction> list = journal.getBusinessObjects();
		for(Transaction search:list){
			if(search!=transaction){
				row++;
			} else{
				return row;
			}
		}
		// TODO: return -1 and catch effects
		return 0;
	}

	public int getRow(Transaction transaction) {
		if(journal==null) return -1;
		return getRowInList(transaction);
	}
}