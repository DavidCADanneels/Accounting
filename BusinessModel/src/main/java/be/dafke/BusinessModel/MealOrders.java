package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.Utils.Utils;

import java.util.ArrayList;

public class MealOrders extends BusinessCollection<MealOrder>{
    private Account mealOrderBalanceAccount, mealOrderServiceAccount, mealOrderRevenueAccount;
    private Journal mealOrderSalesJournal, mealOrderServiceJournal;

    private int id = 0;

    public static MealOrder mergeOrders(ArrayList<MealOrder> ordersToAdd) {
        MealOrder mealOrder = new MealOrder();
        for(MealOrder orderToAdd: ordersToAdd){
            ArrayList<MealOrderItem> mealOrderItemsToAdd = orderToAdd.getBusinessObjects();
            for(MealOrderItem mealOrderItemToAdd : mealOrderItemsToAdd){
                int numberOfItems = mealOrderItemToAdd.getNumberOfItems();
                Meal meal = mealOrderItemToAdd.getMeal();
                MealOrderItem newMealOrderItem = new MealOrderItem(numberOfItems, meal);
                mealOrder.addBusinessObject(newMealOrderItem );
            }
        }
        return mealOrder;
    }

    public MealOrder addBusinessObject(MealOrder mealOrder) throws EmptyNameException, DuplicateNameException {
        id++;
        if(mealOrder.getId()==null){
            mealOrder.setId(id);
        }
        mealOrder.setName(Utils.toIDString("DEL", mealOrder.getId(),3));
        super.addBusinessObject(mealOrder);
        return mealOrder;
    }

    public Account getMealOrderBalanceAccount() {
        return mealOrderBalanceAccount;
    }

    public void setMealOrderBalanceAccount(Account mealOrderBalanceAccount) {
        this.mealOrderBalanceAccount = mealOrderBalanceAccount;
    }

    public Account getMealOrderServiceAccount() {
        return mealOrderServiceAccount;
    }

    public void setMealOrderServiceAccount(Account mealOrderServiceAccount) {
        this.mealOrderServiceAccount = mealOrderServiceAccount;
    }

    public Account getMealOrderRevenueAccount() {
        return mealOrderRevenueAccount;
    }

    public void setMealOrderRevenueAccount(Account mealOrderRevenueAccount) {
        this.mealOrderRevenueAccount = mealOrderRevenueAccount;
    }

    public Journal getMealOrderSalesJournal() {
        return mealOrderSalesJournal;
    }

    public void setMealOrderSalesJournal(Journal mealOrderSalesJournal) {
        this.mealOrderSalesJournal = mealOrderSalesJournal;
    }

    public int nrOfMeals(Meal meal){
        int result = 0;
        ArrayList<MealOrder> mealOrders = getBusinessObjects();
        for (MealOrder mealOrder:mealOrders) {
            result+=mealOrder.nrOfMeals(meal);
        }
        return result;
    }

    public Journal getMealOrderServiceJournal() {
        return mealOrderServiceJournal;
    }

    public void setMealOrderServiceJournal(Journal mealOrderServiceJournal) {
        this.mealOrderServiceJournal = mealOrderServiceJournal;
    }
}
