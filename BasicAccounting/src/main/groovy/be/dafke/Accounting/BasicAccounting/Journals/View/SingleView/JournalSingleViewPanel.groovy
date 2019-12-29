package be.dafke.Accounting.BasicAccounting.Journals.View.SingleView


import be.dafke.Accounting.BasicAccounting.MainApplication.PopupForTableActivator
import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.BusinessModelDao.AccountingSession
import be.dafke.Accounting.BusinessModelDao.Session
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import java.awt.*

class JournalSingleViewPanel extends JPanel {
    final SelectableTable<Booking> table
    final JournalDetailsPopupMenu popup

    final JournalDetailsDataModel journalDetailsDataModel
    final JournalColorRenderer renderer

    JournalSingleViewPanel() {
        setLayout(new BorderLayout())
        journalDetailsDataModel = new JournalDetailsDataModel()

        table = new SelectableTable<>(journalDetailsDataModel)
        table.setPreferredScrollableViewportSize(new Dimension(800, 200))
        table.setRowSorter(null)

        renderer = new JournalColorRenderer()
        table.setDefaultRenderer(String.class, renderer)
        table.setDefaultRenderer(Account.class, renderer)
        table.setDefaultRenderer(BigDecimal.class, renderer)

        popup = new JournalDetailsPopupMenu(table)
        table.addMouseListener(PopupForTableActivator.getInstance(popup, table))

        JPanel center = new JPanel()
        JScrollPane scrollPane = new JScrollPane(table)
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS))
        center.add(scrollPane)
        add(center, BorderLayout.CENTER)
    }

    void setAccounting(Accounting accounting) {
//        popup.accounting = accounting
        setJournals(accounting == null ? null : accounting.journals)
        AccountingSession accountingSession = Session.getAccountingSession(accounting)
        setJournal(accounting == null ? null : accountingSession.activeJournal)
    }

    void setJournals(Journals journals) {
        popup.setJournals(journals)
    }

    void setJournal(Journal journal) {
        renderer.journal = journal
        journalDetailsDataModel.journal = journal
        journalDetailsDataModel.fireTableDataChanged()
    }

    void selectBooking(Booking booking) {
        int row = journalDetailsDataModel.getRow(booking)
        if (table) {
            table.setRowSelectionInterval(row, row)
            Rectangle cellRect = table.getCellRect(row, 0, false)
            table.scrollRectToVisible(cellRect)
        }
    }

    void selectTransaction(Transaction transaction) {
        ArrayList<Booking> bookings = transaction.businessObjects
        if (bookings && !bookings.empty) {
            Booking firstBooking = bookings.get(0)
            Booking lastBooking = bookings.get(bookings.size() - 1)
            int firstRow = journalDetailsDataModel.getRow(firstBooking)
            int lastRow = journalDetailsDataModel.getRow(lastBooking)

            if (table) {
                table.setRowSelectionInterval(firstRow, lastRow)
                Rectangle cellRect = table.getCellRect(lastRow, 0, false)
                table.scrollRectToVisible(cellRect)
            }
        }
    }

    void fireJournalDataChanged() {
        journalDetailsDataModel.fireTableDataChanged()
    }

    void closePopups(){
        popup.visible = false
    }
}