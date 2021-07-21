package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BusinessModel.Meal
import be.dafke.Accounting.BusinessModel.MealOrder
import be.dafke.Accounting.BusinessModel.MealOrderItem
import be.dafke.ComponentModel.SelectableTableModel

import static java.util.ResourceBundle.getBundle 

class MealOrderViewDataTableModel extends SelectableTableModel<MealOrderItem> {
    static int NR_COL = 0
    static int ID_COL = 1
    static int NAME_COL = 2
    static int SALES_PRICE_SINGLE_COL = 3
    static int SALES_PRICE_TOTAL_COL = 4
    static int NR_OF_COL = 5
    protected HashMap<Integer,String> columnNames = new HashMap<>()
    protected HashMap<Integer,Class> columnClasses = new HashMap<>()
    protected MealOrder mealOrder

    MealOrderViewDataTableModel() {
        setColumnNames()
        setColumnClasses()
    }

    protected void setColumnClasses() {
        columnClasses.put(NR_COL, Integer.class)
        columnClasses.put(ID_COL, String.class)
        columnClasses.put(NAME_COL, String.class)
        columnClasses.put(SALES_PRICE_SINGLE_COL, BigDecimal.class)
        columnClasses.put(SALES_PRICE_TOTAL_COL, BigDecimal.class)
    }

    protected void setColumnNames() {
        columnNames.put(NR_COL, getBundle("Accounting").getString("NR"))
        columnNames.put(ID_COL, getBundle("Accounting").getString("MEAL_NR"))
        columnNames.put(NAME_COL, getBundle("Accounting").getString("MEAL_NAME"))
        columnNames.put(SALES_PRICE_SINGLE_COL, getBundle("Accounting").getString("PRICE_ITEM"))
        columnNames.put(SALES_PRICE_TOTAL_COL, getBundle("Accounting").getString("TOTAL_VAT_INCL"))
    }


    // DE GET METHODEN
// ===============
    Object getValueAt(int row, int col) {
        if(mealOrder == null) return null
        ArrayList<MealOrderItem> mealOrderItems = mealOrder.businessObjects
        if(mealOrderItems == null) return null
        MealOrderItem mealOrderItem = getObject(row, col)
        if(mealOrderItem == null) return null
        if (col == NR_COL) {
            return mealOrderItem.numberOfItems
        }
        Meal meal = mealOrderItem.getMeal()
        if(meal ==null) return null
        if (col == ID_COL) return meal.name
        if (col == NAME_COL) return meal.getMealName()
        if (col == SALES_PRICE_SINGLE_COL) return meal.getSalesPrice()
        if (col == SALES_PRICE_TOTAL_COL) {
            BigDecimal salesPrice = meal.getSalesPrice()
            return salesPrice?salesPrice.multiply(new BigDecimal(mealOrderItem.numberOfItems)):null
        }
        null
    }

    @Override
    int getRowCount() {
        mealOrder?mealOrder.businessObjects.size():0
    }

    @Override
    int getColumnCount() {
        NR_OF_COL
    }

    @Override
    MealOrderItem getObject(int row, int col) {
        List<MealOrderItem> businessObjects = mealOrder.businessObjects
        if(businessObjects == null || businessObjects.size() == 0) null
        businessObjects.get(row)
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
        false
    }

    void setMealOrder(MealOrder mealOrder) {
        this.mealOrder = mealOrder
        fireTableDataChanged()
    }
}