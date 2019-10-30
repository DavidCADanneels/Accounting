package be.dafke.BusinessModelDao;

import be.dafke.BusinessModel.*;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

import static be.dafke.BusinessModelDao.XMLConstants.*;
import static be.dafke.BusinessModelDao.XMLReader.*;
import static be.dafke.BusinessModelDao.XMLWriter.getXmlHeader;
import static be.dafke.Utils.Utils.parseBigDecimal;

public class MealsIO {
    public static void readMeals(Accounting accounting){
        Meals meals = accounting.getMeals();
        Ingredients ingredients = accounting.getIngredients();
        File xmlFile = new File(ACCOUNTINGS_XML_FOLDER +accounting.getName()+"/"+MEALS + XML_EXTENSION);
        Element rootElement = getRootElement(xmlFile, MEALS);
        for (Element mealElement : getChildren(rootElement, MEAL)) {

            String mealNr = getValue(mealElement, MEAL_NR);
            Meal meal = new Meal(mealNr);

            String mealName = getValue(mealElement, MEAL_NAME);
            if(mealName!=null)
                meal.setMealName(mealName);

            String salesPrice = getValue(mealElement, PRICE);
            if(salesPrice!=null)
                meal.setSalesPrice(parseBigDecimal(salesPrice));

            String description = getValue(mealElement, DESCRIPTION);
            if(description!=null)
                meal.setDescription(description);

            for (Element recipeElement : getChildren(mealElement, MEAL_RECIPE_LINE)) {
                String ingredientName = getValue(recipeElement, NAME);
                Ingredient ingredient = ingredients.getBusinessObject(ingredientName);

                RecipeLine recipeLine = new RecipeLine(ingredient);

                String amountString = getValue(mealElement, AMOUNT);
                if(amountString!=null){
                    BigDecimal amount = new BigDecimal(amountString);
                    recipeLine.setAmount(amount);
                }

                String preparation = getValue(mealElement, MEAL_RECIPE_PREPARATION);
                String instructions = getValue(mealElement, MEAL_RECIPE_INSTRUCTIONS);
                recipeLine.setPreparation(preparation);
                recipeLine.setInstructions(instructions);

                Recipe recipe = meal.getRecipe();
                try {
                    recipe.addBusinessObject(recipeLine);
                } catch (EmptyNameException | DuplicateNameException e) {
                    e.printStackTrace();
                }
            }

            try {
                meals.addBusinessObject(meal);
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }
    }

    public static String calculatePdfPath(Accounting accounting){
        return ACCOUNTINGS_XML_FOLDER + accounting.getName() + "/MealsWithAllergenes" + PDF_EXTENSION;
    }

    public static void writeMeals(Accounting accounting) {
        writeMeals(accounting, false);
    }

    public static String writeMeals(Accounting accounting, boolean details) {
        Meals meals = accounting.getMeals();
        String filename = details?"MealDetails":MEALS;
        String path = ACCOUNTINGS_XML_FOLDER + accounting.getName() + "/" + filename + XML_EXTENSION;
        File file = new File(path);
        try {
            Writer writer = new FileWriter(file);
            writer.write(getXmlHeader(MEALS, 2));
            for (Meal meal : meals.getBusinessObjects()) {
                writer.write(
                        "  <" + MEAL + ">\n" +
                                "    <" + MEAL_NR + ">" + meal.getName() + "</" + MEAL_NR + ">\n" +
                                "    <" + MEAL_NAME + ">" + meal.getMealName() + "</" + MEAL_NAME + ">\n" +
                                "    <" + PRICE + ">" + meal.getSalesPrice() + "</" + PRICE + ">\n" +
                                "    <" + DESCRIPTION + ">" + meal.getDescription() + "</" + DESCRIPTION + ">\n" +
                                "    <" + MEAL_RECIPE + ">\n"
                );
                Recipe recipe = meal.getRecipe();
                for(RecipeLine line:recipe.getBusinessObjects()){
                    Ingredient ingredient = line.getIngredient();
                    writer.write(
                           "      <" + MEAL_RECIPE_LINE + ">\n" +
                               "        <" + NAME + ">" + ingredient.getName() + "</" + NAME + ">\n" +
//                            "      <" + AMOUNT + ">" + line.getInstructions() + "</" + AMOUNT + ">\n" +
//                            "      <" + AMOUNT + ">" + line.getPreparation() + "</" + AMOUNT + ">\n" +
                               "        <" + AMOUNT + ">" + line.getAmount() + "</" + AMOUNT + ">\n"
                    );
                    if(details){
                        Allergenes allergenes = ingredient.getAllergenes();
                        for (Allergene allergene:allergenes.getBusinessObjects()) {
                            writer.write(
                           "        <" + ALLERGENE + ">\n" +
                               "          <" + ID + ">" + allergene.getName() + "</" + ID + ">\n" +
                               "          <" + NAME + ">" + allergene.getShortName() + "</" + NAME + ">\n" +
                               "          <" + DESCRIPTION + ">" + allergene.getDescription() + "</" + DESCRIPTION + ">\n" +
                               "        </" + ALLERGENE + ">\n"
                            );
                        }
                    }
                    writer.write("      </" + MEAL_RECIPE_LINE + ">\n");
                }
                writer.write(
                            "    </" + MEAL_RECIPE + ">\n" +
                                "  </" + MEAL + ">\n"

                );
            }
            writer.write("</" + MEALS + ">\n");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Meal.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return path;
        }
    }
}
