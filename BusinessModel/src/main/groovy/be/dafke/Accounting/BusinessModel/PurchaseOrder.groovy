package be.dafke.Accounting.BusinessModel


import java.math.RoundingMode
import java.util.function.Predicate

class PurchaseOrder extends Order {

    private Transaction purchaseTransaction

    BigDecimal getTotalPurchasePriceExclVat() {
        BigDecimal total = BigDecimal.ZERO.setScale(2)
        for (OrderItem orderItem : getBusinessObjects()) {
            BigDecimal purchasePriceWithoutVat = orderItem.getPurchasePriceWithoutVat()
            if(purchasePriceWithoutVat!=null) {
                total = total.add(purchasePriceWithoutVat).setScale(2, RoundingMode.HALF_DOWN)
            }
        }
        total
    }

    BigDecimal getTotalPurchasePriceExclVat(Predicate<OrderItem> predicate) {
        BigDecimal total = BigDecimal.ZERO.setScale(2)
        for (OrderItem orderItem : getBusinessObjects(predicate)) {
            BigDecimal purchasePriceWithoutVat = orderItem.getPurchasePriceWithoutVat()
            if(purchasePriceWithoutVat!=null) {
                total = total.add(purchasePriceWithoutVat).setScale(2, RoundingMode.HALF_DOWN)
            }
        }
        total
    }

    BigDecimal getTotalPurchasePriceInclVat() {
        BigDecimal total = BigDecimal.ZERO.setScale(2)
        for (OrderItem orderItem : getBusinessObjects()) {
            BigDecimal purchasePriceWithVat = orderItem.getPurchasePriceWithVat()
            if(purchasePriceWithVat!=null) {
                total = total.add(purchasePriceWithVat).setScale(2, RoundingMode.HALF_DOWN)
            }
        }
        total
    }

    BigDecimal getTotalPurchasePriceInclVat(Predicate<OrderItem> predicate) {
        BigDecimal total = BigDecimal.ZERO.setScale(2)
        for (OrderItem orderItem : getBusinessObjects(predicate)) {
            BigDecimal purchasePriceWithVat = orderItem.getPurchasePriceWithVat()
            if(purchasePriceWithVat!=null) {
                total = total.add(purchasePriceWithVat.setScale(2, RoundingMode.HALF_DOWN))
            }
        }
        total
    }

    BigDecimal getTotalPurchaseVat() {
        BigDecimal total = BigDecimal.ZERO.setScale(2)
        for (OrderItem orderItem : getBusinessObjects()) {
            BigDecimal purchaseVatAmount = orderItem.getPurchaseVatAmount()
            if (purchaseVatAmount != null) {
                total = total.add(purchaseVatAmount).setScale(2, RoundingMode.HALF_DOWN)
            }
        }
        total
    }

    BigDecimal getTotalPurchaseVat(Predicate<OrderItem> predicate) {
        BigDecimal total = BigDecimal.ZERO.setScale(2)
        for (OrderItem orderItem : getBusinessObjects(predicate)) {
            BigDecimal purchaseVatAmount = orderItem.getPurchaseVatAmount()
            if(purchaseVatAmount!=null) {
                total = total.add(purchaseVatAmount).setScale(2, RoundingMode.HALF_DOWN)
            }
        }
        total
    }

    BigDecimal calculateTotalPurchaseVat() {
        BigDecimal total = BigDecimal.ZERO.setScale(2)
        BigDecimal totalPurchasePriceExclVat = getTotalPurchasePriceExclVat()
        BigDecimal totalPurchasePriceInclVat = getTotalPurchasePriceInclVat()
        if(totalPurchasePriceExclVat!=null && totalPurchasePriceInclVat!=null) {
            total = totalPurchasePriceInclVat.subtract(totalPurchasePriceExclVat).setScale(2, RoundingMode.HALF_DOWN)
        }
        total
    }

    void addPurchaseOrderToArticles() {
        getBusinessObjects().forEach({ orderItem ->
            Article article = orderItem.getArticle()
            article.addPurchaseOrder(this)
        })
    }

    void setPurchaseTransaction(Transaction purchaseTransaction) {
        this.purchaseTransaction = purchaseTransaction

        if(!isCreditNote()) {
            getBusinessObjects().forEach({ orderItem ->
                Article article = orderItem.getArticle()
                int numberOfItems = orderItem.getNumberOfItems()
                article.setPoOrdered(numberOfItems)
            })
        } else {
            getBusinessObjects().forEach({ orderItem ->
                Article article = orderItem.getArticle()
                int numberOfItems = orderItem.getNumberOfItems()
                article.setPoCnOrdered(numberOfItems)
            })
        }

    }

    Transaction getPurchaseTransaction() {
        purchaseTransaction
    }
}
