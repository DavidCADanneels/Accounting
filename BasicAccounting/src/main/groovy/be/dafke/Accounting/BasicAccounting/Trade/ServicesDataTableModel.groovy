package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BasicAccounting.MainApplication.ActionUtils
import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.ComponentModel.SelectableTableModel

import java.awt.*
import java.util.List

import static java.util.ResourceBundle.getBundle

class ServicesDataTableModel extends SelectableTableModel<Service> {
    final Services services
    static int SERVICE_NAME_COL = 0
    static int SUPPLIER_COL = 1
    static int UNIT_PRICE_COL = 2
    static int NR_OF_COL = 3
    final Component parent
    HashMap<Integer,String> columnNames = new HashMap<>()
    HashMap<Integer,Class> columnClasses = new HashMap<>()
    List<Integer> editableColumns = new ArrayList<>()

    ServicesDataTableModel(Component parent, Services services) {
        this.parent = parent
        this.services = services
        setColumnNames()
        setColumnClasses()
        setEditableColumns()
    }

    void setEditableColumns() {
        editableColumns.add(SERVICE_NAME_COL)
        editableColumns.add(UNIT_PRICE_COL)
        editableColumns.add(SUPPLIER_COL)
    }

    void setColumnClasses() {
        columnClasses.put(SERVICE_NAME_COL, String.class)
        columnClasses.put(UNIT_PRICE_COL, Integer.class)
        columnClasses.put(SUPPLIER_COL, Contact.class)
    }

    void setColumnNames() {
        columnNames.put(SERVICE_NAME_COL, getBundle("Accounting").getString("SERVICE"))
        columnNames.put(UNIT_PRICE_COL, getBundle("Accounting").getString("UNIT_PRICE"))
        columnNames.put(SUPPLIER_COL, getBundle("Contacts").getString("SUPPLIER"))
    }
    // DE GET METHODEN
// ===============
    Object getValueAt(int row, int col) {
        Service service = getObject(row, col)
        if(service==null) return null
        if (col == SERVICE_NAME_COL) return service.name
        if (col == UNIT_PRICE_COL) return service.unitPrice
        if (col == SUPPLIER_COL) return service.supplier
        null
    }

    int getColumnCount() {
        NR_OF_COL
    }

    int getRowCount() {
        services?services.businessObjects.size():0
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
        Service service = getObject(row,col)
        if(col == UNIT_PRICE_COL){
            service.unitPrice = (Integer) value
        }
        if(col == SUPPLIER_COL){
            service.supplier = (Contact) value
        }
        if(col == SERVICE_NAME_COL) {
//            service.setName((String) value)
            String oldName = service.name
            String newName = (String) value
            if (newName != null && !oldName.trim().equals(newName.trim())) {
                try {
                    services.modifyName(oldName, newName)
                } catch (DuplicateNameException e) {
                    ActionUtils.showErrorMessage(parent, ActionUtils.ARTICLE_DUPLICATE_NAME, newName.trim())
                } catch (EmptyNameException e) {
                    ActionUtils.showErrorMessage(parent, ActionUtils.ARTICLE_NAME_EMPTY)
                }
            }
        }
        fireTableDataChanged()
    }

    @Override
    Service getObject(int row, int col) {
        services.businessObjects.get(row)
    }
}
