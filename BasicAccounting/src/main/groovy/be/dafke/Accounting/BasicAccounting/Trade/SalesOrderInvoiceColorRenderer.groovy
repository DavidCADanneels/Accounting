package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BusinessModel.SalesOrder

import javax.swing.JTable
import javax.swing.table.DefaultTableCellRenderer
import java.awt.Color
import java.awt.Component

class SalesOrderInvoiceColorRenderer extends DefaultTableCellRenderer {
    @Override
    Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column)
        SalesOrder salesOrder = ((SalesOrdersOverviewDataTableModel) table.getModel()).getObject(row, column)
        if(isSelected){
            cell.setBackground(Color.CYAN)
        } else if (salesOrder != null) {
            if (salesOrder.invoice) {
                cell.setBackground(Color.GREEN)
            } else {
                cell.setBackground(Color.WHITE)
            }
        }
        cell
    }
}
