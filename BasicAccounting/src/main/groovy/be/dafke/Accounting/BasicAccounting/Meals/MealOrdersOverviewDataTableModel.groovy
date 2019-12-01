package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BusinessModel.MealOrder
import be.dafke.Accounting.BusinessModel.MealOrders
import be.dafke.ComponentModel.SelectableTableModel
import be.dafke.Utils.Utils

import static java.util.ResourceBundle.getBundle 

class MealOrdersOverviewDataTableModel extends SelectableTableModel<MealOrder> {
    static int ID_COL = 0
    static int DATE_COL = 1
    static int DESCRIPTION_COL = 2
    static int PRICE_TOTAL_COL = 3
    static int NR_OF_COL = 4
    protected HashMap<Integer,String> columnNames = new HashMap<>()
    protected HashMap<Integer,Class> columnClasses = new HashMap<>()

    MealOrders mealOrders

    MealOrdersOverviewDataTableModel(MealOrders mealOrders) {
        this.mealOrders = mealOrders
        setColumnNames()
        setColumnClasses()
    }

    void setMealOrders(MealOrders mealOrders) {
        this.mealOrders = mealOrders
    }

    protected void setColumnClasses() {
        columnClasses.put(ID_COL, String.class)
        columnClasses.put(DATE_COL, String.class)
        columnClasses.put(DESCRIPTION_COL, String.class)
        columnClasses.put(PRICE_TOTAL_COL, BigDecimal.class)
    }

    protected void setColumnNames() {
        columnNames.put(ID_COL, getBundle("Accounting").getString("NR"))
        columnNames.put(DATE_COL, getBundle("Accounting").getString("DATE"))
        columnNames.put(DESCRIPTION_COL, getBundle("Accounting").getString("DESCRIPTION"))
        columnNames.put(PRICE_TOTAL_COL, getBundle("Accounting").getString("PRICE"))
    }


    // DE GET METHODEN
// ===============
    Object getValueAt(int row, int col) {
        if (mealOrders == null) return null
        MealOrder mealOrder = getObject(row, col)
        if(mealOrder == null) return null
        if (col == ID_COL) return mealOrder.name
        if (col == DATE_COL) {
            Calendar date = mealOrder.date
            return date?Utils.toString(date):null
        }
        if (col == DESCRIPTION_COL) return mealOrder.description
        if (col == PRICE_TOTAL_COL) return mealOrder.getTotalPrice()
        null
    }

    @Override
    int getRowCount() {
        mealOrders?mealOrders.businessObjects.size():0
    }

    @Override
    int getColumnCount() {
        NR_OF_COL
    }

    @Override
    MealOrder getObject(int row, int col) {
        List<MealOrder> businessObjects = this.mealOrders.businessObjects
        if(businessObjects == null || businessObjects.size() == 0) null
        businessObjects.get(row)
    }

    int getRow(MealOrder mealOrder){
        ArrayList<MealOrder> businessObjects = mealOrders.businessObjects
        businessObjects.indexOf(mealOrder)
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
}