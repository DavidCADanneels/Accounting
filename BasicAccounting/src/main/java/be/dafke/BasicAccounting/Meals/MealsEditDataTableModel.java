package be.dafke.BasicAccounting.Meals;

import be.dafke.BasicAccounting.MainApplication.ActionUtils;
import be.dafke.Accounting.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTableModel;
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException;

import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class MealsEditDataTableModel extends SelectableTableModel<Meal> {
	private final MealOrders mealOrders;
	private final Meals meals;
	public static int NR_COL = 0;
	public static int NAME_COL = 1;
	public static int SALES_PRICE_COL = 2;
	public static int DESCRIPTION_COL = 3;
	public static int USAGE_COL = 4;
	public static int NR_OF_COL = 5;
	private final Component parent;
	private HashMap<Integer,String> columnNames = new HashMap<>();
	private HashMap<Integer,Class> columnClasses = new HashMap<>();
	private List<Integer> editableColumns = new ArrayList<>();

	public MealsEditDataTableModel(Component parent, Accounting accounting) {
		meals = accounting.getMeals();
		mealOrders = accounting.getMealOrders();
		this.parent = parent;
		setColumnNames();
		setColumnClasses();
		setEditableColumns();
	}

	private void setEditableColumns() {
		editableColumns.add(NR_COL);
		editableColumns.add(NAME_COL);
		editableColumns.add(DESCRIPTION_COL);
		editableColumns.add(SALES_PRICE_COL);
	}

	private void setColumnClasses() {
		columnClasses.put(NR_COL, String.class);
		columnClasses.put(USAGE_COL, Integer.class);
		columnClasses.put(NAME_COL, String.class);
		columnClasses.put(DESCRIPTION_COL, String.class);
		columnClasses.put(SALES_PRICE_COL, BigDecimal.class);
	}

	private void setColumnNames() {
		columnNames.put(NR_COL, getBundle("Accounting").getString("MEAL_NR"));
		columnNames.put(USAGE_COL, getBundle("Accounting").getString("USAGE"));
		columnNames.put(NAME_COL, getBundle("Accounting").getString("MEAL_NAME"));
		columnNames.put(DESCRIPTION_COL, getBundle("Accounting").getString("DESCRIPTION"));
		columnNames.put(SALES_PRICE_COL, getBundle("Accounting").getString("PRICE"));
	}
	// DE GET METHODEN
// ===============
	public Object getValueAt(int row, int col) {
		Meal meal = getObject(row, col);
		if(meal==null) return null;
		if (col == NR_COL) {
			return meal.getName();
		}
		if (col == USAGE_COL) {
			return mealOrders.nrOfMeals(meal);
		}
		if (col == NAME_COL) {
			return meal.getMealName();
		}
		if (col == DESCRIPTION_COL) {
			return meal.getDescription();
		}
		if (col == SALES_PRICE_COL) {
			return meal.getSalesPrice();
		}
		return null;
	}

	public int getColumnCount() {
		return NR_OF_COL;
	}

	public int getRowCount() {
        if(meals == null){
            return 0;
        }
		return meals.getBusinessObjects().size();
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
		Meal meal = getObject(row,col);
		if(col == SALES_PRICE_COL){
			BigDecimal purchasePrice = (BigDecimal) value;
			meal.setSalesPrice(purchasePrice.setScale(2));
		}
		if(col == DESCRIPTION_COL){
            meal.setDescription((String) value);
		}
		if(col == NAME_COL){
            meal.setMealName((String) value);
		}
		if(col == NR_COL) {
//            meal.setName((String) value);
			String oldName = meal.getName();
			String newName = (String) value;
			if (newName != null && !oldName.trim().equals(newName.trim())) {
				try {
					meals.modifyName(oldName, newName);
				} catch (DuplicateNameException e) {
					ActionUtils.showErrorMessage(parent, ActionUtils.ARTICLE_DUPLICATE_NAME, newName.trim());
				} catch (EmptyNameException e) {
					ActionUtils.showErrorMessage(parent, ActionUtils.ARTICLE_NAME_EMPTY);
				}
			}
		}
		fireTableDataChanged();
	}

	@Override
	public Meal getObject(int row, int col) {
		return meals.getBusinessObjects().get(row);
	}
}