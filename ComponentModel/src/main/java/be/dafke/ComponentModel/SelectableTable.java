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
            int rowInModel = rowSorter.convertRowIndexToModel(selectedRow);
            BusinessObject businessObject = model.getObject(rowInModel, col);
            if(businessObject!=null)
                businessObjectArrayList.add(businessObject);
        }
        return businessObjectArrayList;
    }

    public BusinessObject getSelectedObject() {
        int selectedRow = getSelectedRow();
        RowSorter<? extends TableModel> rowSorter = getRowSorter();
        int rowInModel = rowSorter.convertRowIndexToModel(selectedRow);
        int col = getSelectedColumn();
        return model.getObject(rowInModel, col);
    }
}
