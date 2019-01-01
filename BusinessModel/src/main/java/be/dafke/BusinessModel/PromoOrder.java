package be.dafke.BusinessModel;

public class PromoOrder extends Order {

    public void addPromoOrderToArticles() {
        getBusinessObjects().forEach(orderItem -> {
            Article article = orderItem.getArticle();
            article.addPromoOrder(this);
        });
    }
}
