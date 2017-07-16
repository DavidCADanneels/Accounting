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

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */
public class JournalDetailsDataModel extends SelectableTableModel<Booking> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Journal journal;
	private final String[] columnNames = {
			getBundle("Accounting").getString("NR"),
			getBundle("Accounting").getString("DATE"),
			getBundle("Accounting").getString("ACCOUNT"),
			getBundle("Accounting").getString("ACCOUNT"),
			getBundle("Accounting").getString("DEBIT"),
			getBundle("Accounting").getString("CREDIT"),
			getBundle("Accounting").getString("DESCRIPTION") };
	private final Class[] columnClasses = { String.class, String.class, Account.class, Account.class, BigDecimal.class,
			BigDecimal.class, String.class };

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
		return 7;
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
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
		Booking boeking = getValueAt(row);
        boolean first = (boeking == boeking.getTransaction().getBusinessObjects().get(0));
        if (col == 0) {
            if(first){
                return boeking.getTransaction().getAbbreviation() + boeking.getTransaction().getId();
            } else return "";
        } else if (col == 1) {
            if(first){
                return Utils.toString(boeking.getTransaction().getDate());
            } else return "";
        } else if (col == 2) {
			if (boeking.isDebit())
				return boeking.getAccount();
			else return null;
		} else if (col == 3) {
			if(!boeking.isDebit())
				return boeking.getAccount();
			else return null;
        } else if (col == 4) {
            if (boeking.isDebit()) return boeking.getAmount();
            return "";
        } else if (col == 5) {
            if (!boeking.isDebit()) return boeking.getAmount();
            return "";
        } else{
            if(first){
                return boeking.getTransaction().getDescription();
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
        Booking booking = getObject(row,col);
		if(booking!=null) {
			Transaction transaction = booking.getTransaction();
			if (col == 1) {
				Calendar date = transaction.getDate();
				Calendar newDate = Utils.toCalendar((String) value);
				if (journal != null && newDate != null) {
					journal.changeDate(transaction, newDate);
				} else setValueAt(Utils.toString(date), row, col);
			} else if (col == 5) {
				transaction.setDescription((String) value);
			}
			fireTableDataChanged();
		}
	}

	@Override
	public Booking getObject(int row, int col) {
		if(journal==null) return null;
		ArrayList<Booking> boekingen = new ArrayList<>();
		for(Transaction transaction : journal.getBusinessObjects()){
			boekingen.addAll(transaction.getBusinessObjects());
		}
		return boekingen.get(row);
	}

	public int getRow(Booking booking) {
		if(journal==null) return -1;
		int row = 0;
		ArrayList<Booking> boekingen = new ArrayList<>();
		for(Transaction transaction : journal.getBusinessObjects()){
			boekingen.addAll(transaction.getBusinessObjects());
		}
		for(Booking booking1:boekingen){
			if(booking1!=booking){
				row++;
			} else{
				return row;
			}
		}
		return 0;
	}
}
