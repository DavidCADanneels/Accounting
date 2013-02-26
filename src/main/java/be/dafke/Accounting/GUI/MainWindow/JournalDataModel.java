package be.dafke.Accounting.GUI.MainWindow;

import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Booking;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class JournalDataModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String[] columnNames = { getBundle("Accounting").getString("DEBET"),
			getBundle("Accounting").getString("CREDIT"),
			getBundle("Accounting").getString("D"),
			getBundle("Accounting").getString("C") };
	Class[] columnClasses = { Account.class, Account.class, BigDecimal.class, BigDecimal.class };
    private Accounting accounting;

    public JournalDataModel(Accounting accounting){
        this.accounting = accounting;
    }
    
// DE GET METHODEN
// ===============
	@Override
	public Object getValueAt(int row, int col) {
		Booking booking = accounting.getCurrentTransaction().getBookings().get(row);
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

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
        if(accounting == null){
            return 0;
        }
		return accounting.getCurrentTransaction().getBookings().size();
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

    public void setAccounting(Accounting accounting) {
        this.accounting = accounting;
    }
}