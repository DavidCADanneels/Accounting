package be.dafke.Accounting.BusinessModel

import java.math.RoundingMode

class Order extends OrderItems {
    Contact customer, supplier
    Transaction paymentTransaction
    Integer id
    String deliveryDate = null
    String description
    boolean creditNote

    void setArticles(Articles articles) {
        articles.businessObjects.forEach({ article ->
            addBusinessObject(new OrderItem(0, article, this))
        })
    }

    BigDecimal getTotalStockValue() {
        BigDecimal total = BigDecimal.ZERO.setScale(2)
        for (OrderItem orderItem : getBusinessObjects()) {
            total = total.add(orderItem.getStockValue()).setScale(2, RoundingMode.HALF_DOWN)
//            total = total.add(orderItem.getStockValue())
        }
        // FIXME: set scale for each addition or only at the end?
//        total.setScale(2, RoundingMode.HALF_DOWN)
        total
    }

    void removeEmptyOrderItems() {
        getBusinessObjects().forEach({ orderItem ->
            int numberOfUnits = orderItem.numberOfUnits
            int numberOfItems = orderItem.numberOfItems
            if (numberOfUnits == 0 && numberOfItems == 0) {
                remove(orderItem, false, true)
            }
        })
    }
//
//    void setName(String name) {
//        super.setName(name)
//    }
}