package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BasicAccounting.MainApplication.ActionUtils
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Meal
import be.dafke.Accounting.BusinessModel.MealOrders
import be.dafke.Accounting.BusinessModel.Meals
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.ComponentModel.SelectableTableModel

import java.awt.*
import java.util.List

import static java.util.ResourceBundle.getBundle 

class MealsEditDataTableModel extends SelectableTableModel<Meal> {
    final MealOrders mealOrders
    final Meals meals
    static int NR_COL = 0
    static int NAME_COL = 1
    static int SALES_PRICE_COL = 2
    static int DESCRIPTION_COL = 3
    static int USAGE_COL = 4
    static int NR_OF_COL = 5
    final Component parent
    HashMap<Integer,String> columnNames = new HashMap<>()
    HashMap<Integer,Class> columnClasses = new HashMap<>()
    List<Integer> editableColumns = new ArrayList<>()

    MealsEditDataTableModel(Component parent, Accounting accounting) {
        meals = accounting.meals
        mealOrders = accounting.mealOrders
        this.parent = parent
        setColumnNames()
        setColumnClasses()
        setEditableColumns()
    }

    void setEditableColumns() {
        editableColumns.add(NR_COL)
        editableColumns.add(NAME_COL)
        editableColumns.add(DESCRIPTION_COL)
        editableColumns.add(SALES_PRICE_COL)
    }

    void setColumnClasses() {
        columnClasses.put(NR_COL, String.class)
        columnClasses.put(USAGE_COL, Integer.class)
        columnClasses.put(NAME_COL, String.class)
        columnClasses.put(DESCRIPTION_COL, String.class)
        columnClasses.put(SALES_PRICE_COL, BigDecimal.class)
    }

    void setColumnNames() {
        columnNames.put(NR_COL, getBundle("Accounting").getString("MEAL_NR"))
        columnNames.put(USAGE_COL, getBundle("Accounting").getString("USAGE"))
        columnNames.put(NAME_COL, getBundle("Accounting").getString("MEAL_NAME"))
        columnNames.put(DESCRIPTION_COL, getBundle("Accounting").getString("DESCRIPTION"))
        columnNames.put(SALES_PRICE_COL, getBundle("Accounting").getString("PRICE"))
    }
    // DE GET METHODEN
// ===============
    Object getValueAt(int row, int col) {
        Meal meal = getObject(row, col)
        if(meal==null) return null
        if (col == NR_COL) return meal.name
        if (col == USAGE_COL) return mealOrders.nrOfMeals(meal)
        if (col == NAME_COL) return meal.getMealName()
        if (col == DESCRIPTION_COL) return meal.description
        if (col == SALES_PRICE_COL) return meal.getSalesPrice()
        null
    }

    int getColumnCount() {
        NR_OF_COL
    }

    int getRowCount() {
        meals?meals.businessObjects.size():0
    }

    @Override
    String getColumnName(int col) {
        columnNames.get(col)
    }

    @Override
    Class getColumnClass(int col) {
        columnClasses.get(col)
    }

    @Override
    boolean isCellEditable(int row, int col) {
        editableColumns.contains(col)
    }

// DE SET METHODEN
// ===============
    @Override
    void setValueAt(Object value, int row, int col) {
        Meal meal = getObject(row,col)
        if(col == SALES_PRICE_COL){
            BigDecimal purchasePrice = (BigDecimal) value
            meal.setSalesPrice(purchasePrice.setScale(2))
        }
        if(col == DESCRIPTION_COL){
            meal.setDescription((String) value)
        }
        if(col == NAME_COL){
            meal.setMealName((String) value)
        }
        if(col == NR_COL) {
//            meal.setName((String) value)
            String oldName = meal.name
            String newName = (String) value
            if (newName != null && !oldName.trim().equals(newName.trim())) {
                try {
                    meals.modifyName(oldName, newName)
                } catch (DuplicateNameException e) {
                    ActionUtils.showErrorMessage(parent, ActionUtils.ARTICLE_DUPLICATE_NAME, newName.trim())
                } catch (EmptyNameException e) {
                    ActionUtils.showErrorMessage(parent, ActionUtils.ARTICLE_NAME_EMPTY)
                }
            }
        }
        fireTableDataChanged()
    }

    @Override
    Meal getObject(int row, int col) {
        meals.businessObjects.get(row)
    }
}