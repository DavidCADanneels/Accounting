package be.dafke.BusinessModel;

public class Stock extends OrderItems {
    private Articles articles;

    public Stock(Articles articles) {
        super();
        this.articles = articles;
    }

//    public void addItem(Article article, int numberToAdd){
//        Integer itemsPerUnit = article.getItemsPerUnit();
//        int numberInStock = getNumberInStock(article);
//        stock.put(article, numberInStock+numberToAdd*itemsPerUnit);
//    }
//
//    public void removeItem(Article article, int numberToRemove){
//        Integer itemsPerUnit = article.getItemsPerUnit();
//        int numberInStock = getNumberInStock(article);
//        int result = numberInStock-numberToRemove*itemsPerUnit;
//        if (result < 0){
//            // TODO: throw error
//        } else if (result == 0){
//            stock.remove(article);
//        } else {
//            stock.put(article, result);
//        }
//    }

    public void addLoad(OrderItems load){
        load.getBusinessObjects().forEach( stockItem -> {
            addBusinessObject(stockItem);
        });
    }

    public void removeLoad(OrderItems load){
        load.getBusinessObjects().forEach( stockItem -> {
            removeBusinessObject(stockItem);
        });
    }

    public OrderItem getBusinessObject(String name){
        Article article = articles.getBusinessObject(name);
        return getBusinessObject(article);
    }

}
