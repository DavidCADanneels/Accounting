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

class IngredientsDataEditTableModel extends SelectableTableModel<Ingredient> {
    private Ingredients ingredients
    static int NAME_COL = 0
    static int UNIT_COL = 1
    static int ALLERGENES_COL = 2
    static int NR_OF_COL = 3
    private final Component parent
    private HashMap<Integer,String> columnNames = new HashMap<>()
    private HashMap<Integer,Class> columnClasses = new HashMap<>()
    List<Integer> editableColumns = new ArrayList<>()


    IngredientsDataEditTableModel(Component parent) {
        this.parent = parent
        setColumnNames()
        setColumnClasses()
        setEditableColumns()
    }


    void setEditableColumns() {
        editableColumns.add(NAME_COL)
        editableColumns.add(UNIT_COL)
    }

    private void setColumnClasses() {
        columnClasses.put(NAME_COL, String.class)
        columnClasses.put(UNIT_COL, Unit.class)
        columnClasses.put(ALLERGENES_COL, String.class)
    }

    private void setColumnNames() {
        columnNames.put(NAME_COL, getBundle("Accounting").getString("INGREDIENT_NAME"))
        columnNames.put(UNIT_COL, getBundle("Accounting").getString("INGREDIENT_UNIT"))
        columnNames.put(ALLERGENES_COL, getBundle("Accounting").getString("ALLERGENES"))
    }
    // DE GET METHODEN
// ===============
    Object getValueAt(int row, int col) {
        Ingredient ingredient = getObject(row, col)
        if(ingredient==null) null
        if (col == NAME_COL) {
            ingredient.getName()
        }
        if (col == UNIT_COL) {
            ingredient.getUnit()
        }
        if (col == ALLERGENES_COL) {
            ingredient.getAllergenesString()
        }
        null
    }

    int getColumnCount() {
        NR_OF_COL
    }

    int getRowCount() {
        if(ingredients == null){
            0
        }
        ingredients.getBusinessObjects().size()
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
//            article.setName((String) value)
            String oldName = ingredient.getName()
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
        ingredients.getBusinessObjects().get(row)
    }
}