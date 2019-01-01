package be.dafke.BusinessModel;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;
import be.dafke.Utils.Utils;

public class PromoOrders extends BusinessCollection<PromoOrder>{

    private static int id = 0;

    public PromoOrders() {
        super();
    }

    public static void setId(int id) {
        PromoOrders.id = id;
    }

    public PromoOrder addBusinessObject(PromoOrder promoOrder) throws EmptyNameException, DuplicateNameException {
        if(promoOrder.getName()==null) {
            id++;
            promoOrder.setName(Utils.toIDString("PR", id, 3));
            promoOrder.setId(id);
        }
        promoOrder.addPromoOrderToArticles();
        return super.addBusinessObject(promoOrder);
    }

    public void removeBusinessObject(Order order){
        removeBusinessObject(order.getUniqueProperties());
    }
}
