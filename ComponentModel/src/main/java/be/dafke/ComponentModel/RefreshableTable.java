package be.dafke.ComponentModel;

import javax.swing.*;
import java.util.ArrayList;

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

    public ArrayList<BusinessObject> getSelectedObjects() {
        int[] rows = getSelectedRows();
        ArrayList<BusinessObject> accountList = new ArrayList<>();
        for(int row : rows) {
            BusinessObject account = (BusinessObject) model.getValueAt(row, 0);
            accountList.add(account);
        }
        return accountList;
    }


    public void refresh() {
        model.fireTableDataChanged();
    }
}
