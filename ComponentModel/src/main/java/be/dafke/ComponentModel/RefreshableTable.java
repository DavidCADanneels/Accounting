package be.dafke.ComponentModel;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by ddanneel on 17/02/2015.
 */
public class RefreshableTable<BusinessObject> extends JTable{
    protected RefreshableTableModel<BusinessObject> model;
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
        return (selectedRow !=-1 && selectedColumn!=-1)?model.getObject(selectedRow, selectedColumn):null;
    }



    public ArrayList<BusinessObject> getSelectedObjects() {
        int[] rows = getSelectedRows();
        ArrayList<BusinessObject> businessObjectArrayList = new ArrayList<>();
        for(int row : rows) {
            BusinessObject businessObject = (BusinessObject) model.getValueAt(row, 0);
            businessObjectArrayList.add(businessObject);
        }
        // check on which element is clicked
        BusinessObject selectedObject = getSelectedObject();
        if(selectedObject!=null && !businessObjectArrayList.contains(selectedObject)){
            // otherwise return only the clicked element
            ArrayList<BusinessObject> list = new ArrayList<>();
            list.add(selectedObject);
            return list;
        } else {
            // return all selected elements if mouse was clicked on one of them
            return businessObjectArrayList;
        }

    }
}
