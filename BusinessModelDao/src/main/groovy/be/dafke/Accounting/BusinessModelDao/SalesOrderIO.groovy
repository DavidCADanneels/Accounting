package be.dafke.Accounting.BusinessModelDao

import be.dafke.Accounting.BusinessModel.*
import be.dafke.Accounting.ObjectModel.Exceptions.DuplicateNameException
import be.dafke.Accounting.ObjectModel.Exceptions.EmptyNameException
import be.dafke.Utils.Utils
import org.w3c.dom.Element

import java.util.logging.Level
import java.util.logging.Logger

import static be.dafke.Accounting.BusinessModelDao.XMLConstants.*
import static be.dafke.Accounting.BusinessModelDao.XMLReader.*
import static be.dafke.Accounting.BusinessModelDao.XMLWriter.getXmlHeader
import static be.dafke.Utils.Utils.parseBigDecimal
import static be.dafke.Utils.Utils.parseInt 

class SalesOrderIO {
    static void readSalesOrders(Accounting accounting){
        PurchaseOrders purchaseOrders = accounting.purchaseOrders
        Contacts contacts = accounting.contacts
        Articles articles = accounting.articles
        File xmlFile = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$SALES_ORDERS$XML_EXTENSION")
        Element rootElement = getRootElement(xmlFile, SALES_ORDERS)

        SalesOrders salesOrders = accounting.salesOrders
        for (Element orderElement : getChildren(rootElement, SALES_ORDER)) {
            SalesOrder order = new SalesOrder()

            String idString = getValue(orderElement, ID)
            order.id = parseInt(idString)

//            String orderName = getValue(orderElement, NAME)
//            order.setName(orderName)

            boolean cn = getBooleanValue(orderElement, CREDIT_NOTE)
            order.creditNote = cn

            String customerString = getValue(orderElement, CUSTOMER)
            Contact customer = contacts.getBusinessObject(customerString)
            order.customer = customer

            order.invoice = getBooleanValue(orderElement, INVOICE)
            order.invoiceNumber = getValue(orderElement, INVOICE_NUMBER)
            String invoicePath = getValue(orderElement, INVOICE_PATH)
            if(invoicePath){
                order.invoicePath = invoicePath
            }

            for (Element element : getChildren(orderElement, ARTICLE)) {

                // Fetch constructor arguments
                //
                String name = getValue(element, NAME)
                Article article = articles.getBusinessObject(name)
                if(article == null){
                    System.err.println("$name not found in Articles")
                }
                //
                String numberOfItemsString = getValue(element, NR_OF_ITEMS)
                int numberOfItems = parseInt(numberOfItemsString)
                //
                // Create OrderItem
                //
                OrderItem orderItem = new OrderItem(numberOfItems, article, order)

                // set Item Price
                //
                String salesPriceForItemString = getValue(element, SALESPRICE_FOR_ITEM)
                BigDecimal salesPriceForItem = null
                if (salesPriceForItemString != null) {
                    salesPriceForItem = parseBigDecimal(salesPriceForItemString)
                    if (salesPriceForItem != null) {
                        orderItem.setSalesPriceForItem(salesPriceForItem)
                    }
                }

                // set Item Price
                orderItem.setItemsPerUnit(parseInt(getValue(element, ITEMS_PER_UNIT)))

                // set Sales VAT Rate
                orderItem.setSalesVatRate(parseInt(getValue(element, SALES_VAT_RATE)))

                String purchaseOrderForItemString = getValue(element, PURCHASE_ORDER)
                if (purchaseOrderForItemString != null) {
                    PurchaseOrder purchaseOrder = purchaseOrders.getBusinessObject(purchaseOrderForItemString)
                    orderItem.setPurchaseOrder(purchaseOrder)
                }

                orderItem.setName(name)
                order.addBusinessObject(orderItem)
            }
            Transactions transactions = accounting.transactions

            int salesTransactionId = parseInt(getValue(orderElement, SALES_TRANSACTION))
            Transaction salesTransaction = transactions.getBusinessObject salesTransactionId
            order.salesTransaction = salesTransaction
            salesTransaction?.order = order

            int paymentTransactionId = parseInt(getValue(orderElement, PAYMENT_TRANSACTION))
            Transaction paymentTransaction = transactions.getBusinessObject(paymentTransactionId)
            order.paymentTransaction = paymentTransaction
            paymentTransaction?.order = order

            int gainTransactionId = parseInt(getValue(orderElement, GAIN_TRANSACTION))
            Transaction gainTransaction = transactions.getBusinessObject(gainTransactionId)
            order.gainTransaction = gainTransaction
            gainTransaction?.order = order

            try {
                salesOrders.addBusinessObject(order)
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace()
            }
        }
    }

