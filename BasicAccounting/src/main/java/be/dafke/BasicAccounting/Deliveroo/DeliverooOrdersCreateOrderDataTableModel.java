package be.dafke.BasicAccounting.Deliveroo;

import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTableModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class DeliverooOrdersCreateOrderDataTableModel extends SelectableTableModel<MealOrderItem> {
	public static int NR_COL = 0;
	public static int ID_COL = 1;
	public static int NAME_COL = 2;
	public static int SALES_PRICE_SINGLE_COL = 3;
	public static int SALES_PRICE_TOTAL_COL = 4;
//	public static int DESCRIPTION_COL = 4;
	public static int NR_OF_COL = 5;
	protected HashMap<Integer,String> columnNames = new HashMap<>();
	protected HashMap<Integer,Class> columnClasses = new HashMap<>();
	private MealOrder mealOrder;

//	MealOrders mealOrders;
	DeliverooMeals deliverooMeals;

	public DeliverooOrdersCreateOrderDataTableModel(DeliverooMeals deliverooMeals, MealOrder mealOrder) {
//		this.mealOrders = mealOrders;
		this.deliverooMeals = deliverooMeals;
		this.mealOrder = mealOrder;
		setColumnNames();
		setColumnClasses();
	}

	protected void setColumnClasses() {
		columnClasses.put(NR_COL, Integer.class);
		columnClasses.put(ID_COL, String.class);
		columnClasses.put(NAME_COL, String.class);
//		columnClasses.put(DESCRIPTION_COL, String.class);
		columnClasses.put(SALES_PRICE_SINGLE_COL, BigDecimal.class);
		columnClasses.put(SALES_PRICE_TOTAL_COL, BigDecimal.class);
	}

	protected void setColumnNames() {
		columnNames.put(NR_COL, getBundle("Accounting").getString("NR"));
		columnNames.put(ID_COL, getBundle("Accounting").getString("MEAL_NR"));
		columnNames.put(NAME_COL, getBundle("Accounting").getString("MEAL_NAME"));
//		columnNames.put(DESCRIPTION_COL, getBundle("Accounting").getString("DESCRIPTION"));
		columnNames.put(SALES_PRICE_SINGLE_COL, getBundle("Accounting").getString("PRICE_ITEM"));
		columnNames.put(SALES_PRICE_TOTAL_COL, getBundle("Accounting").getString("TOTAL_VAT_INCL"));
	}


	// DE GET METHODEN
// ===============
	public Object getValueAt(int row, int col) {
		ArrayList<MealOrderItem> mealOrderItems = mealOrder.getBusinessObjects();
		if(mealOrderItems == null) return null;
		MealOrderItem mealOrderItem = getObject(row, col);

		if (col == NR_COL) {
			return mealOrderItem.getNumberOfItems();
		}
		DeliverooMeal deliverooMeal = mealOrderItem.getDeliverooMeal();
		if(deliverooMeal==null) return null;
		if (col == ID_COL) {
			return deliverooMeal.getName();
		}
		if (col == NAME_COL) {
			return deliverooMeal.getMealName();
		}
//		if (col == DESCRIPTION_COL) {
//			return deliverooMeal.getDescription();
//		}
		if (col == SALES_PRICE_SINGLE_COL) {
			return deliverooMeal.getSalesPrice();
		}
		if (col == SALES_PRICE_TOTAL_COL) {
			BigDecimal salesPrice = deliverooMeal.getSalesPrice();
			return salesPrice.multiply(new BigDecimal(mealOrderItem.getNumberOfItems()));
		}
		return null;
	}

	@Override
	public void setValueAt(Object object, int row, int col){
		if(col == NR_COL){
			Integer numberOfItems = (Integer) object;
			MealOrderItem mealOrderItem = getObject(row, col);
			mealOrderItem.setNumberOfItems(numberOfItems);
			mealOrder.setOrderItem(mealOrderItem);
			DeliverooOrderCreateGUI.calculateTotalsForAll();
		}
	}

	@Override
	public int getRowCount() {
		if(deliverooMeals == null) return 0;
		List<DeliverooMeal> businessObjects = deliverooMeals.getBusinessObjects();
		if(businessObjects == null || businessObjects.size() == 0) return 0;
		return businessObjects.size();
	}

	@Override
	public int getColumnCount() {
		return NR_OF_COL;
	}

	@Override
	public MealOrderItem getObject(int row, int col) {
		List<DeliverooMeal> businessObjects = deliverooMeals.getBusinessObjects();
		if(businessObjects == null || businessObjects.size() == 0) return null;
		DeliverooMeal deliverooMeal = businessObjects.get(row);
		MealOrderItem orderItem = mealOrder.getBusinessObject(deliverooMeal.getName());
		return orderItem;
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
		return col==NR_COL;
	}

	public void setMealOrder(MealOrder mealOrder) {
		this.mealOrder = mealOrder;
		fireTableDataChanged();
	}
}