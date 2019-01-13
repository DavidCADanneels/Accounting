package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.Utils.Utils;

public class SalesOrders extends BusinessCollection<SalesOrder>{

    private int id = 0;

    public SalesOrder addBusinessObject(SalesOrder order) throws EmptyNameException, DuplicateNameException {
        id++;
        if (order.getId()==null) {
            order.setId(id);
        }
        order.setName(Utils.toIDString("SO", order.getId(), 3));
        order.addSalesOrderToArticles();
        return super.addBusinessObject(order);
    }

    public void removeBusinessObject(Order order){
        removeBusinessObject(order.getUniqueProperties());
    }

}
