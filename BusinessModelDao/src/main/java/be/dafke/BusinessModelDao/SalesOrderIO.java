package be.dafke.BusinessModelDao;

import be.dafke.BusinessModel.*;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.Utils.Utils;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static be.dafke.BusinessModelDao.XMLConstants.*;
import static be.dafke.BusinessModelDao.XMLReader.*;
import static be.dafke.BusinessModelDao.XMLWriter.getXmlHeader;
import static be.dafke.Utils.Utils.parseBigDecimal;
import static be.dafke.Utils.Utils.parseInt;

public class SalesOrderIO {
    public static void readSalesOrders(Accounting accounting){
        PurchaseOrders purchaseOrders = accounting.getPurchaseOrders();
        Contacts contacts = accounting.getContacts();
        Articles articles = accounting.getArticles();
        File xmlFile = new File(ACCOUNTINGS_XML_FOLDER +accounting.getName()+"/"+SALES_ORDERS + XML_EXTENSION);
        Element rootElement = getRootElement(xmlFile, SALES_ORDERS);

        SalesOrders salesOrders = accounting.getSalesOrders();
        for (Element orderElement : getChildren(rootElement, SALES_ORDER)) {
            SalesOrder order = new SalesOrder();

            String idString = getValue(orderElement, ID);
            order.setId(parseInt(idString));

//            String orderName = getValue(orderElement, NAME);
//            order.setName(orderName);

            boolean cn = getBooleanValue(orderElement, CREDIT_NOTE);
            order.setCreditNote(cn);

            String customerString = getValue(orderElement, CUSTOMER);
            Contact customer = contacts.getBusinessObject(customerString);
            order.setCustomer(customer);

            order.setInvoice(getBooleanValue(orderElement, INVOICE));
            order.setInvoiceNumber(getValue(orderElement, INVOICE_NUMBER));

            for (Element element : getChildren(orderElement, ARTICLE)) {

                // Fetch constructor arguments
                //
                String name = getValue(element, NAME);
                Article article = articles.getBusinessObject(name);
                if(article == null){
                    System.err.println(name + " not found in Articles");
                }
                //
                String numberOfItemsString = getValue(element, NR_OF_ITEMS);
                int numberOfItems = parseInt(numberOfItemsString);
                //
                // Create OrderItem
                //
                OrderItem orderItem = new OrderItem(numberOfItems, article, order);

                // set Item Price
                //
                String salesPriceForItemString = getValue(element, SALESPRICE_FOR_ITEM);
                BigDecimal salesPriceForItem = null;
                if (salesPriceForItemString != null) {
                    salesPriceForItem = parseBigDecimal(salesPriceForItemString);
                    if (salesPriceForItem != null) {
                        orderItem.setSalesPriceForItem(salesPriceForItem);
                    }
                }

                // set Item Price
                orderItem.setItemsPerUnit(parseInt(getValue(element, ITEMS_PER_UNIT)));

                // set Sales VAT Rate
                orderItem.setSalesVatRate(parseInt(getValue(element, SALES_VAT_RATE)));

                String purchaseOrderForItemString = getValue(element, PURCHASE_ORDER);
                if (purchaseOrderForItemString != null) {
                    PurchaseOrder purchaseOrder = purchaseOrders.getBusinessObject(purchaseOrderForItemString);
                    orderItem.setPurchaseOrder(purchaseOrder);
                }

                orderItem.setName(name);
                order.addBusinessObject(orderItem);
            }
            Transactions transactions = accounting.getTransactions();
            int salesTransactionId = parseInt(getValue(orderElement, SALES_TRANSACTION));
            Transaction salesTransaction = transactions.getBusinessObject(salesTransactionId);
            order.setSalesTransaction(salesTransaction);

            int paymentTransactionId = parseInt(getValue(orderElement, PAYMENT_TRANSACTION));
            Transaction paymentTransaction = transactions.getBusinessObject(paymentTransactionId);
            order.setPaymentTransaction(paymentTransaction);

            int gainTransactionId = parseInt(getValue(orderElement, GAIN_TRANSACTION));
            Transaction gainTransaction = transactions.getBusinessObject(gainTransactionId);
            order.setGainTransaction(gainTransaction);

            try {
                salesOrders.addBusinessObject(order);
            } catch (EmptyNameException | DuplicateNameException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeSalesOrders(Accounting accounting) {
        SalesOrders salesOrders = accounting.getSalesOrders();
        File file = new File(ACCOUNTINGS_XML_FOLDER + accounting.getName() + "/" + SALES_ORDERS + XML_EXTENSION);
        try {
            Writer writer = new FileWriter(file);
            writer.write(getXmlHeader(SALES_ORDERS, 2));
            for (SalesOrder salesOrder : salesOrders.getBusinessObjects()) {
                writer.write(
                             "  <" + SALES_ORDER + ">\n" +
                                 "    <" + ID + ">" + salesOrder.getId() + "</" + ID + ">\n" +
                                 "    <" + NAME + ">" + salesOrder.getName() + "</" + NAME + ">\n" +
                                 "    <" + CUSTOMER + ">" + salesOrder.getCustomer() + "</" + CUSTOMER + ">\n" +
                                 "    <" + INVOICE + ">" + salesOrder.isInvoice() + "</" + INVOICE + ">\n"
                );
                if(salesOrder.isCreditNote()){
                    // only write if 'true' ('false' is default value)
                    writer.write(
                                    "    <" + CREDIT_NOTE + ">" + salesOrder.isCreditNote() + "</" + CREDIT_NOTE + ">\n"
                    );
                }
                if(salesOrder.getInvoiceNumber()!=null) {
                    writer.write("    <" + INVOICE_NUMBER + ">" + salesOrder.getInvoiceNumber() + "</" + INVOICE_NUMBER + ">\n");
                }
                Transaction salesTransaction = salesOrder.getSalesTransaction();
                if(salesTransaction!=null) {
                    writer.write("    <" + SALES_TRANSACTION + ">" + salesTransaction.getTransactionId() + "</" + SALES_TRANSACTION + ">\n");
                } else {
                    writer.write("    <" + SALES_TRANSACTION + ">null</" + SALES_TRANSACTION + ">\n");
                }
                Transaction gainTransaction = salesOrder.getGainTransaction();
                if(gainTransaction!=null) {
                    writer.write("    <" + GAIN_TRANSACTION + ">" + gainTransaction.getTransactionId() + "</" + GAIN_TRANSACTION + ">\n");
                } else {
                    writer.write("    <" + GAIN_TRANSACTION + ">null</" + GAIN_TRANSACTION + ">\n");
                }
                Transaction paymentTransaction = salesOrder.getPaymentTransaction();
                if(paymentTransaction!=null) {
                    writer.write("    <" + PAYMENT_TRANSACTION + ">" + paymentTransaction.getTransactionId() + "</" + PAYMENT_TRANSACTION + ">\n");
                } else {
                    writer.write("    <" + PAYMENT_TRANSACTION + ">null</" + PAYMENT_TRANSACTION + ">\n");
                }
                for (OrderItem orderItem : salesOrder.getBusinessObjects()) {
                    Article article = orderItem.getArticle();

                    // TODO: 1/ save OrderItem fields (I/O): itemsPerUnit, salesVatRate

                    writer.write(
                            "    <" + ARTICLE + ">\n" +
                                "      <" + NAME + ">" + article.getName() + "</" + NAME + ">\n" +
                                "      <" + NR_OF_ITEMS + ">" + orderItem.getNumberOfItems() + "</" + NR_OF_ITEMS + ">\n" +
                                "      <" + ITEMS_PER_UNIT + ">" + orderItem.getItemsPerUnit() + "</" + ITEMS_PER_UNIT + ">\n" +
                                "      <" + SALES_VAT_RATE + ">" + orderItem.getSalesVatRate() + "</" + SALES_VAT_RATE + ">\n" +
                                "      <" + SALESPRICE_FOR_ITEM + ">" + orderItem.getSalesPriceForItem() + "</" + SALESPRICE_FOR_ITEM + ">\n" +
                                "      <" + PURCHASE_ORDER + ">" + orderItem.getPurchaseOrder() + "</" + PURCHASE_ORDER + ">\n" +
                                "    </" + ARTICLE + ">\n"
                    );
                }
                writer.write("  </" + SALES_ORDER + ">\n");
            }
            writer.write("</" + SALES_ORDERS + ">\n");
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Accounts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String calculatePdfPath(Accounting accounting, SalesOrder salesOrder){
        return ACCOUNTINGS_XML_FOLDER + accounting.getName() + "/" + INVOICES + "/Factuur-" + salesOrder.getInvoiceNumber() + PDF_EXTENSION;
    }

    public static String writeInvoiceXmlInputFile(Accounting accounting, SalesOrder salesOrder){
        Integer id = salesOrder.getId();
        String idString = Utils.toIDString("SO", id, 6);

        String folderPath = ACCOUNTINGS_XML_FOLDER + accounting.getName() + "/" + INVOICES;
        File folder = new File(folderPath);
        folder.mkdirs();
        String path = folderPath + "/" + idString + XML_EXTENSION;
        File file = new File(path);
        try {
            Writer writer = new FileWriter(file);
            writer.write(getXmlHeader(INVOICE, 3));

            Contact supplier = salesOrder.getSupplier();
            writer.write(
                            "  <InvoiceNumber>" + salesOrder.getInvoiceNumber()+ "</InvoiceNumber>\n" +
                            "  <"+DATE+">" + salesOrder.getDeliveryDate()+ "</"+DATE+">\n" +
                            "  <"+DESCRIPTION+">" + salesOrder.getDescription()+ "</"+DESCRIPTION+">\n" +
                            "  <" + SUPPLIER + ">\n" +
                            "    <" + NAME + ">" + supplier.getName() + "</" + NAME + ">\n" +
                            "    <" + OFFICIAL_NAME + ">" + supplier.getOfficialName() + "</" + OFFICIAL_NAME + ">\n" +
                            "    <" + STREET_AND_NUMBER + ">" + supplier.getStreetAndNumber() + "</" + STREET_AND_NUMBER + ">\n" +
                            "    <" + POSTAL_CODE + ">" + supplier.getPostalCode() + "</" + POSTAL_CODE + ">\n" +
                            "    <" + CITY + ">" + supplier.getCity() + "</" + CITY + ">\n" +
                            "    <" + COUNTRY_CODE + ">" + supplier.getCountryCode() + "</" + COUNTRY_CODE + ">\n" +
                            "    <" + VAT_NUMBER + ">" + supplier.getVatNumber() + "</" + VAT_NUMBER + ">\n" +
                            "  </" + SUPPLIER + ">\n"
            );

            Contact customer = salesOrder.getCustomer();
            writer.write(
                    "  <" + CUSTOMER + ">\n" +
                            "    <" + NAME + ">" + customer.getName() + "</" + NAME + ">\n" +
                            "    <" + OFFICIAL_NAME + ">" + customer.getOfficialName() + "</" + OFFICIAL_NAME + ">\n" +
                            "    <" + STREET_AND_NUMBER + ">" + customer.getStreetAndNumber() + "</" + STREET_AND_NUMBER + ">\n" +
                            "    <" + POSTAL_CODE + ">" + customer.getPostalCode() + "</" + POSTAL_CODE + ">\n" +
                            "    <" + CITY + ">" + customer.getCity() + "</" + CITY + ">\n" +
                            "    <" + COUNTRY_CODE + ">" + customer.getCountryCode() + "</" + COUNTRY_CODE + ">\n" +
                            "    <" + VAT_NUMBER + ">" + customer.getVatNumber() + "</" + VAT_NUMBER + ">\n" +
                            "  </" + CUSTOMER + ">\n"
            );

            ArrayList<Integer> vatRates = new ArrayList<>();

            writer.write("  <"+SALE+">\n");
            writer.write("  <TotalPrice>" + salesOrder.getTotalSalesPriceInclVat()+ "</TotalPrice>\n");

            for (OrderItem orderItem : salesOrder.getBusinessObjects()) {
                Article article = orderItem.getArticle();
                Integer salesVatRate = article.getSalesVatRate();
                if(!vatRates.contains(salesVatRate)){
                    vatRates.add(salesVatRate);
                }

                writer.write(
                            "    <" + ARTICLE + ">\n" +
                                "      <" + NAME + ">" + (article.getName()) + "</" + NAME + ">\n" +
                                "      <" + NUMBER + ">" + (orderItem.getNumberOfItems()) + "</" + NUMBER + ">\n" +
                                "      <" + ITEM_PRICE + ">" + orderItem.getSalesPriceForItem() + "</" + ITEM_PRICE + ">\n" +
                                "      <" + TAX_RATE + ">" + salesVatRate + "</" + TAX_RATE + ">\n" +
                                "      <" + TOTAL_PRICE + ">" + orderItem.getSalesPriceWithVat() + "</" + TOTAL_PRICE + ">\n" +
                                "    </" + ARTICLE + ">\n"
                );
            }
            writer.write("  </"+SALE+">\n");

            writer.write("  <"+TOTALS+">\n");
            for (Integer vatRate:vatRates) {
                writer.write("    <"+TOTALS_LINE+">\n");
                writer.write("      <"+TOTALS_LINE_EXCL+">" + salesOrder.getTotalSalesPriceExclVat(OrderItem.withSalesVatRate(vatRate)) + "</"+TOTALS_LINE_EXCL+">"+"\n");
                writer.write("      <"+TOTALS_LINE_VAT_AMOUNT+">" + salesOrder.getTotalSalesVat(OrderItem.withSalesVatRate(vatRate)) + "</"+TOTALS_LINE_VAT_AMOUNT+">"+"\n");
                writer.write("      <"+TOTALS_LINE_VAT_PCT+">" + vatRate + "</"+TOTALS_LINE_VAT_PCT+">"+"\n");
                writer.write("      <"+TOTALS_LINE_INCL+">" + salesOrder.getTotalSalesPriceInclVat(OrderItem.withSalesVatRate(vatRate)) + "</"+TOTALS_LINE_INCL+">"+"\n");
                writer.write("    </"+TOTALS_LINE+">\n");
            }
            writer.write("    <"+TOTAL_EXCL_VAT+">" + salesOrder.getTotalSalesPriceExclVat() + "</"+TOTAL_EXCL_VAT+">"+"\n");
            writer.write("    <"+TOTAL_VAT+">" + salesOrder.getTotalSalesVat() + "</"+TOTAL_VAT+">"+"\n");
            writer.write("    <"+TOTAL_INCL_VAT+">" + salesOrder.getTotalSalesPriceInclVat() + "</"+TOTAL_INCL_VAT+">"+"\n");
            writer.write("  </"+TOTALS+">\n");
            writer.write("</"+INVOICE+">\n");

            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Accounts.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return path;
        }
    }
}
