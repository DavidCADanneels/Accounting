package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BasicAccounting.MainApplication.Main
import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Article
import be.dafke.Accounting.BusinessModel.OrderItem
import be.dafke.ComponentModel.SelectableTable

import javax.swing.*
import java.awt.*

class StockHistoryPanel extends JPanel {
    final SelectableTable<OrderItem> transactionsTable
    final SelectableTable<Article> balanceTable
    StockBalanceDataTableModel stockBalanceDataTableModel
    StockTransactionsDataTableModel stockTransactionsDataTableModel

    StockHistoryPanel(Accounting accounting) {
        stockBalanceDataTableModel = new StockBalanceDataTableModel(accounting)
        balanceTable = new SelectableTable<>(stockBalanceDataTableModel)
        balanceTable.setPreferredScrollableViewportSize(new Dimension(1000, 400))

        stockTransactionsDataTableModel = new StockTransactionsDataTableModel(accounting.stockTransactions)
        transactionsTable = new SelectableTable<>(stockTransactionsDataTableModel)
        transactionsTable.setPreferredScrollableViewportSize(new Dimension(1000, 400))

        JScrollPane historyPanel = new JScrollPane(transactionsTable)
        JScrollPane balancePanel = new JScrollPane(balanceTable)

        JSplitPane splitPane = Main.createSplitPane(historyPanel, balancePanel, JSplitPane.VERTICAL_SPLIT)

        setLayout(new BorderLayout())
        add(splitPane, BorderLayout.CENTER)
    }

    void fireStockContentChanged() {
        stockBalanceDataTableModel.fireTableDataChanged()
        stockTransactionsDataTableModel.fireTableDataChanged()
    }
}