    static void writeSalesOrders(Accounting accounting) {
        SalesOrders salesOrders = accounting.salesOrders
        File file = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$SALES_ORDERS$XML_EXTENSION")
        try {
            Writer writer = new FileWriter(file)
            writer.write getXmlHeader(SALES_ORDERS, 2)
            for (SalesOrder salesOrder : salesOrders.businessObjects) {
                writer.write """\
  <$SALES_ORDER>
    <$ID>$salesOrder.id</$ID>
    <$NAME>$salesOrder.name</$NAME>
    <$CUSTOMER>$salesOrder.customer</$CUSTOMER>
    <$INVOICE>$salesOrder.invoice</$INVOICE>"""
                // only write if 'true' ('false' is default value)
                if(salesOrder.creditNote) writer.write """
    <$CREDIT_NOTE>$salesOrder.creditNote</$CREDIT_NOTE>"""
                if(salesOrder.invoiceNumber) writer.write """
    <$INVOICE_NUMBER>$salesOrder.invoiceNumber</$INVOICE_NUMBER>"""
                if(salesOrder.invoicePath) writer.write """
    <$INVOICE_PATH>$salesOrder.invoicePath</$INVOICE_PATH>"""
                Transaction salesTransaction = salesOrder.salesTransaction
                if(salesTransaction) writer.write """
    <$SALES_TRANSACTION>$salesTransaction.transactionId</$SALES_TRANSACTION>"""
                else writer.write """
    <$SALES_TRANSACTION>null</$SALES_TRANSACTION>"""
                Transaction gainTransaction = salesOrder.gainTransaction
                if(gainTransaction) writer.write """
    <$GAIN_TRANSACTION>$gainTransaction.transactionId</$GAIN_TRANSACTION>"""
                else writer.write """
    <$GAIN_TRANSACTION>null</$GAIN_TRANSACTION>"""
                Transaction paymentTransaction = salesOrder.paymentTransaction
                if(paymentTransaction) writer.write """
    <$PAYMENT_TRANSACTION>$paymentTransaction.transactionId</$PAYMENT_TRANSACTION>"""
                else writer.write """
    <$PAYMENT_TRANSACTION>null</$PAYMENT_TRANSACTION>"""
                for (OrderItem orderItem : salesOrder.businessObjects) {
                    Article article = orderItem.article

                    // TODO: 1/ save OrderItem fields (I/O): itemsPerUnit, salesVatRate

                    writer.write """
    <$ARTICLE>
      <$NAME>$article.name</$NAME>
      <$NR_OF_ITEMS>$orderItem.numberOfItems</$NR_OF_ITEMS>
      <$ITEMS_PER_UNIT>$orderItem.itemsPerUnit</$ITEMS_PER_UNIT>
      <$SALES_VAT_RATE>$orderItem.salesVatRate</$SALES_VAT_RATE>
      <$SALESPRICE_FOR_ITEM>$orderItem.salesPriceForItem</$SALESPRICE_FOR_ITEM>
      <$PURCHASE_ORDER>$orderItem.purchaseOrder</$PURCHASE_ORDER>
    </$ARTICLE>"""
                }
                writer.write """
  </$SALES_ORDER>
"""
            }
            writer.write """\
</$SALES_ORDERS>
"""
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(Accounts.class.name).log(Level.SEVERE, null, ex)
        }
    }

    static String calculatePdfPath(Accounting accounting, SalesOrder salesOrder){
        "$PDFPATH/$accounting.name/$INVOICES/${salesOrder.creditNote?'Creditnota':'Factuur'}-$salesOrder.invoiceNumber$PDF_EXTENSION"
    }

