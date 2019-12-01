package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessCollection
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.Utils.Utils

class MealOrders extends BusinessCollection<MealOrder> {
    Account mealOrderBalanceAccount, mealOrderServiceAccount, mealOrderRevenueAccount
    Journal mealOrderSalesJournal, mealOrderServiceJournal

    int id = 0

    static MealOrder mergeOrders(ArrayList<MealOrder> ordersToAdd) {
        MealOrder mealOrder = new MealOrder()
        for(MealOrder orderToAdd: ordersToAdd){
            ArrayList<MealOrderItem> mealOrderItemsToAdd = orderToAdd.businessObjects
            for(MealOrderItem mealOrderItemToAdd : mealOrderItemsToAdd){
                int numberOfItems = mealOrderItemToAdd.numberOfItems
                Meal meal = mealOrderItemToAdd.getMeal()
                MealOrderItem newMealOrderItem = new MealOrderItem(numberOfItems, meal)
                mealOrder.addBusinessObject(newMealOrderItem )
            }
        }
        mealOrder
    }

    MealOrder addBusinessObject(MealOrder mealOrder) throws EmptyNameException, DuplicateNameException {
        id++
        if(mealOrder.id==null){
            mealOrder.id = id
        }
        mealOrder.setName(Utils.toIDString("DEL", mealOrder.id,3))
        super.addBusinessObject(mealOrder)
        mealOrder
    }

    Account getMealOrderBalanceAccount() {
        mealOrderBalanceAccount
    }

    void setMealOrderBalanceAccount(Account mealOrderBalanceAccount) {
        this.mealOrderBalanceAccount = mealOrderBalanceAccount
    }

    Account getMealOrderServiceAccount() {
        mealOrderServiceAccount
    }

    void setMealOrderServiceAccount(Account mealOrderServiceAccount) {
        this.mealOrderServiceAccount = mealOrderServiceAccount
    }

    Account getMealOrderRevenueAccount() {
        mealOrderRevenueAccount
    }

    void setMealOrderRevenueAccount(Account mealOrderRevenueAccount) {
        this.mealOrderRevenueAccount = mealOrderRevenueAccount
    }

    Journal getMealOrderSalesJournal() {
        mealOrderSalesJournal
    }

    void setMealOrderSalesJournal(Journal mealOrderSalesJournal) {
        this.mealOrderSalesJournal = mealOrderSalesJournal
    }

    int nrOfMeals(Meal meal){
        int result = 0
        ArrayList<MealOrder> mealOrders = getBusinessObjects()
        for (MealOrder mealOrder:mealOrders) {
            result+=mealOrder.nrOfMeals(meal)
        }
        result
    }

    Journal getMealOrderServiceJournal() {
        mealOrderServiceJournal
    }

    void setMealOrderServiceJournal(Journal mealOrderServiceJournal) {
        this.mealOrderServiceJournal = mealOrderServiceJournal
    }
}
