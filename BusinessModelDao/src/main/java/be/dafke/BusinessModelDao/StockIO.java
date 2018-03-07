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

            String numberString = getValue(element, NUMBER);
            int number = parseInt(numberString);

            StockItem stockItem = new StockItem(number, article);
            stock.addBusinessObject(stockItem);
        }
    }

    public static void writeStock(Stock stock, File accountingFolder) {
        File file = new File(accountingFolder, STOCK + XML_EXTENSION);
        try {
            Writer writer = new FileWriter(file);
            writer.write(getXmlHeader(STOCK, 2));
            for (StockItem stockItem : stock.getBusinessObjects()) {
                Article article = stockItem.getArticle();
                writer.write(
                         "  <" + ARTICLE + ">\n" +
                             "    <" + NAME + ">" + article.getName() + "</" + NAME + ">\n" +
                             "    <" + NUMBER + ">" + article.getHSCode() + "</" + NUMBER + ">\n" +
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
