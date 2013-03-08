package be.dafke.Accounting.GUI.JournalManagement;

import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Journal;

import javax.swing.table.AbstractTableModel;

public class NewJournalDataModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = { "Journal (Name)", "Type", "Next Index" };
	private final Class[] columnClasses = { Journal.class, String.class, Integer.class };
	private final Accounting accounting;

	public NewJournalDataModel(Accounting accounting) {
		this.accounting = accounting;
	}

	@Override
	public int getColumnCount() {
		return columnClasses.length;
	}

	@Override
	public int getRowCount() {
		return accounting.getJournals().getBusinessObjects().size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		Journal journal = accounting.getJournals().getBusinessObjects().get(row);
		if (col == 0) {
			return journal;
		} else if (col == 1) {
			return journal.getJournalType();
		} else if (col == 2) {
			return journal.getId();
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
