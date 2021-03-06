package be.dafke.Accounting.BasicAccounting.Balances

import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Accounts
import be.dafke.Accounting.BasicAccounting.MainApplication.PopupForTableActivator
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import java.awt.*

class TestBalancePanel extends JPanel{

    final JPopupMenu popup
    SelectableTable<Account> tabel
    TestBalanceDataModel testBalanceDataModel

    TestBalancePanel(Accounting accounting, Accounts accounts) {
        setLayout(new BorderLayout())

        testBalanceDataModel = new TestBalanceDataModel(accounts)

        tabel = new SelectableTable<>(testBalanceDataModel)
        tabel.setPreferredScrollableViewportSize(new Dimension(500, 200))
//        tabel.setRowSorter(null)
        JScrollPane scrollPane = new JScrollPane(tabel)

        popup = new BalancePopupMenu(accounting, tabel)
        tabel.addMouseListener(PopupForTableActivator.getInstance(popup,tabel))

        add(scrollPane, BorderLayout.CENTER)
    }

    void fireAccountDataChanged() {
        testBalanceDataModel.fireTableDataChanged()
    }
}
