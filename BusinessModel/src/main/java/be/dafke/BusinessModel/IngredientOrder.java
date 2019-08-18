package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import java.math.BigDecimal;

public class IngredientOrder extends BusinessCollection<IngredientOrderItem> {

    private Integer id;

    public void addIngredientOrderToArticles() {
        getBusinessObjects().forEach(orderItem -> {
            Article article = orderItem.getArticle();
            article.addIngredientOrder(this);
        });
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



    public void setIngredients(Ingredients ingredients){
        ingredients.getBusinessObjects().forEach( ingredient -> {
            try {
                addBusinessObject(new IngredientOrderItem(BigDecimal.ZERO,ingredient));
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        });
    }
}
