package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;

import java.util.ArrayList;
import java.util.HashMap;

public class StockItems extends BusinessCollection<StockItem>{
    protected HashMap<Article,Integer> stock;

    public StockItems() {
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

    public StockItem addBusinessObject(StockItem stockItem){
        addItem(stockItem.getArticle(),stockItem.getNumber());
        return stockItem;
    }

    public ArrayList<StockItem> getBusinessObjects() {
        ArrayList<StockItem> result = new ArrayList<>();
        for (Article article : stock.keySet()){
            Integer number = stock.get(article);
            StockItem stockItem = new StockItem(number, article);
            result.add(stockItem);
        }
        return result;
    }

    public void removeBusinessObject(StockItem stockItem){
        removeItem(stockItem.getArticle(),stockItem.getNumber());
    }

    public StockItem getBusinessObject(Article article){
        Integer numberInStock = stock.get(article);
        return new StockItem(numberInStock==null?0:numberInStock, article);
    }

}
