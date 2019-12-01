package be.dafke.Accounting.BusinessModel

class PromoOrder extends Order {

    void addPromoOrderToArticles() {
        getBusinessObjects().forEach({ orderItem ->
            Article article = orderItem.article
            article.addPromoOrder(this)
        })
    }
}