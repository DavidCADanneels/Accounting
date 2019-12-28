package be.dafke.Accounting.BasicAccounting.Accounts.AccountDetails

import be.dafke.Accounting.BasicAccounting.MainApplication.PopupForTableActivator
import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.Booking
import be.dafke.Accounting.BusinessModel.Journals
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import java.awt.*

class AccountDetailsPanel extends JPanel {
    final AccountDetailsPopupMenu popup
    SelectableTable<Booking> table
    AccountDetailsDataModel accountDetailsDataModel

    AccountDetailsPanel(Account account, Journals journals) {

        setLayout(new BorderLayout())
        accountDetailsDataModel = new AccountDetailsDataModel(account)

        table = new SelectableTable<>(accountDetailsDataModel)
        table.setPreferredScrollableViewportSize(new Dimension(500, 200))
        AccountColorRenderer renderer = new AccountColorRenderer()
        table.setDefaultRenderer(String.class, renderer)
        table.setDefaultRenderer(Account.class, renderer)
        table.setDefaultRenderer(BigDecimal.class, renderer)

        popup = new AccountDetailsPopupMenu(journals, table)
        table.addMouseListener(PopupForTableActivator.getInstance(popup, table))

        JScrollPane scrollPane = new JScrollPane(table)
        add(scrollPane, BorderLayout.CENTER)
    }


    void selectObject(Booking object){
        int row = accountDetailsDataModel.getRow(object)
        if(table){
            table.setRowSelectionInterval(row, row)
            Rectangle cellRect = table.getCellRect(row, 0, false)
            table.scrollRectToVisible(cellRect)
        }
    }

    void closePopups(){
        popup.visible = false
    }

    void fireAccountDataChanged() {
        accountDetailsDataModel.fireTableDataChanged()
    }
}
