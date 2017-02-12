package be.dafke.ComponentModel;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by ddanneel on 17/02/2015.
 */
public class RefreshableTable<BusinessObject> extends JTable{
    protected RefreshableTableModel<BusinessObject> model;

    public RefreshableTable(RefreshableTableModel<BusinessObject> model) {
        super(model);
        this.model = model;
    }

    public ArrayList<BusinessObject> getSelectedObjects() {
        int[] rows = getSelectedRows();
        int col = getSelectedColumn();
        ArrayList<BusinessObject> businessObjectArrayList = new ArrayList<>();
        for(int row : rows) {
            BusinessObject businessObject = model.getObject(row, col);
            businessObjectArrayList.add(businessObject);
        }
        return businessObjectArrayList;
    }
}
