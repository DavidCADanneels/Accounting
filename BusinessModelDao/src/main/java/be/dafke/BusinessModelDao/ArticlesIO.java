package be.dafke.BusinessModelDao;

import be.dafke.BusinessModel.*;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
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
public class ArticlesIO {
    public static void readArticles(Accounting accounting, File accountingFolder){
        Articles articles = accounting.getArticles();
        Contacts contacts = accounting.getContacts();
        File xmlFile = new File(accountingFolder, ARTICLES + XML);
        Element rootElement = getRootElement(xmlFile, ARTICLES);
        for (Element element : getChildren(rootElement, ARTICLE)) {

            String name = getValue(element, NAME);
            Article article = new Article(name);

            String hsCode = getValue(element, ARTICLE_HS_CODE);
            if(hsCode!=null)
                article.setHSCode(hsCode);

            String purchasePrice = getValue(element, ARTICLE_PURCHASE_PRICE);
            if(purchasePrice!=null)
                article.setPurchasePrice(parseBigDecimal(purchasePrice));

            String vatRate = getValue(element, ARTICLE_VAT_RATE);
            if(vatRate!=null)
                article.setVatRate(parseInt(vatRate));

            String supplierName = getValue(element, SUPPLIER);
            if(supplierName!=null) {
                Contact supplier = contacts.getBusinessObject(supplierName);
                List<Contact> suppliersList = contacts.getBusinessObjects(Contact::isSupplier);
                if (supplier != null) {
                    if (!suppliersList.contains(supplier)) {
                        System.err.println("The contact " + supplierName + " is not marked as Supplier");
                        // TODO: setSupplier ?
//                        supplier.setSupplier(true);
                    }
                    article.setSupplier(supplier);
                }
            }
            try {
                articles.addBusinessObject(article);
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeArticles(Articles articles, File accountingFolder) {
        File file = new File(accountingFolder, ARTICLES + XML);
        File folder = new File(accountingFolder, ARTICLE);
        try {
            Writer writer = new FileWriter(file);
            writer.write(getXmlHeader(ARTICLES, 2));
                for (Article article : articles.getBusinessObjects()) {
                writer.write(
                        "  <" + ARTICLE + ">\n" +
                                "    <" + NAME + ">" + article.getName() + "</" + NAME + ">\n" +
                                "    <" + ARTICLE_HS_CODE + ">" + article.getHSCode() + "</" + ARTICLE_HS_CODE + ">\n" +
                                "    <" + ARTICLE_VAT_RATE + ">" + article.getVatRate() + "</" + ARTICLE_VAT_RATE + ">\n" +
                                "    <" + ARTICLE_PURCHASE_PRICE + ">" + article.getPurchasePrice() + "</" + ARTICLE_PURCHASE_PRICE + ">\n" +
                                "    <" + SUPPLIER + ">" + article.getSupplier() + "</" + SUPPLIER + ">\n" +
                                "  </" + ARTICLE + ">\n"
                );
            }
            writer.write("</" + ARTICLES + ">\n");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Accounts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
