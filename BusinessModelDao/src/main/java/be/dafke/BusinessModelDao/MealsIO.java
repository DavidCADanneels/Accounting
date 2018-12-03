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

/**
 * Created by ddanneels on 15/01/2017.
 */
public class MealsIO {
    public static void readMeals(Accounting accounting){
        DeliverooMeals deliverooMeals = accounting.getDeliverooMeals();
        File xmlFile = new File(ACCOUNTINGS_XML_FOLDER +accounting.getName()+"/"+MEALS + XML_EXTENSION);
        Element rootElement = getRootElement(xmlFile, MEALS);
        for (Element element : getChildren(rootElement, MEAL)) {

            String mealNr = getValue(element, MEAL_NR);
            DeliverooMeal deliverooMeal = new DeliverooMeal(mealNr);

            String mealName = getValue(element, MEAL_NAME);
            if(mealName!=null)
                deliverooMeal.setMealName(mealName);

            String salesPrice = getValue(element, PRICE);
            if(salesPrice!=null)
                deliverooMeal.setSalesPrice(parseBigDecimal(salesPrice));

            String description = getValue(element, DESCRIPTION);
            if(description!=null)
                deliverooMeal.setDescription(description);

            try {
                deliverooMeals.addBusinessObject(deliverooMeal);
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeMeals(Accounting accounting) {
        DeliverooMeals deliverooMeals = accounting.getDeliverooMeals();
        File file = new File(ACCOUNTINGS_XML_FOLDER + accounting.getName() + "/" + MEALS + XML_EXTENSION);
        try {
            Writer writer = new FileWriter(file);
            writer.write(getXmlHeader(MEALS, 2));
                for (DeliverooMeal deliverooMeal : deliverooMeals.getBusinessObjects()) {
                writer.write(
                        "  <" + MEAL + ">\n" +
                                "    <" + MEAL_NR + ">" + deliverooMeal.getName() + "</" + MEAL_NR + ">\n" +
                                "    <" + MEAL_NAME + ">" + deliverooMeal.getMealName() + "</" + MEAL_NAME + ">\n" +
                                "    <" + PRICE + ">" + deliverooMeal.getSalesPrice() + "</" + PRICE + ">\n" +
                                "    <" + DESCRIPTION + ">" + deliverooMeal.getDescription() + "</" + DESCRIPTION + ">\n" +
                                "  </" + MEAL + ">\n"
                );
            }
            writer.write("</" + MEALS + ">\n");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(DeliverooMeal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
