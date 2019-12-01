package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessCollection
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.Accounting.ObjectModel.Exceptions.NotEmptyException

class OrderItems extends BusinessCollection<OrderItem> {

    OrderItem addBusinessObject(OrderItem orderItem) {
        String orderItemName = orderItem.name
        try {
            super.addBusinessObject(orderItem)
        } catch (EmptyNameException e) {
            // Cannot occur since we set the name above
            e.printStackTrace()
            null
        } catch (DuplicateNameException e) {
            int itemsToAdd = orderItem.numberOfItems
            OrderItem itemInStock = getBusinessObject(orderItemName)
            if(itemInStock==null){
                // cannot be null since DuplicateNameException
                null
            }
            itemInStock.addNumberOfItems(itemsToAdd)
            setOrderItem(itemInStock)
            itemInStock
        }
    }

    void removeBusinessObject(OrderItem orderItem) {
        remove(orderItem, false, true)
    }

    void remove(OrderItem orderItem, boolean calculate, boolean removeIfEmpty){
        Article article = orderItem.article
        String articleName = article.name
        orderItem.setName(articleName)
        try {
            super.removeBusinessObject(orderItem)
        } catch (NotEmptyException e1) {
            int itemsToRemove = orderItem.numberOfItems
            OrderItem itemInStock = getBusinessObject(articleName)
            if (itemInStock != null) {
                itemInStock.removeNumberOfItems(itemsToRemove)
                int numberOfItems = itemInStock.numberOfItems
                if(removeIfEmpty && numberOfItems==0){
                    try {
                        // remove stock item
                        super.removeBusinessObject(itemInStock)
                    } catch (NotEmptyException e) {
                        // unreachable code
                        e.printStackTrace()
                    }
                }
                setOrderItem(itemInStock)
            }
        }
    }

    void setOrderItem(OrderItem orderItem){
        TreeMap<String, String> keyMap = orderItem.getUniqueProperties()
        for (Map.Entry<String, String> entry : keyMap.entrySet()) {
            // This will not throw any exceptions: we already handled them above.
            addBusinessObject(orderItem, entry)
        }
    }
}
