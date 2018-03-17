package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

public class SalesOrders extends BusinessCollection<SalesOrder>{

    private static int id = 0;
    private Account VATAccount;
    private Account stockAccount;
    private Journal salesJournal;
    private Journal gainJournal;
    private Account gainAccount;
    private Account salesAccount;

    public SalesOrders() {
        super();
    }

    public static void setId(int id) {
        SalesOrders.id = id;
    }

    public SalesOrder addBusinessObject(SalesOrder order) throws EmptyNameException, DuplicateNameException {
        return addBusinessObject(order, id+1);
    }
    public SalesOrder addBusinessObject(SalesOrder order, int newId) throws EmptyNameException, DuplicateNameException {
        if (order.getName()==null) {
            order.setName("SO" + newId);
        }
        id++;
        return super.addBusinessObject(order);
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
}
