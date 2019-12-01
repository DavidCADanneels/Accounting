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
//	static int DESCRIPTION_COL = 4
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
//		columnClasses.put(DESCRIPTION_COL, String.class)
        columnClasses.put(SALES_PRICE_SINGLE_COL, BigDecimal.class)
        columnClasses.put(SALES_PRICE_TOTAL_COL, BigDecimal.class)
    }

    protected void setColumnNames() {
        columnNames.put(NR_COL, getBundle("Accounting").getString("NR"))
        columnNames.put(ID_COL, getBundle("Accounting").getString("MEAL_NR"))
        columnNames.put(NAME_COL, getBundle("Accounting").getString("MEAL_NAME"))
//		columnNames.put(DESCRIPTION_COL, getBundle("Accounting").getString("DESCRIPTION"))
        columnNames.put(SALES_PRICE_SINGLE_COL, getBundle("Accounting").getString("PRICE_ITEM"))
        columnNames.put(SALES_PRICE_TOTAL_COL, getBundle("Accounting").getString("TOTAL_VAT_INCL"))
    }


    // DE GET METHODEN
// ===============
    Object getValueAt(int row, int col) {
        if(mealOrder == null) null
        ArrayList<MealOrderItem> mealOrderItems = mealOrder.getBusinessObjects()
        if(mealOrderItems == null) null
        MealOrderItem mealOrderItem = getObject(row, col)

        if(mealOrderItem == null) null
        if (col == NR_COL) {
            mealOrderItem.getNumberOfItems()
        }
        Meal meal = mealOrderItem.getMeal()
        if(meal ==null) null
        if (col == ID_COL) {
            meal.getName()
        }
        if (col == NAME_COL) {
            meal.getMealName()
        }
//		if (col == DESCRIPTION_COL) {
//			meal.getDescription()
//		}
        if (col == SALES_PRICE_SINGLE_COL) {
            meal.getSalesPrice()
        }
        if (col == SALES_PRICE_TOTAL_COL) {
            BigDecimal salesPrice = meal.getSalesPrice()
            salesPrice.multiply(new BigDecimal(mealOrderItem.getNumberOfItems()))
        }
        null
    }

    @Override
    int getRowCount() {
        if(mealOrder == null) 0
        List<MealOrderItem> businessObjects = mealOrder.getBusinessObjects()
        if(businessObjects == null || businessObjects.size() == 0) 0
        businessObjects.size()
    }

    @Override
    int getColumnCount() {
        NR_OF_COL
    }

    @Override
    MealOrderItem getObject(int row, int col) {
        List<MealOrderItem> businessObjects = mealOrder.getBusinessObjects()
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