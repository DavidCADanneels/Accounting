package be.dafke.BasicAccounting.Meals;

import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTableModel;

import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class MealRecipeEditDataTableModel extends SelectableTableModel<RecipeLine> {
	private Recipe recipe;
	public static int NAME_COL = 0;
	public static int AMOUNT_COL = 1;
	public static int UNIT_COL = 2;
	public static int ALLERGENES_COL = 3;
	public static int NR_OF_COL = 4;
	HashMap<Integer,String> columnNames = new HashMap<>();
	HashMap<Integer,Class> columnClasses = new HashMap<>();
	List<Integer> editableColumns = new ArrayList<>();


	public MealRecipeEditDataTableModel() {
		setColumnNames();
		setColumnClasses();
		setEditableColumns();
	}


	void setEditableColumns() {
		editableColumns.add(AMOUNT_COL);
		editableColumns.add(UNIT_COL);
	}

	void setColumnClasses() {
		columnClasses.put(NAME_COL, String.class);
		columnClasses.put(AMOUNT_COL, BigDecimal.class);
		columnClasses.put(UNIT_COL, Unit.class);
		columnClasses.put(ALLERGENES_COL, String.class);
	}

	void setColumnNames() {
		columnNames.put(NAME_COL, getBundle("Accounting").getString("INGREDIENT_NAME"));
		columnNames.put(AMOUNT_COL, getBundle("Accounting").getString("INGREDIENT_AMOUNT"));
		columnNames.put(UNIT_COL, getBundle("Accounting").getString("INGREDIENT_UNIT"));
		columnNames.put(ALLERGENES_COL, getBundle("Accounting").getString("ALLERGENES"));
	}
	// DE GET METHODEN
// ===============
	public Object getValueAt(int row, int col) {
		RecipeLine recipeLine = getObject(row, col);
		if (col == AMOUNT_COL) {
			return recipeLine.getAmount();
		}
		Ingredient ingredient = recipeLine.getIngredient();
		if(ingredient==null) return null;
		if (col == NAME_COL) {
			return ingredient.getName();
		}
		if (col == UNIT_COL) {
			return ingredient.getUnit();
		}
		if (col == ALLERGENES_COL) {
			return ingredient.getAllergenesString();
		}
		return null;
	}

	@Override
	public int getColumnCount() {
		return NR_OF_COL;
	}

	@Override
	public int getRowCount() {
		if(recipe == null){
			return 0;
		}
		return recipe.getBusinessObjects().size();
	}

	@Override
	public String getColumnName(int col) {
		return columnNames.get(col);
	}

	@Override
	public Class getColumnClass(int col) {
		return columnClasses.get(col);
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return editableColumns.contains(col);
	}

	// DE SET METHODEN
	// ===============
	@Override
	public void setValueAt(Object value, int row, int col) {
		// existing items not editable
		if (col == AMOUNT_COL) {
			BigDecimal amount = (BigDecimal) value;
			RecipeLine recipeLine = getObject(row, col);
			recipeLine.setAmount(amount);
		}

	}

	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}

	@Override
	public RecipeLine getObject(int row, int col) {
		return recipe==null?null:recipe.getBusinessObjects().get(row);
	}
}