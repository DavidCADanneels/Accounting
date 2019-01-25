package be.dafke.BasicAccounting.Deliveroo;

import be.dafke.BusinessModel.*;

import java.util.List;

/**
 * @author David Danneels
 */

public class DeliverooOrderCreateDataTableModel extends DeliverooOrderViewDataTableModel {
	private final DeliverooMeals deliverooMeals;
	private final Accounting accounting;

	public DeliverooOrderCreateDataTableModel(Accounting accounting) {
		super();
		this.accounting = accounting;
		deliverooMeals = accounting.getDeliverooMeals();
	}

	@Override
	public void setValueAt(Object object, int row, int col){
		if(col == NR_COL){
			Integer numberOfItems = (Integer) object;
			MealOrderItem mealOrderItem = getObject(row, col);
			mealOrderItem.setNumberOfItems(numberOfItems);
			mealOrder.setOrderItem(mealOrderItem);
			DeliverooOrderCreateGUI.calculateTotalsForAll(accounting);
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
	public MealOrderItem getObject(int row, int col) {
		List<DeliverooMeal> businessObjects = deliverooMeals.getBusinessObjects();
		if(businessObjects == null || businessObjects.size() == 0) return null;
		DeliverooMeal deliverooMeal = businessObjects.get(row);
		MealOrderItem orderItem = mealOrder.getBusinessObject(deliverooMeal.getName());
		return orderItem;
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return col==NR_COL;
	}

}