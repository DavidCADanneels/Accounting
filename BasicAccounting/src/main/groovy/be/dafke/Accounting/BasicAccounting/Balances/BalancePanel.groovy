package be.dafke.Accounting.BasicAccounting.Balances

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BasicAccounting.MainApplication.PopupForTableActivator
import be.dafke.Accounting.BusinessModel.Account
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Balance
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import java.awt.*

class BalancePanel extends JPanel {

    JPopupMenu popupLeft, popupRight
    SelectableTable<Account> balanceLeftTable
    SelectableTable<Account> balanceRightTable
    BalanceDataModel balanceLeftDataModel, balanceRightDataModel
    BalanceTotalsDataModel balanceTotalsDataModel
    JTable balanceTotalTable

    BalancePanel(Accounting accounting, Balance balance, boolean includeEmpty) {
        setLayout(new BorderLayout())
        balanceLeftDataModel = new BalanceDataModel(balance, true, includeEmpty)
        balanceRightDataModel = new BalanceDataModel(balance, false, includeEmpty)

        //
        balanceLeftTable = new SelectableTable<>(balanceLeftDataModel)
        balanceLeftTable.setPreferredScrollableViewportSize(new Dimension(250, 200))
//        balanceLeftTable.setRowSorter(null)

        popupLeft = new BalancePopupMenu(accounting, balanceLeftTable)
        balanceLeftTable.addMouseListener(PopupForTableActivator.getInstance(popupLeft,balanceLeftTable))
        balanceLeftTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)

        JScrollPane dataLeftScrollPane = new JScrollPane(balanceLeftTable)
        //

        //
        balanceRightTable = new SelectableTable<>(balanceRightDataModel)
        balanceRightTable.setPreferredScrollableViewportSize(new Dimension(250, 200))
//        balanceRightTable.setRowSorter(null)

        popupRight = new BalancePopupMenu(accounting, balanceRightTable)
        balanceRightTable.addMouseListener(PopupForTableActivator.getInstance(popupRight,balanceRightTable))
        balanceRightTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)

        JScrollPane dataRightScrollPane = new JScrollPane(balanceRightTable)
        //

        balanceTotalsDataModel = new BalanceTotalsDataModel(balance, false)
        balanceTotalTable = new JTable(balanceTotalsDataModel)
        balanceTotalTable.setPreferredScrollableViewportSize(new Dimension(500, 50))
        balanceTotalTable.setRowSorter(null)
        JScrollPane totalScrollPane = new JScrollPane(balanceTotalTable)

        JSplitPane splitPane = Main.createSplitPane(dataLeftScrollPane, dataRightScrollPane, JSplitPane.HORIZONTAL_SPLIT)

        add(splitPane, BorderLayout.CENTER)
        add(totalScrollPane, BorderLayout.SOUTH)
    }

    void fireTableDataChanged() {
        balanceLeftDataModel.fireTableDataChanged()
        balanceRightDataModel.fireTableDataChanged()
    }

    void setBalance(Balance balance) {
        balanceLeftDataModel.balance = balance
        balanceRightDataModel.balance  = balance
        balanceTotalsDataModel.balance = balance
    }
}
