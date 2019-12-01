package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BusinessModel.Booking
import be.dafke.Accounting.BusinessModel.Journal
import be.dafke.Accounting.BusinessModel.Transaction
import be.dafke.ComponentModel.SelectableTableModel

import javax.swing.*
import javax.swing.table.DefaultTableCellRenderer
import java.awt.*

class JournalColorRenderer extends DefaultTableCellRenderer {
    Journal journal

    @Override
    Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                            int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column)
        SelectableTableModel<Booking> model = (SelectableTableModel<Booking>) table.getModel()
        Booking booking = model.getObject(row, column)
        if (booking != null) {
            Transaction transaction = booking.transaction
            Journal transactionJournal = transaction.journal
            if (isSelected) {
                cell.setBackground(Color.CYAN)
            } else if (journal == null || transactionJournal == null) {
                cell.setBackground(Color.RED)
            } else if (journal != transactionJournal) {
                cell.setBackground(Color.GREEN)
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


    void setJournal(Journal journal) {
        this.journal = journal
    }
}