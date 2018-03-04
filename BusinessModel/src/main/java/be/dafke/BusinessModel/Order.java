package be.dafke.BusinessModel;

public class Order extends StockItems{
    private Articles articles;

    public Order(Articles articles) {
        super();
        this.articles = articles;
    }

    public void setItem(Article article, int totalNumber){
        stock.put(article, totalNumber);
    }

    public int getItem(Article article){
        Integer nr = stock.get(article);
        return nr==null?0:nr;
    }

    public StockItem getBusinessObject(String name){
        Article article = articles.getBusinessObject(name);
        return getBusinessObject(article);
    }

}
