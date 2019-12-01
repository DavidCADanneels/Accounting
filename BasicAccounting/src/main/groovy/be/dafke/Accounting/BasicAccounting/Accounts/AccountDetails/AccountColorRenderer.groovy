package be.dafke.Accounting.BasicAccounting.Accounts.AccountDetails

import be.dafke.Accounting.BusinessModel.Booking
import be.dafke.Accounting.BusinessModel.Transaction

import javax.swing.*
import javax.swing.table.DefaultTableCellRenderer
import java.awt.*

class AccountColorRenderer extends DefaultTableCellRenderer {

    @Override
    Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                            int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column)
        Booking booking = ((AccountDetailsDataModel) table.getModel()).getObject(row, column)
        if(booking!=null) {
            Transaction transaction = booking.transaction
            if(isSelected){
                cell.setBackground(Color.CYAN)
            } else {
                cell.setBackground(Color.WHITE)
            }
            if (transaction.balanceTransaction) {
                cell.setForeground(Color.RED)
            } else {
                cell.setForeground(Color.BLACK)
            }
        }
        cell
    }
}