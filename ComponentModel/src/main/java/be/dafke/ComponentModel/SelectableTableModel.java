package be.dafke.ComponentModel;

import javax.swing.table.AbstractTableModel;

public abstract class SelectableTableModel<O> extends AbstractTableModel {
    public abstract O getObject(int row, int col);
}
