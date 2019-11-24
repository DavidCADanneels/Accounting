package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessCollection
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException

class Recipe extends BusinessCollection<RecipeLine> {

    Ingredients getIngredients() {
        Ingredients ingredients = new Ingredients()
        ArrayList<RecipeLine> recipeLines = getBusinessObjects()
        for (RecipeLine line : recipeLines) {
            Ingredient ingredient = line.getIngredient()
            try {
                ingredients.addBusinessObject(ingredient)
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace()
            }
        }
        ingredients
    }
}