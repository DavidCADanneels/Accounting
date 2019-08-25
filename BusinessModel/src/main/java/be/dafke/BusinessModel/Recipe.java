package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;

import java.util.List;
import java.util.stream.Collectors;

public class Recipe extends BusinessCollection<RecipeLine> {

    public List<Ingredient> getIngredients(){
        return getBusinessObjects().stream().map(RecipeLine::getIngredient).collect(Collectors.toList());
    }
}
