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
        Articles articles = accounting.getArticles()
        Contacts contacts = accounting.getContacts()
        Ingredients ingredients = accounting.getIngredients()
        File xmlFile = new File(ACCOUNTINGS_XML_FOLDER +accounting.getName()+"/"+ARTICLES + XML_EXTENSION)
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
        Articles articles = accounting.getArticles()
        File file = new File(ACCOUNTINGS_XML_FOLDER + accounting.getName() + "/" + ARTICLES + XML_EXTENSION)
        try {
            Writer writer = new FileWriter(file)
            writer.write(getXmlHeader(ARTICLES, 2))
            for (Article article : articles.getBusinessObjects()) {
                Ingredient ingredient = article.getIngredient()
                writer.write(
                        "  <" + ARTICLE + ">\n" +
                                "    <" + NAME + ">" + article.getName() + "</" + NAME + ">\n" +
                                "    <" + INGREDIENT + ">" + (ingredient==null?"null":ingredient.getName()) + "</" + INGREDIENT + ">\n" +
                                "    <" + AMOUNT + ">" + article.getIngredientAmount() + "</" + AMOUNT + ">\n" +
                                "    <" + ITEMS_PER_UNIT + ">" + article.getItemsPerUnit() + "</" + ITEMS_PER_UNIT + ">\n" +
                                "    <" + ARTICLE_HS_CODE + ">" + article.getHSCode() + "</" + ARTICLE_HS_CODE + ">\n" +
                                "    <" + PURCHASE_PRICE + ">" + article.getPurchasePrice() + "</" + PURCHASE_PRICE + ">\n" +
                                "    <" + SALES_SINGLE_PRICE + ">" + article.getSalesPriceItemWithVat() + "</" + SALES_SINGLE_PRICE + ">\n" +
                                "    <" + PURCHASE_VAT_RATE + ">" + article.getPurchaseVatRate() + "</" + PURCHASE_VAT_RATE + ">\n" +
                                "    <" + SALES_VAT_RATE + ">" + article.getSalesVatRate() + "</" + SALES_VAT_RATE + ">\n" +
                                "    <" + NR_IN_STOCK + ">" + article.getNrInStock() + "</" + NR_IN_STOCK + ">\n" +
                                "    <" + NR_ADDED + ">" + article.getNrAdded() + "</" + NR_ADDED + ">\n" +
                                "    <" + NR_REMOVED + ">" + article.getNrRemoved() + "</" + NR_REMOVED + ">\n" +
                                "    <" + NR_SO_ORDERED + ">" + article.getNrOrderedForSO() + "</" + NR_SO_ORDERED + ">\n" +
                                "    <" + NR_PO_ORDERED + ">" + article.getNrOrderedByPO() + "</" + NR_PO_ORDERED + ">\n" +
                                "    <" + NR_INIT_STOCK + ">" + article.getInitStock() + "</" + NR_INIT_STOCK + ">\n" +
                                "    <" + NR_PROMO_REMOVED + ">" + article.getNrPromo() + "</" + NR_PROMO_REMOVED + ">\n" +
                                "    <" + SUPPLIER + ">" + article.getSupplier() + "</" + SUPPLIER + ">\n"
                )
                ArrayList<PurchaseOrder> purchaseOrders = article.getPurchaseOrders()
                for(PurchaseOrder purchaseOrder:purchaseOrders) {
                    writer.write(
                            "    <" + PURCHASE_ORDER + ">" + purchaseOrder + "</" + PURCHASE_ORDER + ">\n"
                    )
                }
                ArrayList<SalesOrder> salesOrders = article.getSalesOrders()
                for(SalesOrder salesOrder:salesOrders){
                    writer.write(
                            "    <" + SALES_ORDER + ">" + salesOrder + "</" + SALES_ORDER + ">\n"
                    )
                }
                ArrayList<StockOrder> stockOrders = article.getStockOrders()
                for(StockOrder stockOrder:stockOrders){
                    writer.write(
                            "    <" + STOCK_ORDER + ">" + stockOrder + "</" + STOCK_ORDER + ">\n"
                    )
                }
                ArrayList<PromoOrder> promoOrders = article.getPromoOrders()
                for(PromoOrder promoOrder:promoOrders){
                    writer.write(
                            "    <" + PROMO_ORDER + ">" + promoOrder + "</" + PROMO_ORDER + ">\n"
                    )
                }
                writer.write(
                        "  </" + ARTICLE + ">\n"
                )
            }
            writer.write("</" + ARTICLES + ">\n")
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(Accounts.class.getName()).log(Level.SEVERE, null, ex)
        }
    }
}
