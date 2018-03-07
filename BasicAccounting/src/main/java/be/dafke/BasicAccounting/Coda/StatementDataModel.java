package be.dafke.BasicAccounting.Coda;

import be.dafke.BusinessModel.CounterParty;
import be.dafke.BusinessModel.Statement;
import be.dafke.BusinessModel.Statements;
import be.dafke.Utils.Utils;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;

public class StatementDataModel extends AbstractTableModel {
	private final String[] columnNames = { "Name", "Date", "D/C", "Amount", "CounterParty",
			"TransactionCode", "Communication" };
	private final Class[] columnClasses = { String.class, String.class, String.class, BigDecimal.class,
			CounterParty.class, String.class, String.class };
    private final Statements statements;

	public StatementDataModel(Statements statements) {
		this.statements = statements;
	}

	// DE GET METHODEN
	// ===============
	public Object getValueAt(int row, int col) {
		Statement m = (Statement)statements.getBusinessObjects().get(row);
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

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return statements.getBusinessObjects().size();
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