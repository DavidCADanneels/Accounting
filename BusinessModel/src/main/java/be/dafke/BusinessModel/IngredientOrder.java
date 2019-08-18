package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.ObjectModel.Exceptions.NotEmptyException;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

public class IngredientOrder extends BusinessCollection<IngredientOrderItem> {

    private Integer id;

    public void addIngredientOrderToArticles() {
        getBusinessObjects().forEach(orderItem -> {
            Article article = orderItem.getArticle();
            if(article==null){
                System.err.println("article == null");
            } else {
                article.addIngredientOrder(this);
            }
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

    public void removeEmptyOrderItems() {
        getBusinessObjects().forEach(ingredientOrderItem -> {
            BigDecimal quantity = ingredientOrderItem.getQuantity();
            if (BigDecimal.ZERO.compareTo(quantity)==0) {
                remove(ingredientOrderItem,  true);
            }
        });
    }

    public void remove(IngredientOrderItem orderItem, boolean removeIfEmpty){
//        Article article = orderItem.getArticle();
//        String articleName = article.getName();
//        orderItem.setName(articleName);
        Ingredient ingredient = orderItem.getIngredient();
        orderItem.setName(ingredient.getName());
        try {
            super.removeBusinessObject(orderItem);
        } catch (NotEmptyException e1) {
            BigDecimal quantityToRemove = orderItem.getQuantity();
            IngredientOrderItem itemInStock = getBusinessObject(ingredient.getName());
            if (itemInStock != null) {
                itemInStock.removeQuantity(quantityToRemove);
                BigDecimal quantityLeft = itemInStock.getQuantity();
                if(removeIfEmpty && BigDecimal.ZERO.compareTo(quantityLeft)==0){
                    try {
                        // remove stock item
                        super.removeBusinessObject(itemInStock);
                    } catch (NotEmptyException e) {
                        // unreachable code
                        e.printStackTrace();
                    }
                }
                setOrderItem(itemInStock);
            }
        }
    }

    public void setOrderItem(IngredientOrderItem orderItem){
        TreeMap<String, String> keyMap = orderItem.getUniqueProperties();
        for (Map.Entry<String, String> entry : keyMap.entrySet()) {
            // This will not throw any exceptions: we already handled them above.
            addBusinessObject(orderItem, entry);
        }
    }
}
