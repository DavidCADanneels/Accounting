package be.dafke.ComponentModel

import javax.swing.JTable
import javax.swing.RowSorter
import javax.swing.table.TableModel

class SelectableTable<BusinessObject> extends JTable{
    protected SelectableTableModel<BusinessObject> model

    SelectableTable(SelectableTableModel<BusinessObject> model) {
        super(model)
        this.model = model
        setAutoCreateRowSorter(true)
//        setRowSorter(null)
    }

    ArrayList<BusinessObject> getSelectedObjects() {
        int[] selectedRows = getSelectedRows()
        int col = getSelectedColumn()
        RowSorter<? extends TableModel> rowSorter = getRowSorter()
        ArrayList<BusinessObject> businessObjectArrayList = new ArrayList<>()
        for(int selectedRow : selectedRows) {
            BusinessObject businessObject
            if(rowSorter!=null) {
                int rowInModel = rowSorter.convertRowIndexToModel(selectedRow)
                businessObject = model.getObject(rowInModel, col)
            } else {
                businessObject = model.getObject(selectedRow, col)
            }
            if(businessObject!=null) {
                businessObjectArrayList.add(businessObject)
            }
        }
        businessObjectArrayList
    }

    BusinessObject getSelectedObject() {
        int selectedRow = getSelectedRow()
        if(selectedRow == -1) null
        RowSorter<? extends TableModel> rowSorter = getRowSorter()
        int col = getSelectedColumn()
        if(rowSorter!=null) {
            int rowInModel = rowSorter.convertRowIndexToModel(selectedRow)
            model.getObject(rowInModel, col)
        } else {
            model.getObject(selectedRow,col)
        }
    }
}
