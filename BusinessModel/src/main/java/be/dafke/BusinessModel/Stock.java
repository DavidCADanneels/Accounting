package be.dafke.BusinessModel;

public class Stock extends StockItems{
    private Articles articles;

    public Stock(Articles articles) {
        super();
        this.articles = articles;
    }

    public void addLoad(StockItems load){
        load.getBusinessObjects().forEach( stockItem -> {
            addBusinessObject(stockItem);
        });
    }

    public void removeLoad(StockItems load){
        load.getBusinessObjects().forEach( stockItem -> {
            removeBusinessObject(stockItem);
        });
    }

    public StockItem getBusinessObject(String name){
        Article article = articles.getBusinessObject(name);
        return getBusinessObject(article);
    }

}
