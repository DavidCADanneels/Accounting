package be.dafke.ComponentModel;

import javax.swing.table.AbstractTableModel;

/**
 * Created by ddanneel on 17/02/2015.
 */
public abstract class SelectableTableModel<O> extends AbstractTableModel {
    public abstract O getObject(int row, int col);
}
