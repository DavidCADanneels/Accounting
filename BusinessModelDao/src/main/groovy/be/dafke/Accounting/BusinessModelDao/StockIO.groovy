package be.dafke.Accounting.BusinessModelDao

import be.dafke.Accounting.BusinessModel.*
import org.w3c.dom.Element

import java.util.logging.Level
import java.util.logging.Logger

import static be.dafke.Accounting.BusinessModelDao.XMLConstants.*
import static be.dafke.Accounting.BusinessModelDao.XMLReader.*
import static be.dafke.Accounting.BusinessModelDao.XMLWriter.getXmlHeader 

class StockIO {
    static void readStockSettings(Accounting accounting) {
        StockTransactions stockTransactions = accounting.stockTransactions
        File xmlFile = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$STOCK_TRANSACTIONS$XML_EXTENSION")
        Element transactionsElement = getRootElement(xmlFile, STOCK_TRANSACTIONS)

        Accounts accounts = accounting.accounts
        Journals journals = accounting.journals

        String stockAccountString = getValue(transactionsElement, STOCK_ACCOUNT)
        if (stockAccountString != null) {
            stockTransactions.setStockAccount(accounts.getBusinessObject(stockAccountString))
        }

        String purchaseJournalString = getValue(transactionsElement, PURCHASE_JOURNAL)
        if (purchaseJournalString != null) {
            Journal journal = journals.getBusinessObject(purchaseJournalString)
            if(journal!=null) {
                stockTransactions.setPurchaseJournal(journal)
            }
        }

        String journalNameSales = getValue(transactionsElement, SALES_JOURNAL)
        if (journalNameSales != null) {
            Journal journal = journals.getBusinessObject(journalNameSales)
            if (journal != null) {
                stockTransactions.setSalesJournal(journal)
            }
        }
        String journalNoInvoiceNameSales = getValue(transactionsElement, SALES_NO_INVOICE_JOURNAL)
        if (journalNoInvoiceNameSales != null) {
            Journal journal = journals.getBusinessObject(journalNoInvoiceNameSales)
            if (journal != null) {
                stockTransactions.setSalesNoInvoiceJournal(journal)
            }
        }
        String journalNameGain = getValue(transactionsElement, GAIN_JOURNAL)
        if (journalNameGain != null) {
            Journal journal = journals.getBusinessObject(journalNameGain)
            if (journal != null) {
                stockTransactions.setGainJournal(journal)
            }
        }
        String gainAccount = getValue(transactionsElement, GAIN_ACCOUNT)
        if (gainAccount != null) {
            Account account = accounts.getBusinessObject(gainAccount)
            if (account != null) {
                stockTransactions.setGainAccount(account)
            }
        }
        String salesAccount = getValue(transactionsElement, SALES_ACCOUNT)
        if (salesAccount != null) {
            Account account = accounts.getBusinessObject(salesAccount)
            if (account != null) {
                stockTransactions.setSalesAccount(account)
            }
        }
        String salesGainAccount = getValue(transactionsElement, SALES_GAIN_ACCOUNT)
        if (salesGainAccount != null) {
            Account account = accounts.getBusinessObject(salesGainAccount)
            if (account != null) {
                stockTransactions.setSalesGainAccount(account)
            }
        }
        String promoAccount = getValue(transactionsElement, PROMO_ACCOUNT)
        if (promoAccount != null) {
            Account account = accounts.getBusinessObject(promoAccount)
            if (account != null) {
                stockTransactions.setPromoAccount(account)
            }
        }

    }

    static void readStockTransactions(Accounting accounting){
        StockTransactions stockTransactions = accounting.stockTransactions

        File xmlFile = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$STOCK_TRANSACTIONS$XML_EXTENSION")
        Element transactionsElement = getRootElement(xmlFile, STOCK_TRANSACTIONS)

        for (Element element : getChildren(transactionsElement, STOCK_TRANSACTION)) {
            String name = getValue(element, NAME)
            String type = getValue(element, TYPE)
            String date = getValue(element, DATE)
            String description = getValue(element, DESCRIPTION)

            if(type!=null&&type.equals(PURCHASE_ORDER)){
                PurchaseOrders purchaseOrders = accounting.purchaseOrders
                PurchaseOrder purchaseOrder = purchaseOrders.getBusinessObject name
                stockTransactions.addOrder purchaseOrder
                purchaseOrder.description = description
                purchaseOrder.deliveryDate = date
            } else if(type!=null&&type.equals(SALES_ORDER)){
                SalesOrders salesOrders = accounting.salesOrders
                SalesOrder salesOrder = salesOrders.getBusinessObject name
                stockTransactions.addOrder salesOrder
                salesOrder.description = description
                salesOrder.deliveryDate = date
            } else if(type!=null&&type.equals(STOCK_ORDER)){
                StockOrders stockOrders = accounting.stockOrders
                StockOrder stockOrder = stockOrders.getBusinessObject name
                stockTransactions.addOrder stockOrder
                stockOrder.description = description
                stockOrder.deliveryDate = date
            } else if(type!=null&&type.equals(PROMO_ORDER)){
                PromoOrders promoOrders = accounting.promoOrders
                PromoOrder promoOrder = promoOrders.getBusinessObject name
                stockTransactions.addOrder promoOrder
                promoOrder.description = description
                promoOrder.deliveryDate = date
            }
        }
    }

