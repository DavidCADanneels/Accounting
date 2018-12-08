package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.Utils.Utils;

public class SalesOrders extends BusinessCollection<SalesOrder>{

    private static int id = 0;
    private Account VATAccount;
    private Account stockAccount;
    private Journal salesJournal;
    private Journal salesNoInvoiceJournal;
    private Journal gainJournal;
    private Account gainAccount;
    private Account salesAccount;
    private Account salesGainAccount;

    public SalesOrders() {
        super();
    }

    public SalesOrder addBusinessObject(SalesOrder salesOrder) throws EmptyNameException, DuplicateNameException {
        if (salesOrder.getName()==null) {
            id++;
            salesOrder.setName(Utils.toIDString("SO", id, 3));
            salesOrder.setId(id);
        }
        salesOrder.addSalesOrderToArticles();
        return super.addBusinessObject(salesOrder);
    }

    public void removeBusinessObject(Order order){
        removeBusinessObject(order.getUniqueProperties());
    }

    public Account getVATAccount() {
        return VATAccount;
    }

    public Account getStockAccount() {
        return stockAccount;
    }

    public void setVATAccount(Account VATAccount) {
        this.VATAccount = VATAccount;
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

    public Journal getSalesNoInvoiceJournal() {
        return salesNoInvoiceJournal;
    }

    public void setSalesNoInvoiceJournal(Journal salesNoInvoiceJournal) {
        this.salesNoInvoiceJournal = salesNoInvoiceJournal;
    }

    public Account getSalesGainAccount() {
        return salesGainAccount;
    }

    public void setSalesGainAccount(Account salesGainAccount) {
        this.salesGainAccount = salesGainAccount;
    }
}
