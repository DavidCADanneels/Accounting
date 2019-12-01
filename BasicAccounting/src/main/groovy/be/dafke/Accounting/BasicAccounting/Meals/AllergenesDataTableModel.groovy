package be.dafke.Accounting.BasicAccounting.Meals

import be.dafke.Accounting.BusinessModel.Allergene
import be.dafke.Accounting.BusinessModel.Allergenes
import be.dafke.ComponentModel.SelectableTableModel

import static java.util.ResourceBundle.getBundle 

class AllergenesDataTableModel extends SelectableTableModel<Allergene> {
    Allergenes allergenes
    static int ID_COL = 0
    static int NAME_COL = 1
    static int DESC_COL = 2
    static int NR_OF_COL = 3
    HashMap<Integer,String> columnNames = new HashMap<>()
    HashMap<Integer,Class> columnClasses = new HashMap<>()
    List<Integer> editableColumns = new ArrayList<>()

    AllergenesDataTableModel() {
        setColumnNames()
        setColumnClasses()
        setEditableColumns()
    }


    void setEditableColumns() {
        editableColumns.add(NAME_COL)
        editableColumns.add(DESC_COL)
    }

    void setColumnClasses() {
        columnClasses.put(ID_COL, String.class)
        columnClasses.put(NAME_COL, String.class)
        columnClasses.put(DESC_COL, String.class)
    }

    void setColumnNames() {
        columnNames.put(ID_COL, getBundle("Accounting").getString("ALLERGENES_ID"))
        columnNames.put(NAME_COL, getBundle("Accounting").getString("ALLERGENES_NAME"))
        columnNames.put(DESC_COL, getBundle("Accounting").getString("ALLERGENES_DESC"))
    }
    // DE GET METHODEN
// ===============
    Object getValueAt(int row, int col) {
        Allergene allergene = getObject(row, col)
        if(allergene==null) return null
        if (col == ID_COL) return allergene.name
        if (col == NAME_COL) return allergene.shortName
        if (col == DESC_COL) allergene.description
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
        editableColumns.contains(col)
    }

    // DE SET METHODEN
    // ===============
    @Override
    void setValueAt(Object value, int row, int col) {
        Allergene allergene = getObject(row,col)
        if(col == NAME_COL){
            allergene.setShortName((String)value)
        }
        if(col == DESC_COL) {
            allergene.setDescription((String)value)
        }
        fireTableDataChanged()
    }

    void setAllergenes(Allergenes allergenes) {
        this.allergenes = allergenes
        fireTableDataChanged()
    }

    @Override
    Allergene getObject(int row, int col) {
        allergenes.businessObjects.get(row)
    }
}