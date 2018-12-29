package be.dafke.BasicAccounting.Trade;

import be.dafke.BasicAccounting.MainApplication.Main;
import be.dafke.BusinessModel.Accounting;
import be.dafke.BusinessModel.Article;
import be.dafke.BusinessModel.OrderItem;
import be.dafke.ComponentModel.SelectableTable;

import javax.swing.*;
import java.awt.*;

public class StockHistoryPanel extends JPanel {
    private final SelectableTable<OrderItem> transactionsTable;
    private final SelectableTable<Article> balanceTable;
    private StockBalanceDataTableModel stockBalanceDataTableModel;
    private StockTransactionsDataTableModel stockTransactionsDataTableModel;

    public StockHistoryPanel(Accounting accounting) {
        stockBalanceDataTableModel = new StockBalanceDataTableModel(accounting);
        balanceTable = new SelectableTable<>(stockBalanceDataTableModel);
        balanceTable.setPreferredScrollableViewportSize(new Dimension(1000, 400));

        stockTransactionsDataTableModel = new StockTransactionsDataTableModel(accounting.getStockTransactions());
        transactionsTable = new SelectableTable<>(stockTransactionsDataTableModel);
        transactionsTable.setPreferredScrollableViewportSize(new Dimension(1000, 400));

        JScrollPane historyPanel = new JScrollPane(transactionsTable);
        JScrollPane balancePanel = new JScrollPane(balanceTable);

        JSplitPane splitPane = Main.createSplitPane(historyPanel, balancePanel, JSplitPane.HORIZONTAL_SPLIT);

        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);
    }

    public void fireStockContentChanged() {
        stockBalanceDataTableModel.fireTableDataChanged();
        stockTransactionsDataTableModel.fireTableDataChanged();
    }
}
