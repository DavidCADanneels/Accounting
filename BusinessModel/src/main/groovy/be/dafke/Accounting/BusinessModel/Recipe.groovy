package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessCollection
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException

class Recipe extends BusinessCollection<RecipeLine> {

    String preparation
    String instructions

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

    String getPreparation() {
        return preparation
    }

    void setPreparation(String preparation) {
        this.preparation = preparation
    }

    String getInstructions() {
        return instructions
    }

    void setInstructions(String instructions) {
        this.instructions = instructions
    }
}