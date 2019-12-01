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
        File xmlFile = new File(ACCOUNTINGS_XML_FOLDER +accounting.name+"/"+SALES_ORDERS + XML_EXTENSION)
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

            for (Element element : getChildren(orderElement, ARTICLE)) {

                // Fetch constructor arguments
                //
                String name = getValue(element, NAME)
                Article article = articles.getBusinessObject(name)
                if(article == null){
                    System.err.println(name + " not found in Articles")
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

            int paymentTransactionId = parseInt(getValue(orderElement, PAYMENT_TRANSACTION))
            Transaction paymentTransaction = transactions.getBusinessObject(paymentTransactionId)
            order.paymentTransaction = paymentTransaction

            int gainTransactionId = parseInt(getValue(orderElement, GAIN_TRANSACTION))
            Transaction gainTransaction = transactions.getBusinessObject(gainTransactionId)
            order.gainTransaction = gainTransaction

            try {
                salesOrders.addBusinessObject(order)
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace()
            }
        }
    }

    static void writeSalesOrders(Accounting accounting) {
        SalesOrders salesOrders = accounting.salesOrders
        File file = new File(ACCOUNTINGS_XML_FOLDER + accounting.name + "/" + SALES_ORDERS + XML_EXTENSION)
        try {
            Writer writer = new FileWriter(file)
            writer.write(getXmlHeader(SALES_ORDERS, 2))
            for (SalesOrder salesOrder : salesOrders.businessObjects) {
                writer.write(
                        "  <" + SALES_ORDER + ">\n" +
                                "    <" + ID + ">" + salesOrder.id + "</" + ID + ">\n" +
                                "    <" + NAME + ">" + salesOrder.name + "</" + NAME + ">\n" +
                                "    <" + CUSTOMER + ">" + salesOrder.customer + "</" + CUSTOMER + ">\n" +
                                "    <" + INVOICE + ">" + salesOrder.invoice + "</" + INVOICE + ">\n"
                )
                if(salesOrder.creditNote){
                    // only write if 'true' ('false' is default value)
                    writer.write(
                            "    <" + CREDIT_NOTE + ">" + salesOrder.creditNote + "</" + CREDIT_NOTE + ">\n"
                    )
                }
                if(salesOrder.invoiceNumber!=null) {
                    writer.write("    <" + INVOICE_NUMBER + ">" + salesOrder.invoiceNumber + "</" + INVOICE_NUMBER + ">\n")
                }
                Transaction salesTransaction = salesOrder.salesTransaction
                if(salesTransaction!=null) {
                    writer.write("    <" + SALES_TRANSACTION + ">" + salesTransaction.transactionId + "</" + SALES_TRANSACTION + ">\n")
                } else {
                    writer.write("    <" + SALES_TRANSACTION + ">null</" + SALES_TRANSACTION + ">\n")
                }
                Transaction gainTransaction = salesOrder.gainTransaction
                if(gainTransaction!=null) {
                    writer.write("    <" + GAIN_TRANSACTION + ">" + gainTransaction.transactionId + "</" + GAIN_TRANSACTION + ">\n")
                } else {
                    writer.write("    <" + GAIN_TRANSACTION + ">null</" + GAIN_TRANSACTION + ">\n")
                }
                Transaction paymentTransaction = salesOrder.paymentTransaction
                if(paymentTransaction!=null) {
                    writer.write("    <" + PAYMENT_TRANSACTION + ">" + paymentTransaction.transactionId + "</" + PAYMENT_TRANSACTION + ">\n")
                } else {
                    writer.write("    <" + PAYMENT_TRANSACTION + ">null</" + PAYMENT_TRANSACTION + ">\n")
                }
                for (OrderItem orderItem : salesOrder.businessObjects) {
                    Article article = orderItem.article

                    // TODO: 1/ save OrderItem fields (I/O): itemsPerUnit, salesVatRate

                    writer.write(
                            "    <" + ARTICLE + ">\n" +
                                    "      <" + NAME + ">" + article.name + "</" + NAME + ">\n" +
                                    "      <" + NR_OF_ITEMS + ">" + orderItem.numberOfItems + "</" + NR_OF_ITEMS + ">\n" +
                                    "      <" + ITEMS_PER_UNIT + ">" + orderItem.itemsPerUnit + "</" + ITEMS_PER_UNIT + ">\n" +
                                    "      <" + SALES_VAT_RATE + ">" + orderItem.salesVatRate + "</" + SALES_VAT_RATE + ">\n" +
                                    "      <" + SALESPRICE_FOR_ITEM + ">" + orderItem.salesPriceForItem + "</" + SALESPRICE_FOR_ITEM + ">\n" +
                                    "      <" + PURCHASE_ORDER + ">" + orderItem.purchaseOrder + "</" + PURCHASE_ORDER + ">\n" +
                                    "    </" + ARTICLE + ">\n"
                    )
                }
                writer.write("  </" + SALES_ORDER + ">\n")
            }
            writer.write("</" + SALES_ORDERS + ">\n")
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(Accounts.class.name).log(Level.SEVERE, null, ex)
        }
    }

    static String calculatePdfPath(Accounting accounting, SalesOrder salesOrder){
        ACCOUNTINGS_XML_FOLDER + accounting.name + "/" + INVOICES + "/Factuur-" + salesOrder.invoiceNumber + PDF_EXTENSION
    }

    static String writeInvoiceXmlInputFile(Accounting accounting, SalesOrder salesOrder){
        Integer id = salesOrder.id
        String idString = Utils.toIDString("SO", id, 6)

        String folderPath = ACCOUNTINGS_XML_FOLDER + accounting.name + "/" + INVOICES
        File folder = new File(folderPath)
        folder.mkdirs()
        String path = folderPath + "/" + idString + XML_EXTENSION
        File file = new File(path)
        try {
            Writer writer = new FileWriter(file)
            writer.write(getXmlHeader(INVOICE, 3))

            Contact supplier = salesOrder.supplier
            writer.write(
                    "  <InvoiceNumber>" + salesOrder.invoiceNumber+ "</InvoiceNumber>\n" +
                            "  <"+DATE+">" + salesOrder.deliveryDate+ "</"+DATE+">\n" +
                            "  <"+DESCRIPTION+">" + salesOrder.description+ "</"+DESCRIPTION+">\n" +
                            "  <" + SUPPLIER + ">\n" +
                            "    <" + NAME + ">" + supplier.name + "</" + NAME + ">\n" +
                            "    <" + OFFICIAL_NAME + ">" + supplier.officialName + "</" + OFFICIAL_NAME + ">\n" +
                            "    <" + STREET_AND_NUMBER + ">" + supplier.streetAndNumber + "</" + STREET_AND_NUMBER + ">\n" +
                            "    <" + POSTAL_CODE + ">" + supplier.postalCode + "</" + POSTAL_CODE + ">\n" +
                            "    <" + CITY + ">" + supplier.city + "</" + CITY + ">\n" +
                            "    <" + COUNTRY_CODE + ">" + supplier.countryCode + "</" + COUNTRY_CODE + ">\n" +
                            "    <" + VAT_NUMBER + ">" + supplier.vatNumber + "</" + VAT_NUMBER + ">\n" +
                            "  </" + SUPPLIER + ">\n"
            )

            Contact customer = salesOrder.customer
            writer.write(
                    "  <" + CUSTOMER + ">\n" +
                            "    <" + NAME + ">" + customer.name + "</" + NAME + ">\n" +
                            "    <" + OFFICIAL_NAME + ">" + customer.officialName + "</" + OFFICIAL_NAME + ">\n" +
                            "    <" + STREET_AND_NUMBER + ">" + customer.streetAndNumber + "</" + STREET_AND_NUMBER + ">\n" +
                            "    <" + POSTAL_CODE + ">" + customer.postalCode + "</" + POSTAL_CODE + ">\n" +
                            "    <" + CITY + ">" + customer.city + "</" + CITY + ">\n" +
                            "    <" + COUNTRY_CODE + ">" + customer.countryCode + "</" + COUNTRY_CODE + ">\n" +
                            "    <" + VAT_NUMBER + ">" + customer.vatNumber + "</" + VAT_NUMBER + ">\n" +
                            "  </" + CUSTOMER + ">\n"
            )

            ArrayList<Integer> vatRates = new ArrayList<>()

            writer.write("  <"+SALE+">\n")
            writer.write("  <TotalPrice>" + salesOrder.totalSalesPriceInclVat+ "</TotalPrice>\n")

            for (OrderItem orderItem : salesOrder.businessObjects) {
                Article article = orderItem.article
                Integer salesVatRate = article.salesVatRate
                if(!vatRates.contains(salesVatRate)){
                    vatRates.add(salesVatRate)
                }

                writer.write(
                        "    <" + ARTICLE + ">\n" +
                                "      <" + NAME + ">" + (article.name) + "</" + NAME + ">\n" +
                                "      <" + NUMBER + ">" + (orderItem.numberOfItems) + "</" + NUMBER + ">\n" +
                                "      <" + ITEM_PRICE + ">" + orderItem.salesPriceForItem + "</" + ITEM_PRICE + ">\n" +
                                "      <" + TAX_RATE + ">" + salesVatRate + "</" + TAX_RATE + ">\n" +
                                "      <" + TOTAL_PRICE + ">" + orderItem.salesPriceWithVat + "</" + TOTAL_PRICE + ">\n" +
                                "    </" + ARTICLE + ">\n"
                )
            }
            writer.write("  </"+SALE+">\n")

            writer.write("  <"+TOTALS+">\n")
            for (Integer vatRate:vatRates) {
                writer.write("    <"+TOTALS_LINE+">\n")
                writer.write("      <"+TOTALS_LINE_EXCL+">" + salesOrder.getTotalSalesPriceExclVat(OrderItem.withSalesVatRate(vatRate)) + "</"+TOTALS_LINE_EXCL+">"+"\n")
                writer.write("      <"+TOTALS_LINE_VAT_AMOUNT+">" + salesOrder.getTotalSalesVat(OrderItem.withSalesVatRate(vatRate)) + "</"+TOTALS_LINE_VAT_AMOUNT+">"+"\n")
                writer.write("      <"+TOTALS_LINE_VAT_PCT+">" + vatRate + "</"+TOTALS_LINE_VAT_PCT+">"+"\n")
                writer.write("      <"+TOTALS_LINE_INCL+">" + salesOrder.getTotalSalesPriceInclVat(OrderItem.withSalesVatRate(vatRate)) + "</"+TOTALS_LINE_INCL+">"+"\n")
                writer.write("    </"+TOTALS_LINE+">\n")
            }
            writer.write("    <"+TOTAL_EXCL_VAT+">" + salesOrder.totalSalesPriceExclVat + "</"+TOTAL_EXCL_VAT+">"+"\n")
            writer.write("    <"+TOTAL_VAT+">" + salesOrder.totalSalesVat + "</"+TOTAL_VAT+">"+"\n")
            writer.write("    <"+TOTAL_INCL_VAT+">" + salesOrder.totalSalesPriceInclVat + "</"+TOTAL_INCL_VAT+">"+"\n")
            writer.write("  </"+TOTALS+">\n")
            writer.write("</"+INVOICE+">\n")

            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(Accounts.class.name).log(Level.SEVERE, null, ex)
        } finally {
            path
        }
    }
}
