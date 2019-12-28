package be.dafke.Accounting.BasicAccounting.Journals

import be.dafke.Accounting.BasicAccounting.MainApplication.PopupForTableActivator
import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.Booking
import be.dafke.Accounting.BusinessModel.Journal
import be.dafke.Accounting.BusinessModel.Journals
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import java.awt.*

class JournalDetailsPanel extends JPanel{
    final JournalDetailsPopupMenu popup
    SelectableTable<Booking> table
    JournalDetailsDataModel journalDetailsDataModel

    JournalDetailsPanel(Journal journal, Journals journals) {
        journalDetailsDataModel = new JournalDetailsDataModel()
        journalDetailsDataModel.journal = journal

        table = new SelectableTable<>(journalDetailsDataModel)
        table.setPreferredScrollableViewportSize(new Dimension(500, 200))
        JournalColorRenderer renderer = new JournalColorRenderer()
        renderer.journal = journal
        table.setDefaultRenderer(String.class, renderer)
        table.setDefaultRenderer(Account.class, renderer)
        table.setDefaultRenderer(BigDecimal.class, renderer)
        table.setRowSorter(null)

        popup = new JournalDetailsPopupMenu(journals, table)
        table.addMouseListener(PopupForTableActivator.getInstance(popup, table))

        JScrollPane scrollPane = new JScrollPane(table)
        setLayout(new BorderLayout())
        add(scrollPane, BorderLayout.CENTER)
    }

    void selectObject(Booking object){
        int row = journalDetailsDataModel.getRow(object)
        if(table){
            table.setRowSelectionInterval(row, row)
            Rectangle cellRect = table.getCellRect(row, 0, false)
            table.scrollRectToVisible(cellRect)
        }
    }

    void closePopups(){
        popup.visible = false
    }

    void fireJournalDataChanged() {
        journalDetailsDataModel.fireTableDataChanged()
    }
}
