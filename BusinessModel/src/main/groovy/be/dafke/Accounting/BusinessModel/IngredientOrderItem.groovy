package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessObject

import java.util.function.Predicate

class IngredientOrderItem extends BusinessObject{
    private Article article
    private Ingredient ingredient
    private BigDecimal quantity

    IngredientOrderItem(BigDecimal quantity, Ingredient ingredient, Article article) {
        setName(ingredient.getName())
        this.quantity = quantity
        this.ingredient = ingredient
        this.article = article
    }

    boolean isDeletable() {
        BigDecimal.ZERO.compareTo(quantity)==0
    }

    static Predicate<IngredientOrderItem> containsArticle(Article article){
        { orderItem -> orderItem.getArticle() == article }
    }

    static Predicate<IngredientOrderItem> containsIngredient(Ingredient ingredient){
        { orderItem -> orderItem.getIngredient() == ingredient }
    }

    Ingredient getIngredient() {
        ingredient
    }

    BigDecimal getQuantity() {
        quantity
    }

    Article getArticle() {
        article
    }

    void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient
    }

    void setQuantity(BigDecimal quantity) {
        this.quantity = quantity
    }

    void setArticle(Article article) {
        this.article = article
    }

    void removeQuantity(BigDecimal quantityToRemove) {
        quantity = quantity.subtract(quantityToRemove)
    }
}
