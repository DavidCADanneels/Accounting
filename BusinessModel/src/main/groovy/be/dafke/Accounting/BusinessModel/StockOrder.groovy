package be.dafke.Accounting.BusinessModel

class StockOrder extends Order {

    private Transaction balanceTransaction

    void addStockOrderToArticles() {
        getBusinessObjects().forEach({ orderItem ->
            Article article = orderItem.getArticle()
            article.addStockOrder(this)
        })
    }

    void setBalanceTransaction(Transaction balanceTransaction) {
        this.balanceTransaction = balanceTransaction
    }

    Transaction getBalanceTransaction() {
        balanceTransaction
    }
}
