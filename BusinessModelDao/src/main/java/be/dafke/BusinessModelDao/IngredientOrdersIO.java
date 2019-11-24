package be.dafke.BusinessModelDao;

import be.dafke.Accounting.BusinessModel.*;
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

import static be.dafke.BusinessModelDao.XMLConstants.*;
import static be.dafke.BusinessModelDao.XMLReader.getChildren;
import static be.dafke.BusinessModelDao.XMLReader.getRootElement;
import static be.dafke.BusinessModelDao.XMLWriter.getXmlHeader;

public class IngredientOrdersIO {
    public static void readIngredientOrders(Accounting accounting){
        IngredientOrders ingredientOrders = accounting.getIngredientOrders();
        File xmlFile = new File(ACCOUNTINGS_XML_FOLDER +accounting.getName()+"/"+INGREDIENT_ORDERS + XML_EXTENSION);
        Element rootElement = getRootElement(xmlFile, INGREDIENT_ORDERS);
        for (Element ingredientElement : getChildren(rootElement, INGREDIENT_ORDER)) {

            IngredientOrder ingredient = new IngredientOrder();
            try {
                ingredientOrders.addBusinessObject(ingredient);
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeIngredientOrders(Accounting accounting) {
        IngredientOrders ingredientOrders = accounting.getIngredientOrders();
        File file = new File(ACCOUNTINGS_XML_FOLDER + accounting.getName() + "/" + INGREDIENT_ORDERS + XML_EXTENSION);
        try {
            Writer writer = new FileWriter(file);
            writer.write(getXmlHeader(INGREDIENT_ORDERS, 2));
            for (IngredientOrder ingredientOrder : ingredientOrders.getBusinessObjects()) {
                writer.write("  <" + INGREDIENT_ORDER + ">\n");
                for (IngredientOrderItem ingredientOrderItem : ingredientOrder.getBusinessObjects()) {
                    Ingredient ingredient = ingredientOrderItem.getIngredient();
                    BigDecimal quantity = ingredientOrderItem.getQuantity();
                    Article article = ingredientOrderItem.getArticle();
                    writer.write(
                            "    <" + INGREDIENT_ORDER_ITEM + ">\n" +
                                    "      <" + INGREDIENT + ">" + ingredient.getName() + "</" + INGREDIENT + ">\n" +
                                    "      <" + QUANTITY + ">" + quantity + "</" + QUANTITY + ">\n" +
                                    "      <" + ARTICLE + ">" + article.getName() + "</" + ARTICLE + ">\n");
                    writer.write("    </" + INGREDIENT_ORDER_ITEM + ">\n");
                }
                writer.write("  </" + INGREDIENT_ORDER + ">\n");
            }
            writer.write("</" + INGREDIENT_ORDERS + ">\n");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Accounts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
