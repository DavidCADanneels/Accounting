package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessObject

class MealOrderItem extends BusinessObject{
    int numberOfItems = 0
    Meal meal

    MealOrderItem(Integer numberOfItems, Meal meal) {
        setName(meal.name)
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