package be.dafke.Accounting.GUI.Details;

import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Movement;
import be.dafke.Utils;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;
import java.util.Calendar;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */
public class AccountDetailsDataModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Account rekening;
	private final String[] columnNames = {
			getBundle("Accounting").getString("NR"),
			getBundle("Accounting").getString("DATUM"),
			getBundle("Accounting").getString("DEBET"),
			getBundle("Accounting").getString("CREDIT"),
			getBundle("Accounting").getString("OMSCHRIJVING") };
	private final Class[] columnClasses = { String.class, String.class, BigDecimal.class, BigDecimal.class,
			String.class };

	public AccountDetailsDataModel(Account account) {
		rekening = account;
	}

// DE GET METHODEN
// ===============

	@Override
	public int getRowCount() {
		return rekening.getMovements().size();
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public Object getValueAt(int row, int col) {
        Movement movement = rekening.getMovements().get(row);
        if (col == 0) {
            return movement.getBooking().getTransaction().getAbbreviation() + movement.getBooking().getTransaction().getId();
        } else if (col == 1) {
            return Utils.toString(movement.getBooking().getTransaction().getDate());
        } else if (col == 2) {
            if (movement.isDebit()) return movement.getAmount();
            return "";
        } else if (col == 3) {
            if (!movement.isDebit()) return movement.getAmount();
            return "";
        } else {
            return movement.getBooking().getTransaction().getDescription();
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
		Movement movement = rekening.getMovements().get(row);
		if (col == 1) {
			Calendar oudeDatum = movement.getBooking().getTransaction().getDate();
			Calendar nieuweDatum = Utils.toCalendar((String) value);
			if (nieuweDatum != null) movement.getBooking().getTransaction().setDate(nieuweDatum);
			else setValueAt(Utils.toString(oudeDatum), row, col);
		} else if (col == 4) {
			movement.getBooking().getTransaction().setDescription((String) value);
		}
//		parent.repaintAllFrames();
//		super.refresh();
	}
}
