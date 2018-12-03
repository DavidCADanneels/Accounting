package be.dafke.BusinessModelDao;

import be.dafke.BusinessModel.*;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.Utils.Utils;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static be.dafke.BusinessModelDao.XMLConstants.*;
import static be.dafke.BusinessModelDao.XMLReader.*;
import static be.dafke.BusinessModelDao.XMLWriter.getXmlHeader;
import static be.dafke.Utils.Utils.parseBigDecimal;
import static be.dafke.Utils.Utils.parseInt;

/**
 * Created by ddanneels on 15/01/2017.
 */
public class MealOrderIO {
    public static void readMealOrders(Accounting accounting){
        MealOrders mealOrders = accounting.getMealOrders();
        DeliverooMeals deliverooMeals = accounting.getDeliverooMeals();

        File xmlFile = new File(ACCOUNTINGS_XML_FOLDER +accounting.getName()+"/"+MEAL_ORDERS + XML_EXTENSION);
        Element rootElement = getRootElement(xmlFile, MEAL_ORDERS);


        for (Element mealOrderElement : getChildren(rootElement, MEAL_ORDER)) {
            MealOrder mealOrder = new MealOrder();

            mealOrder.setDescription(getValue(mealOrderElement, DESCRIPTION));
            mealOrder.setDate(Utils.toCalendar(getValue(mealOrderElement, DATE)));

            for (Element element : getChildren(mealOrderElement, MEAL)) {
                String id = getValue(element, MEAL_NR);
                DeliverooMeal deliverooMeal = deliverooMeals.getBusinessObject(id);

                String nrSting = getValue(element, NR_OF_ITEMS);
                Integer nr = Utils.parseInt(nrSting);

                MealOrderItem mealOrderItem = new MealOrderItem(nr, deliverooMeal);

                mealOrder.addBusinessObject(mealOrderItem);
            }
            try {
                mealOrders.addBusinessObject(mealOrder);
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeMealOrders(Accounting accounting) {
        MealOrders mealOrders = accounting.getMealOrders();

        File file = new File(ACCOUNTINGS_XML_FOLDER + accounting.getName() + "/" + MEAL_ORDERS + XML_EXTENSION);
        try {
            Writer writer = new FileWriter(file);
            writer.write(getXmlHeader(MEAL_ORDERS, 2));
            for (MealOrder order : mealOrders.getBusinessObjects()) {
                writer.write(
                             "  <" + MEAL_ORDER + ">\n" +
                                 "    <" + DATE + ">" + order.getDate() + "</" + DATE + ">\n" +
                                 "    <" + DESCRIPTION + ">" + order.getDescription() + "</" + DESCRIPTION + ">\n" +
                                 "    <" + NAME + ">" + order.getName() + "</" + NAME + ">\n" +
                                 "    <" + TOTAL_PRICE + ">" + order.getTotalPrice() + "</" + TOTAL_PRICE + ">\n"
                );
                for (MealOrderItem orderItem : order.getBusinessObjects()) {
                    DeliverooMeal deliverooMeal = orderItem.getDeliverooMeal();

                    writer.write(
                            "    <" + MEAL + ">\n" +
                                "      <" + NR_OF_ITEMS + ">" + orderItem.getNumberOfItems() + "</" + NR_OF_ITEMS + ">\n" +
                                "      <" + MEAL_NR + ">" + deliverooMeal.getName() + "</" + MEAL_NR + ">\n" +
                                "      <" + NAME + ">" + deliverooMeal.getMealName() + "</" + NAME + ">\n" +
                                "    </" + MEAL + ">\n"
                    );
                }
                writer.write("  </" + MEAL_ORDER + ">\n");
            }
            writer.write("</" + MEAL_ORDERS + ">\n");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(MealOrder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
