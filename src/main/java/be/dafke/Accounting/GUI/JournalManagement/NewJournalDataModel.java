package be.dafke.Accounting.GUI.JournalManagement;

import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Accountings;
import be.dafke.Accounting.Objects.Accounting.Journal;

import javax.swing.table.AbstractTableModel;

public class NewJournalDataModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = { "Journal (Name)", "Type", "Next Index" };
	private final Class[] columnClasses = { Journal.class, String.class, Integer.class };
	private final Accountings accountings;

	public NewJournalDataModel(Accountings accountings) {
		this.accountings = accountings;
	}

	@Override
	public int getColumnCount() {
		return columnClasses.length;
	}

	@Override
	public int getRowCount() {
		Accounting accounting = accountings.getCurrentAccounting();
		return accounting.getJournals().size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		Accounting accounting = accountings.getCurrentAccounting();
		Journal journal = accounting.getJournals().getAllJournals().get(row);
		if (col == 0) {
			return journal;
		} else if (col == 1) {
			return journal.getType();
		} else if (col == 2) {
			return Integer.valueOf(journal.getId());
		} else return null;
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
