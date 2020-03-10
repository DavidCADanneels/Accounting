package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BusinessModel.Ingredient
import be.dafke.Accounting.BusinessModel.Recipe
import be.dafke.Accounting.BusinessModel.RecipeLine
import be.dafke.Accounting.BusinessModel.Unit
import be.dafke.ComponentModel.SelectableTableModel

import static java.util.ResourceBundle.getBundle 

class MealRecipeEditDataTableModel extends SelectableTableModel<RecipeLine> {
    Recipe recipe
    static int NAME_COL = 0
    static int AMOUNT_COL = 1
    static int UNIT_COL = 2
    static int ALLERGENES_COL = 3
    static int NR_OF_COL = 4
    HashMap<Integer,String> columnNames = new HashMap<>()
    HashMap<Integer,Class> columnClasses = new HashMap<>()
    List<Integer> editableColumns = new ArrayList<>()


    MealRecipeEditDataTableModel() {
        setColumnNames()
        setColumnClasses()
        setEditableColumns()
    }


    void setEditableColumns() {
        editableColumns.add(AMOUNT_COL)
        editableColumns.add(UNIT_COL)
    }

    void setColumnClasses() {
        columnClasses.put(NAME_COL, String.class)
        columnClasses.put(AMOUNT_COL, BigDecimal.class)
        columnClasses.put(UNIT_COL, Unit.class)
        columnClasses.put(ALLERGENES_COL, String.class)
    }

    void setColumnNames() {
        columnNames.put(NAME_COL, getBundle("Accounting").getString("INGREDIENT_NAME"))
        columnNames.put(AMOUNT_COL, getBundle("Accounting").getString("INGREDIENT_AMOUNT"))
        columnNames.put(UNIT_COL, getBundle("Accounting").getString("INGREDIENT_UNIT"))
        columnNames.put(ALLERGENES_COL, getBundle("Accounting").getString("ALLERGENES"))
    }
    // DE GET METHODEN
// ===============
    Object getValueAt(int row, int col) {
        RecipeLine recipeLine = getObject(row, col)
        if (col == AMOUNT_COL) return recipeLine.amount
        Ingredient ingredient = recipeLine.getIngredient()
        if(ingredient==null) return null
        if (col == NAME_COL) return ingredient.name
        if (col == UNIT_COL) return ingredient.unit
        if (col == ALLERGENES_COL) return ingredient.allergenesString
        null
    }

    @Override
    int getColumnCount() {
        NR_OF_COL
    }

    @Override
    int getRowCount() {
        recipe?recipe.businessObjects.size():0
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
        editableColumns.contains(col)
    }

    // DE SET METHODEN
    // ===============
    @Override
    void setValueAt(Object value, int row, int col) {
        // existing items not editable
        if (col == AMOUNT_COL) {
            BigDecimal amount = (BigDecimal) value
            RecipeLine recipeLine = getObject(row, col)
            recipeLine.setAmount(amount)
        }

    }

    void setRecipe(Recipe recipe) {
        this.recipe = recipe
    }

    @Override
    RecipeLine getObject(int row, int col) {
        recipe?.businessObjects?.get(row)
    }
}