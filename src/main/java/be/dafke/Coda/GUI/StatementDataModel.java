package be.dafke.Coda.GUI;

import be.dafke.Accounting.Objects.Accounting;
import be.dafke.Coda.Objects.CounterParty;
import be.dafke.Coda.Objects.Statement;
import be.dafke.Utils.Utils;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;

public class StatementDataModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = { "Name", "Date", "D/C", "Amount", "CounterParty",
			"TransactionCode", "Communication" };
	private final Class[] columnClasses = { String.class, String.class, String.class, BigDecimal.class,
			CounterParty.class, String.class, String.class };
	private final Accounting accounting;

	public StatementDataModel(Accounting accounting) {
		this.accounting = accounting;
	}

	// DE GET METHODEN
	// ===============
	@Override
	public Object getValueAt(int row, int col) {
		Statement m = accounting.getStatements().getBusinessObjects().get(row);
		if (col == 0) {
			return m.getName();
		} else if (col == 1) {
			return Utils.toString(m.getDate());
		} else if (col == 2) {
			return (m.isDebit()) ? "(D) -" : "(C) +";
		} else if (col == 3) {
			return m.getAmount();
		} else if (col == 4) {
			return m.getCounterParty();
		} else if (col == 5) {
			return m.getTransactionCode();
		} else if (col == 6) {
			return m.getCommunication();
		} else return "";
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return accounting.getStatements().getBusinessObjects().size();
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