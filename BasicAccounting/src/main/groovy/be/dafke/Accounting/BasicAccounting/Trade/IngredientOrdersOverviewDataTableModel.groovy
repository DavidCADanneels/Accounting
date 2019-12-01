package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.IngredientOrder
import be.dafke.Accounting.BusinessModel.IngredientOrders
import be.dafke.ComponentModel.SelectableTableModel

import static java.util.ResourceBundle.getBundle 

class IngredientOrdersOverviewDataTableModel  extends SelectableTableModel<IngredientOrder> {
    static int ORDER_NR_COL = 0
    static int DATE_COL = 1
    static int NR_OF_COL = 2
    protected HashMap<Integer,String> columnNames = new HashMap<>()
    protected HashMap<Integer,Class> columnClasses = new HashMap<>()

    private IngredientOrders ingredientOrders

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
        if (ingredientOrders == null) null
        IngredientOrder ingredientOrder = getObject(row, col)
        if(ingredientOrder == null) null

        if (col == ORDER_NR_COL) {
            ingredientOrder.getName()
        }
        if (col == DATE_COL) {
            ""
        }
        null
    }

    @Override
    int getRowCount() {
        if(ingredientOrders == null) 0
        List<IngredientOrder> businessObjects = ingredientOrders.getBusinessObjects()
        if(businessObjects == null || businessObjects.size() == 0) 0
        businessObjects.size()
    }

    @Override
    IngredientOrder getObject(int row, int col) {
        List<IngredientOrder> businessObjects = ingredientOrders.getBusinessObjects()
        if(businessObjects == null || businessObjects.size() == 0) null
        businessObjects.get(row)
    }

    void setAccounting(Accounting accounting) {
        setIngredientsOrders(accounting == null ? null : accounting.getIngredientOrders())
    }

    void setIngredientsOrders(IngredientOrders ingredientsOrders){
        this.ingredientOrders=ingredientsOrders
        fireTableDataChanged()
    }
}
