package be.dafke.BasicAccounting.Journals;

import be.dafke.BasicAccounting.MainApplication.PopupForTableActivator;
import be.dafke.BusinessModel.Account;
import be.dafke.BusinessModel.Booking;
import be.dafke.BusinessModel.Journal;
import be.dafke.BusinessModel.Journals;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class JournalDetailsPanel extends JPanel{
    private final JournalDetailsPopupMenu popup;
    private SelectableTable<Booking> table;
    private JournalDetailsDataModel journalDetailsDataModel;

    public JournalDetailsPanel(Journal journal, Journals journals) {
        journalDetailsDataModel = new JournalDetailsDataModel();
        journalDetailsDataModel.setJournal(journal);

        table = new SelectableTable<>(journalDetailsDataModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));
        JournalColorRenderer renderer = new JournalColorRenderer();
        renderer.setJournal(journal);
        table.setDefaultRenderer(String.class, renderer);
        table.setDefaultRenderer(Account.class, renderer);
        table.setDefaultRenderer(BigDecimal.class, renderer);
        //table.setAutoCreateRowSorter(true);
        table.setRowSorter(null);

        popup = new JournalDetailsPopupMenu(journals, table);
        table.addMouseListener(PopupForTableActivator.getInstance(popup, table));

        JScrollPane scrollPane = new JScrollPane(table);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
    }

    public void selectObject(Booking object){
        int row = journalDetailsDataModel.getRow(object);
        if(table !=null){
            table.setRowSelectionInterval(row, row);
            Rectangle cellRect = table.getCellRect(row, 0, false);
            table.scrollRectToVisible(cellRect);
        }
    }

    public void closePopups(){
        popup.setVisible(false);
    }

    public void fireJournalDataChanged() {
        journalDetailsDataModel.fireTableDataChanged();
    }
}
