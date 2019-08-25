package be.dafke.BasicAccounting.Meals;

import be.dafke.BusinessModel.Accounting;

import java.awt.*;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class MealsEditDataViewTableModel extends MealsEditDataTableModel {
	public MealsEditDataViewTableModel(Component parent, Accounting accounting) {
		super(parent, accounting);
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	// DE SET METHODEN
	// ===============
	@Override
	public void setValueAt(Object value, int row, int col) {
		// Not editable
	}
}