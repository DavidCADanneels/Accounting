package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

import java.math.BigDecimal;

public class DeliverooMeal extends BusinessObject{
    private String mealName = "";
    private String description = "";
    private BigDecimal salesPrice = null;

    public DeliverooMeal(String name){
        setName(name);
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public void setSalesPrice(BigDecimal salesPrice) {
        this.salesPrice = salesPrice;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getSalesPrice(){
        return salesPrice;
    }

    public String getMealName() {
        return mealName;
    }

    public String getDescription() {
        return description;
    }

    BigDecimal getPurchasePrice(int number){
        return salesPrice.multiply(new BigDecimal(number));
    }

}
