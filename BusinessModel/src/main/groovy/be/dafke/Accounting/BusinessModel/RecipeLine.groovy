package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessObject

class RecipeLine extends BusinessObject {
    Ingredient ingredient
    BigDecimal amount

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
}
