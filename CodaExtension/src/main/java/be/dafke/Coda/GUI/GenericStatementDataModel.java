package be.dafke.Coda.GUI;

import be.dafke.Coda.Objects.CounterParty;
import be.dafke.Coda.Objects.Statement;
import be.dafke.Coda.Objects.Statements;
import be.dafke.Coda.Objects.TmpCounterParty;
import be.dafke.ComponentModel.RefreshableTableModel;
import be.dafke.Utils.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

public class GenericStatementDataModel extends RefreshableTableModel<Statement> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = { "Name", "Date", "D/C", "Amount", "Old CounterParty",
			"New CounterParty", "TransactionCode", "Communication" };
	private final Class[] columnClasses = { String.class, Calendar.class, String.class, BigDecimal.class,
			CounterParty.class, TmpCounterParty.class, String.class, String.class };
	private Statement singleStatement;
    private SearchOptions searchOptions;
    private Statements statements;

    public GenericStatementDataModel(SearchOptions searchOptions, Statements statements) {
		this.statements = statements;
        this.searchOptions = searchOptions;
	}

	public void setSingleStatement(Statement statement) {
		singleStatement = statement;
		fireTableDataChanged();
	}

	// DE GET METHODEN
	// ===============
	public Object getValueAt(int row, int col) {
		Statement m = getAllStatements().get(row);
        if (col == 0) {
			return m.getName();
		} else if (col == 1) {
			return Utils.toString(m.getDate());
		} else if (col == 2) {
			return (m.isDebit()) ? "D" : "C";
		} else if (col == 3) {
			return m.getAmount();
		} else if (col == 4) {
			return m.getCounterParty();
        } else if (col == 5) {
		    return m.getTmpCounterParty();
		} else if (col == 6) {
			return m.getTransactionCode();
		} else if (col == 7) {
			return m.getCommunication();
		} else return "";
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return getAllStatements().size();
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

	public ArrayList<Statement> getAllStatements() {
        if (singleStatement != null) {
			ArrayList<Statement> result = new ArrayList<Statement>();
			result.add(singleStatement);
			return result;
		}
		return statements.getStatements(searchOptions);
	}

	@Override
	public Statement getObject(int row, int col) {
		return null;
	}

	@Override
	public int getRow(Statement statement) {
		return 0;
	}
}