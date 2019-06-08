package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.Utils.Utils;

public class MealOrders extends BusinessCollection<MealOrder>{
    private Account mealOrderBalanceAccount, mealOrderServiceAccount, mealOrderRevenueAccount;
    private Journal mealOrderSalesJournal, mealOrderServiceJournal;

    private int id = 0;

    public MealOrder addBusinessObject(MealOrder mealOrder) throws EmptyNameException, DuplicateNameException {
        id++;
        if(mealOrder.getId()==null){
            mealOrder.setId(id);
        }
        mealOrder.setName(Utils.toIDString("DEL", mealOrder.getId(),3));
        super.addBusinessObject(mealOrder);
        mealOrder.addUsage();
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

    public Journal getMealOrderServiceJournal() {
        return mealOrderServiceJournal;
    }

    public void setMealOrderServiceJournal(Journal mealOrderServiceJournal) {
        this.mealOrderServiceJournal = mealOrderServiceJournal;
    }
}
