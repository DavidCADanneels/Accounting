package be.dafke.BusinessModel;

public class SalesOrder extends Order {

    public SalesOrder(Articles articles) {
        super(articles);
    }

    public void setOrderItem(OrderItem orderItem){
        Article article = orderItem.getArticle();
        int totalNumber = orderItem.getNumberOfItems();
        if(totalNumber<=0){
            stock.remove(article);
        } else {
            stock.put(article, totalNumber);
        }
    }
}
