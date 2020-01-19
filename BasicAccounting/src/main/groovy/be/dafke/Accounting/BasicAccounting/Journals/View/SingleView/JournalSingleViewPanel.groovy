package be.dafke.Accounting.BasicAccounting.Journals.View.SingleView

import be.dafke.Accounting.BasicAccounting.Journals.View.TransactionSelectionModel
import be.dafke.Accounting.BasicAccounting.MainApplication.PopupForTableActivator
import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.BusinessModelDao.AccountingSession
import be.dafke.Accounting.BusinessModelDao.Session
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import java.awt.*

class JournalSingleViewPanel extends JPanel {
    final SelectableTable<Booking> journalDetailsTable
    final JournalDetailsPopupMenu popup

    final JournalDetailsDataModel journalDetailsDataModel
    final JournalColorRenderer renderer
    TransactionSelectionModel transactionSelectionModel

    JournalSingleViewPanel(TransactionSelectionModel transactionSelectionModel) {
        setLayout(new BorderLayout())
        journalDetailsDataModel = new JournalDetailsDataModel()

        journalDetailsTable = new SelectableTable<>(journalDetailsDataModel)
        journalDetailsTable.setPreferredScrollableViewportSize(new Dimension(800, 200))
        journalDetailsTable.setRowSorter(null)

        renderer = new JournalColorRenderer()
        journalDetailsTable.setDefaultRenderer(String.class, renderer)
        journalDetailsTable.setDefaultRenderer(Account.class, renderer)
        journalDetailsTable.setDefaultRenderer(BigDecimal.class, renderer)

        popup = new JournalDetailsPopupMenu(journalDetailsTable)
        journalDetailsTable.addMouseListener(PopupForTableActivator.getInstance(popup, journalDetailsTable))

        this.transactionSelectionModel = transactionSelectionModel
        journalDetailsTable.setSelectionModel(transactionSelectionModel)

        JPanel center = new JPanel()
        JScrollPane scrollPane = new JScrollPane(journalDetailsTable)
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS))
        center.add(scrollPane)
        add(center, BorderLayout.CENTER)
    }

    void updateSelection(){
        ArrayList<Booking> bookings = journalDetailsTable.selectedObjects
        if(bookings.size() == 1){
            Booking booking = journalDetailsTable.selectedObject
            transactionSelectionModel.selectBooking(booking)
        } else if(bookings.size() > 1) {
            transactionSelectionModel.selectTransaction(bookings[0].transaction)
//            transactionSelectionModel.selectBookings(bookings)
        }
//        setSelection()
    }

    void setSelection(){
        if (transactionSelectionModel.selectedBooking) {
            selectBooking(transactionSelectionModel.selectedBooking)
        } else
        if (transactionSelectionModel.selectedTransaction) {
            selectTransaction(transactionSelectionModel.selectedTransaction)
//        } else if (transactionSelectionModel.selectedBooking) {
//            selectBooking(transactionSelectionModel.selectedBooking)
        }
    }

    void setAccounting(Accounting accounting) {
//        popup.accounting = accounting
        setJournals(accounting == null ? null : accounting.journals)
        AccountingSession accountingSession = Session.getAccountingSession(accounting)
        setJournal(accountingSession?accountingSession.activeJournal:null)
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
        if(booking) {
            int row = journalDetailsDataModel.getRow(booking)
            if (journalDetailsTable) {
                journalDetailsTable.setRowSelectionInterval(row, row)
                Rectangle cellRect = journalDetailsTable.getCellRect(row, 0, false)
                journalDetailsTable.scrollRectToVisible(cellRect)
            }
        }
    }

//    void selectTransactions(ArrayList<Transaction> transactions) {
//
//    }

    void selectTransaction(Transaction transaction) {
        if(transaction) {
            ArrayList<Booking> bookings = transaction.businessObjects
            if (bookings && !bookings.isEmpty()) {
                Booking firstBooking = bookings.get(0)
                Booking lastBooking = bookings.get(bookings.size() - 1)
                int firstRow = journalDetailsDataModel.getRow(firstBooking)
                int lastRow = journalDetailsDataModel.getRow(lastBooking)

                if (journalDetailsTable) {
                    journalDetailsTable.setRowSelectionInterval(firstRow, lastRow)
                    Rectangle cellRect = journalDetailsTable.getCellRect(lastRow, 0, false)
                    journalDetailsTable.scrollRectToVisible(cellRect)
                }
            }
        }
    }

    void  fireJournalDataChanged() {
        journalDetailsDataModel.fireTableDataChanged()
    }

    void closePopups(){
        popup.visible = false
    }
}