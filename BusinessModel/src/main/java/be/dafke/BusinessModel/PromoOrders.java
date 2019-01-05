package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.Utils.Utils;

public class PromoOrders extends BusinessCollection<PromoOrder>{

    private int id = 0;

    public PromoOrder addBusinessObject(PromoOrder order) throws EmptyNameException, DuplicateNameException {
        if(order.getId()==null) {
            id++;
            order.setId(id);
        }
        order.setName(Utils.toIDString("PR", order.getId(), 3));
        order.addPromoOrderToArticles();
        return super.addBusinessObject(order);
    }

    public void removeBusinessObject(Order order){
        removeBusinessObject(order.getUniqueProperties());
    }
}
