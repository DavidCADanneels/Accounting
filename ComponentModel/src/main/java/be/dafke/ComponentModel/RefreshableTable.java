package be.dafke.ComponentModel;

import javax.swing.*;

/**
 * Created by ddanneel on 17/02/2015.
 */
public class RefreshableTable<BusinessObject> extends JTable{
    RefreshableTableModel<BusinessObject> model;
    protected int selectedRow;
    protected int selectedColumn;

    public RefreshableTable(RefreshableTableModel<BusinessObject> model) {
        super(model);
        this.model = model;
    }

    public void setSelectedRow(int row){
        selectedRow = row;
    }

    public void setSelectedColumn(int col){
        selectedColumn = col;
    }

    // Overriding this method causes empty tables !!!
//    @Override
//    public RefreshableTableModel<BusinessObject> getModel(){
//        return model;
//    }

    public BusinessObject getSelectedObject(){
        return model.getObject(selectedRow, selectedColumn);
    }

    public void selectObject(BusinessObject object){
        int row = model.getRow(object);
        setRowSelectionInterval(row, row);
        scrollRectToVisible(getCellRect(row, 0, false));
    }

    public void refresh() {
        model.fireTableDataChanged();
    }
}
