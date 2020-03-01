package be.dafke.Accounting.BusinessModel


import java.math.RoundingMode
import java.util.function.Predicate

class PurchaseOrder extends Order {

    Transaction purchaseTransaction

    BigDecimal getTotalPurchasePriceExclVat() {
        BigDecimal total = BigDecimal.ZERO.setScale(2)
        for (OrderItem orderItem : getBusinessObjects()) {
            BigDecimal purchasePriceWithoutVat = orderItem.getPurchasePriceWithoutVat()
            if(purchasePriceWithoutVat) {
                total = total.add(purchasePriceWithoutVat).setScale(2, RoundingMode.HALF_DOWN)
            }
        }
        total
    }

    BigDecimal getTotalPurchasePriceExclVat(Predicate<OrderItem> predicate) {
        BigDecimal total = BigDecimal.ZERO.setScale(2)
        for (OrderItem orderItem : getBusinessObjects(predicate)) {
            BigDecimal purchasePriceWithoutVat = orderItem.getPurchasePriceWithoutVat()
            if(purchasePriceWithoutVat) {
                total = total.add(purchasePriceWithoutVat).setScale(2, RoundingMode.HALF_DOWN)
            }
        }
        total
    }

    BigDecimal getTotalPurchasePriceInclVat() {
        BigDecimal total = BigDecimal.ZERO.setScale(2)
        for (OrderItem orderItem : getBusinessObjects()) {
            BigDecimal purchasePriceWithVat = orderItem.getPurchasePriceWithVat()
            if(purchasePriceWithVat) {
                total = total.add(purchasePriceWithVat).setScale(2, RoundingMode.HALF_DOWN)
            }
        }
        total
    }

    BigDecimal getTotalPurchasePriceInclVat(Predicate<OrderItem> predicate) {
        BigDecimal total = BigDecimal.ZERO.setScale(2)
        for (OrderItem orderItem : getBusinessObjects(predicate)) {
            BigDecimal purchasePriceWithVat = orderItem.getPurchasePriceWithVat()
            if(purchasePriceWithVat) {
                total = total.add(purchasePriceWithVat.setScale(2, RoundingMode.HALF_DOWN))
            }
        }
        total
    }

    BigDecimal getTotalPurchaseVat() {
        BigDecimal total = BigDecimal.ZERO.setScale(2)
        for (OrderItem orderItem : getBusinessObjects()) {
            BigDecimal purchaseVatAmount = orderItem.getPurchaseVatAmount()
            if (purchaseVatAmount) {
                total = total.add(purchaseVatAmount).setScale(2, RoundingMode.HALF_DOWN)
            }
        }
        total
    }

    BigDecimal getTotalPurchaseVat(Predicate<OrderItem> predicate) {
        BigDecimal total = BigDecimal.ZERO.setScale(2)
        for (OrderItem orderItem : getBusinessObjects(predicate)) {
            BigDecimal purchaseVatAmount = orderItem.getPurchaseVatAmount()
            if(purchaseVatAmount) {
                total = total.add(purchaseVatAmount).setScale(2, RoundingMode.HALF_DOWN)
            }
        }
        total
    }

    BigDecimal calculateTotalPurchaseVat() {
        BigDecimal total = BigDecimal.ZERO.setScale(2)
        BigDecimal totalPurchasePriceExclVat = totalPurchasePriceExclVat
        BigDecimal totalPurchasePriceInclVat = totalPurchasePriceInclVat
        if(totalPurchasePriceExclVat && totalPurchasePriceInclVat) {
            total = totalPurchasePriceInclVat.subtract(totalPurchasePriceExclVat).setScale(2, RoundingMode.HALF_DOWN)
        }
        total
    }

    void addPurchaseOrderToArticles() {
        getBusinessObjects().forEach({ orderItem ->
            Article article = orderItem.article
            article.addPurchaseOrder(this)
//            if(article instanceof Good){
//                Good good = (Good) orderItem.article
//                good.addPurchaseOrder(this)
//            }
//            if(article instanceof Service){
//                Service service = (Service) orderItem.article
//                service.addPurchaseOrder(this)
//            }
        })
    }

    void setPurchaseTransaction(Transaction purchaseTransaction) {
        this.purchaseTransaction = purchaseTransaction
    }

    static Predicate<PurchaseOrder> payed() {
        { order -> order.paymentTransaction != null }
    }

    static Predicate<PurchaseOrder> unPayed() {
        { order -> order.paymentTransaction == null }
    }
}
