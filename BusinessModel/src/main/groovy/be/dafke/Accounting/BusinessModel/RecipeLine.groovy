package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessObject

class RecipeLine extends BusinessObject {
    Ingredient ingredient
    BigDecimal amount
    String preparation
    String instructions

    RecipeLine(Ingredient ingredient) {
        this.ingredient = ingredient
        setName(ingredient.name)
    }

    Ingredient getIngredient() {
        ingredient
    }

    void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient
    }

    BigDecimal getAmount() {
        amount
    }

    void setAmount(BigDecimal amount) {
        this.amount = amount
    }

    String getPreparation() {
        preparation
    }

    void setPreparation(String preparation) {
        this.preparation = preparation
    }

    String getInstructions() {
        instructions
    }

    void setInstructions(String instructions) {
        this.instructions = instructions
    }
}
