package be.dafke.Coda;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.table.AbstractTableModel;

import be.dafke.Utils;
import be.dafke.Coda.Objects.Movement;

public class GenericMovementDataModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = { "Statement", "Sequence", "Date", "D/C", "Amount", "CounterParty",
			"TransactionCode", "Communication" };
	private final Class[] columnClasses = { String.class, String.class, Calendar.class, String.class, BigDecimal.class,
			CounterParty.class, String.class, String.class };

	private final String transactionCode;
	private CounterParty counterParty;
	private final boolean allowNull;

	private Movement singleMovement;

	public GenericMovementDataModel(CounterParty counterParty, String transactionCode) {
		this(counterParty, transactionCode, false);
	}

	public GenericMovementDataModel(CounterParty counterParty, String transactionCode, boolean allowNull) {
		this.counterParty = counterParty;
		this.transactionCode = transactionCode;
		this.allowNull = allowNull;
	}

	public void setSingleMovement(Movement movement) {
		singleMovement = movement;
		fireTableDataChanged();
	}

	public void switchCounterParty(CounterParty newCounterparty) {
		counterParty = newCounterparty;
		fireTableDataChanged();
	}

	// DE GET METHODEN
	// ===============
	@Override
	public Object getValueAt(int row, int col) {
		Movement m;
		if (singleMovement != null) {
			m = singleMovement;
		} else {
			m = Movements.getMovements(counterParty, transactionCode, allowNull).get(row);
		}
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
			CounterParty c = m.getCounterParty();
			if (c == null) c = m.getTmpCounterParty();
			return c;
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
		if (singleMovement != null) {
			return 1;
		}
		return Movements.getMovements(counterParty, transactionCode, allowNull).size();
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

	/*	public Movement getMovement(int row) {
			if (singleMovement != null) {
				return singleMovement;
			}
			return Movements.getMovements(counterParty, transactionCode, allowNull).get(row);
		}*/

	public ArrayList<Movement> getAllMovements() {
		if (singleMovement != null) {
			ArrayList<Movement> result = new ArrayList<Movement>();
			result.add(singleMovement);
			return result;
		}
		return Movements.getMovements(counterParty, transactionCode, allowNull);
	}
}