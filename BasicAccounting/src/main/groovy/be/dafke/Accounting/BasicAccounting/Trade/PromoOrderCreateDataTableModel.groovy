package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BusinessModel.Accounting
import be.dafke.Accounting.BusinessModel.Article
import be.dafke.Accounting.BusinessModel.OrderItem
import be.dafke.Accounting.BusinessModel.PromoOrder
import be.dafke.Accounting.BusinessModel.StockTransactions

class PromoOrderCreateDataTableModel extends PromoOrderViewDataTableModel {
    final StockTransactions stockTransactions
    TotalsPanel totalsPanel

    PromoOrderCreateDataTableModel(Accounting accounting, PromoOrder order, TotalsPanel totalsPanel) {
        super()
        stockTransactions = accounting.stockTransactions
        this.totalsPanel = totalsPanel
        this.order = order
    }


    int getRowCount() {
        stockTransactions.stock.keySet().size()
    }

    @Override
    boolean isCellEditable(int row, int col) {
        col==NR_OF_ITEMS_COL || col == PURCHASE_PRICE_UNIT_COL
    }

    // DE SET METHODEN
// ===============
    @Override
    void setValueAt(Object value, int row, int col) {
        OrderItem orderItem = getObject(row,col)
        if(col == NR_OF_ITEMS_COL){
            int nr = (Integer) value
            orderItem.setNumberOfItems(nr)
        } else if(col == PURCHASE_PRICE_UNIT_COL){
            BigDecimal amount = (BigDecimal) value
            orderItem.setPurchasePriceForUnit(amount)
        }
        order.setOrderItem(orderItem)
        totalsPanel.fireOrderContentChanged(order)
        fireTableDataChanged()
    }

    @Override
    OrderItem getObject(int row, int col) {
        List<Article> articleList = stockTransactions.stock.keySet().collect()
        if(articleList == null) return null
        Article article = articleList.get(row)
        order.getBusinessObject(article.name)
    }
}
