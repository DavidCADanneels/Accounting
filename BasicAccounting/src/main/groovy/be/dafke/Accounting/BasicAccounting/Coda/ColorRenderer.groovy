package be.dafke.Accounting.BasicAccounting.Coda

import be.dafke.Accounting.BusinessModel.CounterParty
import be.dafke.Accounting.BusinessModel.TmpCounterParty

import javax.swing.*
import javax.swing.table.DefaultTableCellRenderer
import java.awt.*

class ColorRenderer extends DefaultTableCellRenderer {
    @Override
    Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column)
        if (value instanceof TmpCounterParty) {
            cell.setForeground(Color.GREEN)
        } else if (value instanceof CounterParty) {
            cell.setForeground(Color.RED)
        } else {
            cell.setForeground(Color.BLACK)
        }
        cell
    }
}
