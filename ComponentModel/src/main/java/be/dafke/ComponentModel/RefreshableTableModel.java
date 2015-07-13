package be.dafke.ComponentModel;

import javax.swing.table.AbstractTableModel;

/**
 * Created by ddanneel on 17/02/2015.
 */
public abstract class RefreshableTableModel<BusinessObject> extends AbstractTableModel {
    public abstract BusinessObject getObject(int row, int col);

    public abstract int getRow(BusinessObject object);
}
