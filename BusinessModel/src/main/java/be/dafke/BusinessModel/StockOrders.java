package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.Utils.Utils;

public class StockOrders extends BusinessCollection<StockOrder>{

    private int id = 0;

    public StockOrder addBusinessObject(StockOrder order) throws EmptyNameException, DuplicateNameException {
        id++;
        if(order.getId()==null) {
            order.setId(id);
        }
        order.setName(Utils.toIDString("ST", order.getId(), 3));
        order.addStockOrderToArticles();
        return super.addBusinessObject(order);
    }

    public void removeBusinessObject(Order order){
        removeBusinessObject(order.getUniqueProperties());
    }
}
