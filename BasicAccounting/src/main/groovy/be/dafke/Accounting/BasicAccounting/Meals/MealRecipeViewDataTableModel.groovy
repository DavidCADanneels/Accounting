package be.dafke.Accounting.BasicAccounting.Meals

class MealRecipeViewDataTableModel extends MealRecipeEditDataTableModel {

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