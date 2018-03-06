package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

public class SalesOrders extends BusinessCollection<Order>{

    private static int id = 0;

    public SalesOrders() {
        super();
    }

    public static void setId(int id) {
        SalesOrders.id = id;
    }

    public Order addBusinessObject(Order order) throws EmptyNameException, DuplicateNameException {
        if (order.getName()==null) {
            order.setName("SO" + ++id);
        }
        return super.addBusinessObject(order);
    }
}