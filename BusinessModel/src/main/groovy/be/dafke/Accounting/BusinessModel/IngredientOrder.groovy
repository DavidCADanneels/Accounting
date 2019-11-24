package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessCollection
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.Accounting.ObjectModel.Exceptions.NotEmptyException

class IngredientOrder extends BusinessCollection<IngredientOrderItem> {

    private Integer id

    void addIngredientOrderToArticles() {
        getBusinessObjects().forEach({ orderItem ->
            Article article = orderItem.getArticle()
            if (article == null) {
                System.err.println("article == null")
            } else {
                article.addIngredientOrder(this)
            }
        })
    }

    Integer getId() {
        return id
    }

    void setId(Integer id) {
        this.id = id
    }

    void setIngredients(Ingredients ingredients){
        ingredients.getBusinessObjects().forEach({ ingredient ->
            try {
                addBusinessObject(new IngredientOrderItem(BigDecimal.ZERO, ingredient, null))
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace()
            }
        })
    }

    void removeEmptyOrderItems() {
        getBusinessObjects().forEach({ ingredientOrderItem ->
            BigDecimal quantity = ingredientOrderItem.getQuantity()
            if (BigDecimal.ZERO.compareTo(quantity) == 0) {
                remove(ingredientOrderItem, true)
            }
        })
    }

    void remove(IngredientOrderItem orderItem, boolean removeIfEmpty){
        Ingredient ingredient = orderItem.getIngredient()
        orderItem.setName(ingredient.getName())
        try {
            super.removeBusinessObject(orderItem)
        } catch (NotEmptyException e1) {
            BigDecimal quantityToRemove = orderItem.getQuantity()
            IngredientOrderItem itemInStock = getBusinessObject(ingredient.getName())
            if (itemInStock != null) {
                itemInStock.removeQuantity(quantityToRemove)
                BigDecimal quantityLeft = itemInStock.getQuantity()
                if(removeIfEmpty && BigDecimal.ZERO.compareTo(quantityLeft)==0){
                    try {
                        // remove stock item
                        super.removeBusinessObject(itemInStock)
                    } catch (NotEmptyException e) {
                        // unreachable code
                        e.printStackTrace()
                    }
                }
                setOrderItem(itemInStock)
            }
        }
    }

    void setOrderItem(IngredientOrderItem orderItem){
        TreeMap<String, String> keyMap = orderItem.getUniqueProperties()
        for (Map.Entry<String, String> entry : keyMap.entrySet()) {
            // This will not throw any exceptions: we already handled them above.
            addBusinessObject(orderItem, entry)
        }
    }
}
