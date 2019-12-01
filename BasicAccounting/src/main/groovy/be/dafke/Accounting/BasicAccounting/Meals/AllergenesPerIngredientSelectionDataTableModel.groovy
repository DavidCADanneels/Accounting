package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BusinessModel.Allergene
import be.dafke.Accounting.BusinessModel.Allergenes
import be.dafke.Accounting.BusinessModel.Ingredient
import be.dafke.ComponentModel.SelectableTableModel

import static java.util.ResourceBundle.getBundle 

class AllergenesPerIngredientSelectionDataTableModel extends SelectableTableModel<Allergene> {
    private Allergenes allergenes
    private Ingredient ingredient
    static int CHECKED_COL = 0
    static int NAME_COL = 1
    static int DESC_COL = 2
    static int NR_OF_COL = 3
    private HashMap<Integer,String> columnNames = new HashMap<>()
    private HashMap<Integer,Class> columnClasses = new HashMap<>()

    AllergenesPerIngredientSelectionDataTableModel(Ingredient ingredient, Allergenes allergenes){
        this.allergenes = allergenes
        this.ingredient = ingredient
        setColumnNames()
        setColumnClasses()
    }

    private void setColumnClasses() {
        columnClasses.put(CHECKED_COL, Boolean.class)
        columnClasses.put(NAME_COL, String.class)
        columnClasses.put(DESC_COL, String.class)
    }

    private void setColumnNames() {
        columnNames.put(CHECKED_COL, getBundle("Accounting").getString("ALLERGENES_CHECKED"))
        columnNames.put(NAME_COL, getBundle("Accounting").getString("ALLERGENES_NAME"))
        columnNames.put(DESC_COL, getBundle("Accounting").getString("ALLERGENES_DESC"))
    }
    // DE GET METHODEN
// ===============
    Object getValueAt(int row, int col) {
        Allergene allergene = getObject(row, col)
        if(allergene==null) null
        if (col == CHECKED_COL) {
            ingredient.getAllergenes().getBusinessObjects().contains(allergene)
        }
        if (col == NAME_COL) {
            allergene.getShortName()
        }
        if (col == DESC_COL) {
            allergene.getDescription()
        }
        null
    }

    int getColumnCount() {
        NR_OF_COL
    }

    int getRowCount() {
        if(allergenes == null){
            0
        }
        allergenes.getBusinessObjects().size()
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
        allergenes.getBusinessObjects().get(row)
    }
}