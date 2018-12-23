package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

public class PurchaseOrders extends BusinessCollection<PurchaseOrder>{

    private static int id = 0;

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
}
