package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;

public class PurchaseOrders extends BusinessCollection<Order>{

    private static int id = 1;

    public PurchaseOrders() {
        super();
    }

    public String getId() {
        return "PO" + id++;
    }
}
