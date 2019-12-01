package be.dafke.Accounting.BusinessModel

class StockOrder extends Order {

    Transaction balanceTransaction

    void addStockOrderToArticles() {
        getBusinessObjects().forEach({ orderItem ->
            Article article = orderItem.article
            article.addStockOrder(this)
        })
    }
}
