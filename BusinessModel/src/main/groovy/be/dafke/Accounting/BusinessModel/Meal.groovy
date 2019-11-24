package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessObject

class Meal extends BusinessObject{
    private String mealName = ""
    private String description = ""
    private BigDecimal salesPrice = null
    private Recipe recipe = new Recipe()

    Meal(Meal meal){
        this(meal.getName())
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
//        recipe.getIngredients()
//    }

    Ingredients getIngredients() {
        recipe.getIngredients()
    }
}
