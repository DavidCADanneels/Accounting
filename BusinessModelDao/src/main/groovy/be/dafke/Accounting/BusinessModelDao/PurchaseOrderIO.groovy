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

class PurchaseOrderIO {
    static void readPurchaseOrders(Accounting accounting){
        PurchaseOrders purchaseOrders = accounting.purchaseOrders
        Contacts contacts = accounting.contacts
        Articles articles = accounting.articles
        File xmlFile = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$PURCHASE_ORDERS$XML_EXTENSION")
        Element rootElement = getRootElement(xmlFile, PURCHASE_ORDERS)

        for (Element orderElement : getChildren(rootElement, PURCHASE_ORDER)) {
            PurchaseOrder order = new PurchaseOrder()

            String idString = getValue(orderElement, ID)
            order.id = parseInt(idString)

//            String orderName = getValue(orderElement, NAME)
//            order.setName(orderName)

            String supplierString = getValue(orderElement, SUPPLIER)
            Contact supplier = contacts.getBusinessObject(supplierString)
            order.supplier = supplier

            for (Element element : getChildren(orderElement, ARTICLE)) {
                String name = getValue(element, NAME)
                Article article = articles.getBusinessObject(name)

                String numberOfItemsString = getValue(element, NR_OF_ITEMS)
                int numberOfItems = parseInt(numberOfItemsString)

                String purchaseVatRateString = getValue(element, PURCHASE_VAT_RATE)
                int purchaseVatRate = parseInt(purchaseVatRateString)

                String purchasePriceString = getValue(element, PURCHASE_PRICE)
                BigDecimal purchasePrice = parseBigDecimal(purchasePriceString)

                OrderItem orderItem = new OrderItem(numberOfItems, article, order)
                orderItem.setPurchaseVatRate(purchaseVatRate)
                orderItem.setPurchasePriceForUnit(purchasePrice)
                orderItem.setName(name)
                order.addBusinessObject(orderItem)
            }

            Transactions transactions = accounting.transactions
            int purchaseTransactionId = parseInt(getValue(orderElement, PURCHASE_TRANSACTION))
            Transaction purchaseTransaction = transactions.getBusinessObject(purchaseTransactionId)
            order.purchaseTransaction = purchaseTransaction

            int paymentTransactionId = parseInt(getValue(orderElement, PAYMENT_TRANSACTION))
            Transaction paymentTransaction = transactions.getBusinessObject(paymentTransactionId)
            order.paymentTransaction = paymentTransaction

            try {
                purchaseOrders.addBusinessObject(order)
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace()
            }
        }
    }

    static void writePurchasesOrders(Accounting accounting) {
        PurchaseOrders purchaseOrders = accounting.purchaseOrders
        File file = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$PURCHASE_ORDERS$XML_EXTENSION")
        try {
            Writer writer = new FileWriter(file)
            writer.write getXmlHeader(PURCHASE_ORDERS, 2)
            for (PurchaseOrder order : purchaseOrders.businessObjects) {
                writer.write """\
  <$PURCHASE_ORDER>
    <$ID>$order.id</$ID>
    <$NAME>$order.name</$NAME>
    <$SUPPLIER>$order.supplier</$SUPPLIER>"""
                Transaction purchaseTransaction = order.purchaseTransaction
                if(purchaseTransaction!=null) writer.write"""
    <$PURCHASE_TRANSACTION>$purchaseTransaction.transactionId</$PURCHASE_TRANSACTION>"""
                Transaction paymentTransaction = order.paymentTransaction
                if(paymentTransaction!=null) writer.write"""
    <$PAYMENT_TRANSACTION>$paymentTransaction.transactionId</$PAYMENT_TRANSACTION>"""

                for (OrderItem orderItem : order.businessObjects) {
                    Article article = orderItem.article
                    writer.write """
    <$ARTICLE>
      <$NAME>$article.name</$NAME>
      <$NR_OF_ITEMS>$orderItem.numberOfItems</$NR_OF_ITEMS>
      <$PURCHASE_VAT_RATE>$orderItem.purchaseVatRate</$PURCHASE_VAT_RATE>
      <$PURCHASE_PRICE>$orderItem.purchasePriceForUnit</$PURCHASE_PRICE>
    </$ARTICLE>"""
                }
                writer.write """
  </$PURCHASE_ORDER>
"""
            }
            writer.write """\
</$PURCHASE_ORDERS>
"""
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(Accounts.class.name).log(Level.SEVERE, null, ex)
        }
    }
}
