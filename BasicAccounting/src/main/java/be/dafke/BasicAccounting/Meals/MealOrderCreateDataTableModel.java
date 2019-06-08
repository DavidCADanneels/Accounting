package be.dafke.BasicAccounting.Meals;

import be.dafke.BusinessModel.*;

import java.util.List;

/**
 * @author David Danneels
 */

public class MealOrderCreateDataTableModel extends MealOrderViewDataTableModel {
	private final Meals meals;
	private final Accounting accounting;

	public MealOrderCreateDataTableModel(Accounting accounting) {
		super();
		this.accounting = accounting;
		meals = accounting.getMeals();
	}

	@Override
	public void setValueAt(Object object, int row, int col){
		if(col == NR_COL){
			Integer numberOfItems = (Integer) object;
			MealOrderItem mealOrderItem = getObject(row, col);
			mealOrderItem.setNumberOfItems(numberOfItems);
			mealOrder.setOrderItem(mealOrderItem);
			MealOrderCreateGUI.calculateTotalsForAll(accounting);
		}
	}

	@Override
	public int getRowCount() {
		if(meals == null) return 0;
		List<Meal> businessObjects = meals.getBusinessObjects();
		if(businessObjects == null || businessObjects.size() == 0) return 0;
		return businessObjects.size();
	}

	@Override
	public MealOrderItem getObject(int row, int col) {
		List<Meal> businessObjects = meals.getBusinessObjects();
		if(businessObjects == null || businessObjects.size() == 0) return null;
		Meal meal = businessObjects.get(row);
		MealOrderItem orderItem = mealOrder.getBusinessObject(meal.getName());
		return orderItem;
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return col==NR_COL;
	}

}