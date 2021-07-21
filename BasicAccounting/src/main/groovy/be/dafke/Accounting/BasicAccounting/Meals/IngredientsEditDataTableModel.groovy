package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BasicAccounting.MainApplication.ActionUtils
import be.dafke.Accounting.BusinessModel.Ingredient
import be.dafke.Accounting.BusinessModel.Ingredients
import be.dafke.Accounting.BusinessModel.Unit
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.ComponentModel.SelectableTableModel

import java.awt.*
import java.util.List

import static java.util.ResourceBundle.getBundle 

class IngredientsEditDataTableModel extends SelectableTableModel<Ingredient> {
    Ingredients ingredients
    static int NAME_COL = 0
    static int ALLERGENES_COL = 1
    static int UNIT_COL = 2
    static int NR_OF_COL = 3
    final Component parent
    HashMap<Integer,String> columnNames = new HashMap<>()
    HashMap<Integer,Class> columnClasses = new HashMap<>()
    List<Integer> editableColumns = new ArrayList<>()


    IngredientsEditDataTableModel(Component parent) {
        this.parent = parent
        setColumnNames()
        setColumnClasses()
        setEditableColumns()
    }


    void setEditableColumns() {
        editableColumns.add(NAME_COL)
        editableColumns.add(UNIT_COL)
    }

    void setColumnClasses() {
        columnClasses.put(NAME_COL, String.class)
        columnClasses.put(UNIT_COL, Unit.class)
        columnClasses.put(ALLERGENES_COL, String.class)
    }

    void setColumnNames() {
        columnNames.put(NAME_COL, getBundle("Accounting").getString("INGREDIENT_NAME"))
        columnNames.put(UNIT_COL, getBundle("Accounting").getString("INGREDIENT_UNIT"))
        columnNames.put(ALLERGENES_COL, getBundle("Accounting").getString("ALLERGENES"))
    }
    // DE GET METHODEN
// ===============
    Object getValueAt(int row, int col) {
        Ingredient ingredient = getObject(row, col)
        if(ingredient==null) return null
        if (col == NAME_COL) return ingredient.name
        if (col == UNIT_COL) return ingredient.unit
        if (col == ALLERGENES_COL) return ingredient.allergenesString
        null
    }

    int getColumnCount() {
        NR_OF_COL
    }

    int getRowCount() {
        ingredients?ingredients.businessObjects.size():0
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
        Ingredient ingredient = getObject(row,col)
        if(col == UNIT_COL){
            ingredient.setUnit((Unit) value)
        }
        if(col == NAME_COL) {
            String oldName = ingredient.name
            String newName = (String) value
            if (newName != null && !oldName.trim().equals(newName.trim())) {
                try {
                    ingredients.modifyName(oldName, newName)
                } catch (DuplicateNameException e) {
                    ActionUtils.showErrorMessage(parent, ActionUtils.INGREDIENT_DUPLICATE_NAME, newName.trim())
                } catch (EmptyNameException e) {
                    ActionUtils.showErrorMessage(parent, ActionUtils.INGREDIENT_NAME_EMPTY)
                }
            }
        }
        fireTableDataChanged()
    }

    void setIngredients(Ingredients ingredients) {
        this.ingredients = ingredients
    }

    @Override
    Ingredient getObject(int row, int col) {
        ingredients.businessObjects.get(row)
    }
}