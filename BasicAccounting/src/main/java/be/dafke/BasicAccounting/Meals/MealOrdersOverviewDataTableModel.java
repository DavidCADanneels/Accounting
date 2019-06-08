package be.dafke.BasicAccounting.Meals;

import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTableModel;
import be.dafke.Utils.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class MealOrdersOverviewDataTableModel extends SelectableTableModel<MealOrder> {
	public static int ID_COL = 0;
	public static int DATE_COL = 1;
	public static int DESCRIPTION_COL = 2;
	public static int PRICE_TOTAL_COL = 3;
	public static int NR_OF_COL = 4;
	protected HashMap<Integer,String> columnNames = new HashMap<>();
	protected HashMap<Integer,Class> columnClasses = new HashMap<>();

	MealOrders mealOrders;

	public MealOrdersOverviewDataTableModel(MealOrders mealOrders) {
		this.mealOrders = mealOrders;
		setColumnNames();
		setColumnClasses();
	}

	public void setMealOrders(MealOrders mealOrders) {
		this.mealOrders = mealOrders;
	}

	protected void setColumnClasses() {
		columnClasses.put(ID_COL, String.class);
		columnClasses.put(DATE_COL, String.class);
		columnClasses.put(DESCRIPTION_COL, String.class);
		columnClasses.put(PRICE_TOTAL_COL, BigDecimal.class);
	}

	protected void setColumnNames() {
		columnNames.put(ID_COL, getBundle("Accounting").getString("NR"));
		columnNames.put(DATE_COL, getBundle("Accounting").getString("DATE"));
		columnNames.put(DESCRIPTION_COL, getBundle("Accounting").getString("DESCRIPTION"));
		columnNames.put(PRICE_TOTAL_COL, getBundle("Accounting").getString("PRICE"));
	}


	// DE GET METHODEN
// ===============
	public Object getValueAt(int row, int col) {
		if (mealOrders == null) return null;
		MealOrder mealOrder = getObject(row, col);
		if(mealOrder == null) return null;

		if (col == ID_COL) {
			return mealOrder.getName();
		}
		if (col == DATE_COL) {
			Calendar date = mealOrder.getDate();
			return Utils.toString(date);
		}
		if (col == DESCRIPTION_COL) {
			return mealOrder.getDescription();
		}
		if (col == PRICE_TOTAL_COL) {
			return mealOrder.getTotalPrice();
		}
		return null;
	}

	@Override
	public int getRowCount() {
		if(mealOrders == null) return 0;
		List<MealOrder> businessObjects = mealOrders.getBusinessObjects();
		if(businessObjects == null || businessObjects.size() == 0) return 0;
		return businessObjects.size();
	}

	@Override
	public int getColumnCount() {
		return NR_OF_COL;
	}

	@Override
	public MealOrder getObject(int row, int col) {
		List<MealOrder> businessObjects = this.mealOrders.getBusinessObjects();
		if(businessObjects == null || businessObjects.size() == 0) return null;
		return businessObjects.get(row);
	}

	public int getRow(MealOrder mealOrder){
		ArrayList<MealOrder> businessObjects = mealOrders.getBusinessObjects();
		return businessObjects.indexOf(mealOrder);
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
		return false;
	}
}