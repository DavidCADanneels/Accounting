package be.dafke.BasicAccounting.Meals;

import be.dafke.BusinessModel.Ingredient;
import be.dafke.BusinessModel.Recipe;
import be.dafke.BusinessModel.RecipeLine;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class MealIngredientsEditDataTableModel extends MealRecipeEditDataTableModel {
	private Recipe recipe;
	public static int NAME_COL = 0;
	public static int OVERWRITE_ALLERGENES_COL = 1;

	@Override
	public int getColumnCount() {
		return 2;
	}


	public static int NR_OF_COL = 4;

	void setColumnClasses() {
		columnClasses.put(NAME_COL, String.class);
		columnClasses.put(OVERWRITE_ALLERGENES_COL, String.class);
	}

	void setColumnNames() {
		columnNames.put(NAME_COL, getBundle("Accounting").getString("INGREDIENT_NAME"));
		columnNames.put(OVERWRITE_ALLERGENES_COL, getBundle("Accounting").getString("ALLERGENES"));
	}
	// DE GET METHODEN
// ===============
	public Object getValueAt(int row, int col) {
		RecipeLine recipeLine = getObject(row, col);
		Ingredient ingredient = recipeLine.getIngredient();
		if(ingredient==null) return null;
		if (col == NAME_COL) {
			return ingredient.getName();
		}
		if (col == OVERWRITE_ALLERGENES_COL) {
			return ingredient.getAllergenesString();
		}
		return null;
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