package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BusinessModel.IngredientOrder
import be.dafke.Accounting.BusinessModelDao.Session
import be.dafke.ComponentModel.SelectableTableModel

import static java.util.ResourceBundle.getBundle 

class IngredientOrdersOverviewDataTableModel  extends SelectableTableModel<IngredientOrder> {
    static int ORDER_NR_COL = 0
    static int DATE_COL = 1
    static int NR_OF_COL = 2
    protected HashMap<Integer,String> columnNames = new HashMap<>()
    protected HashMap<Integer,Class> columnClasses = new HashMap<>()

    IngredientOrdersOverviewDataTableModel() {
        setColumnNames()
        setColumnClasses()
    }

    protected void setColumnClasses() {
        columnClasses.put(ORDER_NR_COL, String.class)
        columnClasses.put(DATE_COL, String.class)
    }

    protected void setColumnNames() {
        columnNames.put(ORDER_NR_COL, getBundle("Accounting").getString("ORDER_NR"))
        columnNames.put(DATE_COL, getBundle("Accounting").getString("TRANSFER_DATE"))
    }

    int getColumnCount() {
        NR_OF_COL
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

    // DE SET METHODEN
// ===============
    @Override
    void setValueAt(Object value, int row, int col) {
        // No editable cells !
    }

    // DE GET METHODEN
// ===============
    Object getValueAt(int row, int col) {
        if (Session.activeAccounting.ingredientOrders == null) return null
        IngredientOrder ingredientOrder = getObject(row, col)
        if(ingredientOrder == null) return null
        if (col == ORDER_NR_COL) return ingredientOrder.name
        if (col == DATE_COL) return ""
        null
    }

    @Override
    int getRowCount() {
        Session.activeAccounting.ingredientOrders?.businessObjects?.size()?:0
    }

    @Override
    IngredientOrder getObject(int row, int col) {
        List<IngredientOrder> businessObjects = Session.activeAccounting.ingredientOrders.businessObjects
        if(businessObjects == null || businessObjects.size() == 0) null
        businessObjects.get(row)
    }
}
