package be.dafke.BasicAccounting.Accounts.AccountDetails;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTableModel;
import be.dafke.Utils.Utils;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */
public class AccountDetailsDataModel extends SelectableTableModel<Booking> {
	private final Account rekening;

	public static final int NR = 0;
	public static final int DATE = 1;
	public static final int DEBIT = 2;
	public static final int CREDIT = 3;
	public static final int VATINFO = 4;
	public static final int DESCRIPTION = 5;
	private HashMap<Integer, String> columnNames = new HashMap<>();
	private HashMap<Integer, Class> columnClasses = new HashMap<>();



	public AccountDetailsDataModel(Account account) {
		rekening = account;
		createColumnNames();
		createColumnClasses();
	}

	private void createColumnNames() {
		columnNames.put(NR, getBundle("Accounting").getString("NR"));
		columnNames.put(DATE, getBundle("Accounting").getString("DATE"));
		columnNames.put(DEBIT, getBundle("Accounting").getString("DEBIT"));
		columnNames.put(CREDIT, getBundle("Accounting").getString("CREDIT"));
		columnNames.put(VATINFO, getBundle("Accounting").getString("VATINFO"));
		columnNames.put(DESCRIPTION, getBundle("Accounting").getString("DESCRIPTION"));
	}

	private void createColumnClasses() {
		columnClasses.put(NR, String.class);
		columnClasses.put(DATE, String.class);
		columnClasses.put(DEBIT, BigDecimal.class);
		columnClasses.put(CREDIT, BigDecimal.class);
		columnClasses.put(VATINFO, String.class);
		columnClasses.put(DESCRIPTION, String.class);
	}

// DE GET METHODEN
// ===============

	public int getRowCount() {
		return rekening.getBusinessObjects().size();
	}

	public int getColumnCount() {
		return columnClasses.size();
	}

	@Override
	public String getColumnName(int col) {
		return columnNames.get(col);
	}

	public Booking getObject(int row, int col) {
		return rekening.getBusinessObjects().get(row).getBooking();
	}

	public int getRow(Booking booking){
		int row=0;
		for(Movement movement:rekening.getBusinessObjects()){
			if(movement.getBooking()!=booking){
				row++;
			} else{
				return row;
			}
		}
		return 0;
	}

	public Object getValueAt(int row, int col) {
        Movement movement = rekening.getBusinessObjects().get(row);
        if (col == NR) {
            return movement.getTransactionString();
        } else if (col == DATE) {
            return Utils.toString(movement.getDate());
        } else if (col == DEBIT) {
            if (movement.isDebit()) return movement.getAmount();
            return "";
        } else if (col == CREDIT) {
            if (!movement.isDebit()) return movement.getAmount();
            return "";
        } else if (col == DESCRIPTION) {
            return movement.getDescription();
        } else if (col == VATINFO) {
			return movement.getBooking().getVATBookingsString();
		} else return null;
    }

	@Override
	public Class getColumnClass(int col) {
		return columnClasses.get(col);
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return (col == 1 || col == 4);
	}

// DE SET METHODEN
// ===============

	@Override
	public void setValueAt(Object value, int row, int col) {
		Movement movement = rekening.getBusinessObjects().get(row);
		if (col == 1) {
			Calendar newDate = Utils.toCalendar((String) value);
			Transaction transaction = getTransaction(movement);
			Journal journal = getJournal(movement);
			if(newDate!=null && transaction != null && journal != null) {
				journal.changeDate(transaction, newDate);
				Main.fireJournalDataChanged(journal);
			}
		} else if (col == 4) {
			if(movement!=null) {
				movement.setDescription((String) value);
			}
		}
		fireTableDataChanged();
	}

	private Journal getJournal(Movement movement){
		return getTransaction(movement)==null?null:getTransaction(movement).getJournal();
	}

	private Transaction getTransaction(Movement movement){
		return movement==null?null:movement.getBooking()==null?null:movement.getBooking().getTransaction();
	}
}
