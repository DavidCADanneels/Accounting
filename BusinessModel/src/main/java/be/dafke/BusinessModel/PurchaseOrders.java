package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.Utils.Utils;

public class PurchaseOrders extends BusinessCollection<PurchaseOrder>{

    private int id = 0;

    public PurchaseOrder addBusinessObject(PurchaseOrder order) throws EmptyNameException, DuplicateNameException {
        id++;
        if(order.getId()==null) {
            order.setId(id);
        }
        order.setName(Utils.toIDString("PO", order.getId(), 3));
        order.addPurchaseOrderToArticles();
        return super.addBusinessObject(order);
    }

    public void removeBusinessObject(Order order){
        removeBusinessObject(order.getUniqueProperties());
    }
}
