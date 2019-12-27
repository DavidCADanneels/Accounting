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

    final JPopupMenu popup
    SelectableTable<Account> balanceLeftTable
    SelectableTable<Account> balanceRightTable
    BalanceLeftDataModel balanceLeftDataModel
    BalanceRightDataModel balanceRightDataModel
    BalanceTotalsDataModel balanceTotalsDataModel
    JTable balanceTotalTable

    BalancePanel(Accounting accounting, Balance balance) {
        setLayout(new BorderLayout())

        balanceLeftDataModel = new BalanceLeftDataModel(balance)
        balanceRightDataModel = new BalanceRightDataModel(balance)

        //
        balanceLeftTable = new SelectableTable<>(balanceLeftDataModel)
        balanceLeftTable.setPreferredScrollableViewportSize(new Dimension(250, 200))
//        balanceLeftTable.setRowSorter(null)

        popup = new BalancePopupMenu(accounting, balanceLeftTable)
        balanceLeftTable.addMouseListener(PopupForTableActivator.getInstance(popup,balanceLeftTable))
        balanceLeftTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)

        JScrollPane dataLeftScrollPane = new JScrollPane(balanceLeftTable)
        //

        //
        balanceRightTable = new SelectableTable<>(balanceRightDataModel)
        balanceRightTable.setPreferredScrollableViewportSize(new Dimension(250, 200))
//        balanceRightTable.setRowSorter(null)

        popup = new BalancePopupMenu(accounting, balanceRightTable)
        balanceRightTable.addMouseListener(PopupForTableActivator.getInstance(popup,balanceRightTable))
        balanceRightTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)

        JScrollPane dataRightScrollPane = new JScrollPane(balanceRightTable)
        //

        balanceTotalsDataModel = new BalanceTotalsDataModel(balance)
        balanceTotalTable = new JTable(balanceTotalsDataModel)
        balanceTotalTable.setPreferredScrollableViewportSize(new Dimension(500, 50))
        balanceTotalTable.setRowSorter(null)
        JScrollPane totalScrollPane = new JScrollPane(balanceTotalTable)

        JSplitPane splitPane = Main.createSplitPane(dataLeftScrollPane, dataRightScrollPane, JSplitPane.HORIZONTAL_SPLIT)

        add(splitPane, BorderLayout.CENTER)
        add(totalScrollPane, BorderLayout.SOUTH)
    }

    void fireAccountDataChanged() {
        balanceLeftDataModel.fireTableDataChanged()
        balanceRightDataModel.fireTableDataChanged()
    }
}
