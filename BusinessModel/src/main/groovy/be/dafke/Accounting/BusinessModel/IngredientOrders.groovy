package be.dafke.Accounting.BusinessModel

import be.dafke.Accounting.ObjectModel.BusinessCollection
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.Utils.Utils

class IngredientOrders extends BusinessCollection<IngredientOrder> {

    int id = 0

    IngredientOrder addBusinessObject(IngredientOrder order) throws EmptyNameException, DuplicateNameException {
        id++
        if(order.id==null) {
            order.id = id
        }
        order.setName(Utils.toIDString("IO", order.id, 3))
        order.addIngredientOrderToArticles()
        super.addBusinessObject(order)
    }

    static IngredientOrder mergeOrders(ArrayList<IngredientOrder> ordersToAdd) {
        IngredientOrder ingredientOrder = new IngredientOrder()
        for (IngredientOrder orderToAdd:ordersToAdd) {
            ArrayList<IngredientOrderItem> orderItemsToAdd = orderToAdd.businessObjects
            for (IngredientOrderItem orderitemToAdd : orderItemsToAdd) {
                BigDecimal quantity = orderitemToAdd.getQuantity()
                Ingredient ingredient = orderitemToAdd.getIngredient()
                Article article = orderitemToAdd.article
                IngredientOrderItem newItem = new IngredientOrderItem(quantity, ingredient, article)
//                newItem.setArticle(article)
                try {
                    ingredientOrder.addBusinessObject(newItem)
                } catch (EmptyNameException | DuplicateNameException e) {
                    e.printStackTrace()
                }
            }
        }
        ingredientOrder
    }


    void removeBusinessObject(Order order){
        removeBusinessObject(order.getUniqueProperties())
    }
}
