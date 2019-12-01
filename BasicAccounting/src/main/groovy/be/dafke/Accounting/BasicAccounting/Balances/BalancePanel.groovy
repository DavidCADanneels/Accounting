package be.dafke.Accounting.BasicAccounting.Balances

import be.dafke.Accounting.BasicAccounting.MainApplication.PopupForTableActivator
import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Balance
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import java.awt.*

class BalancePanel extends JPanel {

    private final JPopupMenu popup
    private SelectableTable<Account> tabel
    private BalanceDataModel balanceDataModel

    BalancePanel(Accounting accounting, Balance balance) {
        setLayout(new BorderLayout())

        balanceDataModel = new BalanceDataModel(balance)

        tabel = new SelectableTable<>(balanceDataModel)
        tabel.setPreferredScrollableViewportSize(new Dimension(500, 200))
        tabel.setRowSorter(null)
        JScrollPane scrollPane = new JScrollPane(tabel)

        popup = new BalancePopupMenu(accounting, tabel)
        tabel.addMouseListener(PopupForTableActivator.getInstance(popup,tabel))
        tabel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)

        add(scrollPane, BorderLayout.CENTER)
    }

    void fireAccountDataChanged() {
        balanceDataModel.fireTableDataChanged()
    }
}
