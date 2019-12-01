package be.dafke.Accounting.BusinessModelDao

import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import org.w3c.dom.Element

import java.util.logging.Level
import java.util.logging.Logger

import static be.dafke.Accounting.BusinessModelDao.XMLConstants.*
import static be.dafke.Accounting.BusinessModelDao.XMLReader.*
import static be.dafke.Accounting.BusinessModelDao.XMLWriter.getXmlHeader
import static be.dafke.Utils.Utils.parseBigDecimal 

class MealsIO {
    static void readMeals(Accounting accounting){
        Meals meals = accounting.meals
        Ingredients ingredients = accounting.ingredients
        File xmlFile = new File(ACCOUNTINGS_XML_FOLDER +accounting.name+"/"+MEALS + XML_EXTENSION)
        Element rootElement = getRootElement(xmlFile, MEALS)
        for (Element mealElement : getChildren(rootElement, MEAL)) {

            String mealNr = getValue(mealElement, MEAL_NR)
            Meal meal = new Meal(mealNr)

            String mealName = getValue(mealElement, MEAL_NAME)
            if(mealName!=null)
                meal.setMealName(mealName)

            String salesPrice = getValue(mealElement, PRICE)
            if(salesPrice!=null)
                meal.setSalesPrice(parseBigDecimal(salesPrice))

            String description = getValue(mealElement, DESCRIPTION)
            if(description!=null)
                meal.setDescription(description)

            for (Element recipeElement : getChildren(mealElement, MEAL_RECIPE_LINE)) {
                String ingredientName = getValue(recipeElement, NAME)
                Ingredient ingredient = ingredients.getBusinessObject(ingredientName)

                RecipeLine recipeLine = new RecipeLine(ingredient)

                String amountString = getValue(mealElement, AMOUNT)
                if(amountString!=null){
                    BigDecimal amount = new BigDecimal(amountString)
                    recipeLine.setAmount(amount)
                }

                String preparation = getValue(mealElement, MEAL_RECIPE_PREPARATION)
                String instructions = getValue(mealElement, MEAL_RECIPE_INSTRUCTIONS)
                recipeLine.setPreparation(preparation)
                recipeLine.setInstructions(instructions)

                Recipe recipe = meal.getRecipe()
                try {
                    recipe.addBusinessObject(recipeLine)
                } catch (EmptyNameException | DuplicateNameException e) {
                    e.printStackTrace()
                }
            }

            try {
                meals.addBusinessObject(meal)
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace()
            }
        }
    }

    static String calculatePdfPath(Accounting accounting){
        ACCOUNTINGS_XML_FOLDER + accounting.name + "/MealsWithAllergenes" + PDF_EXTENSION
    }

    static void writeMeals(Accounting accounting) {
        writeMeals(accounting, false)
    }

    static String writeMeals(Accounting accounting, boolean details) {
        Meals meals = accounting.meals
        String filename = details?"MealDetails":MEALS
        String path = ACCOUNTINGS_XML_FOLDER + accounting.name + "/" + filename + XML_EXTENSION
        File file = new File(path)
        try {
            Writer writer = new FileWriter(file)
            writer.write(getXmlHeader(MEALS, 2))
            for (Meal meal : meals.businessObjects) {
                writer.write(
                        "  <" + MEAL + ">\n" +
                                "    <" + MEAL_NR + ">" + meal.name + "</" + MEAL_NR + ">\n" +
                                "    <" + MEAL_NAME + ">" + meal.getMealName() + "</" + MEAL_NAME + ">\n" +
                                "    <" + PRICE + ">" + meal.getSalesPrice() + "</" + PRICE + ">\n" +
                                "    <" + DESCRIPTION + ">" + meal.description + "</" + DESCRIPTION + ">\n" +
                                "    <" + MEAL_RECIPE + ">\n"
                )
                Recipe recipe = meal.getRecipe()
                for(RecipeLine line:recipe.businessObjects){
                    Ingredient ingredient = line.getIngredient()
                    writer.write(
                            "      <" + MEAL_RECIPE_LINE + ">\n" +
                                    "        <" + NAME + ">" + ingredient.name + "</" + NAME + ">\n" +
//                            "      <" + AMOUNT + ">" + line.getInstructions() + "</" + AMOUNT + ">\n" +
//                            "      <" + AMOUNT + ">" + line.getPreparation() + "</" + AMOUNT + ">\n" +
                                    "        <" + AMOUNT + ">" + line.amount + "</" + AMOUNT + ">\n"
                    )
                    if(details){
                        Allergenes allergenes = ingredient.allergenes
                        for (Allergene allergene:allergenes.businessObjects) {
                            writer.write(
                                    "        <" + ALLERGENE + ">\n" +
                                            "          <" + ID + ">" + allergene.name + "</" + ID + ">\n" +
                                            "          <" + NAME + ">" + allergene.shortName + "</" + NAME + ">\n" +
                                            "          <" + DESCRIPTION + ">" + allergene.description + "</" + DESCRIPTION + ">\n" +
                                            "        </" + ALLERGENE + ">\n"
                            )
                        }
                    }
                    writer.write("      </" + MEAL_RECIPE_LINE + ">\n")
                }
                writer.write(
                        "    </" + MEAL_RECIPE + ">\n" +
                                "  </" + MEAL + ">\n"

                )
            }
            writer.write("</" + MEALS + ">\n")
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(Meal.class.name).log(Level.SEVERE, null, ex)
        } finally {
            path
        }
    }
}
