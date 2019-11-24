package be.dafke.BasicAccounting.Meals;

import be.dafke.Accounting.BusinessModel.Ingredient;
import be.dafke.Accounting.BusinessModel.Recipe;
import be.dafke.Accounting.BusinessModel.RecipeLine;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class MealIngredientsViewDataTableModel extends MealIngredientsEditDataTableModel {
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