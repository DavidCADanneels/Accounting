package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Article
import be.dafke.Accounting.BusinessModel.OrderItem
import be.dafke.Accounting.BusinessModel.SalesOrder
import be.dafke.Accounting.BusinessModel.StockTransactions

class SalesOrderCreateDataTableModel extends SalesOrderViewDataTableModel {
    final StockTransactions stockTransactions
    TotalsPanel totalsPanel

    SalesOrderCreateDataTableModel(Accounting accounting, SalesOrder order, TotalsPanel totalsPanel) {
        super()
        this.totalsPanel = totalsPanel
        stockTransactions = accounting.stockTransactions
        this.order = order
    }

    int getRowCount() {
        stockTransactions.stock.keySet().size()
    }

    @Override
    boolean isCellEditable(int row, int col) {
        col==NR_OF_ITEMS_COL || col == PRICE_ITEM_COL || col == VAT_RATE_COL
    }

// DE SET METHODEN
// ===============
    @Override
    void setValueAt(Object value, int row, int col) {
        OrderItem orderItem = getObject(row,col)
        if(col == NR_OF_ITEMS_COL){
            int nr = (Integer) value
            orderItem.setNumberOfItems(nr)
        } else if (col == PRICE_ITEM_COL){
            BigDecimal amount = (BigDecimal) value
            orderItem.setSalesPriceForItem(amount)
        } else if (col == VAT_RATE_COL){
            int nr = (Integer) value
            orderItem.setSalesVatRate(nr)
        }
        order.setOrderItem(orderItem)
        totalsPanel.fireOrderContentChanged(order)
        fireTableDataChanged()
    }

    @Override
    OrderItem getObject(int row, int col) {
        List<Article> articleList = stockTransactions.stock.keySet().collect()
        if(articleList == null) null
        Article article = articleList.get(row)
        order.getBusinessObject(article.name)
    }
}
