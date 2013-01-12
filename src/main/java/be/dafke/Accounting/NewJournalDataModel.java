package be.dafke.Accounting;

import javax.swing.table.AbstractTableModel;

import be.dafke.Accounting.Objects.Accountings;
import be.dafke.Accounting.Objects.Journal;

public class NewJournalDataModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = { "Journal (Name)", "Type", "Next Index" };
	private final Class[] columnClasses = { Journal.class, String.class, Integer.class };

//	private final AccountingGUIFrame parent;

//	public NewJournalDataModel(AccountingGUIFrame parent) {
//		this.parent = parent;
//	}

	@Override
	public int getColumnCount() {
		return columnClasses.length;
	}

	@Override
	public int getRowCount() {
		return Accountings.getCurrentAccounting().getJournals().size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		Journal journal = Accountings.getCurrentAccounting().getJournals().getAllJournals().get(row);
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
