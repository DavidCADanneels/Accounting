package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

import java.math.BigDecimal;
import java.util.function.Predicate;

public class MealOrderItem extends BusinessObject{
    private int numberOfItems = 0;
    private DeliverooMeal deliverooMeal;

    public MealOrderItem(Integer numberOfItems, DeliverooMeal deliverooMeal) {
        setName(deliverooMeal.getName());
        this.numberOfItems = numberOfItems;
        this.deliverooMeal = deliverooMeal;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public void addNumberOfItems(int numberOfItems) {
        this.numberOfItems += numberOfItems;
    }

    public void removeNumberOfItems(int numberOfItems) {
        this.numberOfItems -= numberOfItems;
    }

    public DeliverooMeal getDeliverooMeal() {
        return deliverooMeal;
    }


}
