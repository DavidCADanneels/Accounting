package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

public class StockOrders extends BusinessCollection<StockOrder>{

    private static int id = 0;

    public StockOrders() {
        super();
    }

    public static void setId(int id) {
        StockOrders.id = id;
    }

    public StockOrder addBusinessObject(StockOrder stockOrder) throws EmptyNameException, DuplicateNameException {
        if(stockOrder.getName()==null) {
            stockOrder.setName("ST" + ++id);
        }
        stockOrder.addStockOrderToArticles();
        return super.addBusinessObject(stockOrder);
    }

    public void removeBusinessObject(Order order){
        removeBusinessObject(order.getUniqueProperties());
    }
}
