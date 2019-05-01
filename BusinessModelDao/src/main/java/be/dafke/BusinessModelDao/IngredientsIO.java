package be.dafke.BusinessModelDao;

import be.dafke.BusinessModel.*;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static be.dafke.BusinessModelDao.XMLConstants.*;
import static be.dafke.BusinessModelDao.XMLReader.*;
import static be.dafke.BusinessModelDao.XMLWriter.getXmlHeader;

public class IngredientsIO {
    public static void readIngredients(Accounting accounting){
        Ingredients ingredients = accounting.getIngredients();
        File xmlFile = new File(ACCOUNTINGS_XML_FOLDER +accounting.getName()+"/"+INGREDIENTS + XML_EXTENSION);
        Element rootElement = getRootElement(xmlFile, INGREDIENTS);
        for (Element element : getChildren(rootElement, INGREDIENT)) {

            String name = getValue(element, NAME);
            String unitName = getValue(element, UNIT);
            Unit unit = Unit.valueOf(unitName);
            Ingredient ingredient = new Ingredient(name, unit);
            try {
                ingredients.addBusinessObject(ingredient);
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeIngredientes(Accounting accounting) {
        Ingredients ingredients = accounting.getIngredients();
        File file = new File(ACCOUNTINGS_XML_FOLDER + accounting.getName() + "/" + INGREDIENTS + XML_EXTENSION);
        try {
            Writer writer = new FileWriter(file);
            writer.write(getXmlHeader(INGREDIENTS, 2));
            for (Ingredient ingredient : ingredients.getBusinessObjects()) {
                writer.write(
                        "  <" + INGREDIENT + ">\n" +
                            "    <" + NAME + ">" + ingredient.getName() + "</" + NAME + ">\n" +
                            "    <" + UNIT + ">" + ingredient.getUnit().getName().toUpperCase() + "</" + UNIT + ">\n" +
                            "  </" + INGREDIENT + ">\n"
                );
            }
            writer.write("</" + INGREDIENTS + ">\n");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Accounts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}