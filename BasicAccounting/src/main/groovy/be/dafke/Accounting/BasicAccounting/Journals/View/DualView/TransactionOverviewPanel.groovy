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

class TransactionOverviewPanel extends JPanel { //implements ListSelectionListener {
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
    boolean multiSelection = false

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
        // TODO: extra (or same?) model to select booking(s)
//        transactionDataTable.setSelectionModel(transactionSelectionModel)
    }

    void updateSelection() {
        ArrayList<Transaction> transactions = transactionOverviewTable.selectedObjects
        ArrayList<Booking> bookings = transactionDataTable.selectedObjects
//        if (transactionSelectionModel.multiSelection) {
        if(transactions.size()>1){
            transactionSelectionModel.selectTransactions(transactions)
            transactionSelectionModel.selectedBookings = bookings
        } else if (transactions.size() == 1) {
            transactionSelectionModel.selectTransaction(transactions[0])
            transactionSelectionModel.selectedBooking = null
        } else if (transactions.size() == 0) {
            transactionSelectionModel.selectBooking(bookings[0])
        }
        setSelection()
    }

    void setSelection() {
        ArrayList<Transaction> transactions = transactionSelectionModel.selectedTransactions
        Transaction transaction = transactionSelectionModel.selectedTransaction
        Booking booking = transactionSelectionModel.selectedBooking
        if (transactionSelectionModel.multiSelection) {
            if (transactions!=null && !transactions.empty) {
                System.err.println("Transactions:${transactions.size}()")
                selectTransactions(transactions)
                selectTransactionDetails(mergeTransactions(transactions))
            } else if (transaction!=null) {
                System.err.println("Transaction:${transaction}")
                selectTransaction(transaction)
                selectTransactionDetails(transaction)
            }
        } else {
            if (transaction!=null) {
                System.err.println("Transaction:$transaction.transactionId")
                selectTransaction(transaction)
                selectTransactionDetails(transaction)
            } else if (booking!=null) {
                System.out.println("Booking:$booking")
                selectBooking(booking)
            }
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

    Transaction mergeTransactions(ArrayList<Transaction> transactions) {
        HashMap<Account, Booking> newTransactionData = new HashMap<>()
        transactions.forEach({ transaction ->
            ArrayList<Booking> bookings = transaction.businessObjects
            bookings.forEach({ booking ->
                Account account = booking.account
                Booking foundBooking = newTransactionData.get(account)
                if (foundBooking == null) {
                    Booking newBooking = new Booking(booking)
                    newTransactionData.put(account, newBooking)
                } else {
                    BigDecimal totalAmount = foundBooking.amount.add(booking.amount).setScale(2, BigDecimal.ROUND_HALF_DOWN)
                    foundBooking.setAmount(totalAmount)
                    ArrayList<VATBooking> vatBookings = booking.vatBookings
                    vatBookings.each {foundBooking.addVatBooking(it)}
                    newTransactionData.put(account, foundBooking)
                }
            })
        })
        Transaction mergedTransaction = new Transaction(Calendar.getInstance(), "")
        newTransactionData.forEach({ account, booking ->
            mergedTransaction.addBusinessObject(booking)
        })
        mergedTransaction
    }

    void setMultiSelection(boolean multiSelection){
        this.multiSelection = multiSelection
        setSelection()
    }

    void selectBooking(Booking booking){
        if(booking) {
            selectTransaction(booking.transaction)
            selectTransactionDetails(booking.transaction)
            def row = transactionDataModel.getRow(booking)
            if (row != -1) transactionDataTable.setRowSelectionInterval(row, row)
        }
    }

    void selectTransactions(ArrayList<Transaction> transactions) {
        if(transactions&&!transactions.empty){
            if(transactions.size()==1) selectTransaction(transactions[0])
            int first = transactionOverviewDataModel.getRow(transactions[0])
            int last = transactionOverviewDataModel.getRow(transactions[transactions.size()-1])
            if(first>-1 && last >-1) transactionOverviewTable.setRowSelectionInterval(first, last)
        }
    }
    void selectTransaction(Transaction transaction) {
        if(transaction) {
            int row = transactionOverviewDataModel.getRow(transaction)
            if (row != -1) transactionOverviewTable.setRowSelectionInterval(row, row)
//            selectTransactionDetails(transaction) // could be selectTransactionDetails(mergedTransactions) as well
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