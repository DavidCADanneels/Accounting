package be.dafke.BusinessModel;

public class Stock extends OrderItems {

    // Add per Unit
    public OrderItem addBusinessObject(OrderItem orderItem){
        int unitsToAdd = orderItem.getNumberOfUnits();
        int itemsToAdd = orderItem.getNumberOfItems();
        return add(orderItem, unitsToAdd, itemsToAdd);
    }

    // Remove per Item
    public void removeBusinessObject(OrderItem orderItem){
        int unitsToRemove = orderItem.getNumberOfUnits();
        int itemsToRemove = orderItem.getNumberOfItems();
        remove(orderItem, unitsToRemove, itemsToRemove);
    }

    public void addLoad(OrderItems load){
        load.getBusinessObjects().forEach(this::addBusinessObject);
    }

    public void removeLoad(OrderItems load){
        load.getBusinessObjects().forEach(this::removeBusinessObject);
    }
}
