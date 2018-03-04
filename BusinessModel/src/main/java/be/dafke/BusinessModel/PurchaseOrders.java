package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;

public class PurchaseOrders extends BusinessCollection<Order>{

    private static int id = 0;

    public PurchaseOrders() {
        super();
    }

    public static void setId(int id) {
        PurchaseOrders.id = id;
    }

    public String getId() {
        return "PO" + ++id;
    }
}
