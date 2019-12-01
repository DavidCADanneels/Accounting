package be.dafke.Accounting.BasicAccounting.Meals

class MealIngredientsViewDataTableModel extends MealIngredientsEditDataTableModel {
    @Override
    boolean isCellEditable(int row, int col) {
        false
    }

    // DE SET METHODEN
    // ===============
    @Override
    void setValueAt(Object value, int row, int col) {
        // Not editable
    }
}