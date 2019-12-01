package be.dafke.ComponentModel

import javax.swing.table.AbstractTableModel

abstract class SelectableTableModel<O> extends AbstractTableModel {
    abstract O getObject(int row, int col)
}
