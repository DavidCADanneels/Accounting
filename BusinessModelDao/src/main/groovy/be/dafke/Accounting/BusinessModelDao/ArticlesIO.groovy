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
import static be.dafke.Utils.Utils.parseBigDecimal
import static be.dafke.Utils.Utils.parseInt 

class ArticlesIO {
    static void readArticles(Accounting accounting){
        Articles articles = accounting.articles
        Contacts contacts = accounting.contacts
        Ingredients ingredients = accounting.ingredients
        File xmlFile = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$ARTICLES$XML_EXTENSION")
        Element rootElement = getRootElement(xmlFile, ARTICLES)
        for (Element element : getChildren(rootElement, ARTICLE)) {

            String name = getValue(element, NAME)
            Article article = new Article(name)

            String ingredientName = getValue(element, INGREDIENT)
            if(ingredientName!=null){
                Ingredient ingredient = ingredients.getBusinessObject(ingredientName)
                article.setIngredient(ingredient)
            }

            String ingredientAmountSrting = getValue(element, AMOUNT)
            if(ingredientAmountSrting!=null){
                article.setIngredientAmount(parseBigDecimal(ingredientAmountSrting))
            }

            String hsCode = getValue(element, ARTICLE_HS_CODE)
            if(hsCode!=null)
                article.setHSCode(hsCode)

            String purchasePrice = getValue(element, PURCHASE_PRICE)
            if(purchasePrice!=null)
                article.setPurchasePrice(parseBigDecimal(purchasePrice))

            String salesPriceSingle = getValue(element, SALES_SINGLE_PRICE)
            if(salesPriceSingle!=null)
                article.setSalesPriceItemWithVat(parseBigDecimal(salesPriceSingle))

            String purchaseVatRate = getValue(element, PURCHASE_VAT_RATE)
            if(purchaseVatRate!=null)
                article.setPurchaseVatRate(parseInt(purchaseVatRate))

            String salesVatRate = getValue(element, SALES_VAT_RATE)
            if(salesVatRate!=null)
                article.setSalesVatRate(parseInt(salesVatRate))

            String itemsPerUnit = getValue(element, ITEMS_PER_UNIT)
            if(itemsPerUnit!=null)
                article.setItemsPerUnit(parseInt(itemsPerUnit))

            String supplierName = getValue(element, SUPPLIER)
            if(supplierName!=null) {
                Contact supplier = contacts.getBusinessObject(supplierName)
                if (supplier != null) {
                    article.setSupplier(supplier)
                }
            }
            try {
                articles.addBusinessObject(article)
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace()
            }
        }
    }

    static void writeArticles(Accounting accounting) {
        Articles articles = accounting.articles
        File file = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$ARTICLES$XML_EXTENSION")
        try {
            Writer writer = new FileWriter(file)
            writer.write getXmlHeader(ARTICLES, 2)
            for (Article article : articles.businessObjects) {
                Ingredient ingredient = article.getIngredient()
                writer.write """\
  <$ARTICLE>
    <$NAME>$article.name</$NAME>
    <$INGREDIENT>${(ingredient==null?"null":ingredient.name)}</$INGREDIENT>
    <$AMOUNT>$article.ingredientAmount</$AMOUNT>
    <$ITEMS_PER_UNIT>$article.itemsPerUnit</$ITEMS_PER_UNIT>
    <$ARTICLE_HS_CODE>$article.HSCode</$ARTICLE_HS_CODE>
    <$PURCHASE_PRICE>$article.purchasePrice</$PURCHASE_PRICE>
    <$SALES_SINGLE_PRICE>$article.salesPriceItemWithVat</$SALES_SINGLE_PRICE>
    <$PURCHASE_VAT_RATE>$article.purchaseVatRate</$PURCHASE_VAT_RATE>
    <$SALES_VAT_RATE>$article.salesVatRate</$SALES_VAT_RATE>
    <$SUPPLIER>$article.supplier</$SUPPLIER>"""
                
                for(PurchaseOrder purchaseOrder:article.purchaseOrders) writer.write """
    <$PURCHASE_ORDER>$purchaseOrder</$PURCHASE_ORDER>"""

                for(SalesOrder salesOrder:article.salesOrders) writer.write """
    <$SALES_ORDER>$salesOrder</$SALES_ORDER>"""

                for(StockOrder stockOrder:article.stockOrders) writer.write """
    <$STOCK_ORDER>$stockOrder</$STOCK_ORDER>"""

                for(PromoOrder promoOrder:article.promoOrders) writer.write """
    <$PROMO_ORDER>$promoOrder</$PROMO_ORDER>"""

                writer.write """
  </$ARTICLE>
"""
            }
            writer.write """\
</$ARTICLES>
"""
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(Accounts.class.name).log(Level.SEVERE, null, ex)
        }
    }
}
