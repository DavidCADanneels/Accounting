package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BasicAccounting.MainApplication.PopupForTableActivator
import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Booking
import be.dafke.Accounting.BusinessModel.Journal
import be.dafke.Accounting.BusinessModel.Journals
import be.dafke.Accounting.BusinessModel.Transaction
import be.dafke.Accounting.BusinessModel.VATBooking
import be.dafke.Accounting.BusinessModelDao.AccountingSession
import be.dafke.Accounting.BusinessModelDao.Session
import be.dafke.ComponentModel.SelectableTable

import javax.swing.DefaultListSelectionModel
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JSplitPane
import javax.swing.JTextField
import java.awt.BorderLayout
import java.awt.Dimension

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
    boolean multiSelection = false

    TransactionOverviewPanel() {
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

        DefaultListSelectionModel selectionModel = new DefaultListSelectionModel()
        selectionModel.addListSelectionListener({ e ->
            if (!e.getValueIsAdjusting()) {
                setSelection()
            }
        })

        transactionOverviewTable.setSelectionModel(selectionModel)
    }

    void setMultiSelection(boolean multiSelection) {
        this.multiSelection = multiSelection
        setSelection()
        transactionOverviewDataModel.fireTableDataChanged()
    }

    void setSelection() {
        if (multiSelection) {
            ArrayList<Transaction> transactions = transactionOverviewTable.selectedObjects
            selectTransactions(transactions)
        } else {
            Transaction transaction = transactionOverviewTable.selectedObject
            selectTransaction(transaction)
        }
    }

    void updateTotals(Transaction transaction) {
        debet.setText(transaction == null ? "" : transaction.debitTotal.toString())
        credit.setText(transaction == null ? "" : transaction.creditTotal.toString())
    }

    void setAccounting(Accounting accounting) {
        setJournals(accounting == null ? null : accounting.journals)
        AccountingSession accountingSession = Session.getAccountingSession(accounting)
        setJournal(accounting?accountingSession.activeJournal:null)
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
        selectTransaction(transaction)
    }

    void selectTransactions(ArrayList<Transaction> transactions) {
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
        selectTransaction(mergedTransaction)
    }

    void selectTransaction(Transaction transaction) {
        transactionDataModel.setTransaction(transaction)
        transactionDataModel.fireTableDataChanged()
        vatBookingDataModel.setTransaction(transaction)
        vatBookingDataModel.fireTableDataChanged()
        updateTotals(transaction)
    }

    void fireJournalDataChanged() {
        transactionOverviewDataModel.fireTableDataChanged()
    }
}