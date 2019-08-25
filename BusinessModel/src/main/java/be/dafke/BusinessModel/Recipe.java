package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Recipe extends BusinessCollection<RecipeLine> {

    public Ingredients getIngredients(){
        Ingredients ingredients = new Ingredients();
        ArrayList<RecipeLine> recipeLines = getBusinessObjects();
        for(RecipeLine line:recipeLines){
            Ingredient ingredient = line.getIngredient();
            try {
                ingredients.addBusinessObject(ingredient);
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }
        return ingredients;
    }
}
