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

class PromoOrderIO {
    static void readPromoOrders(Accounting accounting){
        Articles articles = accounting.articles
        File xmlFile = new File(ACCOUNTINGS_XML_FOLDER +accounting.name+"/"+PROMO_ORDERS + XML_EXTENSION)
        Element rootElement = getRootElement(xmlFile, PROMO_ORDERS)

        for (Element orderElement : getChildren(rootElement, PROMO_ORDER)) {
            PromoOrders promoOrders = accounting.promoOrders

            PromoOrder order = new PromoOrder()

            String idString = getValue(orderElement, ID)
            order.id = parseInt(idString)

//            String orderName = getValue(orderElement, NAME)
//            order.setName(orderName)

            for (Element element : getChildren(orderElement, ARTICLE)) {

                // Fetch constructor arguments
                //
                String name = getValue(element, NAME)
                Article article = articles.getBusinessObject(name)
                ///
                String numberOfItemsString = getValue(element, NR_OF_ITEMS)
                int numberOfItems = parseInt(numberOfItemsString)
                //
                // Create OrderItem
                //
                OrderItem orderItem = new OrderItem(numberOfItems, article, order)

                // set Item Price
                orderItem.setItemsPerUnit(parseInt(getValue(element, ITEMS_PER_UNIT)))

                // set Sales VAT Rate
                orderItem.setSalesVatRate(0)

//                    String purchaseOrderForItemString = getValue(element, PURCHASE_ORDER)
//                    if (purchaseOrderForItemString != null) {
//                        PurchaseOrder purchaseOrder = purchaseOrders.getBusinessObject(purchaseOrderForItemString)
//                        orderItem.setPurchaseOrder(purchaseOrder)
//                    }

//                String purchasePriceItemString = getValue(element, PURCHASE_PRICE_ITEM)
//                BigDecimal purchasePriceItem = parseBigDecimal(purchasePriceItemString)

                String purchasePriceUnitString = getValue(element, PURCHASE_PRICE_UNIT)
                if(purchasePriceUnitString!=null) {
                    BigDecimal purchasePriceUnit = parseBigDecimal(purchasePriceUnitString)
                    orderItem.setPurchasePriceForUnit(purchasePriceUnit)
                }

                orderItem.setName(name)
                order.addBusinessObject(orderItem)
            }
            Transactions transactions = accounting.transactions
            int paymentTransactionId = parseInt(getValue(orderElement, PAYMENT_TRANSACTION))
            Transaction paymentTransaction = transactions.getBusinessObject(paymentTransactionId)
            order.paymentTransaction = paymentTransaction

            try {
                promoOrders.addBusinessObject(order)
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace()
            }
        }
    }

    static void writePromoOrders(Accounting accounting) {
        PromoOrders promoOrders = accounting.promoOrders
        File file = new File(ACCOUNTINGS_XML_FOLDER + accounting.name + "/" + PROMO_ORDERS + XML_EXTENSION)
        try {
            Writer writer = new FileWriter(file)
            writer.write(getXmlHeader(PROMO_ORDERS, 2))
            for (PromoOrder promoOrder : promoOrders.businessObjects) {
                writer.write(
                        "  <" + PROMO_ORDER + ">\n" +
                                "    <" + ID + ">" + promoOrder.id + "</" + ID + ">\n" +
                                "    <" + NAME + ">" + promoOrder.name + "</" + NAME + ">\n"
                )
                Transaction paymentTransaction = promoOrder.paymentTransaction
                if(paymentTransaction!=null) {
                    writer.write("    <" + PAYMENT_TRANSACTION + ">" + paymentTransaction.transactionId + "</" + PAYMENT_TRANSACTION + ">\n")
                } else {
                    writer.write("    <" + PAYMENT_TRANSACTION + ">null</" + PAYMENT_TRANSACTION + ">\n")
                }
                for (OrderItem orderItem : promoOrder.businessObjects) {
                    Article article = orderItem.article
                    writer.write(
                            "    <" + ARTICLE + ">\n" +
                                    "      <" + NAME + ">" + article.name + "</" + NAME + ">\n" +
                                    "      <" + NR_OF_ITEMS + ">" + orderItem.numberOfItems + "</" + NR_OF_ITEMS + ">\n" +
                                    "      <" + ITEMS_PER_UNIT + ">" + orderItem.itemsPerUnit + "</" + ITEMS_PER_UNIT + ">\n" +
                                    "      <" + PURCHASE_PRICE_UNIT + ">" + orderItem.getPurchasePriceForUnit() + "</" + PURCHASE_PRICE_UNIT + ">\n" +
                                    "      <" + PURCHASE_PRICE_ITEM + ">" + orderItem.getPurchasePriceForItem() + "</" + PURCHASE_PRICE_ITEM + ">\n" +
                                    "      <" + PURCHASE_PRICE_TOTAL + ">" + orderItem.getStockValue() + "</" + PURCHASE_PRICE_TOTAL + ">\n" +
                                    "    </" + ARTICLE + ">\n"
                    )
                }
                writer.write("  </" + PROMO_ORDER + ">\n")
            }
            writer.write("</" + PROMO_ORDERS + ">\n")
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(Accounts.class.name).log(Level.SEVERE, null, ex)
        }
    }
}
