package be.dafke.BusinessModel;

import java.util.ArrayList;

public class StockTransactions {
    private Account stockAccount;
    private Journal salesJournal;
    private Journal purchaseJournal;
    private Journal salesNoInvoiceJournal;
    private Journal gainJournal;
    private Account gainAccount;
    private Account salesAccount;
    private Account salesGainAccount;

    protected final ArrayList<Order> orders = new ArrayList<>();

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order order){
        orders.add(order);
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
