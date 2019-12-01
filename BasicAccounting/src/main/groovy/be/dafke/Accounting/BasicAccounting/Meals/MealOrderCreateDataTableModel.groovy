package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Meal
import be.dafke.Accounting.BusinessModel.MealOrderItem
import be.dafke.Accounting.BusinessModel.Meals

class MealOrderCreateDataTableModel extends MealOrderViewDataTableModel {
    final Meals meals
    final Accounting accounting

    MealOrderCreateDataTableModel(Accounting accounting) {
        super()
        this.accounting = accounting
        meals = accounting.meals
    }

    @Override
    void setValueAt(Object object, int row, int col){
        if(col == NR_COL){
            Integer numberOfItems = (Integer) object
            MealOrderItem mealOrderItem = getObject(row, col)
            mealOrderItem.setNumberOfItems(numberOfItems)
            mealOrder.setOrderItem(mealOrderItem)
            MealOrderCreateGUI.calculateTotalsForAll(accounting)
        }
    }

    @Override
    int getRowCount() {
        meals?meals.businessObjects.size():0
    }

    @Override
    MealOrderItem getObject(int row, int col) {
        List<Meal> businessObjects = meals.businessObjects
        if(businessObjects == null || businessObjects.size() == 0) return null
        Meal meal = businessObjects.get(row)
        MealOrderItem orderItem = mealOrder.getBusinessObject(meal.name)
        orderItem
    }

    @Override
    boolean isCellEditable(int row, int col) {
        col==NR_COL
    }

}