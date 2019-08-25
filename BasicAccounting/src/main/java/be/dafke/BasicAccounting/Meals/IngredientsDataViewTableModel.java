package be.dafke.BasicAccounting.Meals;

import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.BusinessModel.Ingredient;
import be.dafke.BusinessModel.Ingredients;
import be.dafke.BusinessModel.Unit;
import be.dafke.ComponentModel.SelectableTableModel;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class IngredientsDataViewTableModel extends IngredientsDataEditTableModel{

	public static int OVERWRITE_NAME_COL = 0;
	public static int OVERWRITE_ALLERGENES_COL = 1;

	public IngredientsDataViewTableModel(Component parent) {
		super(parent);
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	@Override
	public Object getValueAt(int row, int col) {
		Ingredient ingredient = getObject(row, col);
		if(ingredient==null) return null;
		if (col == OVERWRITE_NAME_COL) {
			return ingredient.getName();
		}
		if (col == OVERWRITE_ALLERGENES_COL) {
			return ingredient.getAllergenesString();
		}
		return null;
	}

	// DE SET METHODEN
	// ===============
	@Override
	public void setValueAt(Object value, int row, int col) {
		// not editable
	}
}