package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

public class MealOrders extends BusinessCollection<MealOrder>{

    public MealOrders() {
        super();
    }

    public MealOrder addBusinessObject(MealOrder mealOrder) throws EmptyNameException, DuplicateNameException {
        super.addBusinessObject(mealOrder);
        mealOrder.getBusinessObjects().forEach(mealOrderItem -> {
            DeliverooMeal deliverooMeal = mealOrderItem.getDeliverooMeal();
            deliverooMeal.addUsage(mealOrderItem.getNumberOfItems());
        });
        return mealOrder;
    }
}
