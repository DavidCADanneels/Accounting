package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BusinessModel.Ingredient

import java.awt.Component

class IngredientsDataViewTableModel extends IngredientsDataEditTableModel{

    static int OVERWRITE_NAME_COL = 0
    static int OVERWRITE_ALLERGENES_COL = 1

    IngredientsDataViewTableModel(Component parent) {
        super(parent)
    }

    @Override
    int getColumnCount() {
        2
    }

    @Override
    boolean isCellEditable(int row, int col) {
        false
    }

    @Override
    Object getValueAt(int row, int col) {
        Ingredient ingredient = getObject(row, col)
        if(ingredient==null) return null
        if (col == OVERWRITE_NAME_COL) return ingredient.name
        if (col == OVERWRITE_ALLERGENES_COL) return ingredient.allergenesString
        null
    }

    // DE SET METHODEN
    // ===============
    @Override
    void setValueAt(Object value, int row, int col) {
        // not editable
    }
}