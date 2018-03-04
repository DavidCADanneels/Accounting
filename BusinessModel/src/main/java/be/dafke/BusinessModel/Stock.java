package be.dafke.BusinessModel;

import java.util.HashMap;

public class Stock {
    private HashMap<Article,Integer> stock;

    public Stock() {
        stock = new HashMap<>();
    }

    public void addItem(Article article, int numberToAdd){
        int numberInStock = getNumberInStock(article);
        stock.put(article, numberInStock+numberToAdd);
    }

    public void removeItem(Article article, int numberToRemove){
        int numberInStock = getNumberInStock(article);
        stock.put(article, numberInStock-numberToRemove);
    }

    public int getNumberInStock(Article article){
        if(!stock.containsKey(article)) {
            return 0;
        } else {
            return stock.get(article);
        }
    }

}
