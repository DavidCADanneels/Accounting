package be.dafke.BusinessModel;

public class Order extends StockItems{
    private Articles articles;

    public Order(Articles articles) {
        super();
        this.articles = articles;
    }

    public enum OrderType{
        PURCHASE, SALE;
    }

    public void setItem(StockItem stockItem){
        Article article = stockItem.getArticle();
        int totalNumber = stockItem.getNumber();
        if(totalNumber<=0){
            stock.remove(article);
        } else {
            stock.put(article, totalNumber);
        }
    }

    public StockItem getBusinessObject(String name){
        Article article = articles.getBusinessObject(name);
        return getBusinessObject(article);
    }

}
