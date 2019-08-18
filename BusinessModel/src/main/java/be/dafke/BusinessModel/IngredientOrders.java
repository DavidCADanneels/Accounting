package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.Utils.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;

public class IngredientOrders extends BusinessCollection<IngredientOrder>{

    private int id = 0;

    public IngredientOrder addBusinessObject(IngredientOrder order) throws EmptyNameException, DuplicateNameException {
        id++;
        if(order.getId()==null) {
            order.setId(id);
        }
        order.setName(Utils.toIDString("IO", order.getId(), 3));
        order.addIngredientOrderToArticles();
        return super.addBusinessObject(order);
    }

    public static IngredientOrder mergeOrders(ArrayList<IngredientOrder> ordersToAdd) {
        IngredientOrder ingredientOrder = new IngredientOrder();
        for (IngredientOrder orderToAdd:ordersToAdd) {
            ArrayList<IngredientOrderItem> orderItemsToAdd = orderToAdd.getBusinessObjects();
            for (IngredientOrderItem orderitemToAdd : orderItemsToAdd) {
                BigDecimal quantity = orderitemToAdd.getQuantity();
                Ingredient ingredient = orderitemToAdd.getIngredient();
                Article article = orderitemToAdd.getArticle();
                IngredientOrderItem newItem = new IngredientOrderItem(quantity, ingredient, article);
//                newItem.setArticle(article);
                try {
                    ingredientOrder.addBusinessObject(newItem);
                } catch (EmptyNameException | DuplicateNameException e) {
                    e.printStackTrace();
                }
            }
        }
        return ingredientOrder;
    }


    public void removeBusinessObject(Order order){
        removeBusinessObject(order.getUniqueProperties());
    }
}
