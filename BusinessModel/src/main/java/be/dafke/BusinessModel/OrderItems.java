package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderItems extends BusinessCollection<OrderItem>{
    protected HashMap<Article,Integer> stock;

    public OrderItems() {
        super();
        stock = new HashMap<>();
    }

    public void addItem(Article article, int numberToAdd){
        int numberInStock = getNumberInStock(article);
        stock.put(article, numberInStock+numberToAdd);
    }

    public void removeItem(Article article, int numberToRemove){
        int numberInStock = getNumberInStock(article);
        int result = numberInStock-numberToRemove;
        if (result < 0){
            // TODO: throw error
        } else if (result == 0){
            stock.remove(article);
        } else {
            stock.put(article, result);
        }
    }

    public int getNumberInStock(Article article){
        if(!stock.containsKey(article)) {
            return 0;
        } else {
            return stock.get(article);
        }
    }

    public OrderItem addBusinessObject(OrderItem orderItem){
        addItem(orderItem.getArticle(), orderItem.getNumber());
        return orderItem;
    }

    public ArrayList<OrderItem> getBusinessObjects() {
        ArrayList<OrderItem> result = new ArrayList<>();
        for (Article article : stock.keySet()){
            Integer number = stock.get(article);
            OrderItem orderItem = new OrderItem(number, article);
            result.add(orderItem);
        }
        return result;
    }

    public void removeBusinessObject(OrderItem orderItem){
        removeItem(orderItem.getArticle(), orderItem.getNumber());
    }

    public OrderItem getBusinessObject(Article article){
        Integer numberInStock = stock.get(article);
        return new OrderItem(numberInStock==null?0:numberInStock, article);
    }

}
