package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BusinessModel.Order

import javax.swing.*
import javax.swing.table.DefaultTableCellRenderer
import java.awt.*

class OrderPayedColorRenderer extends DefaultTableCellRenderer {
    @Override
    Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column)
        Order purchaseOrder = ((OrdersOverviewDataTableModel) table.getModel()).getObject(row, column)
        if(isSelected){
            cell.setBackground(Color.CYAN)
        } else {
            cell.setBackground(Color.WHITE)
        }
        if (purchaseOrder != null) {
            if (purchaseOrder.paymentTransaction!= null) {
                cell.setForeground(Color.BLACK)
            } else {
                cell.setForeground(Color.RED)
            }
        }
        cell
    }
}
