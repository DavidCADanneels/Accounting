package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.ObjectModel.Exceptions.NotEmptyException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class OrderItems extends BusinessCollection<OrderItem>{
    public OrderItem addBusinessObject(OrderItem orderItem) {
        Article article = orderItem.getArticle();
        String articleName = article.getName();
        orderItem.setName(articleName);
        try {
            return super.addBusinessObject(orderItem);
        } catch (EmptyNameException e) {
            // Cannot occur since we set the name above
            e.printStackTrace();
            return null;
        } catch (DuplicateNameException e) {
            int unitsToAdd = orderItem.getNumberOfUnits();
            int itemsToAdd = orderItem.getNumberOfItems();
            OrderItem itemInStock = getBusinessObject(articleName);
            if(itemInStock==null){
                // cannot be null since DuplicateNameException
                return null;
            }
            itemInStock.addNumberOfItems(itemsToAdd);
            itemInStock.addNumberOfUnits(unitsToAdd);
            setOrderItem(itemInStock);
            return itemInStock;
        }
    }

    public void removeBusinessObject(OrderItem orderItem){
        Article article = orderItem.getArticle();
        String articleName = article.getName();
        orderItem.setName(articleName);
        try {
            super.removeBusinessObject(orderItem);
        } catch (NotEmptyException e1) {
            int unitsToRemove = orderItem.getNumberOfUnits();
            int itemsToRemove = orderItem.getNumberOfItems();
            OrderItem itemInStock = getBusinessObject(articleName);
            if (itemInStock != null) {
                itemInStock.removeNumberOfItems(itemsToRemove);
                itemInStock.removeNumberOfUnits(unitsToRemove);
                setOrderItem(itemInStock);
            }
        }
    }

    public void setOrderItem(OrderItem orderItem){
        TreeMap<String, String> keyMap = orderItem.getUniqueProperties();
        for (Map.Entry<String, String> entry : keyMap.entrySet()) {
            // This will not throw any exceptions: we already handled them above.
            addBusinessObject(orderItem, entry);
        }
    }
}
