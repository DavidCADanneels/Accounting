package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.MainApplication.PopupForTableActivator;
import be.dafke.BusinessModel.*;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;

public class TransactionOverviewPanel extends JPanel {
    private final SelectableTable<Transaction> transactionOverviewTable;
    private final SelectableTable<Booking> transactionDataTable;
    private final TransactionDataPopupMenu transactionDataPopupMenu;
    private final TransactionOverviewPopupMenu transactionOverviewPopupMenu;

    private final TransactionOverviewDataModel transactionOverviewDataModel;
    private final TransactionDataModel transactionDataModel;
//    private final JournalColorRenderer renderer;

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

//        renderer = new JournalColorRenderer();
//        transactionOverviewTable.setDefaultRenderer(String.class, renderer);
//        transactionOverviewTable.setDefaultRenderer(Account.class, renderer);
//        transactionOverviewTable.setDefaultRenderer(BigDecimal.class, renderer);

        transactionOverviewPopupMenu = new TransactionOverviewPopupMenu(transactionOverviewTable);
        transactionDataPopupMenu = new TransactionDataPopupMenu(transactionDataTable);

        transactionOverviewTable.addMouseListener(PopupForTableActivator.getInstance(transactionOverviewPopupMenu, transactionOverviewTable));
        transactionDataTable.addMouseListener(PopupForTableActivator.getInstance(transactionDataPopupMenu, transactionDataTable));

        JScrollPane scrollPane1 = new JScrollPane(transactionOverviewTable);
        JScrollPane scrollPane2 = new JScrollPane(transactionDataTable);
        JSplitPane center = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        center.add(scrollPane1, JSplitPane.TOP);
        center.add(scrollPane2, JSplitPane.BOTTOM);
		add(center, BorderLayout.CENTER);
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
//        renderer.setJournal(journal);
        transactionOverviewDataModel.setJournal(journal);
        transactionOverviewDataModel.fireTableDataChanged();
    }
    
    public void selectTransaction(Transaction transaction){
        int row = transactionOverviewDataModel.getRow(transaction);

        if(transactionOverviewTable!=null){
            transactionOverviewTable.setRowSelectionInterval(row, row);
            Rectangle cellRect = transactionOverviewTable.getCellRect(row, 0, false);
            transactionOverviewTable.scrollRectToVisible(cellRect);
        }
    }

    public void fireJournalDataChanged() {
        transactionOverviewDataModel.fireTableDataChanged();
    }
}