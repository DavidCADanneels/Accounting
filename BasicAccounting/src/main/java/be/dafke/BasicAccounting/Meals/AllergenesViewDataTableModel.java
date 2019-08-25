package be.dafke.BasicAccounting.Meals;

/**
 * @author David Danneels
 */

public class AllergenesViewDataTableModel extends AllergenesDataTableModel {

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	// DE SET METHODEN
	// ===============
	@Override
	public void setValueAt(Object value, int row, int col) {
		// not editable
	}
}