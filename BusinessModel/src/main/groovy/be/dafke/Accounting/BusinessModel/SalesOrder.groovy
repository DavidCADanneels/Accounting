package be.dafke.Accounting.BusinessModel

import java.math.RoundingMode
import java.util.function.Predicate

class SalesOrder extends Order {

    Transaction salesTransaction, gainTransaction
    String invoiceNumber = ""
    boolean invoice = false
    String invoicePath = null
    boolean promoOrder = false

    BigDecimal getTotalSalesPriceExclVat() {
        BigDecimal total = BigDecimal.ZERO.setScale(2)
        for (OrderItem orderItem : getBusinessObjects()) {
            total = total.add(orderItem.salesPriceWithoutVat).setScale(2, RoundingMode.HALF_DOWN)
        }
        total
    }

    BigDecimal getTotalSalesPriceExclVat(Predicate<OrderItem> predicate) {
        BigDecimal totalSalesExcl = BigDecimal.ZERO.setScale(2)
        for (OrderItem orderItem : getBusinessObjects(predicate)) {
            totalSalesExcl = totalSalesExcl.add(orderItem.salesPriceWithoutVat).setScale(2, RoundingMode.HALF_DOWN)
        }
        totalSalesExcl
    }

    BigDecimal getTotalSalesVat() {
        BigDecimal totalSalesVat = BigDecimal.ZERO.setScale(2)
        for (OrderItem orderItem : getBusinessObjects()) {
            totalSalesVat = totalSalesVat.add(orderItem.salesVatAmount).setScale(2, RoundingMode.HALF_DOWN)
        }
        totalSalesVat
    }

    BigDecimal getTotalSalesVat(Predicate<OrderItem> predicate) {
        BigDecimal totalSalesExcl = BigDecimal.ZERO.setScale(2)
        for (OrderItem orderItem : getBusinessObjects(predicate)) {
            totalSalesExcl = totalSalesExcl.add(orderItem.salesVatAmount).setScale(2, RoundingMode.HALF_DOWN)
        }
        totalSalesExcl
    }

    BigDecimal getTotalSalesPriceInclVat() {
        BigDecimal totalSalesExcl = BigDecimal.ZERO.setScale(2)
        for (OrderItem orderItem : getBusinessObjects()) {
            totalSalesExcl = totalSalesExcl.add(orderItem.salesPriceWithVat).setScale(2, RoundingMode.HALF_DOWN)
        }
        totalSalesExcl
    }

    BigDecimal getTotalSalesPriceInclVat(Predicate<OrderItem> predicate) {
        BigDecimal totalSalesExcl = BigDecimal.ZERO.setScale(2)
        for (OrderItem orderItem : getBusinessObjects(predicate)) {
            totalSalesExcl = totalSalesExcl.add(orderItem.salesPriceWithVat.setScale(2, RoundingMode.HALF_DOWN))
        }
        totalSalesExcl
    }
    void setSalesTransaction(Transaction salesTransaction) {
        this.salesTransaction = salesTransaction
    }

    @Override
    boolean isEditable(){
        // TODO: change into: 'salesTransaction==null||!salesTransaction.isVatBooked()'
        salesTransaction==null
    }

    BigDecimal calculateTotalSalesVat() {
        totalSalesPriceInclVat.subtract(totalSalesPriceExclVat).setScale(2, RoundingMode.HALF_DOWN)
    }

    void addSalesOrderToArticles() {
        getBusinessObjects().forEach({ orderItem ->
            Article article = orderItem.article
            article.addSalesOrder(this)
        })
    }
}
