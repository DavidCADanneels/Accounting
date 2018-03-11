package be.dafke.BusinessModelDao;

import be.dafke.BusinessModel.*;
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
import static be.dafke.Utils.Utils.parseInt;

/**
 * Created by ddanneels on 15/01/2017.
 */
public class StockIO {
    public static void readStock(Accounting accounting){
        Stock stock = accounting.getStock();
        Articles articles = accounting.getArticles();
        File xmlFile = new File(XML_PATH+accounting.getName()+"/"+STOCK + XML_EXTENSION);
        Element rootElement = getRootElement(xmlFile, STOCK);
        for (Element element : getChildren(rootElement, ARTICLE)) {

            String name = getValue(element, NAME);
            Article article = articles.getBusinessObject(name);

            String numberOfUnitsString = getValue(element, NR_OF_UNITS);
            int numberOfUnits = parseInt(numberOfUnitsString);

            String numberOfItemsString = getValue(element, NR_OF_ITEMS);
            int numberOfItems = parseInt(numberOfItemsString);

            OrderItem orderItem = new OrderItem(numberOfUnits, numberOfItems, article);
            orderItem.setName(name);
            stock.addBusinessObject(orderItem);
        }
    }

    public static void writeStock(Accounting accounting) {
        Stock stock = accounting.getStock();
        File file = new File(XML_PATH + accounting.getName() + "/" + STOCK + XML_EXTENSION);
        try {
            Writer writer = new FileWriter(file);
            writer.write(getXmlHeader(STOCK, 2));
            for (OrderItem orderItem : stock.getBusinessObjects()) {
                Article article = orderItem.getArticle();
                writer.write(
                         "  <" + ARTICLE + ">\n" +
                             "    <" + NAME + ">" + article.getName() + "</" + NAME + ">\n" +
                             "    <" + NR_OF_UNITS + ">" + orderItem.getNumberOfUnits() + "</" + NR_OF_UNITS + ">\n" +
                             "    <" + NR_OF_ITEMS + ">" + orderItem.getNumberOfItems() + "</" + NR_OF_ITEMS + ">\n" +
                             "  </" + ARTICLE + ">\n"
                );
            }
            writer.write("</" + STOCK + ">\n");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Accounts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
