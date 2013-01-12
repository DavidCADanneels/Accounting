package be.dafke.Coda;

import java.math.BigDecimal;

import javax.swing.table.AbstractTableModel;

import be.dafke.Utils;
import be.dafke.Coda.Objects.Movement;

public class MovementDataModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = { "Statement", "Sequence", "Date", "D/C", "Amount", "CounterParty",
			"TransactionCode", "Communication" };
	private final Class[] columnClasses = { String.class, String.class, String.class, String.class, BigDecimal.class,
			CounterParty.class, String.class, String.class };

	// DE GET METHODEN
	// ===============
	@Override
	public Object getValueAt(int row, int col) {
		Movement m = Movements.getMovement(row);
		if (col == 0) {
			return m.getStatementNr();
		} else if (col == 1) {
			return m.getSequenceNr();
		} else if (col == 2) {
			return Utils.toString(m.getDate());
		} else if (col == 3) {
			return (m.isDebit()) ? "D" : "C";
		} else if (col == 4) {
			return m.getAmount();
		} else if (col == 5) {
			return m.getCounterParty();
		} else if (col == 6) {
			return m.getTransactionCode();
		} else if (col == 7) {
			return m.getCommunication();
		} else return "";
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return Movements.getSize();
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
	}
}