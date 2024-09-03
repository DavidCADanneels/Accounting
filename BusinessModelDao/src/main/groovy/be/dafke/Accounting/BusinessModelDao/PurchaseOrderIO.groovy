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
import static be.dafke.Utils.Utils.parseDouble

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

            boolean cn = getBooleanValue(orderElement, CREDIT_NOTE)
            order.creditNote = cn

            String invoicePath = getValue(orderElement, INVOICE_PATH)
            if(invoicePath){
                order.invoicePath = invoicePath
            }

            for (Element element : getChildren(orderElement, ARTICLE)) {
                String name = getValue(element, NAME)
                Article article = articles.getBusinessObject(name)

                String numberOfUnitsString = getValue(element, NR_OF_UNITS)
                int numberOfUnits = parseInt(numberOfUnitsString)

                String numberOfItemsString = getValue(element, NR_OF_ITEMS)
                int numberOfItems = parseInt(numberOfItemsString)

                String itemsPerUnitString = getValue(element, ITEMS_PER_UNIT)
                int itemsPerUnit = parseInt(itemsPerUnitString)

                String purchaseVatRateString = getValue(element, PURCHASE_VAT_RATE)
                int purchaseVatRate = parseDouble(purchaseVatRateString)

                String purchasePriceString = getValue(element, PURCHASE_PRICE)
                BigDecimal purchasePrice = parseBigDecimal(purchasePriceString)

                OrderItem orderItem = new OrderItem(numberOfItems, article, order)
                // should be:
//                OrderItem orderItem = new OrderItem(numberOfUnits, article, order)
                orderItem.itemsPerUnit = itemsPerUnit
                orderItem.numberOfUnits = numberOfUnits
                orderItem.calculateNumberOfItems()
                assert orderItem.numberOfItems == numberOfItems
                orderItem.setPurchaseVatRate(purchaseVatRate)
                orderItem.setPurchasePriceForUnit(purchasePrice)
                orderItem.setName(name)
                order.addBusinessObject(orderItem)
            }
            // Init
//            order.initializeAmountLeftToPay()

            String amountLeftString = getValue(orderElement, AMOUNT_LEFT)
            if(amountLeftString){
                BigDecimal amountLeft = parseBigDecimal(amountLeftString)
                if(amountLeft){
                    order.amountLeftToPay = amountLeft
                }
            }

            Transactions transactions = accounting.transactions
            int purchaseTransactionId = parseInt(getValue(orderElement, PURCHASE_TRANSACTION))
            Transaction purchaseTransaction = transactions.getBusinessObject(purchaseTransactionId)
            order.purchaseTransaction = purchaseTransaction
            purchaseTransaction?.order = order

            String paymentTransactionIds = getValue(orderElement, PAYMENT_TRANSACTION)
            if(paymentTransactionIds) {
                paymentTransactionIds.split(',').each { String paymentTransactionIdString ->
                    int paymentTransactionId = parseInt(paymentTransactionIdString)
                    if (paymentTransactionId != 0) {
                        Transaction paymentTransaction = transactions.getBusinessObject(paymentTransactionId)
                        if (paymentTransaction != null) {
                            order.paymentTransactions.add paymentTransaction
                            paymentTransaction.order = order
//                            order.amountLeftToPay = order.amountLeftToPay.subtract(paymentTransaction.debitTotal)
                        }
                    }
                }
            }

            if(order.supplier?.supplier) {
                order.supplier.supplier.purchaseOrders.add(order)
            }

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
                if(order.creditNote) writer.write """
    <$CREDIT_NOTE>$order.creditNote</$CREDIT_NOTE>"""
                if(order.invoicePath) writer.write """
    <$INVOICE_PATH>$order.invoicePath</$INVOICE_PATH>"""
                Transaction purchaseTransaction = order.purchaseTransaction
                if(purchaseTransaction) writer.write"""
    <$PURCHASE_TRANSACTION>$purchaseTransaction.transactionId</$PURCHASE_TRANSACTION>"""
                if(order.paymentTransactions){
                    String ids = order.paymentTransactions.collect{it.transactionId}.join(',')
                    writer.write """
    <$PAYMENT_TRANSACTION>$ids</$PAYMENT_TRANSACTION>"""
                }
                if(order.amountLeftToPay != BigDecimal.ZERO) {
                    writer.write """
    <$AMOUNT_LEFT>$order.amountLeftToPay</$AMOUNT_LEFT>"""
                }
                for (OrderItem orderItem : order.businessObjects) {
                    Article article = orderItem.article
                    writer.write """
    <$ARTICLE>
      <$NAME>$article.name</$NAME>
      <$NR_OF_ITEMS>$orderItem.numberOfItems</$NR_OF_ITEMS>
      <$NR_OF_UNITS>$orderItem.numberOfUnits</$NR_OF_UNITS>
      <$ITEMS_PER_UNIT>$orderItem.itemsPerUnit</$ITEMS_PER_UNIT>
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
