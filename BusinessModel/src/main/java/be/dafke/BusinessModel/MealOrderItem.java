package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

public class MealOrderItem extends BusinessObject{
    private int numberOfItems = 0;
    private Meal meal;

    public MealOrderItem(Integer numberOfItems, Meal meal) {
        setName(meal.getName());
        this.numberOfItems = numberOfItems;
        this.meal = meal;
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

    public Meal getMeal() {
        return meal;
    }

    public boolean isDeletable() {
        return numberOfItems==0;
    }
}
