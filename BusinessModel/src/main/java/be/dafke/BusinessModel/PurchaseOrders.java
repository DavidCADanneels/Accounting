package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

public class PurchaseOrders extends BusinessCollection<Order>{

    private static int id = 0;

    public PurchaseOrders() {
        super();
    }

    public static void setId(int id) {
        PurchaseOrders.id = id;
    }

    public Order addBusinessObject(Order order) throws EmptyNameException, DuplicateNameException {
        if(order.getName()==null) {
            order.setName("PO" + ++id);
        }
        return super.addBusinessObject(order);
    }

    public void removeBusinessObject(Order order){
        removeBusinessObject(order.getUniqueProperties());
    }
}
