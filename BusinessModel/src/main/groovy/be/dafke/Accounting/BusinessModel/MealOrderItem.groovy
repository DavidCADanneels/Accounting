package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessObject

class MealOrderItem extends BusinessObject{
    private int numberOfItems = 0
    private Meal meal

    MealOrderItem(Integer numberOfItems, Meal meal) {
        setName(meal.getName())
        this.numberOfItems = numberOfItems
        this.meal = meal
    }

    int getNumberOfItems() {
        numberOfItems
    }

    void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems
    }

    void addNumberOfItems(int numberOfItems) {
        this.numberOfItems += numberOfItems
    }

    void removeNumberOfItems(int numberOfItems) {
        this.numberOfItems -= numberOfItems
    }

    Meal getMeal() {
        meal
    }

    boolean isDeletable() {
        numberOfItems==0
    }
}