package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.ObjectModel.Exceptions.NotEmptyException;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;

public class MealOrder extends BusinessCollection<MealOrderItem>{
    private static int nr=0;
    private Calendar date;
    private String description;

    public MealOrder() {
        nr++;
        setName("DEL"+nr);
    }

    public MealOrder(String name){
        nr++;
        setName(name);
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getTotalPrice(){
        BigDecimal total = BigDecimal.ZERO.setScale(2);
        for(MealOrderItem mealOrderItem:getBusinessObjects()){
            DeliverooMeal deliverooMeal = mealOrderItem.getDeliverooMeal();
            int numberOfItems = mealOrderItem.getNumberOfItems();
            BigDecimal salesPrice = deliverooMeal.getSalesPrice().multiply(new BigDecimal(numberOfItems));
            total = total.add(salesPrice);
        }
        return total;
    }

    public MealOrderItem addBusinessObject(MealOrderItem orderItem) {
        try {
            return super.addBusinessObject(orderItem);
        } catch (EmptyNameException e) {
            // Cannot occur since we set the name above
            e.printStackTrace();
            return null;
        } catch (DuplicateNameException e) {
            int itemsToAdd = orderItem.getNumberOfItems();
            MealOrderItem itemInStock = getBusinessObject(orderItem.getName());
            if(itemInStock==null){
                // cannot be null since DuplicateNameException
                return null;
            }
            itemInStock.addNumberOfItems(itemsToAdd);
            setOrderItem(itemInStock);
            return itemInStock;
        }
    }

    public void setMeals(DeliverooMeals deliverooMeals){
        deliverooMeals.getBusinessObjects().forEach(deliverooMeal -> {
            addBusinessObject(new MealOrderItem(0, deliverooMeal));
        });
    }

    public void removeEmptyOrderItems() {
        getBusinessObjects().forEach(mealOrderItem -> {
            int numberOfItems = mealOrderItem.getNumberOfItems();
            if (numberOfItems==0) {
                remove(mealOrderItem,true);
            }
        });
    }

    public void removeBusinessObject(MealOrderItem orderItem) {
        remove(orderItem,  true);
    }

    public void remove(MealOrderItem orderItem, boolean removeIfEmpty){
        try {
            super.removeBusinessObject(orderItem);
        } catch (NotEmptyException e1) {
            int itemsToRemove = orderItem.getNumberOfItems();
            MealOrderItem mealsInOrderItem = getBusinessObject(orderItem.getName());
            if (mealsInOrderItem != null) {
                mealsInOrderItem.removeNumberOfItems(itemsToRemove);
                int numberOfItems = mealsInOrderItem.getNumberOfItems();
                if(removeIfEmpty&&numberOfItems==0){
                    try {
                        super.removeBusinessObject(mealsInOrderItem);
                    } catch (NotEmptyException e) {
                        e.printStackTrace();
                    }
                }
                setOrderItem(mealsInOrderItem);
            }
        }
    }

    public void setOrderItem(MealOrderItem orderItem){
        TreeMap<String, String> keyMap = orderItem.getUniqueProperties();
        for (Map.Entry<String, String> entry : keyMap.entrySet()) {
            // This will not throw any exceptions: we already handled them above.
            addBusinessObject(orderItem, entry);
        }
    }
}
