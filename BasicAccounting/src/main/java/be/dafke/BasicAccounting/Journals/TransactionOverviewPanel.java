package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.MainApplication.PopupForTableActivator;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

public class TransactionOverviewPanel extends JPanel {
    private final SelectableTable<Transaction> transactionOverviewTable;
    private final SelectableTable<Booking> transactionDataTable;
    private final TransactionDataPopupMenu transactionDataPopupMenu;
    private final TransactionOverviewPopupMenu transactionOverviewPopupMenu;

    private final TransactionOverviewDataModel transactionOverviewDataModel;
    private final TransactionDataModel transactionDataModel;
    private final TransactionOverviewColorRenderer renderer;
    private final JTextField debet, credit;

    public TransactionOverviewPanel() {
		setLayout(new BorderLayout());
        transactionOverviewDataModel = new TransactionOverviewDataModel();
        transactionDataModel = new TransactionDataModel();

        transactionOverviewTable = new SelectableTable<>(transactionOverviewDataModel);
        transactionOverviewTable.setPreferredScrollableViewportSize(new Dimension(800, 200));
        transactionOverviewTable.setRowSorter(null);

        transactionDataTable = new SelectableTable<>(transactionDataModel);
        transactionDataTable.setPreferredScrollableViewportSize(new Dimension(800, 200));
        transactionDataTable.setRowSorter(null);

        renderer = new TransactionOverviewColorRenderer();
        transactionOverviewTable.setDefaultRenderer(String.class, renderer);
        transactionOverviewTable.setDefaultRenderer(Account.class, renderer);
        transactionOverviewTable.setDefaultRenderer(BigDecimal.class, renderer);

        transactionOverviewPopupMenu = new TransactionOverviewPopupMenu(transactionOverviewTable);
        transactionDataPopupMenu = new TransactionDataPopupMenu(transactionDataTable);

        transactionOverviewTable.addMouseListener(PopupForTableActivator.getInstance(transactionOverviewPopupMenu, transactionOverviewTable));
        transactionDataTable.addMouseListener(PopupForTableActivator.getInstance(transactionDataPopupMenu, transactionDataTable));

        JScrollPane overviewScrollPane = new JScrollPane(transactionOverviewTable);
        JScrollPane transactionScrollPane = new JScrollPane(transactionDataTable);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(transactionScrollPane, BorderLayout.CENTER);

        debet = new JTextField(8);
        credit = new JTextField(8);
        debet.setEditable(false);
        credit.setEditable(false);
        JPanel totalsPanel = new JPanel();
        totalsPanel.add(new JLabel(getBundle("Accounting").getString("TOTAL_DEBIT")));
        totalsPanel.add(debet);
        totalsPanel.add(new JLabel(getBundle("Accounting").getString("TOTAL_CREDIT")));
        totalsPanel.add(credit);

        bottomPanel.add(totalsPanel, BorderLayout.SOUTH);

        JSplitPane center = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        center.add(overviewScrollPane, JSplitPane.TOP);
        center.add(bottomPanel, JSplitPane.BOTTOM);
		add(center, BorderLayout.CENTER);

        DefaultListSelectionModel selectionModel = new DefaultListSelectionModel();
        selectionModel.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
//                if (multiSelection) {
//                    ArrayList<Transaction> transactions = transactionOverviewTable.getSelectedObjects();
//                    selectTransactions(transactions);
//                } else {
                Transaction transaction = transactionOverviewTable.getSelectedObject();
                selectTransaction(transaction);
                updateTotals(transaction);
//                }
            }
        });

        transactionOverviewTable.setSelectionModel(selectionModel);
    }

    private void updateTotals(Transaction transaction) {
        debet.setText(transaction==null?"":transaction.getDebetTotaal().toString());
        credit.setText(transaction==null?"":transaction.getCreditTotaal().toString());
    }

    public void setAccounting(Accounting accounting){
        setJournals(accounting==null?null:accounting.getJournals());
        setJournal(accounting==null?null:accounting.getActiveJournal());
    }

    public void setJournals(Journals journals){
        transactionOverviewPopupMenu.setJournals(journals);
        transactionDataPopupMenu.setJournals(journals);
    }

    public void setJournal(Journal journal) {
        renderer.setJournal(journal);
        transactionOverviewDataModel.setJournal(journal);
        transactionOverviewDataModel.fireTableDataChanged();
        Transaction transaction = transactionOverviewTable.getSelectedObject();
        selectTransaction(transaction);
    }
    
    public void selectTransaction(Transaction transaction){
        int row = transactionOverviewDataModel.getRow(transaction);
        if(row!=-1) {
            transactionOverviewTable.setRowSelectionInterval(row, row);
            Rectangle cellRect = transactionOverviewTable.getCellRect(row, 0, false);
            transactionOverviewTable.scrollRectToVisible(cellRect);
        }

        transactionDataModel.setTransaction(transaction);
        transactionDataModel.fireTableDataChanged();
    }

    public void fireJournalDataChanged() {
        transactionOverviewDataModel.fireTableDataChanged();
    }
}