package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessObject

class RecipeLine extends BusinessObject {
    private Ingredient ingredient
    private BigDecimal amount
    private String preparation
    private String instructions

    RecipeLine(Ingredient ingredient) {
        this.ingredient = ingredient
        setName(ingredient.getName())
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
