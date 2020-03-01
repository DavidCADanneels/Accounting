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

class GoodsIO {
    static void readGoods(Accounting accounting){
        Goods goods = accounting.goods
        Articles articles = accounting.articles
        Contacts contacts = accounting.contacts
        Ingredients ingredients = accounting.ingredients
        File xmlFile = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$GOODS$XML_EXTENSION")
        Element rootElement = getRootElement(xmlFile, GOODS)
        for (Element element : getChildren(rootElement, GOOD)) {

            String name = getValue(element, NAME)
            Good good = new Good(name)

            String hsCode = getValue(element, ARTICLE_HS_CODE)
            if(hsCode) {
                good.setHSCode(hsCode)
            }

            String ingredientName = getValue(element, INGREDIENT)
            if(ingredientName){
                Ingredient ingredient = ingredients.getBusinessObject(ingredientName)
                good.setIngredient(ingredient)
            }

            String ingredientAmountSrting = getValue(element, AMOUNT)
            if(ingredientAmountSrting){
                good.setIngredientAmount(parseBigDecimal(ingredientAmountSrting))
            }

            String purchasePrice = getValue(element, PURCHASE_PRICE)
            if(purchasePrice)
                good.setPurchasePrice(parseBigDecimal(purchasePrice))

            String salesPriceSingle = getValue(element, SALES_SINGLE_PRICE)
            if(salesPriceSingle)
                good.setSalesPriceItemWithVat(parseBigDecimal(salesPriceSingle))

            String purchaseVatRate = getValue(element, PURCHASE_VAT_RATE)
            if(purchaseVatRate)
                good.setPurchaseVatRate(parseInt(purchaseVatRate))

            String salesVatRate = getValue(element, SALES_VAT_RATE)
            if(salesVatRate)
                good.setSalesVatRate(parseInt(salesVatRate))

            String itemsPerUnit = getValue(element, ITEMS_PER_UNIT)
            if(itemsPerUnit)
                good.setItemsPerUnit(parseInt(itemsPerUnit))

            String supplierName = getValue(element, SUPPLIER)
            if(supplierName) {
                Contact supplier = contacts.getBusinessObject(supplierName)
                if (supplier != null) {
                    good.setSupplier(supplier)
                }
            }
            try {
                goods.addBusinessObject(good)
                articles.addBusinessObject(good)
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace()
            }
        }
    }

    static void writeGoods(Accounting accounting) {
        Goods goods = accounting.goods
        File file = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$GOODS$XML_EXTENSION")
        try {
            Writer writer = new FileWriter(file)
            writer.write getXmlHeader(GOODS, 2)
            for (Good good : goods.businessObjects) {
                Ingredient ingredient = good.getIngredient()
                writer.write """\
  <$GOOD>
    <$NAME>$good.name</$NAME>
    <$INGREDIENT>${(ingredient==null?"null":ingredient.name)}</$INGREDIENT>
    <$AMOUNT>$good.ingredientAmount</$AMOUNT>
    <$ITEMS_PER_UNIT>$good.itemsPerUnit</$ITEMS_PER_UNIT>
    <$ARTICLE_HS_CODE>$good.HSCode</$ARTICLE_HS_CODE>
    <$PURCHASE_PRICE>$good.purchasePrice</$PURCHASE_PRICE>
    <$SALES_SINGLE_PRICE>$good.salesPriceItemWithVat</$SALES_SINGLE_PRICE>
    <$PURCHASE_VAT_RATE>$good.purchaseVatRate</$PURCHASE_VAT_RATE>
    <$SALES_VAT_RATE>$good.salesVatRate</$SALES_VAT_RATE>
    <$SUPPLIER>$good.supplier</$SUPPLIER>"""
                
                for(PurchaseOrder purchaseOrder:good.purchaseOrders) writer.write """
    <$PURCHASE_ORDER>$purchaseOrder</$PURCHASE_ORDER>"""

                for(SalesOrder salesOrder:good.salesOrders) writer.write """
    <$SALES_ORDER>$salesOrder</$SALES_ORDER>"""

                for(StockOrder stockOrder:good.stockOrders) writer.write """
    <$STOCK_ORDER>$stockOrder</$STOCK_ORDER>"""

                for(PromoOrder promoOrder:good.promoOrders) writer.write """
    <$PROMO_ORDER>$promoOrder</$PROMO_ORDER>"""

                writer.write """
  </$GOOD>
"""
            }
            writer.write """\
</$GOODS>
"""
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(Accounts.class.name).log(Level.SEVERE, null, ex)
        }
    }
}
