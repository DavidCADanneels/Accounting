package be.dafke.BasicAccounting.Journals;

import be.dafke.BusinessModel.*;
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
public class JournalDetailsDataModel extends SelectableTableModel<Booking> {
	public static final int ID = 0;
	public static final int DATE = 1;
	public static final int DEBIT_ACCOUNT = 2;
	public static final int CREDIT_ACCOUNT = 3;
	public static final int DEBIT_AMOUNT = 4;
	public static final int CREDIT_AMOUNT = 5;
	public static final int VATINFO = 6;
	public static final int DESCRIPTION = 7;
	public static final int NR_OF_COLS = 8;

	private HashMap<Integer, String> columnNames = new HashMap<>();
	private HashMap<Integer, Class> columnClasses = new HashMap<>();

	private Journal journal;

	public JournalDetailsDataModel() {
		createColumnNames();
		createColumnClasses();
	}

	private void createColumnNames() {
		columnNames.put(ID, getBundle("Accounting").getString("NR"));
		columnNames.put(DATE, getBundle("Accounting").getString("DATE"));
		columnNames.put(DEBIT_ACCOUNT, getBundle("Accounting").getString("ACCOUNT"));
		columnNames.put(CREDIT_ACCOUNT, getBundle("Accounting").getString("ACCOUNT"));
		columnNames.put(DEBIT_AMOUNT, getBundle("Accounting").getString("DEBIT"));
		columnNames.put(CREDIT_AMOUNT, getBundle("Accounting").getString("CREDIT"));
		columnNames.put(VATINFO, getBundle("Accounting").getString("VATINFO"));
		columnNames.put(DESCRIPTION, getBundle("Accounting").getString("DESCRIPTION"));
	}


	private void createColumnClasses() {
		columnClasses.put(ID, String.class);
		columnClasses.put(DATE, String.class);
		columnClasses.put(DEBIT_ACCOUNT, Account.class);
		columnClasses.put(CREDIT_ACCOUNT, Account.class);
		columnClasses.put(DEBIT_AMOUNT, BigDecimal.class);
		columnClasses.put(CREDIT_AMOUNT, BigDecimal.class);
		columnClasses.put(VATINFO, String.class);
		columnClasses.put(DESCRIPTION, String.class);
	}

	public void setJournal(Journal journal) {
		this.journal = journal;
	}

// DE GET METHODEN
// ===============

	public int getRowCount() {
		if(journal==null) return 0;
		int size = 0;
        for(Transaction transaction : journal.getBusinessObjects()){
			size += transaction.getBusinessObjects().size();
		}
		return size;
	}

	public int getColumnCount() {
		return NR_OF_COLS;
	}

	@Override
	public String getColumnName(int col) {
		return columnNames.get(col);
	}

	public Booking getValueAt(int row) {
		ArrayList<Booking> boekingen = new ArrayList<>();
		if(journal==null) return null;
		for(Transaction transaction : journal.getBusinessObjects()){
			boekingen.addAll(transaction.getBusinessObjects());
		}
		return boekingen.get(row);
	}

	public Object getValueAt(int row, int col) {
		Booking booking = getValueAt(row);
		Transaction transaction = booking.getTransaction();
		boolean first = (booking == transaction.getBusinessObjects().get(0));
		if (col == ID) {
			if(first){
				if(journal==transaction.getJournal()) {
					return journal.getAbbreviation() + journal.getId(transaction);
				} else {
					return journal.getAbbreviation() + journal.getId(transaction) +
							" (" + transaction.getAbbreviation() + transaction.getId() + ")";
				}
            } else return "";
        } else if (col == DATE) {
            if(first){
                return Utils.toString(transaction.getDate());
            } else return "";
        } else if (col == DEBIT_ACCOUNT) {
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
        } else if (col == DESCRIPTION){
            if(first){
                return booking.getTransaction().getDescription();
            } else return "";
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
		return (col == DATE || col == DESCRIPTION);
	}

// DE SET METHODEN
// ===============

	@Override
	public void setValueAt(Object value, int row, int col) {
        Booking booking = getObject(row,col);
		if(booking!=null) {
			Transaction transaction = booking.getTransaction();
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
	}

	private ArrayList<Booking> getAllItems(){
		ArrayList<Booking> bookings = new ArrayList<>();
		for(Transaction transaction : journal.getBusinessObjects()){
			bookings.addAll(transaction.getBusinessObjects());
		}
		return bookings;
	}

	@Override
	public Booking getObject(int row, int col) {
		if(journal==null) return null;
		ArrayList<Booking> bookings = getAllItems();
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
		if(journal==null) return -1;
		ArrayList<Booking> bookings = getAllItems();
		return getRowInList(bookings,booking);
	}
}
