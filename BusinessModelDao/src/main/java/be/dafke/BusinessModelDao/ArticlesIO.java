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
    public static void readArticles(Accounting accounting){
        Articles articles = accounting.getArticles();
        Contacts contacts = accounting.getContacts();
        File xmlFile = new File(ACCOUNTINGS_XML_FOLDER +accounting.getName()+"/"+ARTICLES + XML_EXTENSION);
        Element rootElement = getRootElement(xmlFile, ARTICLES);
        for (Element element : getChildren(rootElement, ARTICLE)) {

            String name = getValue(element, NAME);
            Article article = new Article(name);

            String itemName = getValue(element, ARTICLE_ITEM_NAME);
            article.setItemName(itemName);

            String hsCode = getValue(element, ARTICLE_HS_CODE);
            if(hsCode!=null)
                article.setHSCode(hsCode);

            String purchasePrice = getValue(element, ARTICLE_PURCHASE_PRICE);
            if(purchasePrice!=null)
                article.setPurchasePrice(parseBigDecimal(purchasePrice));

            String salesPriceSingle = getValue(element, ARTICLE_SALES_SINGLE_PRICE);
            if(salesPriceSingle!=null)
                article.setSalesPriceSingleWithVat(parseBigDecimal(salesPriceSingle));

            String salesPricePromo = getValue(element, ARTICLE_SALES_PROMO_PRICE);
            if(salesPricePromo!=null)
                article.setSalesPricePromoWithVat(parseBigDecimal(salesPricePromo));

            String purchaseVatRate = getValue(element, ARTICLE_PURCHASE_VAT_RATE);
            if(purchaseVatRate!=null)
                article.setPurchaseVatRate(parseInt(purchaseVatRate));

            String salesVatRate = getValue(element, ARTICLE_SALES_VAT_RATE);
            if(salesVatRate!=null)
                article.setSalesVatRate(parseInt(salesVatRate));

            String itemsPerUnit = getValue(element, ARTICLE_ITEMS_PER_UNIT);
            if(itemsPerUnit!=null)
                article.setItemsPerUnit(parseInt(itemsPerUnit));

            String minForRed = getValue(element, ARTICLE_MIN_REDUCTION);
            if(minForRed!=null)
                article.setMinimumNumberForReduction(parseInt(minForRed));

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

    public static void writeArticles(Accounting accounting) {
        Articles articles = accounting.getArticles();
        File file = new File(ACCOUNTINGS_XML_FOLDER + accounting.getName() + "/" + ARTICLES + XML_EXTENSION);
        try {
            Writer writer = new FileWriter(file);
            writer.write(getXmlHeader(ARTICLES, 2));
                for (Article article : articles.getBusinessObjects()) {
                writer.write(
                        "  <" + ARTICLE + ">\n" +
                                "    <" + NAME + ">" + article.getName() + "</" + NAME + ">\n" +
                                "    <" + ARTICLE_ITEM_NAME + ">" + article.getItemName() + "</" + ARTICLE_ITEM_NAME + ">\n" +
                                "    <" + ARTICLE_ITEMS_PER_UNIT + ">" + article.getItemsPerUnit() + "</" + ARTICLE_ITEMS_PER_UNIT + ">\n" +
                                "    <" + ARTICLE_MIN_REDUCTION + ">" + article.getMinimumNumberForReduction() + "</" + ARTICLE_MIN_REDUCTION + ">\n" +
                                "    <" + ARTICLE_HS_CODE + ">" + article.getHSCode() + "</" + ARTICLE_HS_CODE + ">\n" +
                                "    <" + ARTICLE_PURCHASE_PRICE + ">" + article.getPurchasePrice() + "</" + ARTICLE_PURCHASE_PRICE + ">\n" +
                                "    <" + ARTICLE_SALES_SINGLE_PRICE + ">" + article.getSalesPriceSingleWithVat() + "</" + ARTICLE_SALES_SINGLE_PRICE + ">\n" +
                                "    <" + ARTICLE_SALES_PROMO_PRICE + ">" + article.getSalesPricePromoWithVat() + "</" + ARTICLE_SALES_PROMO_PRICE + ">\n" +
                                "    <" + ARTICLE_PURCHASE_VAT_RATE + ">" + article.getPurchaseVatRate() + "</" + ARTICLE_PURCHASE_VAT_RATE + ">\n" +
                                "    <" + ARTICLE_SALES_VAT_RATE + ">" + article.getSalesVatRate() + "</" + ARTICLE_SALES_VAT_RATE + ">\n" +
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