    static void writeStock(Accounting accounting) {
        Articles articles = accounting.articles
        StockTransactions stockTransactions = accounting.stockTransactions
        File file = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$STOCK$XML_EXTENSION")
        try {
            Writer writer = new FileWriter(file)
            writer.write getXmlHeader(STOCK, 2)

            writer.write"""\
  <$ARTICLES>"""
            for (Article article : articles.getBusinessObjects()) {
                if(stockTransactions.getNrInStock(article)!=0) {
                    writer.write """
    <$ARTICLE>
      <$NAME>${article.name}</$NAME>
      <$NR_OF_ITEMS>${stockTransactions.getNrInStock(article)}</$NR_OF_ITEMS>
    </$ARTICLE>"""
                }
            }
            writer.write """
  </$ARTICLES>
</$STOCK>
"""
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(Accounts.class.name).log(Level.SEVERE, null, ex)
        }
    }

    static void writeStockTransactions(Accounting accounting) {
        StockTransactions stockTransactions = accounting.stockTransactions
        File file = new File("$ACCOUNTINGS_XML_PATH/$accounting.name/$STOCK_TRANSACTIONS$XML_EXTENSION")
        try {
            Writer writer = new FileWriter(file)
            writer.write getXmlHeader(STOCK_TRANSACTIONS, 2)

            Account stockAccount = stockTransactions.stockAccount
            Journal salesJournal = stockTransactions.salesJournal
            Journal purchaseJournal = stockTransactions.purchaseJournal
            Journal salesNoInvoiceJournal = stockTransactions.salesNoInvoiceJournal
            Journal gainJournal = stockTransactions.gainJournal
            Account gainAccount = stockTransactions.gainAccount
            Account salesAccount = stockTransactions.salesAccount
            Account salesGainAccount = stockTransactions.salesGainAccount
            Account salesPromoAccount = stockTransactions.promoAccount

            writer.write"""\
  <${PURCHASE_JOURNAL}>${(purchaseJournal==null?"null":purchaseJournal.name)}</${PURCHASE_JOURNAL}>
  <$STOCK_ACCOUNT>${(stockAccount==null?"null":stockAccount.name)}</$STOCK_ACCOUNT>
  <$SALES_JOURNAL>${(salesJournal==null?"null":salesJournal.name)}</$SALES_JOURNAL>
  <$SALES_NO_INVOICE_JOURNAL>${(salesNoInvoiceJournal==null?"null":salesNoInvoiceJournal.name)}</$SALES_NO_INVOICE_JOURNAL>
  <$GAIN_JOURNAL>${(gainJournal==null?"null":gainJournal.name)}</$GAIN_JOURNAL>
  <$GAIN_ACCOUNT>${(gainAccount==null?"null":gainAccount)}</$GAIN_ACCOUNT>
  <$SALES_ACCOUNT>${(salesAccount==null?"null":salesAccount)}</$SALES_ACCOUNT>
  <$SALES_GAIN_ACCOUNT>${(salesGainAccount==null?"null":salesGainAccount)}</$SALES_GAIN_ACCOUNT>
  <$PROMO_ACCOUNT>${(salesPromoAccount==null?"null":salesPromoAccount)}</$PROMO_ACCOUNT>"""

            for (Order order : stockTransactions.getOrders()) {
                writer.write """
  <${STOCK_TRANSACTION}>
    <$NAME>${order.name}</$NAME>
    <$DATE>${order.deliveryDate}</$DATE>
    <$DESCRIPTION>${order.description}</$DESCRIPTION>"""
                if (order instanceof PurchaseOrder){
                    writer.write"""
    <$TYPE>$PURCHASE_ORDER</$TYPE>"""
                }
                if (order instanceof SalesOrder){
                    writer.write"""
    <$TYPE>$SALES_ORDER</$TYPE>"""
                }
                if (order instanceof StockOrder){
                    writer.write"""
    <$TYPE>$STOCK_ORDER</$TYPE>"""
                }
                if (order instanceof PromoOrder){
                    writer.write"""
    <$TYPE>$PROMO_ORDER</$TYPE>"""
                }
                writer.write"""
  </$STOCK_TRANSACTION>"""
            }
            writer.write"""
</$STOCK_TRANSACTIONS>
"""
            writer.flush()
            writer.close()
        } catch (IOException ex) {
            Logger.getLogger(Accounts.class.name).log(Level.SEVERE, null, ex)
        }
    }
}
