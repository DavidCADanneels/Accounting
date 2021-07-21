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
        File xmlFile = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$MEALS$XML_EXTENSION")
        Element rootElement = getRootElement(xmlFile, MEALS)
        for (Element mealElement : getChildren(rootElement, MEAL)) {

            String mealNr = getValue(mealElement, MEAL_NR)
            Meal meal = new Meal(mealNr)

            String mealName = getValue(mealElement, MEAL_NAME)
            if(mealName)
                meal.setMealName(mealName)

            String salesPrice = getValue(mealElement, PRICE)
            if(salesPrice)
                meal.setSalesPrice(parseBigDecimal(salesPrice))

            String description = getValue(mealElement, DESCRIPTION)
            if(description)
                meal.setDescription(description)

            Recipe recipe = meal.getRecipe()

            String preparation = getValue(mealElement, MEAL_RECIPE_PREPARATION)
            String instructions = getValue(mealElement, MEAL_RECIPE_INSTRUCTIONS)

            recipe.setPreparation(preparation)
            recipe.setInstructions(instructions)

            for (Element recipeElement : getChildren(mealElement, MEAL_RECIPE_LINE)) {
                String ingredientName = getValue(recipeElement, NAME)
                Ingredient ingredient = ingredients.getBusinessObject(ingredientName)

                RecipeLine recipeLine = new RecipeLine(ingredient)

                String amountString = getValue(mealElement, AMOUNT)
                if(amountString){
                    BigDecimal amount = new BigDecimal(amountString)
                    recipeLine.setAmount(amount)
                }

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

    static String writeMealsWithAllDetails(Accounting accounting) {
        String path = "$ACCOUNTINGS_XML_PATH/$accounting.name/$MEALS$XML_EXTENSION"
        File file = new File(path)
        try {
            Writer writer = new FileWriter(file)
            writer.write getXmlHeader(MEALS, 2)
            accounting.meals.businessObjects.each { meal ->
                writer.write """\
  <$MEAL>
    <$MEAL_NR>$meal.name</$MEAL_NR>
    <$MEAL_NAME>$meal.mealName</$MEAL_NAME>
    <$PRICE>$meal.salesPrice</$PRICE>
    <$DESCRIPTION>$meal.description</$DESCRIPTION>
    <$MEAL_RECIPE>"""
                meal.recipe.businessObjects.each { line ->
                    writer.write """
      <$MEAL_RECIPE_LINE>
        <$NAME>$line.ingredient.name</$NAME>
        <$AMOUNT>$line.amount</$AMOUNT>
        <$AMOUNT_AND_UNIT>${line.amount} ${line.ingredient.unit.symbol}</$AMOUNT_AND_UNIT>"""
                    line.ingredient.allergenes.businessObjects.each { allergene ->
                        writer.write """
        <$ALLERGENE>
          <$ID>$allergene.name</$ID>
          <$NAME>$allergene.shortName</$NAME>
          <$DESCRIPTION>$allergene.description</$DESCRIPTION>
        </$ALLERGENE>"""
                    }
                    writer.write """
      </$MEAL_RECIPE_LINE>"""
                }
                writer.write """
      <$MEAL_RECIPE_PREPARATION>$meal.recipe.preparation</$MEAL_RECIPE_PREPARATION>
      <$MEAL_RECIPE_INSTRUCTIONS>$meal.recipe.instructions</$MEAL_RECIPE_INSTRUCTIONS>
    </$MEAL_RECIPE>
  </$MEAL>
"""
            }
            writer.write """\
</$MEALS>
"""
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(Meal.class.name).log(Level.SEVERE, null, ex)
        } finally {
            return path
        }
    }

}

