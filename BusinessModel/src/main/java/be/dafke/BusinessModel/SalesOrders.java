package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.Utils.Utils;

public class SalesOrders extends BusinessCollection<SalesOrder>{

    private static int id = 0;

    public SalesOrders() {
        super();
    }

    public SalesOrder addBusinessObject(SalesOrder salesOrder) throws EmptyNameException, DuplicateNameException {
        if (salesOrder.getName()==null) {
            id++;
            salesOrder.setName(Utils.toIDString("SO", id, 3));
            salesOrder.setId(id);
        }
        salesOrder.addSalesOrderToArticles();
        return super.addBusinessObject(salesOrder);
    }

    public void removeBusinessObject(Order order){
        removeBusinessObject(order.getUniqueProperties());
    }

}
