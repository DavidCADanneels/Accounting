package be.dafke.Accounting.BasicAccounting.Trade

import be.dafke.Accounting.BusinessModel.Article
import be.dafke.Accounting.BusinessModel.Articles
import be.dafke.Accounting.BusinessModel.OrderItem
import be.dafke.Accounting.BusinessModel.SalesOrder

import java.util.function.Predicate

class SalesOrderCreateDataTableModel extends SalesOrderViewDataTableModel {
    private final Articles articles
//	private Predicate<Article> filter = article -> article.getSalesPriceItemWithVat()!=null
    private Predicate<Article> filter = Article.inStock()
    private TotalsPanel totalsPanel

    SalesOrderCreateDataTableModel(Articles articles, SalesOrder order, TotalsPanel totalsPanel) {
        super()
        this.totalsPanel = totalsPanel
        // articles should be stock instead (stock/articles contains nrInStock, nrSold, ...
        this.articles = articles
        this.order = order
    }

    int getRowCount() {
        List<Article> businessObjects = getArticles()
        if(businessObjects == null) 0
        businessObjects.size()
    }

    @Override
    boolean isCellEditable(int row, int col) {
        col==NR_OF_ITEMS_COL || col == PRICE_ITEM_COL || col == VAT_RATE_COL
    }

    private List<Article> getArticles(){
        if(articles==null || filter==null) null
        List<Article> businessObjects = articles.getBusinessObjects(filter)
        if(businessObjects == null) null
        businessObjects
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
        List<Article> articleList = getArticles()
        if(articleList == null) null
        Article article = articleList.get(row)
        order.getBusinessObject(article.getName())
    }
}
