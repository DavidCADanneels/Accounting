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

class IngredientsIO {
    static void readIngredients(Accounting accounting){
        Ingredients ingredients = accounting.ingredients
        Allergenes allergenes = accounting.allergenes
        File xmlFile = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$INGREDIENTS$XML_EXTENSION")
        Element rootElement = getRootElement(xmlFile, INGREDIENTS)
        for (Element ingredientElement : getChildren(rootElement, INGREDIENT)) {

            String name = getValue(ingredientElement, NAME)
            String unitName = getValue(ingredientElement, UNIT)
            Unit unit = Unit.valueOf(unitName)
            Ingredient ingredient = new Ingredient(name, unit)
            for(Element allergeneElement : getChildren(ingredientElement, ALLERGENE)) {
                String allergeneId = getValue(allergeneElement, ID)
                Allergene allergene = allergenes.getBusinessObject(allergeneId)
                ingredient.addAllergene(allergene)
            }
            try {
                ingredients.addBusinessObject(ingredient)
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace()
            }
        }
    }

    static void writeIngredientes(Accounting accounting) {
        Ingredients ingredients = accounting.ingredients
        File file = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$INGREDIENTS$XML_EXTENSION")
        try {
            Writer writer = new FileWriter(file)
            writer.write getXmlHeader(INGREDIENTS, 2)
            for (Ingredient ingredient : ingredients.businessObjects) {
                writer.write """\
  <$INGREDIENT>
    <$NAME>$ingredient.name</$NAME>
    <$UNIT>${ingredient.unit.name.toUpperCase()}</$UNIT>"""
                Allergenes allergenes = ingredient.allergenes
                for (Allergene allergene:allergenes.businessObjects) writer.write"""
    <$ALLERGENE>
      <$ID>$allergene.name</$ID>
    </$ALLERGENE>"""
                writer.write"""
  </$INGREDIENT>
"""
            }
            writer.write """\
</$INGREDIENTS>
"""
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(Accounts.class.name).log(Level.SEVERE, null, ex)
        }
    }
}
