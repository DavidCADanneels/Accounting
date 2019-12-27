package be.dafke.Accounting.BasicAccounting.Balances

import be.dafke.Accounting.BasicAccounting.MainApplication.PopupForTableActivator
import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Balance
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import java.awt.*

class BalancePanel extends JPanel {

    final JPopupMenu popup
    SelectableTable<Account> balanceTable
    BalanceDataModel balanceDataModel
    BalanceTotalsDataModel balanceTotalsDataModel
    JTable balanceTotalTable

    BalancePanel(Accounting accounting, Balance balance) {
        setLayout(new BorderLayout())

        balanceDataModel = new BalanceDataModel(balance)

        balanceTable = new SelectableTable<>(balanceDataModel)
        balanceTable.setPreferredScrollableViewportSize(new Dimension(500, 200))
        balanceTable.setRowSorter(null)

        popup = new BalancePopupMenu(accounting, balanceTable)
        balanceTable.addMouseListener(PopupForTableActivator.getInstance(popup,balanceTable))
        balanceTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)

        JScrollPane dataScrollPane = new JScrollPane(balanceTable)

        balanceTotalsDataModel = new BalanceTotalsDataModel(balance)
        balanceTotalTable = new JTable(balanceTotalsDataModel)
        balanceTotalTable.setPreferredScrollableViewportSize(new Dimension(500, 50))
        balanceTotalTable.setRowSorter(null)
        JScrollPane totalScrollPane = new JScrollPane(balanceTotalTable)

        add(dataScrollPane, BorderLayout.CENTER)
        add(totalScrollPane, BorderLayout.SOUTH)
    }

    void fireAccountDataChanged() {
        balanceDataModel.fireTableDataChanged()
    }
}
