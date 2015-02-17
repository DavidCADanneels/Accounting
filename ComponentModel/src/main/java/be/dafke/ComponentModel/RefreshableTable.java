package be.dafke.ComponentModel;

import javax.swing.*;

/**
 * Created by ddanneel on 17/02/2015.
 */
public class RefreshableTable<BusinessObject> extends JTable{
    RefreshableTableModel<BusinessObject> model;

    public RefreshableTable(RefreshableTableModel<BusinessObject> model) {
        super(model);
        this.model = model;
    }

    @Override
    public RefreshableTableModel<BusinessObject> getModel(){
        return model;
    }
}
