package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BusinessModel.Allergene
import be.dafke.Accounting.BusinessModel.Allergenes
import be.dafke.Accounting.BusinessModel.Ingredient
import be.dafke.ComponentModel.SelectableTableModel

import static java.util.ResourceBundle.getBundle 

class AllergenesPerIngredientSelectionDataTableModel extends SelectableTableModel<Allergene> {
    Allergenes allergenes
    Ingredient ingredient
    static int CHECKED_COL = 0
    static int NAME_COL = 1
    static int DESC_COL = 2
    static int NR_OF_COL = 3
    HashMap<Integer,String> columnNames = new HashMap<>()
    HashMap<Integer,Class> columnClasses = new HashMap<>()

    AllergenesPerIngredientSelectionDataTableModel(Ingredient ingredient, Allergenes allergenes){
        this.allergenes = allergenes
        this.ingredient = ingredient
        setColumnNames()
        setColumnClasses()
    }

    void setColumnClasses() {
        columnClasses.put(CHECKED_COL, Boolean.class)
        columnClasses.put(NAME_COL, String.class)
        columnClasses.put(DESC_COL, String.class)
    }

    void setColumnNames() {
        columnNames.put(CHECKED_COL, getBundle("Accounting").getString("ALLERGENES_CHECKED"))
        columnNames.put(NAME_COL, getBundle("Accounting").getString("ALLERGENES_NAME"))
        columnNames.put(DESC_COL, getBundle("Accounting").getString("ALLERGENES_DESC"))
    }
    // DE GET METHODEN
// ===============
    Object getValueAt(int row, int col) {
        Allergene allergene = getObject(row, col)
        if(allergene==null) return null
        if (col == CHECKED_COL) return ingredient.allergenes.businessObjects.contains(allergene)
        if (col == NAME_COL) return allergene.shortName
        if (col == DESC_COL) return allergene.description
        null
    }

    int getColumnCount() {
        NR_OF_COL
    }

    int getRowCount() {
        allergenes?allergenes.businessObjects.size():0
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
        col == CHECKED_COL
    }


    // DE SET METHODEN
    // ===============
    @Override
    void setValueAt(Object value, int row, int col) {
        Allergene allergene = getObject(row,col)
        if(col == CHECKED_COL){
            Boolean checked = (Boolean) value
            if(checked){
                ingredient.addAllergene(allergene)
            } else {
                ingredient.removeAllergene(allergene)
            }
        }
    }

    @Override
    Allergene getObject(int row, int col) {
        allergenes.businessObjects.get(row)
    }
}