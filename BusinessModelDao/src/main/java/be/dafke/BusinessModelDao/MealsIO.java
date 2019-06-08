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
import static be.dafke.Utils.Utils.parseBigDecimal;

public class MealsIO {
    public static void readMeals(Accounting accounting){
        Meals meals = accounting.getMeals();
        File xmlFile = new File(ACCOUNTINGS_XML_FOLDER +accounting.getName()+"/"+MEALS + XML_EXTENSION);
        Element rootElement = getRootElement(xmlFile, MEALS);
        for (Element element : getChildren(rootElement, MEAL)) {

            String mealNr = getValue(element, MEAL_NR);
            Meal meal = new Meal(mealNr);

            String mealName = getValue(element, MEAL_NAME);
            if(mealName!=null)
                meal.setMealName(mealName);

            String salesPrice = getValue(element, PRICE);
            if(salesPrice!=null)
                meal.setSalesPrice(parseBigDecimal(salesPrice));

            String description = getValue(element, DESCRIPTION);
            if(description!=null)
                meal.setDescription(description);

            try {
                meals.addBusinessObject(meal);
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeMeals(Accounting accounting) {
        Meals meals = accounting.getMeals();
        File file = new File(ACCOUNTINGS_XML_FOLDER + accounting.getName() + "/" + MEALS + XML_EXTENSION);
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
                                "    <" + USAGE + ">" + meal.getTotalOrdered() + "</" + USAGE + ">\n" +
                                "  </" + MEAL + ">\n"
                );
            }
            writer.write("</" + MEALS + ">\n");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Meal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
