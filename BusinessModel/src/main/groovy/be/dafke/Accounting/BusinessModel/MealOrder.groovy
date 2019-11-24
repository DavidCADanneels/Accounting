package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessCollection
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.Accounting.ObjectModel.Exceptions.NotEmptyException

class MealOrder extends BusinessCollection<MealOrderItem> {
    private Calendar date
    private String description
    private Integer id

    Integer getId() {
        id
    }

    void setId(Integer id) {
        this.id = id
    }

    Calendar getDate() {
        date
    }

    void setDate(Calendar date) {
        this.date = date
    }

    String getDescription() {
        description
    }

    void setDescription(String description) {
        this.description = description
    }

    BigDecimal getTotalPrice(){
        BigDecimal total = BigDecimal.ZERO.setScale(2)
        for(MealOrderItem mealOrderItem:getBusinessObjects()){
            Meal meal = mealOrderItem.getMeal()
            int numberOfItems = mealOrderItem.getNumberOfItems()
            BigDecimal salesPrice = meal.getSalesPrice().multiply(new BigDecimal(numberOfItems))
            total = total.add(salesPrice)
        }
        total
    }

    int nrOfMeals(Meal meal){
        int result = 0
        List<MealOrderItem> mealOrderItems = getBusinessObjects({ mealOrderItem -> mealOrderItem.getMeal() == meal })
        for (MealOrderItem mealOrderItem:mealOrderItems) {
            result += mealOrderItem.getNumberOfItems()
        }
        result
    }

    MealOrderItem addBusinessObject(MealOrderItem orderItem) {
        try {
            super.addBusinessObject(orderItem)
        } catch (EmptyNameException e) {
            // Cannot occur since we set the name above
            e.printStackTrace()
            null
        } catch (DuplicateNameException e) {
            int itemsToAdd = orderItem.getNumberOfItems()
            MealOrderItem itemInStock = getBusinessObject(orderItem.getName())
            if(itemInStock==null){
                // cannot be null since DuplicateNameException
                null
            }
            itemInStock.addNumberOfItems(itemsToAdd)
            setOrderItem(itemInStock)
            itemInStock
        }
    }

    void setMeals(Meals meals){
        meals.getBusinessObjects().forEach({ meal ->
            addBusinessObject(new MealOrderItem(0, meal))
        })
    }

    void removeEmptyOrderItems() {
        getBusinessObjects().forEach({ mealOrderItem ->
            int numberOfItems = mealOrderItem.getNumberOfItems()
            if (numberOfItems == 0) {
                remove(mealOrderItem, true)
            }
        })
    }

    void removeBusinessObject(MealOrderItem orderItem) {
        remove(orderItem,  true)
    }

    void remove(MealOrderItem orderItem, boolean removeIfEmpty){
        try {
            super.removeBusinessObject(orderItem)
        } catch (NotEmptyException e1) {
            int itemsToRemove = orderItem.getNumberOfItems()
            MealOrderItem mealsInOrderItem = getBusinessObject(orderItem.getName())
            if (mealsInOrderItem != null) {
                mealsInOrderItem.removeNumberOfItems(itemsToRemove)
                int numberOfItems = mealsInOrderItem.getNumberOfItems()
                if(removeIfEmpty&&numberOfItems==0){
                    try {
                        super.removeBusinessObject(mealsInOrderItem)
                    } catch (NotEmptyException e) {
                        e.printStackTrace()
                    }
                }
                setOrderItem(mealsInOrderItem)
            }
        }
    }

    void setOrderItem(MealOrderItem orderItem){
        TreeMap<String, String> keyMap = orderItem.getUniqueProperties()
        for (Map.Entry<String, String> entry : keyMap.entrySet()) {
            // This will not throw any exceptions: we already handled them above.
            addBusinessObject(orderItem, entry)
        }
    }
}
