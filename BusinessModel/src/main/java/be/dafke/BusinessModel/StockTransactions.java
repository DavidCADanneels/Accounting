package be.dafke.BusinessModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class StockTransactions {
    protected final ArrayList<Order> orders = new ArrayList<>();

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order order){
        orders.add(order);
    }
}
