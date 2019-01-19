package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.Utils.Utils;

public class MealOrders extends BusinessCollection<MealOrder>{
    private Account deliverooBalanceAccount, deliverooServiceAccount, deliverooRevenueAccount;
    private Journal deliverooSalesJournal, deliverooServiceJournal;

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

    public Account getDeliverooBalanceAccount() {
        return deliverooBalanceAccount;
    }

    public void setDeliverooBalanceAccount(Account deliverooBalanceAccount) {
        this.deliverooBalanceAccount = deliverooBalanceAccount;
    }

    public Account getDeliverooServiceAccount() {
        return deliverooServiceAccount;
    }

    public void setDeliverooServiceAccount(Account deliverooServiceAccount) {
        this.deliverooServiceAccount = deliverooServiceAccount;
    }

    public Account getDeliverooRevenueAccount() {
        return deliverooRevenueAccount;
    }

    public void setDeliverooRevenueAccount(Account deliverooRevenueAccount) {
        this.deliverooRevenueAccount = deliverooRevenueAccount;
    }

    public Journal getDeliverooSalesJournal() {
        return deliverooSalesJournal;
    }

    public void setDeliverooSalesJournal(Journal deliverooSalesJournal) {
        this.deliverooSalesJournal = deliverooSalesJournal;
    }

    public Journal getDeliverooServiceJournal() {
        return deliverooServiceJournal;
    }

    public void setDeliverooServiceJournal(Journal deliverooServiceJournal) {
        this.deliverooServiceJournal = deliverooServiceJournal;
    }
}
