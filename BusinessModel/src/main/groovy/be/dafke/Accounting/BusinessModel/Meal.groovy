package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessObject

class Meal extends BusinessObject{
    String mealName = ""
    String description = ""
    BigDecimal salesPrice = null
    Recipe recipe = new Recipe()

    Meal(Meal meal){
        this(meal.name)
        mealName = meal.mealName
        description = meal.description
        salesPrice = meal.salesPrice
    }
    Meal(String name){
        setName(name)
    }

    void setMealName(String mealName) {
        this.mealName = mealName
    }

    void setSalesPrice(BigDecimal salesPrice) {
        this.salesPrice = salesPrice
    }

    void setDescription(String description) {
        this.description = description
    }

    void setRecipe(Recipe recipe) {
        this.recipe = recipe
    }

    BigDecimal getSalesPrice(){
        salesPrice
    }

    String getMealName() {
        mealName
    }

    String getDescription() {
        description
    }

    Recipe getRecipe() {
        recipe
    }

//    List<Ingredient> getIngredients() {
//        recipe.ingredients
//    }

    Ingredients getIngredients() {
        recipe.ingredients
    }
}
