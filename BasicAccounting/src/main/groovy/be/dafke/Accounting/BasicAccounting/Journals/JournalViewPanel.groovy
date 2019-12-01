package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BasicAccounting.MainApplication.PopupForTableActivator
import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.BusinessModelDao.AccountingSession
import be.dafke.Accounting.BusinessModelDao.Session
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import java.awt.*

class JournalViewPanel extends JPanel {
    private final SelectableTable<Booking> table
    private final JournalDetailsPopupMenu popup

    private final JournalDetailsDataModel journalDetailsDataModel
    private final JournalColorRenderer renderer

    JournalViewPanel() {
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
//        popup.setAccounting(accounting)
        setJournals(accounting == null ? null : accounting.getJournals())
        AccountingSession accountingSession = Session.getAccountingSession(accounting)
        setJournal(accounting == null ? null : accountingSession.getActiveJournal())
    }

    void setJournals(Journals journals) {
        popup.setJournals(journals)
    }

    void setJournal(Journal journal) {
        renderer.setJournal(journal)
        journalDetailsDataModel.setJournal(journal)
        journalDetailsDataModel.fireTableDataChanged()
    }

    void selectBooking(Booking booking) {
        int row = journalDetailsDataModel.getRow(booking)

        if (table != null) {
            table.setRowSelectionInterval(row, row)
            Rectangle cellRect = table.getCellRect(row, 0, false)
            table.scrollRectToVisible(cellRect)
        }
    }

    void selectTransaction(Transaction transaction) {
        ArrayList<Booking> bookings = transaction.getBusinessObjects()
        if (bookings != null && !bookings.isEmpty()) {
            Booking firstBooking = bookings.get(0)
            Booking lastBooking = bookings.get(bookings.size() - 1)
            int firstRow = journalDetailsDataModel.getRow(firstBooking)
            int lastRow = journalDetailsDataModel.getRow(lastBooking)

            if (table != null) {
                table.setRowSelectionInterval(firstRow, lastRow)
                Rectangle cellRect = table.getCellRect(lastRow, 0, false)
                table.scrollRectToVisible(cellRect)
            }
        }
    }

    void fireJournalDataChanged() {
        journalDetailsDataModel.fireTableDataChanged()
    }
}