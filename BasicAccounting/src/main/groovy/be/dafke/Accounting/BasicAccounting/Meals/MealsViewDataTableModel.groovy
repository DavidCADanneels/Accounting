package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BusinessModel.Accounting

import java.awt.Component

class MealsViewDataTableModel extends MealsEditDataTableModel {
    MealsViewDataTableModel(Component parent, Accounting accounting) {
        super(parent, accounting)
    }

    @Override
    boolean isCellEditable(int row, int col) {
        return false
    }

    // DE SET METHODEN
    // ===============
    @Override
    void setValueAt(Object value, int row, int col) {
        // Not editable
    }
}