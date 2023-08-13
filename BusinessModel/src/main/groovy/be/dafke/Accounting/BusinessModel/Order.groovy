package be.dafke.Accounting.BusinessModel

import java.math.RoundingMode
import java.util.function.Predicate

class Order extends OrderItems {
    Contact customer, supplier
    Transaction paymentTransaction
    Integer id
    String deliveryDate = null
    String invoiceDate = null
    String deliveryDescription
    String invoiceDescription
    String invoiceNumber = ""
    boolean invoice = false
    String invoicePath = null
    boolean creditNote

    void setArticles(Articles articles) {
        articles.businessObjects.forEach({ article ->
            addBusinessObject(new OrderItem(0, article, this))
        })
    }

    BigDecimal getTotalStockValue() {
        BigDecimal total = BigDecimal.ZERO.setScale(2)
        for (OrderItem orderItem : getBusinessObjects()) {
            def value = orderItem.getStockValue()
            if(value)
                total = total.add(value).setScale(2, RoundingMode.HALF_DOWN)
//            total = total.add(orderItem.getStockValue())
        }
        // FIXME: set scale for each addition or only at the end?
//        total.setScale(2, RoundingMode.HALF_DOWN)
        return total
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

    static Predicate<Order> payed() {
        { order -> order.paymentTransaction != null }
    }

    static Predicate<Order> unPayed() {
        { order -> order.paymentTransaction == null }
    }

//
//    void setName(String name) {
//        super.setName(name)
//    }
}