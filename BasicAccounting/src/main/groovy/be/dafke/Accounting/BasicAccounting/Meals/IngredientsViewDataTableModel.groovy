package be.dafke.Accounting.BasicAccounting.Meals

import java.awt.Component

class IngredientsViewDataTableModel extends IngredientsEditDataTableModel{

    IngredientsViewDataTableModel(Component parent) {
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

    // DE SET METHODEN
    // ===============
    @Override
    void setValueAt(Object value, int row, int col) {
        // not editable
    }
}