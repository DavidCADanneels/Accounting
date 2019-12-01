package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BusinessModel.Ingredient
import be.dafke.Accounting.BusinessModel.Recipe
import be.dafke.Accounting.BusinessModel.RecipeLine

import static java.util.ResourceBundle.getBundle

class MealIngredientsEditDataTableModel extends MealRecipeEditDataTableModel {
    Recipe recipe
    static int NAME_COL = 0
    static int OVERWRITE_ALLERGENES_COL = 1

    @Override
    int getColumnCount() {
        2
    }


    static int NR_OF_COL = 4

    void setColumnClasses() {
        columnClasses.put(NAME_COL, String.class)
        columnClasses.put(OVERWRITE_ALLERGENES_COL, String.class)
    }

    void setColumnNames() {
        columnNames.put(NAME_COL, getBundle("Accounting").getString("INGREDIENT_NAME"))
        columnNames.put(OVERWRITE_ALLERGENES_COL, getBundle("Accounting").getString("ALLERGENES"))
    }
    // DE GET METHODEN
// ===============
    Object getValueAt(int row, int col) {
        RecipeLine recipeLine = getObject(row, col)
        Ingredient ingredient = recipeLine.getIngredient()
        if(ingredient==null) return null
        if (col == NAME_COL) return ingredient.name
        if (col == OVERWRITE_ALLERGENES_COL) return ingredient.allergenesString
        null
    }

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