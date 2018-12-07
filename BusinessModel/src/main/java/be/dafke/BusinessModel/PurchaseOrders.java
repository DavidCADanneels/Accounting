package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

public class PurchaseOrders extends BusinessCollection<PurchaseOrder>{

    private static int id = 0;
    private Account VATAccount;
    private Account stockAccount;
    private Journal journal;

    public PurchaseOrders() {
        super();
    }

    public static void setId(int id) {
        PurchaseOrders.id = id;
    }

    public PurchaseOrder addBusinessObject(PurchaseOrder purchaseOrder) throws EmptyNameException, DuplicateNameException {
        if(purchaseOrder.getName()==null) {
            purchaseOrder.setName("PO" + ++id);
        }
        purchaseOrder.addPurchaseOrderToArticles();
        return super.addBusinessObject(purchaseOrder);
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

    public Journal getJournal() {
        return journal;
    }

    public void setJournal(Journal journal) {
        this.journal = journal;
    }
}
