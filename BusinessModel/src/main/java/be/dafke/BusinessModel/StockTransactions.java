package be.dafke.BusinessModel;

import java.util.ArrayList;

public class StockTransactions {
    private Journal purchaseJournal;
    private Journal salesJournal;
    private Journal salesNoInvoiceJournal;
    private Journal gainJournal;
    private Account stockAccount;
    private Account gainAccount;
    private Account salesAccount;
    private Account salesGainAccount;

    protected final ArrayList<Order> orders = new ArrayList<>();

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order order){
        orders.add(order);
        if(order instanceof SalesOrder){
            SalesOrder salesOrder = (SalesOrder) order;
            if(!salesOrder.isCreditNote()){
                salesOrder.getBusinessObjects().forEach(orderItem -> {
                    Article article = orderItem.getArticle();
                    int numberOfItems = orderItem.getNumberOfItems();
                    article.setSoDelivered(numberOfItems);
                });

            } else {
                salesOrder.getBusinessObjects().forEach(orderItem -> {
                    Article article = orderItem.getArticle();
                    int numberOfItems = orderItem.getNumberOfItems();
                    article.setSoCnDelivered(numberOfItems);
                });
            }
        }
        if (order instanceof PurchaseOrder){
            PurchaseOrder purchaseOrder = (PurchaseOrder) order;
            if(!purchaseOrder.isCreditNote()) {
                purchaseOrder.getBusinessObjects().forEach(orderItem -> {
                    Article article = orderItem.getArticle();
                    int numberOfItems = orderItem.getNumberOfItems();
                    article.setPoDelivered(numberOfItems);
                });
            } else {
                purchaseOrder.getBusinessObjects().forEach(orderItem -> {
                    Article article = orderItem.getArticle();
                    int numberOfItems = orderItem.getNumberOfItems();
                    article.setPoCnDelivered(numberOfItems);
                });
            }
        }
    }

    public Account getStockAccount() {
        return stockAccount;
    }

    public void setStockAccount(Account stockAccount) {
        this.stockAccount = stockAccount;
    }

    public Journal getSalesJournal() {
        return salesJournal;
    }

    public void setSalesJournal(Journal salesJournal) {
        this.salesJournal = salesJournal;
    }

    public Journal getSalesNoInvoiceJournal() {
        return salesNoInvoiceJournal;
    }

    public void setSalesNoInvoiceJournal(Journal salesNoInvoiceJournal) {
        this.salesNoInvoiceJournal = salesNoInvoiceJournal;
    }

    public Journal getGainJournal() {
        return gainJournal;
    }

    public void setGainJournal(Journal gainJournal) {
        this.gainJournal = gainJournal;
    }

    public Account getGainAccount() {
        return gainAccount;
    }

    public void setGainAccount(Account gainAccount) {
        this.gainAccount = gainAccount;
    }

    public Account getSalesAccount() {
        return salesAccount;
    }

    public void setSalesAccount(Account salesAccount) {
        this.salesAccount = salesAccount;
    }

    public Account getSalesGainAccount() {
        return salesGainAccount;
    }

    public void setSalesGainAccount(Account salesGainAccount) {
        this.salesGainAccount = salesGainAccount;
    }

    public Journal getPurchaseJournal() {
        return purchaseJournal;
    }

    public void setPurchaseJournal(Journal purchaseJournal) {
        this.purchaseJournal = purchaseJournal;
    }
}
