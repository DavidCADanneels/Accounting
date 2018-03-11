package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderItems extends BusinessCollection<OrderItem>{

    public int getUnitsInStock(Article article){
        OrderItem orderItem = getBusinessObject(article.getName());
        return (orderItem == null) ? 0 : orderItem.getNumberOfUnits();
    }

    public int getItemsInStock(Article article){
        OrderItem orderItem = getBusinessObject(article.getName());
        return (orderItem == null) ? 0 : orderItem.getNumberOfItems();
    }

    public OrderItem addBusinessObject(OrderItem orderItem) {
        Article article = orderItem.getArticle();
        Integer itemsPerUnit = article.getItemsPerUnit();
        int unitsToAdd = orderItem.getNumberOfUnits();
        int itemsToAdd = unitsToAdd * itemsPerUnit;
        return add(orderItem, unitsToAdd, itemsToAdd);
    }

    protected OrderItem add(OrderItem orderItem, int unitsToAdd, int itemsToAdd){
        Article article = orderItem.getArticle();
        int unitsInStock = getUnitsInStock(article);
        int itemsInStock = getItemsInStock(article);
        OrderItem orderItemInStock = getBusinessObject(article.getName());
        if(orderItemInStock==null){
            orderItemInStock = orderItem;
            try {
                super.addBusinessObject(orderItemInStock);
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }
        orderItemInStock.setNumberOfUnits(unitsInStock+unitsToAdd);
        orderItemInStock.setNumberOfItems(itemsInStock+itemsToAdd);
        return orderItemInStock;
    }

    public void removeBusinessObject(OrderItem orderItem) {
        Article article = orderItem.getArticle();
        int itemsPerUnit = article.getItemsPerUnit();

        int itemsToRemove = orderItem.getNumberOfItems();
        int unitsToRemove = itemsToRemove / itemsPerUnit;

        remove(orderItem, unitsToRemove, itemsToRemove);

    }

    protected void remove(OrderItem orderItem, int unitsToRemove, int itemsToRemove){
        Article article = orderItem.getArticle();

        int itemsInStock = getItemsInStock(article);
        int totalNumberOfItems = itemsInStock-itemsToRemove;

        int unitsInStock = getUnitsInStock(article);
        int totalNumberOfUnits = unitsInStock-unitsToRemove;

        OrderItem orderItemInStock = getBusinessObject(article.getName());
        if(orderItemInStock!=null) {
            if (totalNumberOfItems < 0) {
                // TODO: throw error
            } else if (totalNumberOfItems == 0) {
                removeBusinessObject(orderItem);
            } else {
                orderItemInStock.setNumberOfItems(totalNumberOfItems);
                orderItemInStock.setNumberOfUnits(totalNumberOfUnits);
            }
        }
    }
}
