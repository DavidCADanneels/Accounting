package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.ObjectModel.Exceptions.NotEmptyException;

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

    public void removeBusinessObject(OrderItem orderItem) {
        remove(orderItem, false, true);
    }

    public void remove(OrderItem orderItem, boolean calculate, boolean removeIfEmpty){
        Article article = orderItem.getArticle();
        String articleName = article.getName();
        orderItem.setName(articleName);
        try {
            super.removeBusinessObject(orderItem);
        } catch (NotEmptyException e1) {
            int itemsToRemove = orderItem.getNumberOfItems();
            OrderItem itemInStock = getBusinessObject(articleName);
            if (itemInStock != null) {
                itemInStock.removeNumberOfItems(itemsToRemove);
                if(calculate){
                    itemInStock.calculateNumberOfUnits();
                } else {
                    int unitsToRemove = orderItem.getNumberOfUnits();
                    itemInStock.removeNumberOfUnits(unitsToRemove);
                }
                int numberOfItems = itemInStock.getNumberOfItems();
                if(removeIfEmpty&&numberOfItems==0){
                    int numberOfUnits = itemInStock.getNumberOfUnits();
                    if(numberOfUnits!=0){
                        System.err.println("Calculation error: items=0, units="+numberOfUnits);
                        itemInStock.setNumberOfUnits(0);
                    }
                    try {
                        // remove stock item
                        super.removeBusinessObject(itemInStock);
                    } catch (NotEmptyException e) {
                        // unreachable code
                        e.printStackTrace();
                    }
                }
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
