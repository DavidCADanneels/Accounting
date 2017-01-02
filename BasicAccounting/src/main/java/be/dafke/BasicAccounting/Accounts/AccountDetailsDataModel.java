package be.dafke.BasicAccounting.Accounts;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.RefreshableTableModel;
import be.dafke.Utils.Utils;

import java.math.BigDecimal;
import java.util.Calendar;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */
public class AccountDetailsDataModel extends RefreshableTableModel<Booking> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Account rekening;
	private final String[] columnNames = {
			getBundle("Accounting").getString("NR"),
			getBundle("Accounting").getString("DATE"),
			getBundle("Accounting").getString("DEBIT"),
			getBundle("Accounting").getString("CREDIT"),
			getBundle("Accounting").getString("DESCRIPTION") };
	private final Class[] columnClasses = { String.class, String.class, BigDecimal.class, BigDecimal.class,
			String.class };

	public AccountDetailsDataModel(Account account) {
		rekening = account;
	}

// DE GET METHODEN
// ===============

	public int getRowCount() {
		return rekening.getBusinessObjects().size();
	}

	public int getColumnCount() {
		return 5;
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
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
        if (col == 0) {
            return movement.getTransactionString();
        } else if (col == 1) {
            return Utils.toString(movement.getDate());
        } else if (col == 2) {
            if (movement.isDebit()) return movement.getAmount();
            return "";
        } else if (col == 3) {
            if (!movement.isDebit()) return movement.getAmount();
            return "";
        } else {
            return movement.getDescription();
        }
    }

	@Override
	public Class getColumnClass(int col) {
		return columnClasses[col];
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
