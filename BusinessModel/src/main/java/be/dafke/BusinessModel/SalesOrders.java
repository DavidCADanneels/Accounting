package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;

public class SalesOrders extends BusinessCollection<Order>{

    private static int id = 1;

    public SalesOrders() {
        super();
    }

    public String getId() {
        return "SO" + id++;
    }
}
