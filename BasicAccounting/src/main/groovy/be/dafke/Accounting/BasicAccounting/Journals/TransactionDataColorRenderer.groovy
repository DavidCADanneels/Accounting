package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BusinessModel.Booking
import be.dafke.Accounting.BusinessModel.Journal
import be.dafke.Accounting.BusinessModel.Transaction
import be.dafke.ComponentModel.SelectableTableModel

import javax.swing.JTable
import javax.swing.table.DefaultTableCellRenderer
import java.awt.Color
import java.awt.Component

class TransactionDataColorRenderer<T> extends DefaultTableCellRenderer {
    private Journal journal

    @Override
    Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column)
        SelectableTableModel<Booking> model = (SelectableTableModel<Booking>)table.getModel()
        Booking booking = model.getObject(row, column)
        if(booking!=null) {
            Transaction transaction = booking.getTransaction()
            if(column == TransactionDataModel.VATINFO) {
                if (transaction.isRegistered()) {
                    cell.setForeground(Color.BLACK)
                } else {
                    cell.setForeground(Color.RED)
                }
            }
        }
        cell
    }

    void setJournal(Journal journal) {
        this.journal = journal
    }
}