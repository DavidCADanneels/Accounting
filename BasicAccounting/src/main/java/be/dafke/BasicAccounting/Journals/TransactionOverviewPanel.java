package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.MainApplication.PopupForTableActivator;
import be.dafke.BusinessModel.*;
import be.dafke.BusinessModelDao.AccountingSession;
import be.dafke.BusinessModelDao.Session;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.util.HashMap;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

import static java.util.ResourceBundle.getBundle;

public class TransactionOverviewPanel extends JPanel {
    private final SelectableTable<Transaction> transactionOverviewTable;
    private final SelectableTable<Booking> transactionDataTable;
    private final SelectableTable<VATBooking> vatTable;
    private final TransactionDataPopupMenu transactionDataPopupMenu;
    private final TransactionOverviewPopupMenu transactionOverviewPopupMenu;

    private final TransactionOverviewDataModel transactionOverviewDataModel;
    private final TransactionDataModel transactionDataModel;
    private final TransactionOverviewColorRenderer transactionOverviewColorRenderer;
    private final JTextField debet, credit;
    private final VATBookingDataModel vatBookingDataModel;
    private final TransactionDataColorRenderer transactionDataColorRenderer;
    private boolean multiSelection = false;

    public TransactionOverviewPanel() {
		setLayout(new BorderLayout());
        transactionOverviewDataModel = new TransactionOverviewDataModel();
        transactionDataModel = new TransactionDataModel();

        transactionOverviewTable = new SelectableTable<>(transactionOverviewDataModel);
        transactionOverviewTable.setPreferredScrollableViewportSize(new Dimension(800, 200));
        transactionOverviewTable.setRowSorter(null);

        transactionDataColorRenderer = new TransactionDataColorRenderer();
        transactionDataTable = new SelectableTable<>(transactionDataModel);
        transactionDataTable.setPreferredScrollableViewportSize(new Dimension(800, 200));
        transactionDataTable.setRowSorter(null);
        transactionDataTable.setDefaultRenderer(String.class, transactionDataColorRenderer);

        transactionOverviewColorRenderer = new TransactionOverviewColorRenderer();
        transactionOverviewTable.setDefaultRenderer(String.class, transactionOverviewColorRenderer);
        transactionOverviewTable.setDefaultRenderer(Account.class, transactionOverviewColorRenderer);
        transactionOverviewTable.setDefaultRenderer(BigDecimal.class, transactionOverviewColorRenderer);

        transactionOverviewPopupMenu = new TransactionOverviewPopupMenu(transactionOverviewTable);
        transactionDataPopupMenu = new TransactionDataPopupMenu(transactionDataTable);

        transactionOverviewTable.addMouseListener(PopupForTableActivator.getInstance(transactionOverviewPopupMenu, transactionOverviewTable));
        transactionDataTable.addMouseListener(PopupForTableActivator.getInstance(transactionDataPopupMenu, transactionDataTable));

        JScrollPane overviewScrollPane = new JScrollPane(transactionOverviewTable);
        JScrollPane transactionScrollPane = new JScrollPane(transactionDataTable);

        vatBookingDataModel = new VATBookingDataModel();
        vatTable = new SelectableTable<>(vatBookingDataModel);

        debet = new JTextField(8);
        credit = new JTextField(8);
        debet.setEditable(false);
        credit.setEditable(false);
        JPanel totalsPanel = new JPanel(new BorderLayout());
        JPanel totalsTopPanel = new JPanel();
        totalsTopPanel.add(new JLabel(getBundle("Accounting").getString("TOTAL_DEBIT")));
        totalsTopPanel.add(debet);
        totalsTopPanel.add(new JLabel(getBundle("Accounting").getString("TOTAL_CREDIT")));
        totalsTopPanel.add(credit);
        totalsPanel.add(totalsTopPanel, BorderLayout.NORTH);
        totalsPanel.add(vatTable, BorderLayout.CENTER);

        JSplitPane center = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        center.add(overviewScrollPane, JSplitPane.TOP);
        center.add(transactionScrollPane, JSplitPane.BOTTOM);
        add(center, BorderLayout.CENTER);
        add(totalsPanel, BorderLayout.SOUTH);

        DefaultListSelectionModel selectionModel = new DefaultListSelectionModel();
        selectionModel.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                setSelection();
            }
        });

        transactionOverviewTable.setSelectionModel(selectionModel);
    }

    public void setMultiSelection(boolean multiSelection) {
        this.multiSelection = multiSelection;
        setSelection();
        transactionOverviewDataModel.fireTableDataChanged();
    }

    private void setSelection() {
        if (multiSelection) {
            ArrayList<Transaction> transactions = transactionOverviewTable.getSelectedObjects();
            selectTransactions(transactions);
        } else {
            Transaction transaction = transactionOverviewTable.getSelectedObject();
            selectTransaction(transaction);
        }
    }

    private void updateTotals(Transaction transaction) {
        debet.setText(transaction==null?"":transaction.getDebetTotaal().toString());
        credit.setText(transaction==null?"":transaction.getCreditTotaal().toString());
    }

    public void setAccounting(Accounting accounting){
        setJournals(accounting==null?null:accounting.getJournals());
        AccountingSession accountingSession = Session.getAccountingSession(accounting);
        setJournal(accounting==null?null:accountingSession.getActiveJournal());
    }

    public void setJournals(Journals journals){
        transactionOverviewPopupMenu.setJournals(journals);
        transactionDataPopupMenu.setJournals(journals);
    }

    public void setJournal(Journal journal) {
        transactionOverviewColorRenderer.setJournal(journal);
        transactionOverviewDataModel.setJournal(journal);
        transactionOverviewDataModel.fireTableDataChanged();
        Transaction transaction = transactionOverviewTable.getSelectedObject();
        selectTransaction(transaction);
    }
    
    public void selectTransactions(ArrayList<Transaction> transactions){
        HashMap<Account,Booking> newTransactionData = new HashMap<>();
        transactions.forEach(transaction -> {
            ArrayList<Booking> bookings = transaction.getBusinessObjects();
            bookings.forEach(booking -> {
                Account account = booking.getAccount();
                Booking foundBooking = newTransactionData.get(account);
                if(foundBooking == null){
                    Booking newBooking = new Booking(booking);
                    newTransactionData.put(account, newBooking);
                } else {
                    BigDecimal totalAmount = foundBooking.getAmount().add(booking.getAmount()).setScale(2, BigDecimal.ROUND_HALF_DOWN);
                    foundBooking.setAmount(totalAmount);
                    ArrayList<VATBooking> vatBookings = booking.getVatBookings();
                    vatBookings.forEach(foundBooking::addVatBooking);
                    newTransactionData.put(account, foundBooking);
                }
            });
        });
        Transaction mergedTransaction = new Transaction(Calendar.getInstance(), "");
        newTransactionData.forEach((account, booking) -> {
            mergedTransaction.addBusinessObject(booking);
        });
        selectTransaction(mergedTransaction);
    }

    public void selectTransaction(Transaction transaction){
        transactionDataModel.setTransaction(transaction);
        transactionDataModel.fireTableDataChanged();
        vatBookingDataModel.setTransaction(transaction);
        vatBookingDataModel.fireTableDataChanged();
        updateTotals(transaction);
    }

    public void fireJournalDataChanged() {
        transactionOverviewDataModel.fireTableDataChanged();
    }
}