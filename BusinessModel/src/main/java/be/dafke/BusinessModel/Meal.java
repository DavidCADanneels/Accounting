package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public class Meal extends BusinessObject{
    private String mealName = "";
    private String description = "";
    private BigDecimal salesPrice = null;
    private Recipe recipe = new Recipe();

    public Meal(Meal meal){
        this(meal.getName());
        mealName = meal.mealName;
        description = meal.description;
        salesPrice = meal.salesPrice;
    }
    public Meal(String name){
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

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
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

    public Recipe getRecipe() {
        return recipe;
    }

    public List<Ingredient> getIngredients() {
        return recipe.getIngredients();
    }
}
