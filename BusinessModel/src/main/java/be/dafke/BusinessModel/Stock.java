package be.dafke.BusinessModel;

public class Stock extends OrderItems {
    private Articles articles;
    private boolean removeIfEmpty = true;

    public Stock(Articles articles) {
        super();
        this.articles = articles;
    }

//    // Add per Unit
//    public OrderItem addBusinessObject(OrderItem orderItem){
//        Article article = orderItem.getArticle();
//        int numberToAdd = orderItem.getNumberOfUnits();
//        Integer itemsPerUnit = article.getItemsPerUnit();
//        int numberOfItems = numberToAdd * itemsPerUnit;
//        orderItem.setNumberOfUnits(numberOfItems);
//        int numberInStock = getNumberInStock(article);
//        stock.put(article, numberInStock+numberOfItems);
//        return orderItem;
//    }
//
//    // Remove per Item
//    public void removeBusinessObject(OrderItem orderItem){
//        Article article = orderItem.getArticle();
////        Integer itemsPerUnit = article.getItemsPerUnit();
//        int numberToRemove = orderItem.getNumberOfUnits();
//        int numberInStock = getNumberInStock(article);
//        int result = numberInStock-numberToRemove;
//        if (result < 0){
//            // TODO: throw error
//        } else if (result == 0){
//            stock.remove(article);
//        } else {
//            stock.put(article, result);
//        }
//    }

    public void purchaseUnits(OrderItems load){
        load.getBusinessObjects().forEach(this::addBusinessObject);
    }

    public void sellItems(OrderItems load){
        for (OrderItem orderItem : load.getBusinessObjects()) {
            remove(orderItem, true, removeIfEmpty);
        }
    }

//    public OrderItem getBusinessObject(String name){
//        Article article = articles.getBusinessObject(name);
//        return getBusinessObject(article);
//    }

}
