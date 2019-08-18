package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessObject;

import java.math.BigDecimal;
import java.util.function.Predicate;

import static java.math.BigDecimal.ROUND_HALF_DOWN;

public class IngredientOrderItem extends BusinessObject{
    private Article article;
    private Ingredient ingredient;
    private BigDecimal quantity;

    public IngredientOrderItem(BigDecimal quantity, Ingredient ingredient, Article article) {
        setName(ingredient.getName());
        this.quantity = quantity;
        this.ingredient = ingredient;
        this.article = article;
    }

    public boolean isDeletable() {
        return BigDecimal.ZERO.compareTo(quantity)==0;
    }

    public static Predicate<IngredientOrderItem> containsArticle(Article article){
        return orderItem -> orderItem.getArticle() == article;
    }

    public static Predicate<IngredientOrderItem> containsIngredient(Ingredient ingredient){
        return orderItem -> orderItem.getIngredient() == ingredient;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public Article getArticle() {
        return article;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public void removeQuantity(BigDecimal quantityToRemove) {
        quantity = quantity.subtract(quantityToRemove);
    }
}
