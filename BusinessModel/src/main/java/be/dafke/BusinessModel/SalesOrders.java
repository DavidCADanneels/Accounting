package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;

public class SalesOrders extends BusinessCollection<Order>{

    private static int id = 0;

    public SalesOrders() {
        super();
    }

    public static void setId(int id) {
        SalesOrders.id = id;
    }

    public String getId() {
        return "SO" + ++id;
    }
}
