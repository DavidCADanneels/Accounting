package be.dafke.Accounting.BasicAccounting.Journals.View.DualView

import be.dafke.Accounting.BasicAccounting.Journals.View.TransactionSelectionModel
import be.dafke.Accounting.BasicAccounting.MainApplication.PopupForTableActivator
import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.BusinessModelDao.AccountingSession
import be.dafke.Accounting.BusinessModelDao.Session
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import java.awt.*

import static java.util.ResourceBundle.getBundle

class TransactionOverviewPanel extends JPanel {
    final SelectableTable<Transaction> transactionOverviewTable
    final SelectableTable<Booking> transactionDataTable
    final SelectableTable<VATBooking> vatTable
    final TransactionDataPopupMenu transactionDataPopupMenu
    final TransactionOverviewPopupMenu transactionOverviewPopupMenu

    final TransactionOverviewDataModel transactionOverviewDataModel
    final TransactionDataModel transactionDataModel
    final TransactionOverviewColorRenderer transactionOverviewColorRenderer
    final JTextField debet, credit
    final VATBookingDataModel vatBookingDataModel
    final TransactionDataColorRenderer transactionDataColorRenderer

    TransactionSelectionModel transactionSelectionModel

    TransactionOverviewPanel(TransactionSelectionModel transactionSelectionModel) {
        setLayout(new BorderLayout())
        transactionOverviewDataModel = new TransactionOverviewDataModel()
        transactionDataModel = new TransactionDataModel()

        transactionOverviewTable = new SelectableTable<>(transactionOverviewDataModel)
        transactionOverviewTable.setPreferredScrollableViewportSize(new Dimension(800, 200))
        transactionOverviewTable.setRowSorter(null)

        transactionDataColorRenderer = new TransactionDataColorRenderer()
        transactionDataTable = new SelectableTable<>(transactionDataModel)
        transactionDataTable.setPreferredScrollableViewportSize(new Dimension(800, 200))
        transactionDataTable.setRowSorter(null)
        transactionDataTable.setDefaultRenderer(String.class, transactionDataColorRenderer)

        transactionOverviewColorRenderer = new TransactionOverviewColorRenderer()
        transactionOverviewTable.setDefaultRenderer(String.class, transactionOverviewColorRenderer)
        transactionOverviewTable.setDefaultRenderer(Account.class, transactionOverviewColorRenderer)
        transactionOverviewTable.setDefaultRenderer(BigDecimal.class, transactionOverviewColorRenderer)

        transactionOverviewPopupMenu = new TransactionOverviewPopupMenu(transactionOverviewTable)
        transactionDataPopupMenu = new TransactionDataPopupMenu(transactionDataTable)

        transactionOverviewTable.addMouseListener(PopupForTableActivator.getInstance(transactionOverviewPopupMenu, transactionOverviewTable))
        transactionDataTable.addMouseListener(PopupForTableActivator.getInstance(transactionDataPopupMenu, transactionDataTable))

        JScrollPane overviewScrollPane = new JScrollPane(transactionOverviewTable)
        JScrollPane transactionScrollPane = new JScrollPane(transactionDataTable)

        vatBookingDataModel = new VATBookingDataModel()
        vatTable = new SelectableTable<>(vatBookingDataModel)

        debet = new JTextField(8)
        credit = new JTextField(8)
        debet.setEditable(false)
        credit.setEditable(false)
        JPanel totalsPanel = new JPanel(new BorderLayout())
        JPanel totalsTopPanel = new JPanel()
        totalsTopPanel.add(new JLabel(getBundle("Accounting").getString("TOTAL_DEBIT")))
        totalsTopPanel.add(debet)
        totalsTopPanel.add(new JLabel(getBundle("Accounting").getString("TOTAL_CREDIT")))
        totalsTopPanel.add(credit)
        totalsPanel.add(totalsTopPanel, BorderLayout.NORTH)
        totalsPanel.add(vatTable, BorderLayout.CENTER)

        JSplitPane center = new JSplitPane(JSplitPane.VERTICAL_SPLIT)
        center.add(overviewScrollPane, JSplitPane.TOP)
        center.add(transactionScrollPane, JSplitPane.BOTTOM)
        add(center, BorderLayout.CENTER)
        add(totalsPanel, BorderLayout.SOUTH)

        this.transactionSelectionModel = transactionSelectionModel
        transactionOverviewTable.setSelectionModel(transactionSelectionModel)

        ListSelectionModel selectionModel = transactionDataTable.getSelectionModel()
        selectionModel.addListSelectionListener( {e ->
            if(!e.valueIsAdjusting){
                Booking booking = transactionDataTable.getSelectedObject()
                transactionSelectionModel.selectedBooking = booking
                transactionSelectionModel.selectedTransaction = null
            }
        })
    }

    void updateSelection() {
        Transaction transaction = transactionOverviewTable.selectedObject
        Booking booking = transactionDataTable.selectedObject
        if(transaction) {
            transactionSelectionModel.selectedTransaction = transaction
            transactionSelectionModel.selectedBooking = null
        } else {
            transactionSelectionModel.selectedBooking = booking
            transactionSelectionModel.selectedTransaction = null
        }
        setSelection()
    }

    void setSelection() {
        Transaction transaction = transactionSelectionModel.selectedTransaction
        Booking booking = transactionSelectionModel.selectedBooking
        if (transaction!=null) {
            selectTransaction(transaction)
            selectTransactionDetails(transaction)
        } else if (booking!=null) {
            selectBooking(booking)
        }
    }

    void updateTotals(Transaction transaction) {
        debet.setText(transaction == null ? "" : transaction.debitTotal.toString())
        credit.setText(transaction == null ? "" : transaction.creditTotal.toString())
    }

    void setAccounting(Accounting accounting) {
        setJournals(accounting == null ? null : accounting.journals)
        AccountingSession accountingSession = Session.getAccountingSession(accounting)
        setJournal(accountingSession?accountingSession.activeJournal:null)
    }

    void setJournals(Journals journals) {
        transactionOverviewPopupMenu.setJournals(journals)
        transactionDataPopupMenu.setJournals(journals)
    }

    void setJournal(Journal journal) {
        transactionOverviewColorRenderer.journal = journal
        transactionOverviewDataModel.journal = journal
        transactionOverviewDataModel.fireTableDataChanged()
        Transaction transaction = transactionOverviewTable.selectedObject
        selectTransactionDetails(transaction)
        selectTransaction(transaction)
    }

    void selectBooking(Booking booking){
        if(booking) {
            selectTransaction(booking.transaction)
            selectTransactionDetails(booking.transaction)
            def row = transactionDataModel.getRow(booking)
            if (row != -1) transactionDataTable.setRowSelectionInterval(row, row)
        }
    }

    void selectTransaction(Transaction transaction) {
        if(transaction) {
            int row = transactionOverviewDataModel.getRow(transaction)
            if (row != -1) transactionOverviewTable.setRowSelectionInterval(row, row)
        }
    }

    void selectTransactionDetails(Transaction transaction) {
        transactionDataModel.setTransaction(transaction)
        transactionDataModel.fireTableDataChanged()
        vatBookingDataModel.setTransaction(transaction)
        vatBookingDataModel.fireTableDataChanged()
        updateTotals(transaction)
    }

    void fireJournalDataChanged() {
        transactionOverviewDataModel.fireTableDataChanged()
    }

    void closePopups(){
        transactionDataPopupMenu.visible = false
        transactionOverviewPopupMenu.visible = false
    }
}