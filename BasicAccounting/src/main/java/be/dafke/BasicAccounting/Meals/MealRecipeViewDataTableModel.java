package be.dafke.BasicAccounting.Meals;

import be.dafke.BusinessModel.Ingredient;
import be.dafke.BusinessModel.Recipe;
import be.dafke.BusinessModel.RecipeLine;
import be.dafke.BusinessModel.Unit;
import be.dafke.ComponentModel.SelectableTableModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class MealRecipeViewDataTableModel extends MealRecipeEditDataTableModel {

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