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

/**
 * Created by ddanneels on 15/01/2017.
 */
public class SalesOrderIO {
    public static void readSalesOrders(Accounting accounting){
        SalesOrders salesOrders = accounting.getSalesOrders();
        Contacts contacts = accounting.getContacts();
        Articles articles = accounting.getArticles();
        File xmlFile = new File(ACCOUNTINGS_XML_FOLDER +accounting.getName()+"/"+SALES_ORDERS + XML_EXTENSION);
        Element rootElement = getRootElement(xmlFile, SALES_ORDERS);
        Accounts accounts = accounting.getAccounts();
        Journals journals = accounting.getJournals();

        String journalNameSales = getValue(rootElement, SALES_JOURNAL);
        if(journalNameSales!=null){
            Journal journal = journals.getBusinessObject(journalNameSales);
            if (journal!=null) {
                salesOrders.setSalesJournal(journal);
            }
        }
        String journalNoInvoiceNameSales = getValue(rootElement, SALES_NO_INVOICE_JOURNAL);
        if(journalNoInvoiceNameSales!=null){
            Journal journal = journals.getBusinessObject(journalNoInvoiceNameSales);
            if (journal!=null) {
                salesOrders.setSalesNoInvoiceJournal(journal);
            }
        }
        String journalNameGain = getValue(rootElement, GAIN_JOURNAL);
        if(journalNameGain!=null){
            Journal journal = journals.getBusinessObject(journalNameGain);
            if (journal!=null) {
                salesOrders.setGainJournal(journal);
            }
        }
        String stockAccountString = getValue(rootElement, STOCK_ACCOUNT);
        if(stockAccountString!=null){
            Account account = accounts.getBusinessObject(stockAccountString);
            if (account!=null) {
                salesOrders.setStockAccount(account);
            }
        }
        String vatAccount = getValue(rootElement, VAT_ACCOUNT);
        if(vatAccount!=null){
            Account account = accounts.getBusinessObject(vatAccount);
            if (account!=null) {
                salesOrders.setVATAccount(account);
            }
        }
        String gainAccount = getValue(rootElement, GAIN_ACCOUNT);
        if(gainAccount !=null){
            Account account = accounts.getBusinessObject(gainAccount);
            if (account!=null) {
                salesOrders.setGainAccount(account);
            }
        }
        String salesAccount = getValue(rootElement, SALES_ACCOUNT);
        if(salesAccount !=null){
            Account account = accounts.getBusinessObject(salesAccount);
            if (account!=null) {
                salesOrders.setSalesAccount(account);
            }
        }
        for (Element salesOrderElement : getChildren(rootElement, SALES_ORDER)) {
            SalesOrder order = new SalesOrder();
            String customerString = getValue(salesOrderElement, CUSTOMER);
            Contact customer = contacts.getBusinessObject(customerString);
            order.setCustomer(customer);

            order.setPlaced(getBooleanValue(salesOrderElement, IS_PLACED));
            order.setDelivered(getBooleanValue(salesOrderElement, IS_DELIVERED));
            order.setInvoice(getBooleanValue(salesOrderElement, INVOICE));
            order.setInvoiceNumber(getValue(salesOrderElement, INVOICE_NUMBER));
            order.setPayed(getBooleanValue(salesOrderElement, IS_PAYED));

            for (Element element : getChildren(salesOrderElement, ARTICLE)) {

                // Fetch constructor arguments
                //
                String name = getValue(element, NAME);
                Article article = articles.getBusinessObject(name);
                ///
                String numberOfUnitsString = getValue(element, NR_OF_UNITS);
                int numberOfUnits = parseInt(numberOfUnitsString);
                //
                String numberOfItemsString = getValue(element, NR_OF_ITEMS);
                int numberOfItems = parseInt(numberOfItemsString);
                //
                // Create OrderItem
                //
                OrderItem orderItem = new OrderItem(numberOfUnits, numberOfItems, article);

                // set Unit Price
                //
                String salesPriceForUnitString = getValue(element, SALESPRICE_FOR_UNIT);
                BigDecimal salesPriceForUnit = null;
                if(salesPriceForUnitString != null) {
                    salesPriceForUnit = parseBigDecimal(salesPriceForUnitString);
                    if(salesPriceForUnit!=null){
                        orderItem.setSalesPriceForUnit(salesPriceForUnit);
                    }
                }

                // set Item Price
                //
                String salesPriceForItemString = getValue(element, SALESPRICE_FOR_ITEM);
                BigDecimal salesPriceForItem = null;
                if(salesPriceForItemString != null) {
                    salesPriceForItem = parseBigDecimal(salesPriceForItemString);
                    if (salesPriceForItem != null){
                        orderItem.setSalesPriceForItem(salesPriceForItem);
                    }
                }

                // set Item Price
                orderItem.setItemsPerUnit(parseInt(getValue(element, ITEMS_PER_UNIT)));

                // set Sales VAT Rate
                orderItem.setSalesVatRate(parseInt(getValue(element, SALES_VAT_RATE)));

                orderItem.setName(name);
                order.addBusinessObject(orderItem);
            }
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
            Journal salesJournal = salesOrders.getSalesJournal();
            Journal salesNoInvoiceJournal = salesOrders.getSalesNoInvoiceJournal();
            Journal gainJournal = salesOrders.getGainJournal();
            writer.write("  <" + SALES_JOURNAL + ">"+ (salesJournal==null?"null":salesJournal.getName())+"</" + SALES_JOURNAL + ">\n");
            writer.write("  <" + SALES_NO_INVOICE_JOURNAL + ">"+ (salesNoInvoiceJournal==null?"null":salesNoInvoiceJournal.getName())+"</" + SALES_NO_INVOICE_JOURNAL + ">\n");
            writer.write("  <" + GAIN_JOURNAL + ">"+ (gainJournal==null?"null":gainJournal.getName())+"</" + GAIN_JOURNAL + ">\n");
            writer.write("  <" + STOCK_ACCOUNT + ">"+salesOrders.getStockAccount()+"</" + STOCK_ACCOUNT + ">\n");
            writer.write("  <" + VAT_ACCOUNT + ">"+salesOrders.getVATAccount()+"</" + VAT_ACCOUNT + ">\n");
            writer.write("  <" + GAIN_ACCOUNT + ">"+salesOrders.getGainAccount()+"</" + GAIN_ACCOUNT + ">\n");
            writer.write("  <" + SALES_ACCOUNT + ">"+salesOrders.getSalesAccount()+"</" + SALES_ACCOUNT + ">\n");
            for (SalesOrder order : salesOrders.getBusinessObjects()) {
                writer.write(
                             "  <" + SALES_ORDER + ">\n" +
                                 "    <" + ID + ">" + order.getId() + "</" + ID + ">\n" +
                                 "    <" + CUSTOMER + ">" + order.getCustomer() + "</" + CUSTOMER + ">\n" +
                                 "    <" + IS_PLACED + ">" + order.isPlaced() + "</" + IS_PLACED + ">\n" +
                                 "    <" + IS_DELIVERED + ">" + order.isDelivered() + "</" + IS_DELIVERED + ">\n" +
                                 "    <" + IS_PAYED + ">" + order.isPayed() + "</" + IS_PAYED + ">\n" +
                                 "    <" + INVOICE + ">" + order.isInvoice() + "</" + INVOICE + ">\n"
                );
                if(order.getInvoiceNumber()!=null) {
                    writer.write("    <" + INVOICE_NUMBER + ">" + order.getInvoiceNumber() + "</" + INVOICE_NUMBER + ">\n");
                }
                Transaction salesTransaction = order.getSalesTransaction();
                if(salesTransaction!=null) {
                    writer.write("    <" + SALES_TRANSACTION + ">" + salesTransaction.getId() + "</" + SALES_TRANSACTION + ">\n");
                }
                Transaction gainTransaction = order.getGainTransaction();
                if(gainTransaction!=null) {
                    writer.write("    <" + GAIN_TRANSACTION + ">" + gainTransaction.getId() + "</" + GAIN_TRANSACTION + ">\n");
                }
                Transaction paymentTransaction = order.getPaymentTransaction();
                if(paymentTransaction!=null) {
                    writer.write("    <" + PAYMENT_TRANSACTION + ">" + paymentTransaction.getId() + "</" + PAYMENT_TRANSACTION + ">\n");
                }
                for (OrderItem orderItem : order.getBusinessObjects()) {
                    Article article = orderItem.getArticle();

                    // TODO: 1/ save OrderItem fields (I/O): itemsPerUnit, salesVatRate

                    writer.write(
                            "    <" + ARTICLE + ">\n" +
                                "      <" + NAME + ">" + article.getName() + "</" + NAME + ">\n" +
                                "      <" + NR_OF_UNITS + ">" + orderItem.getNumberOfUnits() + "</" + NR_OF_UNITS + ">\n" +
                                "      <" + NR_OF_ITEMS + ">" + orderItem.getNumberOfItems() + "</" + NR_OF_ITEMS + ">\n" +
                                "      <" + ITEMS_PER_UNIT + ">" + orderItem.getItemsPerUnit() + "</" + ITEMS_PER_UNIT + ">\n" +
                                "      <" + SALES_VAT_RATE + ">" + orderItem.getSalesVatRate() + "</" + SALES_VAT_RATE + ">\n" +
                                "      <" + SALESPRICE_FOR_UNIT + ">" + orderItem.getSalesPriceForUnit() + "</" + SALESPRICE_FOR_UNIT + ">\n" +
                                "      <" + SALESPRICE_FOR_ITEM + ">" + orderItem.getSalesPriceForItem() + "</" + SALESPRICE_FOR_ITEM + ">\n" +
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
                            "  <"+DATE+">" + salesOrder.getDate()+ "</"+DATE+">\n" +
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

                boolean listNrOfItems = true;
                int numberOfUnits = orderItem.getNumberOfUnits();
                int numberOfItems = orderItem.getNumberOfItems();
                Integer itemsPerUnit = article.getItemsPerUnit();

                if(numberOfUnits > 0 && Math.floorMod(numberOfItems,itemsPerUnit)==0){
                    listNrOfItems = false;
                }

                writer.write(
                            "    <" + ARTICLE + ">\n" +
                                "      <" + NAME + ">" + (listNrOfItems?article.getItemName():article.getName()) + "</" + NAME + ">\n" +
                                "      <" + NUMBER + ">" + (listNrOfItems?orderItem.getNumberOfItems():orderItem.getNumberOfUnits()) + "</" + NUMBER + ">\n" +
                                "      <" + UNIT_PRICE + ">" + (listNrOfItems?orderItem.getSalesPriceForItem():orderItem.getSalesPriceForUnit()) + "</" + UNIT_PRICE + ">\n" +
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