    static String writeInvoiceXmlInputFile(Accounting accounting, SalesOrder salesOrder){
        Integer id = salesOrder.id
        String idString = Utils.toIDString("SO", id, 6)

        String folderPath = "$ACCOUNTINGS_XML_PATH/$accounting.name/$INVOICES"
        File folder = new File(folderPath)
        folder.mkdirs()
        String path = "$folderPath/$idString$XML_EXTENSION"
        File file = new File(path)
        try {
            Writer writer = new FileWriter(file)
            writer.write getXmlHeader(INVOICE, 3)

            Contact supplier = salesOrder.supplier
            writer.write """\
  <InvoiceNumber>$salesOrder.invoiceNumber</InvoiceNumber>
  <$DATE>$salesOrder.invoiceDate</$DATE>
  <$DESCRIPTION>$salesOrder.invoiceDescription</$DESCRIPTION>
  <$SUPPLIER>
    <$NAME>$supplier.name</$NAME>
    <$OFFICIAL_NAME>$supplier.officialName</$OFFICIAL_NAME>
    <$STREET_AND_NUMBER>$supplier.streetAndNumber</$STREET_AND_NUMBER>
    <$POSTAL_CODE>$supplier.postalCode</$POSTAL_CODE>
    <$CITY>$supplier.city</$CITY>
    <$COUNTRY_CODE>$supplier.countryCode</$COUNTRY_CODE>
    <$VAT_NUMBER>$supplier.vatNumber</$VAT_NUMBER>
  </$SUPPLIER>"""

            Contact customer = salesOrder.customer
            writer.write """
  <$CUSTOMER>
    <$NAME>$customer.name</$NAME>
    <$OFFICIAL_NAME>$customer.officialName</$OFFICIAL_NAME>
    <$STREET_AND_NUMBER>$customer.streetAndNumber</$STREET_AND_NUMBER>
    <$POSTAL_CODE>$customer.postalCode</$POSTAL_CODE>
    <$CITY>$customer.city</$CITY>
    <$COUNTRY_CODE>$customer.countryCode</$COUNTRY_CODE>
    <$VAT_NUMBER>$customer.vatNumber</$VAT_NUMBER>
  </$CUSTOMER>"""

            ArrayList<Integer> vatRates = new ArrayList<>()

            writer.write """
  <$SALE>"""
            writer.write """
  <TotalPrice>$salesOrder.totalSalesPriceInclVat</TotalPrice>"""

            for (OrderItem orderItem : salesOrder.businessObjects) {
                Article article = orderItem.article
                Integer salesVatRate = article.salesVatRate
                if(!vatRates.contains(salesVatRate)){
                    vatRates.add(salesVatRate)
                }

                writer.write """
    <$ARTICLE>
          <$NAME>$article.name</$NAME>
          <$NUMBER>$orderItem.numberOfItems</$NUMBER>
          <$ITEM_PRICE>$orderItem.salesPriceForItem</$ITEM_PRICE>
          <$TAX_RATE>$salesVatRate</$TAX_RATE>
          <$TOTAL_PRICE>$orderItem.salesPriceWithVat</$TOTAL_PRICE>
        </$ARTICLE>"""
            }
            writer.write """
  </$SALE>"""

            writer.write """
  <$TOTALS>"""
            for (Integer vatRate:vatRates) {
                writer.write """
    <$TOTALS_LINE>
      <$TOTALS_LINE_EXCL>${salesOrder.getTotalSalesPriceExclVat(OrderItem.withSalesVatRate(vatRate))}</$TOTALS_LINE_EXCL>
      <$TOTALS_LINE_VAT_AMOUNT>${salesOrder.getTotalSalesVat(OrderItem.withSalesVatRate(vatRate))}</$TOTALS_LINE_VAT_AMOUNT>
      <$TOTALS_LINE_VAT_PCT>$vatRate</$TOTALS_LINE_VAT_PCT>
      <$TOTALS_LINE_INCL>${salesOrder.getTotalSalesPriceInclVat(OrderItem.withSalesVatRate(vatRate))}</$TOTALS_LINE_INCL>
    </$TOTALS_LINE>"""
            }
            writer.write """
    <$TOTAL_EXCL_VAT>$salesOrder.totalSalesPriceExclVat</$TOTAL_EXCL_VAT>
    <$TOTAL_VAT>$salesOrder.totalSalesVat</$TOTAL_VAT>
    <$TOTAL_INCL_VAT>$salesOrder.totalSalesPriceInclVat</$TOTAL_INCL_VAT>
  </$TOTALS>
</$INVOICE>"""

            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(Accounts.class.name).log(Level.SEVERE, null, ex)
        } finally {
            return path
        }
    }
}
