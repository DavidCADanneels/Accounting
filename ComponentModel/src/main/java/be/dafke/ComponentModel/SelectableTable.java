package be.dafke.ComponentModel;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.util.ArrayList;

/**
 * Created by ddanneel on 17/02/2015.
 */
public class SelectableTable<BusinessObject> extends JTable{
    protected SelectableTableModel<BusinessObject> model;

    public SelectableTable(SelectableTableModel<BusinessObject> model) {
        super(model);
        this.model = model;
        setAutoCreateRowSorter(true);
//        setRowSorter(null);
    }

    public ArrayList<BusinessObject> getSelectedObjects() {
        int[] selectedRows = getSelectedRows();
        int col = getSelectedColumn();
        RowSorter<? extends TableModel> rowSorter = getRowSorter();
        ArrayList<BusinessObject> businessObjectArrayList = new ArrayList<>();
        for(int selectedRow : selectedRows) {
            BusinessObject businessObject;
            if(rowSorter!=null) {
                int rowInModel = rowSorter.convertRowIndexToModel(selectedRow);
                businessObject = model.getObject(rowInModel, col);
            } else {
                businessObject = model.getObject(selectedRow, col);
            }
            if(businessObject!=null) {
                businessObjectArrayList.add(businessObject);
            }
        }
        return businessObjectArrayList;
    }

    public BusinessObject getSelectedObject() {
        int selectedRow = getSelectedRow();
        if(selectedRow == -1) return null;
        RowSorter<? extends TableModel> rowSorter = getRowSorter();
        int col = getSelectedColumn();
        if(rowSorter!=null) {
            int rowInModel = rowSorter.convertRowIndexToModel(selectedRow);
            return model.getObject(rowInModel, col);
        } else {
            return model.getObject(selectedRow,col);
        }
    }
}
